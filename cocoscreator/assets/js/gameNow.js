// Learn cc.Class:
//  - [Chinese] https://docs.cocos.com/creator/manual/zh/scripting/class.html
//  - [English] http://docs.cocos2d-x.org/creator/manual/en/scripting/class.html
// Learn Attribute:
//  - [Chinese] https://docs.cocos.com/creator/manual/zh/scripting/reference/attributes.html
//  - [English] http://docs.cocos2d-x.org/creator/manual/en/scripting/reference/attributes.html
// Learn life-cycle callbacks:
//  - [Chinese] https://docs.cocos.com/creator/manual/zh/scripting/life-cycle-callbacks.html
//  - [English] https://www.cocos2d-x.org/docs/creator/manual/en/scripting/life-cycle-callbacks.html

var web = require("webSocket");

var Gamer = {
    wait  : 0,
    white : 1,
    black : 2,
    over : 3,
};

cc.Class({
    extends: cc.Component,

    properties: {
        whiteSp:{//白棋的图片
            default:null,
            type:cc.SpriteFrame
        },
        blackSp:{//黑棋的图片
            default:null,
            type:cc.SpriteFrame
        },
        qiziPf:{//棋子的预制资源
            default:null,
            type:cc.Prefab
        },
        qiziList:{//棋子节点的集合，用一维数组表示二维位置
            default: [],
            type: [cc.node]
        },
        gamer:{//当前游戏人
            default:Gamer.white,
            type:cc.Integer
        },
        luozi:{
            default:null,
            type:cc.Node,
            visible:false//属性窗口不显示
        },
        overLabel:{
            default:null,
            type:cc.Label
        },
        overNode:{
            default:null,
            type:cc.Node
        },
        overButton:{
            default:null,
            type:cc.Button
        },
        fiveGroup:[],//五元组
        
        fiveGroupScore:[],//五元组分数
        zhunbeiButton:{
            default:null,
            type:cc.Button
        },
        zhunbeiSp:{
            default:null,
            type:cc.Sprite
        },
        zhunbeiLab:{
            default:null,
            type:cc.Label
        },
        myGamer:0,
        nowGamer:0,
        lastGamer:0,
        winGamer:0,
    },

    // LIFE-CYCLE CALLBACKS:

    onLoad () {
        //本地数据存储
        // cc.sys.localStorage.setItem('userid',"10");
        // console.log(cc.sys.localStorage.getItem('userid'))

        this.zhunbeiSp.node.on(cc.Node.EventType.TOUCH_START, function (event) {}, this);

        //socket连接
        this.ws = web.getInstance();
        this.ws.wsStart(window.userid)
        this.msg = new Array()
        this.ws.omsg(this)
        

        var that = this;
        //初始化棋盘上225个棋子节点，并为每个节点添加事件
        for(var y = 0;y<15;y++){
            for(var x = 0;x < 15;x++){
                var newNode = cc.instantiate(this.qiziPf);//复制Chess预制资源
                this.node.addChild(newNode);
                newNode.setPosition(cc.p(x*35+22,y*35+25));//根据棋盘和棋子大小计算使每个棋子节点位于指定位置
                newNode.mtag = y*15+x;//根据每个节点的tag就可以算出其二维坐标
                newNode.on(cc.Node.EventType.TOUCH_END,function(event){
                    console.log("当前：",that.nowGamer," 我的：",that.myGamer)
                    if (that.myGamer == that.nowGamer){
                        var node = event.target
                        if(node.getComponent(cc.Sprite).spriteFrame == null){
                            that.myluozi(node.mtag)
                            // console.log(that.gamer)
                            // if (that.gamer == Gamer.white){
                            //     node.getComponent(cc.Sprite).spriteFrame = that.whiteSp
                            // }
                            // else if(that.gamer == Gamer.black){
                            //     node.getComponent(cc.Sprite).spriteFrame = that.blackSp
                            // }
                            // that.luozi = event.target
                            // that.checkWin()
                            // if(this.gamer == Gamer.black){
                            //     that.scheduleOnce(function(){that.ai()},1);//延迟一秒电脑下棋
                            // }
                        }
                    }
                });
                this.qiziList.push(newNode);
            }
        }
    },

    start () {
        
    },
    
    checkWin:function(){
        console.log(this.luozi)
        var x0 = this.luozi.mtag % 15;
        var y0 = parseInt(this.luozi.mtag / 15);
        //判断横向
        var fiveCount = 0;
        for(var x = 0;x < 15;x++){
            if((this.qiziList[y0*15+x].getComponent(cc.Sprite)).spriteFrame === this.luozi.getComponent(cc.Sprite).spriteFrame){
                fiveCount++; 
                if(fiveCount==5){
                    if(this.gameState == Gamer.white){
                        this.overLabel.string = "你赢了";
                    }else{
                        this.overLabel.string = "你输了";
                    }
                    this.overNode.active = true;
                    this.gameState = Gamer.over;
                    return;
                }
            }else{
                fiveCount=0;
            }
        }
        //判断纵向
        fiveCount = 0;
        for(var y = 0;y < 15;y++){
            if((this.qiziList[y*15+x0].getComponent(cc.Sprite)).spriteFrame === this.luozi.getComponent(cc.Sprite).spriteFrame){
                fiveCount++; 
                if(fiveCount==5){
                    if(this.gameState == Gamer.white){
                        this.overLabel.string = "你赢了";
                    }else{
                        this.overLabel.string = "你输了";
                    }
                    this.overNode.active = true;
                    this.gameState = Gamer.over;
                    return;
                }
            }else{
                fiveCount=0;
            }
        }
        //判断右上斜向
        var f = y0 - x0;
        fiveCount = 0;
        for(var x = 0;x < 15;x++){
            if(f+x < 0 || f+x > 14){
                continue;
            }
            if((this.qiziList[(f+x)*15+x].getComponent(cc.Sprite)).spriteFrame === this.luozi.getComponent(cc.Sprite).spriteFrame){
                fiveCount++; 
                if(fiveCount==5){
                    if(this.gameState == Gamer.white){
                        this.overLabel.string = "你赢了";
                    }else{
                        this.overLabel.string = "你输了";
                    }
                    this.overNode.active = true;
                    this.gameState = Gamer.over;
                    return;
                }
            }else{
                fiveCount=0;
            }
        }
        //判断右下斜向
        f = y0 + x0;
        fiveCount = 0;
        for(var x = 0;x < 15;x++){
            if(f-x < 0 || f-x > 14){
                continue;
            }
            if((this.qiziList[(f-x)*15+x].getComponent(cc.Sprite)).spriteFrame === this.luozi.getComponent(cc.Sprite).spriteFrame){
                fiveCount++; 
                if(fiveCount==5){
                    if(this.gameState == Gamer.white){
                        this.overLabel.string = "你赢了";
                    }else{
                        this.overLabel.string = "你输了";
                    }
                    this.overNode.active = true;
                    this.gameState = Gamer.over;
                    return;
                }
            }else{
                fiveCount=0;
            }
        }
        //没有输赢交换下子顺序
        if(this.myGamer == Gamer.black){
            this.myGamer = Gamer.white;
        }else if(this.myGamer == Gamer.white){
            this.myGamer = Gamer.black;
        }
    },

    perpare:function(){
        var sendMsg = {
            type:1,
            msg:true
        }
        this.ws.send(JSON.stringify(sendMsg))
    },
    myluozi:function(tag){
        var sendMsg = {
            type:3,
            msg:tag
        }
        this.ws.send(JSON.stringify(sendMsg))
    },

    showOver:function(){
        this.overLabel.string = this.winGamer==Gamer.white?"白棋胜":"黑棋胜"
        this.overNode.active = true
       
    },

    changeQizi:function(data){
        if (this.lastGamer == Gamer.white){
            this.qiziList[Number(data.tag)].getComponent(cc.Sprite).spriteFrame = this.whiteSp
        }
        else if(this.lastGamer == Gamer.black){
            this.qiziList[Number(data.tag)].getComponent(cc.Sprite).spriteFrame = this.blackSp
        }
    },

    reLoadSence(){
        this.overNode.active = false
        this.zhunbeiLab.string = "准备"
        this.zhunbeiButton.interactable = true
        this.zhunbeiSp.node.active = true
        this.zhunbeiButton.node.active = true
        for (let index = 0; index < 225; index++) {
            this.qiziList[index].getComponent(cc.Sprite).spriteFrame = null;
            
        }
    },

    addMsg(data){
        this.msg.push(data)
        this.runMsg()
    },
    
    runMsg(){
        if (this.msg.length > 0){
            console.log("消息处理---------",this.msg[0])
            switch (this.msg[0].code){
                case 1: 
                if (this.msg[0].fromUserId == window.userid){
                    // this.zhunbeiSp.node.active = false
                    this.zhunbeiLab.string = "已准备"
                    this.zhunbeiButton.interactable = false
                }
                ;break;
                case 2: 
                    this.zhunbeiSp.node.active = false
                    this.zhunbeiButton.node.active = false
                    this.myGamer = this.msg[0].data.myGamer
                    this.nowGamer = this.msg[0].data.nowGamer
                ;break;
                case 3:
                    this.lastGamer = this.msg[0].data.lastGamer
                    this.nowGamer = this.msg[0].data.nowGamer
                    this.changeQizi(this.msg[0].data)
                ;break;
                case 4:
                    this.lastGamer = this.msg[0].data.lastGamer
                    this.winGamer = this.msg[0].data.winUser
                    this.nowGamer = Gamer.over
                    this.showOver()
                ;break;
            }
            this.msg.splice(0,1); 
        }
    },

    update (dt) {
        
    },
});
