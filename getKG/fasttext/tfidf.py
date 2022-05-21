import math
import os
import jieba
import bs4
from bs4 import *

jieba.load_userdict('./user_dict.txt')


def wash_sentence(sentence):
    sentence = sentence.replace('\n', '').replace(':', '').replace('：', '').replace('、', '').replace('；', '')
    sentence = sentence.replace('，', '').replace('Java', '').replace('。', '').strip()
    return sentence


def tf_idf(word_list, file_words_map):
    result_map = {}
    for word in word_list:
        if word in result_map:
            continue
        tf = word_list.count(word) / len(word_list)
        num = 1
        for file_words in file_words_map.values():
            if word in file_words:
                num += 1
        idf = math.log10(len(file_words_map) / num)
        result_map[word] = tf * idf
    return sorted(result_map.items(), key=lambda x: x[1], reverse=True)


stopwords_set = set()
with open('./stop_word.txt', 'r', encoding='utf-8') as infile:
    for line in infile:
        stopwords_set.add(line.strip())

file_words_map = {}
for root, dirs, files in os.walk(r"..\corpus"):
    for file in files:
        with open(os.path.join(root, file), encoding="utf-8") as fin:
            word_list = []
            html_doc = fin.read()
            soup = BeautifulSoup(html_doc, 'html.parser')
            t = soup.find('h2')
            while t is not None:
                if type(t) == bs4.element.NavigableString:
                    t = t.nextSibling
                    continue
                if t.name == 'pre' or t.name == 'h3' or t.name == 'h2':
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
                            words = list(jieba.cut(sentence))
                            result = [item.strip() for item in words if
                                      item.strip() not in stopwords_set and item.strip() != '']
                            word_list.extend(result)
                elif t.name == 'ul':
                    for li in t.find_all('li'):
                        s = li.get_text()
                        arr = s.split('。')
                        for sentence in arr:
                            sentence = wash_sentence(sentence)
                            if sentence == '' or sentence.startswith('例如：'):
                                continue
                            # fout.write(' '.join(jieba.cut(sentence)) + '\n')
                            words = list(jieba.cut(sentence))
                            result = [item.strip() for item in words if
                                      item.strip() not in stopwords_set and item.strip() != '']
                            word_list.extend(result)
                else:
                    s = t.get_text()
                    arr = s.split('。')
                    for sentence in arr:
                        sentence = wash_sentence(sentence)
                        if sentence == '' or sentence.startswith('例如：'):
                            continue
                        words = list(jieba.cut(sentence))
                        result = [item.strip() for item in words if
                                  item.strip() not in stopwords_set and item.strip() != '']
                        word_list.extend(result)
                t = t.nextSibling
            file_words_map[file.split('.')[0]] = word_list

for k, v in file_words_map.items():
    print(k + " " ,end='')
    print(tf_idf(v, file_words_map))
