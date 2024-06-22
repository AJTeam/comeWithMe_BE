package com.appjam.come_with_me.handler;

import com.appjam.come_with_me.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private final UserService userService;
    private ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = session.getHandshakeHeaders().getFirst("token");
        if (token == null || token.isEmpty()) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        String roomId = getRoomId(token);
        roomSessions.putIfAbsent(roomId, new CopyOnWriteArraySet<>());
        roomSessions.get(roomId).add(session);

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String token = session.getHandshakeHeaders().getFirst("token");
        if (token == null || token.isEmpty()) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        String roomId = getRoomId(token);

        for (WebSocketSession webSocketSession : roomSessions.get(roomId)) {
            webSocketSession.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String token = session.getHandshakeHeaders().getFirst("token");
        if (token == null || token.isEmpty()) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        String roomId = getRoomId(token);
        roomSessions.get(roomId).remove(session);
    }

    private String getRoomId(String token) {
        return userService.getUserByUserToken(token).getRoom().getId().toString();
    }
}

