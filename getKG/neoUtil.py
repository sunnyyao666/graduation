from py2neo import *
import os
from queue import Queue


class NeoUtil:
    def __init__(self):
        self.graph = Graph('http://localhost:7474', auth=("neo4j", "java"))

    def add_lesson(self, name):
        lesson = Node('Lesson', name=name)
        self.graph.create(lesson)

    def add_chapter(self, name):
        chapter = Node('Chapter', name=name)
        self.graph.create(chapter)

    def add_knowledge(self, chapter, name, info):
        sql = "Merge (m:Chapter{{name:'{0}'}}) Merge (n:Knowledge{{name:'{1}', info:'{2}', parentID:id(m)}}) " \
              "Merge(m)-[r:CONTAIN{{name:'包含'}}]->(n)".format(chapter, name, info)
        self.graph.run(sql)

    def add_code(self, knowledge, code, priority):
        sql = "Merge (m:Knowledge{{name:'{0}'}}) Merge (n:Code{{name:'实例', code:'{1}',priority:{2}}}) " \
              "Merge(n)-[r:CODE{{name:'代码资源'}}]->(m)".format(knowledge, code, priority)
        self.graph.run(sql)

    def make_successor(self, p, s):
        sql = "Merge (m:Knowledge{{name:'{0}'}}) Merge (n:Knowledge{{name:'{1}'}}) " \
              "Merge(m)-[r:PRE{{name:'前驱'}}]->(n)".format(p, s)
        self.graph.run(sql)

    def insert_chapter_priority(self, chapter, priority):
        sql = "Merge (m:Chapter{{name:'{0}'}}) Set m.priority = {1}".format(chapter, priority)
        self.graph.run(sql)

    def insert_knowledge_priority(self, knowledge, priority):
        sql = "Merge (m:Knowledge{{name:'{0}'}}) Set m.priority = {1}".format(knowledge, priority)
        self.graph.run(sql)

    def make_relation(self):
        sql = "Merge (m:Lesson) Merge (n:Chapter) Merge(m)-[r:CONTAIN{name:'包含'}]->(n)"
        self.graph.run(sql)

    def add_ppt(self, name, url):
        ppt = Node('PPT', name=name, url=url)
        self.graph.create(ppt)

    def add_ppt_relation(self, name, knowledge):
        sql = "Merge (m:Knowledge{{name:'{0}'}}) Merge (n:PPT{{name:'{1}'}}) " \
              "Merge(n)-[r:PPT{{name:'PPT资源'}}]->(m)".format(knowledge, name)
        self.graph.run(sql)

    def add_mp4(self, name, url):
        mp4 = Node('MP4', name=name, url=url)
        self.graph.create(mp4)

    def add_mp4_relation(self, name, knowledge):
        sql = "Merge (m:Knowledge{{name:'{0}'}}) Merge (n:MP4{{name:'{1}'}}) " \
              "Merge(n)-[r:MP4{{name:'MP4资源'}}]->(m)".format(knowledge, name)
        self.graph.run(sql)

    def clear(self):
        self.graph.run("Match (n) Detach Delete n")


if __name__ == "__main__":
    n = NeoUtil()
    n.clear()
    n.add_lesson("Java程序设计")

    d = []
    i = 0
    for root, dirs, files in os.walk(r"./corpusFinal"):
        if len(dirs) > 0:
            d = dirs
            continue
        for file in files:
            chapter = d[i]
            with open(os.path.join(root, file), encoding="utf-8") as fin:
                name = ""
                info = ""
                for line in fin:
                    if name == "":
                        name = line.strip()
                        continue
                    if "*******" in line:
                        n.add_knowledge(chapter, name, info)
                        name = ""
                        info = ""
                        continue
                    info += line.replace("\\", "\\\\").replace("'", "\\'")
        i += 1
    n.make_relation()

    code_map = {}
    for root, dirs, files in os.walk(r"./codeFinal"):
        for file in files:
            with open(os.path.join(root, file), encoding="utf-8") as fin:
                knowledge = ""
                code = ""
                for line in fin:
                    if knowledge == "":
                        knowledge = line.strip()
                        priority = code_map.get(knowledge, 0)
                        continue
                    if "*******" in line:
                        priority += 1
                        n.add_code(knowledge, code, priority)
                        code_map[knowledge] = priority
                        knowledge = ""
                        code = ""
                        continue
                    code += line.replace("\\", "\\\\").replace("'", "\\'").replace(".runoob.com",
                                                                                   ".JavaKG.com").replace(
                        "//www.JavaKG.com", "//www.runoob.com")
        i += 1
    with (open('./chapter_priority.txt', encoding="utf-8")) as fin:
        line = fin.readline()
        while line:
            line = line.strip()
            arr = line.split(',')
            n.insert_chapter_priority(arr[0], int(arr[1]))
            line = fin.readline()

    suc = {}
    pre = {}
    with (open('./successor.txt', encoding="utf-8")) as fin:
        line = fin.readline()
        while line:
            line = line.strip()
            arr = line.split(',')
            n.make_successor(arr[0], arr[1])
            p = pre.get(arr[1], [])
            p.append(arr[0])
            pre[arr[1]] = p
            s = suc.get(arr[0], [])
            s.append(arr[1])
            suc[arr[0]] = s
            line = fin.readline()

    priorityMap = {'Java基础语法': 1}
    n.insert_knowledge_priority('Java基础语法', 1)

    q = Queue()
    for k in suc['Java基础语法']:
        q.put(k)
    while not q.empty():
        k = q.get()
        if k in priorityMap:
            continue
        p_max = 0
        f = True
        for p in pre[k]:
            if p not in priorityMap:
                f = False
                q.put(p)
            elif priorityMap[p] > p_max:
                p_max = priorityMap[p]
        if f:
            n.insert_knowledge_priority(k, p_max + 1)
            priorityMap[k] = p_max + 1
            if k in suc:
                for s in suc[k]:
                    q.put(s)
        else:
            q.put(k)

    with (open('./fasttext/ppt_map_final.txt', encoding="utf-8")) as fin:
        for line in fin:
            arr = line.strip().split(' ')
            n.add_ppt(arr[0], arr[1])
            for i in range(2, len(arr)):
                n.add_ppt_relation(arr[0], arr[i].removeprefix('__label__'))

    with (open('./fasttext/mp4_map.txt', encoding="utf-8")) as fin:
        for line in fin:
            arr = line.strip().split(' ')
            n.add_mp4(arr[0], arr[1])
            for i in range(2, len(arr)):
                n.add_mp4_relation(arr[0], arr[i].removeprefix('__label__'))
