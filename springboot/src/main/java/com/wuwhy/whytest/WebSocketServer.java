package com.wuwhy.whytest;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuwhy.whytest.Dto.GamerDto;
import com.wuwhy.whytest.enums.Gamer;
import com.wuwhy.whytest.service.WuZiQiService;
import com.wuwhy.whytest.utils.ResultMap;
import com.wuwhy.whytest.utils.Validator;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint("/imserver/{userId}")
@Component
public class WebSocketServer {

    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";

    private WuZiQiService wuZiQiService = new WuZiQiService();

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        this.session = session;
        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
            //加入set中
        }else{
            webSocketMap.put(userId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        LOGGER.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

        try {
            ResultMap resultMap = new ResultMap();
            resultMap.message("连接成功");
            sendMessage(JSON.toJSONString(resultMap));
        } catch (IOException e) {
            LOGGER.error("用户:"+userId+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
            wuZiQiService.delGame(userId);
        }
        LOGGER.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                String s = jsonObject.getString("msg");
                //追加发送人(防止串改)
//                jsonObject.put("fromUserId",this.userId);
                String toUserId=jsonObject.getString("toUserId");
                ResultMap resultMap = new ResultMap();
                int type = jsonObject.getInteger("type");
                resultMap.put("fromUserId",this.userId);
                switch (type){
                    case 1:
                        if (wuZiQiService.addGame(this.userId)){
                            int i = 1;
                            resultMap.code(2);
                            for (String key : webSocketMap.keySet()) {
                                WebSocketServer value = webSocketMap.get(key);
                                GamerDto gamerDto = new GamerDto();
                                gamerDto.setMyGamer(i++);
                                gamerDto.setNowGamer(Gamer.white.ordinal());
                                resultMap.data(gamerDto);
                                LOGGER.info("群发用户消息:"+value.userId+",报文:"+JSON.toJSONString(resultMap));
                                webSocketMap.get(value.userId).sendMessage(JSON.toJSONString(resultMap));
                            }
                        }else{
                            resultMap.code(1);
                            for (String key : webSocketMap.keySet()) {
                                WebSocketServer value = webSocketMap.get(key);
                                LOGGER.info("群发用户消息:"+value.userId+",报文:"+JSON.toJSONString(resultMap));
                                webSocketMap.get(value.userId).sendMessage(JSON.toJSONString(resultMap));
                            }
                        };break;
                    case 3:
                        resultMap = wuZiQiService.checkWin(s);
                        GamerDto gamerDto = (GamerDto)resultMap.get("data");

                        resultMap.code(3);
                        for (String key : webSocketMap.keySet()) {
                            WebSocketServer value = webSocketMap.get(key);
                            LOGGER.info("群发用户消息:"+value.userId+",报文:"+JSON.toJSONString(resultMap));
                            webSocketMap.get(value.userId).sendMessage(JSON.toJSONString(resultMap));
                        }

                        if (gamerDto.getWinUser() != 0){
                            resultMap = wuZiQiService.gameOver();
                            resultMap.code(4);
                            for (String key : webSocketMap.keySet()) {
                                WebSocketServer value = webSocketMap.get(key);
                                LOGGER.info("群发用户消息:"+value.userId+",报文:"+JSON.toJSONString(resultMap));
                                webSocketMap.get(value.userId).sendMessage(JSON.toJSONString(resultMap));
                            }
                            wuZiQiService.reLoad();
                        }
                        ;break;

                }

//                //私发消息
//                String s = jsonObject.getString("msg");
//                resultMap = wuZiQiService.checkWin(s);
//                resultMap.code(3);
//                LOGGER.info("发送用户消息:"+userId+",报文:"+resultMap.toString());
//                webSocketMap.get(toUserId).sendMessage(JSON.toJSONString(resultMap));break;
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    //群发用户消息
    public void  sendAllMsg(ResultMap resultMap,int code){
        try {
            resultMap.code(code);
            for (String key : webSocketMap.keySet()) {
                WebSocketServer value = webSocketMap.get(key);
                LOGGER.info("群发用户消息:"+value.userId+",报文:"+JSON.toJSONString(resultMap));
                webSocketMap.get(value.userId).sendMessage(JSON.toJSONString(resultMap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        LOGGER.info("发送消息到:"+userId+"，报文:"+message);
        if(Validator.isNotEmpty(userId)&&webSocketMap.containsKey(userId)){
            ResultMap resultMap = new ResultMap();
            resultMap.message(message);
            webSocketMap.get(userId).sendMessage(JSON.toJSONString(resultMap));
        }else{
            LOGGER.error("用户"+userId+",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}