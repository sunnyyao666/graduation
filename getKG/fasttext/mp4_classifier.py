import fasttext
import jieba
import string
import os


def is_all_eng(strs):
    for i in strs:
        if i not in string.ascii_lowercase + string.ascii_uppercase:
            return False
    return True


classifier = fasttext.train_supervised("./corpus.txt", lr=1, epoch=50, wordNgrams=2)

jieba.load_userdict('./user_dict.txt')

stopwords_set = set()
with open('./stop_word.txt', 'r', encoding='utf-8') as infile:
    for line in infile:
        stopwords_set.add(line.strip())

url = 'https://sunny-graduation.oss-cn-shanghai.aliyuncs.com/mp4/'
with open('./mp4_map.txt', 'w', encoding='utf-8') as fout:
    for root, dirs, files in os.walk(r".\txt"):
        for file in files:
            with open(os.path.join(root, file), encoding='utf-8') as fin:
                words = list(jieba.cut(fin.read()))
                result = ' '.join([item.strip() for item in words if
                                   item.strip() not in stopwords_set and item.strip() != '' and not is_all_eng(
                                       item.strip())])
            arr = file.split('.')
            mp4_name = arr[0] + '.mp4'
            fout.write(
                mp4_name + ' ' + url + mp4_name + ' ' + ' '.join(classifier.predict(result, k=2)[0]) + '\n')
