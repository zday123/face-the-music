from google.cloud import vision
from google.cloud.vision import types
from PIL import Image, ImageDraw
client = vision.ImageAnnotatorClient()
import requests
import pathlib
import os

def detect_face(face_file, max_results=1):
    client = vision.ImageAnnotatorClient()

    content = face_file.read()
    image = types.Image(content=content)

    return client.face_detection(image=image).face_annotations

def highlight_faces(image, faces, output_filename):
	im = Image.open(image)
	draw = ImageDraw.Draw(im)

	for face in faces:
		box = [(vertex.x, vertex.y) for vertex in face.bounding_poly.vertices]
		draw.line(box + [box[0]], width=5, fill='#00ff00')

	im.save(output_filename)

def get_emotion(faces):
    face_data = []
    for face in faces:
        x_diff = max([vertex.x for vertex in face.bounding_poly.vertices]) - min([vertex.x for vertex in face.bounding_poly.vertices])
        y_diff = max([vertex.y for vertex in face.bounding_poly.vertices]) - min([vertex.y for vertex in face.bounding_poly.vertices])
        emotions = [face.joy_likelihood, face.sorrow_likelihood, face.anger_likelihood]
        if emotions[0] == emotions[1] == emotions[2]:
            face_data.append((x_diff + y_diff, 'normal'))
        elif max(emotions) == emotions[0]:
            face_data.append((x_diff + y_diff, 'joy'))
        elif max(emotions) == emotions[1]:
            face_data.append((x_diff + y_diff, 'sorrow'))
        else:
            face_data.append((x_diff + y_diff, 'anger'))
    return max(face_data, key=lambda f: f[0])[1]

def get_song():
    pathlib.Path(os.path.abspath("data/")).as_uri()
    payload = {'client_id': '037fc929d3694a2a8b32fe9c32a4af28', 'response_type': 'token', 'redirect_uri': }
    r = requests.get('https://api.github.com/events')

def match_emotion_song():
    #TODO: get image
    with open(input_filename, 'rb') as image:
        faces = detect_face(image, 1)
        print('Found {} face{}'.format(
            len(faces), '' if len(faces) == 1 else 's'))
        get_emotion(faces)
        get_song()



if __name__ == "__main__": main()