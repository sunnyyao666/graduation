import os
import jieba
import bs4
import random
import ahocorasick
from bs4 import *


def handle_str(s):
    s = s.replace('Java', '').replace(' 修饰符', '').replace(' 类', '').replace(' 关键字', '').replace('语句', '')
    s = s.replace(' 循环', '').replace('：', '').replace(' ', '').replace('方式一', '').replace('方式二', '').strip()
    s = s.replace('方式三抽象类和抽象方法', '抽象类-抽象方法').replace('super与this', 'super-this').replace('Super的使用', 'super')
    s = s.replace('if...elseif...else', 'else if').replace('不使用任何', '访问控制符-访问修饰符')
    if s == '' or s == '语法' or s == '总结':
        return None
    if '-' in s:
        result = s.split('-')
    elif '（' in s:
        result = s.split('（')
        result[1] = result[1].replace('）', '')
    elif '(' in s:
        result = s.split('(')
        result[1] = result[1].replace(')', '')
    elif '/' in s:
        result = s.split('/')
    else:
        result = [s]
    return result


def check_h3(sentence):
    h3s = []
    for i in acTree.iter(sentence):
        word = i[1][1]
        h3s.append(word)
    # 防止关键词的子串也被找到
    delete_words = []
    for word1 in h3s:
        for word2 in h3s:
            if word1 in word2 and word1 != word2:
                delete_words.append(word1)
    result = [i for i in h3s if i not in delete_words]
    return result


def get_labels(h3s, base):
    result = {base}
    for h3 in h3s:
        for h2 in h32h2[h3]:
            result.add(h2)
    return result


h32h2 = {}
h22h3 = {}
for root, dirs, files in os.walk(r"..\corpus"):
    for file in files:
        with open(os.path.join(root, file), encoding="utf-8") as fin:
            html_doc = fin.read()
            soup = BeautifulSoup(html_doc, 'html.parser')
            for h2 in soup.find_all('h2', class_=False):
                arr = handle_str(h2.get_text())
                if arr is None:
                    continue
                t = h22h3.get(h2.get_text().strip(), [])
                t.extend(arr)
                h22h3[h2.get_text().strip()] = t
                for text in arr:
                    t = h32h2.get(text, [])
                    t.append(h2.get_text().strip())
                    h32h2[text] = t
            for h3 in soup.find_all('h3'):
                h2 = h3.find_previous_sibling('h2', class_=False)
                if h2 is None:
                    continue
                if 'Java基础语法' in h2.get_text():
                    continue
                arr = handle_str(h3.get_text())
                if arr is None:
                    continue
                h2_text = h2.get_text().strip()
                t = h22h3.get(h2_text, [])
                t.extend(arr)
                h22h3[h2_text] = t
                for text in arr:
                    t = h32h2.get(text, [])
                    t.append(h2_text)
                    h32h2[text] = t

acTree = ahocorasick.Automaton()
i = 0

for k, v in h22h3.items():
    h22h3[k] = set(v)
for k, v in h32h2.items():
    h32h2[k] = list(set(v))
    acTree.add_word(k, (i, k))
    i += 1

acTree.make_automaton()
print(h22h3)
print(h32h2)

with open('./user_dict.txt', 'w', encoding='utf-8') as fout:
    fout.write('Java\n')
    for k in h32h2.keys():
        fout.write(k + '\n')

jieba.load_userdict('./user_dict.txt')


def wash_sentence(sentence):
    sentence = sentence.replace('\n', '').replace(':', '').replace('：', '').replace('、', '').replace('；', '')
    sentence = sentence.replace('，', '').replace('Java', '').replace('。', '').strip()
    return sentence


stopwords_set = set()
with open('./stop_word.txt', 'r', encoding='utf-8') as infile:
    for line in infile:
        stopwords_set.add(line.strip())

with open('./corpus.txt', 'w', encoding='utf-8') as train_out, open('./test.txt', 'w', encoding='utf-8') as test_out:
    for root, dirs, files in os.walk(r"..\corpus"):
        for file in files:
            with open(os.path.join(root, file), encoding="utf-8") as fin:
                html_doc = fin.read()
                soup = BeautifulSoup(html_doc, 'html.parser')
                for h2 in soup.find_all('h2', class_=False):
                    name = h2.get_text().strip()
                    label = '__label__'
                    train_out.write(label + name + ' ' + name + '\n')
                    t = h2.nextSibling
                    while t is not None and t.name != 'h2':
                        if type(t) == bs4.element.NavigableString:
                            t = t.nextSibling
                            continue
                        if t.name == 'pre' or t.name == 'h3':
                            t = t.nextSibling
                            continue
                        if t.name == 'table':
                            heads = []
                            for th in t.find_all('th'):
                                heads.append(th.get_text().strip())
                            for tr in t.find_all('tr'):
                                sentence = ''
                                i = 0
                                for td in tr.find_all('td'):
                                    sentence += heads[i] + " " + td.get_text().strip() + " "
                                    i += 1
                                if sentence != '':
                                    sentence = wash_sentence(sentence)
                                    labels = get_labels(check_h3(sentence), name)
                                    if random.random() > 0.7:
                                        fout = test_out
                                    else:
                                        fout = train_out
                                    for la in labels:
                                        fout.write(label + la + ' ')
                                    words = list(jieba.cut(sentence))
                                    result = [item.strip() for item in words if item.strip() not in stopwords_set]
                                    fout.write(' '.join(result) + '\n')
                        elif t.name == 'ul':
                            for li in t.find_all('li'):
                                s = li.get_text()
                                arr = s.split('。')
                                for sentence in arr:
                                    sentence = wash_sentence(sentence)
                                    if sentence == '' or sentence.startswith('例如：'):
                                        continue
                                    labels = get_labels(check_h3(sentence), name)
                                    if random.random() > 0.7:
                                        fout = test_out
                                    else:
                                        fout = train_out
                                    for la in labels:
                                        fout.write(label + la + ' ')
                                    # fout.write(' '.join(jieba.cut(sentence)) + '\n')
                                    words = list(jieba.cut(sentence))
                                    result = [item.strip() for item in words if item.strip() not in stopwords_set]
                                    fout.write(' '.join(result) + '\n')
                        else:
                            s = t.get_text()
                            arr = s.split('。')
                            for sentence in arr:
                                sentence = wash_sentence(sentence)
                                if sentence == '' or sentence.startswith('例如：'):
                                    continue
                                labels = get_labels(check_h3(sentence), name)
                                if random.random() > 0.7:
                                    fout = test_out
                                else:
                                    fout = train_out
                                for la in labels:
                                    fout.write(label + la + ' ')
                                words = list(jieba.cut(sentence))
                                result = [item.strip() for item in words if item.strip() not in stopwords_set]
                                fout.write(' '.join(result) + '\n')
                        t = t.nextSibling
