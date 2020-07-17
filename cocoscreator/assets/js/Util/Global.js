// window.GAME_URL= "http://localhost/";
window.GAME_URL= "http://39.108.251.128/";
// window.WEB_SOCKET = "ws://localhost/imserver/"
window.WEB_SOCKET = "ws://39.108.251.128/imserver/"

window.LOGIN_URL = window.GAME_URL+"user/login";
window.REGISTER_URL = window.GAME_URL+"user/register";
window.userid = "10";

cc.Class({
    extends: cc.Component,
});
