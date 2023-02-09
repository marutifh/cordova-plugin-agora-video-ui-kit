var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'AgoraVideoUIKit', 'coolMethod', [arg0]);
};

exports.initializeAndJoinChannel = function (options, success, error) {
    if (!options.appId || !options.token || !options.channelName) {
        error("Mandatory fields are missing!");
        return;
    }
    var args = [options.appId, options.token, options.channelName];
    exec(success, error, 'AgoraVideoUIKit', 'initializeAndJoinChannel', args);
};