package com.dtbid.dropthebid.chat.controller;

import com.dtbid.dropthebid.chat.model.Socket;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

  @MessageMapping("/receive")
  @SendTo("/send")
  public Socket SocketHandler(Socket socket) {
    String userName = socket.getUserName();
    String content = socket.getContent();
    
    Socket result = new Socket(userName, content);
    
    return result;
  }
}
