package com.dtbid.dropthebid.chat.config;

import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

  private HashMap<String, WebSocketSession> sessionMap = new HashMap<>();
  
  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    String msg = message.getPayload();
    JSONObject obj = JsonToObjectParser(msg);
    
    for (String key : sessionMap.keySet()) {
      WebSocketSession wss = sessionMap.get(key);
      try {
        wss.sendMessage(new TextMessage(obj.toJSONString()));
      } catch (Exception e) { 
        e.printStackTrace();
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    super.afterConnectionEstablished(session);
    sessionMap.put(session.getId(), session);
    
    JSONObject obj = new JSONObject();
    obj.put("type", "getId");
    obj.put("sessionId", session.getId());
    session.sendMessage(new TextMessage(obj.toJSONString()));
  }
  
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    sessionMap.remove(session.getId());
    super.afterConnectionClosed(session, status);
  }
  
  private static JSONObject JsonToObjectParser(String jsonStr) {
    JSONParser parser = new JSONParser();
    JSONObject obj = null;
    
    try {
      obj = (JSONObject) parser.parse(jsonStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    
    return obj;
  }
}
