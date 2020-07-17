var webSocketHelper = cc.Class({
    extends: cc.Component,

    wsStart:function(id){
        var s = window.WEB_SOCKET+id;
        console.log(s)
        this.ws = new WebSocket(s);
        this.ws.onopen = function (event) {
            console.log("已连接.");
        }.bind(this);
        // this.ws.onmessage = function (event) {
        //     // let data = JSON.parse(event.data);
        //     console.log("收到消息: " + event.data);
        //     callback(event.data)
        // }.bind(this);
        this.ws.onerror = function (event) {
            console.log("发生错误");
        }.bind(this);
        this.ws.onclose = function (event) {
            console.log("已关闭");
        }.bind(this);
    },

    send:function(msg){
        this.ws.send(msg);
    },
    omsg:function(that){
        this.ws.onmessage = function (event) {
            // let data = JSON.parse(event.data);
            console.log("收到消息: " + event.data);
            var d = JSON.parse(event.data)
            that.addMsg(d)
        }.bind(this);
    },
    //单例
    statics:{
        self:null,
 
        getInstance:function()
        {
            if (webSocketHelper.self == null) {
 
                var node=new cc.Node('webSocketHelper');
 
                webSocketHelper.self = node.addComponent(webSocketHelper);
             };
 
            return webSocketHelper.self;
        },
    },
 
    //构造函数
    ctor(){
        webSocketHelper.self = this;
 
        this.scene = new Array();
    },

});
module.exports = webSocketHelper;