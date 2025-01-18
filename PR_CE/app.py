from flask import Flask, Response
import DobotDllType as dType
from dobot import move_box, move_box_back, initialize

app = Flask(__name__)
dobotApi = {}

@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


@app.route("/dobot/moveBox")
def move_box_route():
    print("moving box")
    move_box(dobotApi)
    return Response(status=200)


@app.route("/dobot/moveBoxBack")
def move_box_back_route():
    print("moving box back")
    move_box_back(dobotApi)
    return Response(status=200)


if __name__ == '__main__':
    dobotApi = initialize()
    app.run()
    # Disconnect Dobot
    dType.DisconnectDobot(dobotApi)
