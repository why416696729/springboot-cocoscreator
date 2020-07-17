cc.Class({
    extends: cc.Component,

    properties: {
        login:cc.Button,
        register:cc.Button,
        passWord:{
            default: null,
            type: cc.EditBox
        },
        Name: {
            default: null,
            type: cc.EditBox
        },
        gameStart: cc.Boolean,
        denglu:cc.Node,
        zhuce:cc.Node,
        Alert: cc.Node,
        AlertMsg: cc.Label,
        rName:{
            default: null,
            type: cc.EditBox
        },
        rPw:{
            default: null,
            type: cc.EditBox
        },
        rTel:{
            default: null,
            type: cc.EditBox
        },
        rRegister:cc.Button,
        AlertButton:cc.Button,
        rBack:cc.Button
    },

    // LIFE-CYCLE CALLBACKS:

    onLoad () {
        this.login.node.on('click', this.DengLu, this);
        this.register.node.on('click', this.showZhuCe, this);
        this.rRegister.node.on('click',this.ZhuCe, this);
        this.AlertButton.node.on('click',this.closeNow, this);
        this.rBack.node.on('click',this.back, this);
    },

    start () {

    },

    DengLu() {
        if (this.LoginAlert(this.Name,"请输入账号")&&
        this.LoginAlert(this.passWord,"请输入密码")){
            var userDto = {
                userName : this.Name.string,
                password : this.passWord.string
            }
            window.userid = userDto.userName
            console.log("登陆："+window.LOGIN_URL)
            var that = this;
            window.HttpHelper.httpPost(window.LOGIN_URL, userDto,function (data) {
                that.Alert.active = true
                that.AlertMsg.string = data.msg
                that.gameStart = data.data
            });
        }
    },

    showZhuCe(){
        this.denglu.active = false;
        this.zhuce.active = true;
    },

    closeNow(){
        if (this.gameStart==true){
            cc.director.loadScene("gameSence")
             
        }else{
            this.Alert.active = false;
        }
        
       
    },

    back(){
        this.denglu.active = true;
        this.zhuce.active = false;
    },

    ZhuCe() {
        if (this.LoginAlert(this.rName,"请输入账号")&&
        this.LoginAlert(this.rPw,"请输入密码")&&
        this.LoginAlert(this.rTel,"请输入手机号")){
            var userDto = {
                userName : this.rName.string,
                password : this.rPw.string,
                telephone: this.rTel.string
            }
            console.log("注册："+window.REGISTER_URL)
            var that = this;
            window.HttpHelper.httpPost(window.REGISTER_URL, userDto,function (data) {
                that.Alert.active = true
                that.AlertMsg.string = data.msg
            });
        }
    },

    LoginAlert(node,str){
        if (node.string == ""){
            this.Alert.active = true;
            this.AlertMsg.string = str
            return false;
        }
        return true;
    },
    // update (dt) {},
    
});


