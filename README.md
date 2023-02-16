# Agora VideoUIKit plugin for Cordova.


## Requirements

- Android 24+
- iOS 13.0+ or a macOS 10.15 or later
- Xcode 12.3 or later
- CocoaPods (if installing with CocoaPods)


__Supported Platforms__

- Android
- iOS


## Installation

    cordova plugin add cordova-plugin-agora-video-ui-kit


## Usage

```
var param = {
	uid: 0, // User ID in integer
	appId: "Agora AppID",
	token: "Token generated from Agora",
	channelName: "channel name used"
};

cordova.plugins.AgoraVideoUIKit.initializeAndJoinChannel(
	param,
	function success(result) {
		console.log("result::", result);
	},
	function error(error) {
		console.log("error::", error);
    }
);
```