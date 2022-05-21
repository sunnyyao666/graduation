from pydub import AudioSegment
from pydub.utils import make_chunks
import os

size = 59000  # 切割的毫秒数

for root, dirs, files in os.walk(r"./wav2"):
    for file in files:
        audio = AudioSegment.from_file(os.path.join(root, file), "wav")
        chunks = make_chunks(audio, size)  ##将文件切割为59s一块
        arr = file.split('.')
        path = "./chunks/" + arr[0]
        # os.mkdir(path)

        for i, chunk in enumerate(chunks):
            chunk_name = "/{0}.wav".format(i)
            ##保存文件
            chunk.export(path + chunk_name, format="wav")
