from aip import AipSpeech
import os

""" 你的 APPID AK SK """
APP_ID = '25986444'
API_KEY = 'vmqDuwtpSlUWkuKBkjKgC3qP'
SECRET_KEY = 'qBDjz3pfT7jTOUs5yvpH7jrgd52xuxa8'

client = AipSpeech(APP_ID, API_KEY, SECRET_KEY)


# 读取文件
def get_file_content(file_path):
    with open(file_path, 'rb') as fp:
        return fp.read()


d = []
i = 0
for root, dirs, files in os.walk(r".\chunks"):
    if len(dirs) > 0:
        d = dirs
        continue
    results = []
    for file in files:
        result = client.asr(get_file_content(os.path.join(root, file)), 'wav', 16000, {
            'dev_pid': 1537  # 默认1537（普通话 输入法模型），dev_pid参数见本节开头的表格
        })
        results.append(''.join(result['result']))
    with open("./txt/" + d[i] + ".txt", 'w', encoding='utf-8') as fout:
        fout.write(''.join(results))
    i = i + 1
