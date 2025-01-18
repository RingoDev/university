import DobotDllType as dType
from dobot import move_box, CON_STR

if __name__ == '__main__':
    # Load Dll and get the CDLL object
    api = dType.load()
    # Connect Dobot
    state = dType.ConnectDobot(api, "", 115200)[0]
    print("Connect status:", CON_STR[state])

    if state == dType.DobotConnect.DobotConnect_NoError:

        # Clean Command Queued
        dType.SetQueuedCmdClear(api)

        move_box(api)

        # # Async Motion Params Setting
        dType.SetHOMEParams(api, 0, 0, 0, 0, isQueued=1)

        # # Async Home
        dType.SetHOMECmd(api, temp=0, isQueued=1)

        # Start to Execute Command Queue
        dType.SetQueuedCmdStartExec(api)

        # Stop to Execute Command Queued
        dType.SetQueuedCmdStopExec(api)

    # Disconnect Dobot
    dType.DisconnectDobot(api)


