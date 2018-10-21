import cv2
import os, inspect
import time
import asyncio


class Webcamdriver:
    def __init__(self):
        self.cam = cv2.VideoCapture(0)
        cv2.namedWindow("test")
        self.frame = 0

    def begin_webcam_image(self):
        ret, self.frame = self.cam.read()
        cv2.imshow("test", self.frame)
        cv2.waitKey(1)

    def release_image(self):
        self.cam.release()
        cv2.destroyAllWindows()

    def capture(self):
        img_counter = 0
        img_name = "opencv_frame_{}.png".format(img_counter)
        path = 'data/'
        cv2.imwrite(os.path.join(path, img_name), self.frame)
        img_counter += 1
        return path + img_name


