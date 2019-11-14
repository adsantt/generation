from flask import Flask, request, jsonify

app = Flask(__name__)


@app.route("/api/test", methods=['GET'])
def getInfo():
    return jsonify({'message': 'hola, desde python'})


if __name__ == "__main__":
    app.run(host='0.0.0.0' ,port=4000, debug=True)
