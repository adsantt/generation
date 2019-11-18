from flask import Flask, request, jsonify

app = Flask(__name__)


@app.route("/api/test", methods=['GET'])
def getInfo():
    return {'name': 'Catedral'}
    #json=request.json["message"]
    #return 'Catedral'

@app.route('/api/Monumento', methods=['POST'])
def getName():
    return request.json


if __name__ == "__main__":
    app.run(host='0.0.0.0' ,port=4000, debug=True)
