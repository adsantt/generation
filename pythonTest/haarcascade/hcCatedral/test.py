import cv2
import json

cascade = cv2.CascadeClassifier("classifier\\cascade.xml")

#img = cv2.imread("p\\catedralprueba.jpg")
#img = cv2.imread("n\\c4.jpg")
img = cv2.imread("n\\biblio18.jpg")
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

# anteriores 1.01, 5
json_cascade_file = open("classifier\\detectionSettings.json")
json_cas_values = json.load(json_cascade_file)
structures = cascade.detectMultiScale(gray, json_cas_values['scaleFactore'],
                                      json_cas_values['minNeighbors'])
for (x, y, w, h) in structures:
    img = cv2.rectangle(img, (x, y), (x+w, y+h), (255, 0, 0), 2)

cv2.imshow("img", img)
cv2.waitKey()
cv2.destroyAllWindows()
