import cv2

cam = cv2.namedWindow("test")

img_counter = 0

while True:
    ret, frame = cam.read()
    cv2.imshow("test", frame)
    if not ret:
        break
    k = cv2.waitKey(1)

    if k%256 == 27:
        #Escape key pressed
        print("Escape key pressed, camera closing")
        break
    elif k % 256 == 32:
        # spacebar pressed
        img_name = "opencv_frame_{}.png".format(img_counter)
        cv2.imwrite(img_name, frame)
        print("{} written!".format(img_name))
        img_counter += 1
        
cam.release()
cv2.destroyAllWindows()
