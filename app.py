from flask import Flask, request, jsonify
import base64
import os
import cv2
import json

app = Flask(__name__)

#######################################################
#                 Auxiliary Funtions                  #
#######################################################


def store_requested_img(request):

    try:
        # Leer imagen the
        string = request.json['img']
        str_slip = string.split(',')
        string_b64 = bytes(str_slip[1], encoding='utf8')
        string_deco = base64.b64decode(string_b64)
        file = open('decoImg.jpg', 'wb')
        file.write(string_deco)
        file.close()
        return True

    except Exception as e:
        print(e)
        return False


def searchPatron(img, cascade):
    structure = cascade.detectMultiScale(img, 3, 1)
    x = None
    for (x, y, w, h) in structure:
        cv2.rectangle(img, (x, y), (x+w, y+h), (0, 255, 0), 2)
    if (x is not None):
        return (True, img)
    else:
        return (False, img)


#######################################################
#                    API Resources                    #
#######################################################

# Return the name of the building that match with image sent
# int a Json request
@app.route('/api/monuments', methods=['POST'])
def get_monument():

    try:
        if(store_requested_img(request)):
            img = cv2.imread("decoImg.jpg")
            # Resize
            (height, width, channels) = img.shape
            a = height/300
            height = int(height/a)
            width = int(width/a)
            img = cv2.resize(img, (width, height))
            img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

            cascades_dir = 'haarcascade'
            list_cascades = os.listdir(cascades_dir)
            for cascade_dir in os.listdir(cascades_dir):

                for file in os.listdir(os.path.join(cascades_dir,
                                                    cascade_dir)):

                    if file.endswith(".xml"):

                        casc_path = os.path.join(
                            cascades_dir, cascade_dir, file)
                        monument_cascade = cv2.CascadeClassifier(casc_path)
                        json_cas_settins = open(os.path.join(
                            cascades_dir, cascade_dir, "detectionSettings.json"))
                        json_setting = json.load(json_cas_settins)
                        x = None
                        found = monument_cascade.detectMultiScale(
                            img, json_setting['scaleFactore'], json_setting['minNeighbors'])
                        for (x, y, w, h) in found:
                            cv2.rectangle(
                                img, (x, y), (x+w, y+h), (0, 255, 0), 2)

                            cv2.imwrite("decoImg.jpg", img)
                        if x is not None:

                            return jsonify({"status": "200",
                                            "message": "Monument found",
                                            "name": cascade_dir}), 200

        return jsonify({"status": "404", "message": "Could not found the monument"}), 404

    except expression as identifier:
        return jsonify({"status": "500", "message": "internal error"}), 500


@app.route('/api/monuments', methods=['GET'])
def get_monuments():
    list_monuments = []
    haar_cascades_dir = 'haarcascade'
    for cascade_dir in os.listdir(haar_cascades_dir):
        list_monuments.append(cascade_dir)
    return jsonify({"status": 200,
                    "message": "This is a list of the current monument soported for this APi",
                    "data": list_monuments})


# Return the information about this API
@app.route('/api/info', methods=['GET'])
def get_info():

    return jsonify({'message': """This api offers the service of recognition 
                    of historical monuments of Guadalajara City. You only need 
                    to send an image encoded in base64 and make a POST request 
                    to the URI/api/monument""", 'status': '200'}), 200


# Method to test new features
@app.route('/api/test', methods=['GET'])
def test():
    return {"status": "200",
            "message": "ok"}, 200


# Run Application
if __name__ == "__main__":
    app.run(host='0.0.0.0', debug=True, port=4000)
