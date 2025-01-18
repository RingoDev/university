import DobotDllType as dType

CON_STR = {
    dType.DobotConnect.DobotConnect_NoError: "DobotConnect_NoError",
    dType.DobotConnect.DobotConnect_NotFound: "DobotConnect_NotFound",
    dType.DobotConnect.DobotConnect_Occupied: "DobotConnect_Occupied"}


def initialize():
    print("Initializing connection to Dobot")
    # Load Dll and get the CDLL object
    api: dType = dType.load()
    # Connect Dobot
    state = dType.ConnectDobot(api, "", 115200)[0]
    print("Connect status:", CON_STR[state])
    if state == dType.DobotConnect.DobotConnect_NoError:
        # Clean Command Queued
        dType.SetQueuedCmdClear(api)
        # # Async Motion Params Setting
        dType.SetPTPCmdEx(api, 4, 0, 0, 0, 0, 1)
        return api
    raise ConnectionError("Could not connect to Dobot")


def move_box(api: dType):
    dType.SetPTPCmdEx(api, 4, 0, 0, 0, 0, 1)
    dType.SetPTPCmdEx(api, 4, 0, 54, 60, 0, 1)
    dType.SetEndEffectorSuctionCupEx(api, 1, 1)
    dType.dSleep(1000)
    # dType.SetPTPCmdEx(api, 4, 0, 0, 0, 0, 1)
    dType.SetPTPCmdEx(api, 4, (-90), 0, 0, 0, 1)
    dType.SetPTPCmdEx(api, 4, (-90), 53, 60, 0, 1)
    dType.SetEndEffectorSuctionCupEx(api, 0, 1)
    dType.dSleep(250)
    dType.SetPTPCmdEx(api, 4, (-90), 0, 0, 0, 1)


def move_box_back(api: dType):
    dType.SetPTPCmdEx(api, 4, (-90), 0, 0, 0, 1)
    dType.SetPTPCmdEx(api, 4, (-90), 54, 60, 0, 1)
    dType.SetEndEffectorSuctionCupEx(api, 1, 1)
    dType.dSleep(1000)
    # dType.SetPTPCmdEx(api, 4, (-90), 0, 0, 0, 1)
    dType.SetPTPCmdEx(api, 4, 0, 0, 0, 0, 1)
    dType.SetPTPCmdEx(api, 4, 0, 53, 60, 0, 1)
    dType.SetEndEffectorSuctionCupEx(api, 0, 1)
    dType.dSleep(250)
    dType.SetPTPCmdEx(api, 4, 0, 0, 0, 0, 1)
