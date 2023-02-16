import UIKit
import AgoraUIKit


@objc(AgoraVideoUIKit) class AgoraVideoUIKit : CDVPlugin {

    var pluginResult = CDVPluginResult(status: .error)

    // Fill the App ID of your project generated on Agora Console.
    var appId: String = ""
    // Fill the temp token generated on Agora Console.
    var token: String? = ""
    // Fill the channel name.
    var channelName: String = ""
    // Create the view object.
    var agoraView: AgoraVideoViewer!

    @objc(initializeAndJoinChannel:) 
    func initializeAndJoinChannel(_ command: CDVInvokedUrlCommand) {
        DispatchQueue.main.async {
            let appIda = command.arguments[0] as? String
            let tokena = command.arguments[1] as? String
            let channelNamea = command.arguments[2] as? String
            let uida = command.arguments[3] as? UInt
            if let appid = appIda, let tkn = tokena, let cn = channelNamea, let uid = uida {
                if appid != "" && tkn != "" && cn != "" {
                    let av: AgoraVideo = AgoraVideo(appId: appid, token:tkn, channelName:cn, uid: uid)
                    self.viewController?.show(av, sender: self)
                }
            }
            self.pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Joined successfully!!")
            self.commandDelegate!.send(self.pluginResult, callbackId: command.callbackId)
        }
    }
}


class AgoraVideo: UIViewController {
    var appId: String = ""
    var token: String? = ""
    var channelName: String = ""
    var uid: UInt;
    var agoraView: AgoraVideoViewer!
    convenience init() {
            self.init()
        }
    init(appId: String, token: String, channelName: String, uid: UInt) {
            print("convenience Init")
            self.appId = appId
            self.token = token
            self.channelName = channelName
            self.uid = uid
            super.init(nibName: nil, bundle: nil)
        }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        videoView(appId: self.appId, token: self.token as? String ?? "", channelName: self.channelName, uid: self.uid)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        print("Exiting agora!")
        agoraView.exit()
    }
    
    func videoView(appId: String, token: String, channelName: String, uid: UInt)
        {
//            let appId1 = "2214583653f34a8ea2dcafca28ce2165"
//            let token1 = "007eJxTYNC8ULHONT5M6tCjQ0cunPWvZLhc1/q4JeTz2+8aeb4fPqorMBgZGZqYWhibmRqnGZskWqQmGqUkJ6YlJxpZJKcaGZqZsj14ktwQyMiwObyWhZEBAkF8FoaS1OISBgYACgIiMQ=="
            print("Inside AgoraVideo class!! ")
            print("****************************")
            print("appId:: ", appId)
            print("token:: ", token)
            print("channelName:: ", channelName)
            print("****************************")
            
            self.agoraView = AgoraVideoViewer(
                connectionData: AgoraConnectionData(
                    appId: appId,
                    rtcToken: token
                )
            )
            
//            self.agoraView = AgoraVideoViewer(
//                connectionData: AgoraConnectionData(
//                    appId: appId1,
//                    rtcToken: token1
//                )
//            )
            
            self.agoraView.fills(view: self.view)
            let status = self.agoraView.join(
                channel: channelName,
                with: token,
                as: .broadcaster,
                uid: uid
            )
            
//            let status = self.agoraView.join(
//                channel: channelName,
//                with: token1,
//                as: .broadcaster
//            )
            print("JOIN STATUS::", status);
        }
}
