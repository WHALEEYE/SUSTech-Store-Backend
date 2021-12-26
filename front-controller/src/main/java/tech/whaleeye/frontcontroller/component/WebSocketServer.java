package tech.whaleeye.frontcontroller.component;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import tech.whaleeye.service.ChatHistoryService;
import tech.whaleeye.service.StoreUserService;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author websocket服务
 */
@ServerEndpoint(value = "/imserver/{userId}")
@RestController
@Component
@Log4j2
public class WebSocketServer {
    //记录当前在线连接数
    public static final Map<Integer, Session> sessionMap = new ConcurrentHashMap<>();

    private static ChatHistoryService chatHistoryService;

    private static StoreUserService storeUserService;

    @Autowired
    public void setStoreUserService(StoreUserService service) {
        WebSocketServer.storeUserService = service;
    }

    @Autowired
    public void setChatHistoryService(ChatHistoryService service) {
        WebSocketServer.chatHistoryService = service;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userIdStr) {
        int userId = -1;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (Exception e) {
            log.info("传入UserId={}，不是Integer", userIdStr);
            return;
        }
        sessionMap.put(userId, session);
        log.info("有新用户加入，userId={}, 当前在线人数为：{}", userId, sessionMap.size());
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        result.set("users", array);
        for (Integer key : sessionMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("userId", key.toString());
            jsonObject.set("userName", storeUserService.getStoreUserById(key).getNickname());
            // {"userId": "1", "userName": "admin"}
            array.add(jsonObject);
        }
        // {"users": [{"userId": "12", "userName": "admin"}, { "userId": "89", "userName": "zhang"}]}
        sendAllMessage(JSONUtil.toJsonStr(result));  // 后台发送消息给所有的客户端
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("userId") String userIdStr) {
        int userId = -1;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (Exception e) {
            log.info("传入UserId={}，不是Integer", userIdStr);
            return;
        }
        sessionMap.remove(userId);
        log.info("有一连接关闭，移除userId={}的用户session, 当前在线人数为：{}", userId, sessionMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") String userIdStr) {
        log.info("服务端收到用户userId={}的消息:{}", userIdStr, message);
        JSONObject obj = JSONUtil.parseObj(message);
        int toUserId = -1;
        try {
            toUserId = Integer.parseInt(obj.getStr("to")); // to表示发送给哪个用户，比如 admin
        } catch (Exception e) {
            log.info("传入toUserId={}，不是Integer", obj.getStr("to"));
            return;
        }
        int userId = -1;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (Exception e) {
            log.info("传入UserId={}，不是Integer", userIdStr);
            return;
        }
        String text = obj.getStr("text"); // 发送的消息文本  hello
        // {"to": "admin", "text": "聊天文本"}
        Session toSession = sessionMap.get(toUserId); // 根据 to用户名来获取 session，再通过session发送消息文本
        if (toSession != null) {
            // 服务器端 再把消息组装一下，组装后的消息包含发送人和发送的文本内容
            // {"from": "zhang", "text": "hello"}
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("from", userId);  // from 是 zhang
            jsonObject.set("text", text);  // text 同上面的text
            this.sendMessage(jsonObject.toString(), toSession);
            log.info("发送给用户userId={}，消息：{}", toUserId, jsonObject.toString());
        } else {
            log.info("发送失败，尝试存贮数据库，未找到用户userId={}的session", toUserId);
        }
        // 不论用户是否在线，聊天记录存入数据库
        boolean storageResult = chatHistoryService.addChatHistory(userId, toUserId, text);
        if (!storageResult) {
            log.info("从userId={}发往toUserId={}的message={}，存入数据库失败", userId, toUserId, text);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}

