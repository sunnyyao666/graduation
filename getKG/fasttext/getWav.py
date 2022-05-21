from moviepy.editor import AudioFileClip
import os

for root, dirs, files in os.walk(r".\mp4"):
    for file in files:
        my_audio_clip = AudioFileClip(os.path.join(root, file))
        arr = file.split('.')
        my_audio_clip.write_audiofile("./wav/" + arr[0] + '.wav')
