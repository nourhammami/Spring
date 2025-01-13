package com.example.pfe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;


@Slf4j
@Component
public class TutorialHandler extends TextWebSocketHandler {


    //private static final Logger log = LoggerFactory.getLogger(TutorialHandler.class);

    //private static final Logger log = LoggerFactory.getLogger(MyWebSocketHandler.class);
    public final Map<Long, WebSocketSession> sessionList = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        long restaurantId = 0;

        // Ensure that the session URI is not null
        if (Objects.nonNull(session.getUri())) {
            String query = session.getUri().getQuery();

            // Check if the query string exists and contains an '=' symbol to parse the restaurant ID
            if (query != null && query.contains("=")) {
                String idRest = query.split("=")[1];
                try {
                    // Try to parse the restaurant ID
                    restaurantId = Long.parseLong(idRest);
                } catch (NumberFormatException e) {
                    log.error("Invalid restaurant ID format in query: {}", idRest);
                    session.close(CloseStatus.BAD_DATA);
                    return;  // Exit if the format is invalid
                }
            }
        }

        // Ensure only one session per restaurant ID
        WebSocketSession existingSession = sessionList.get(restaurantId);
        if (existingSession == null || !existingSession.isOpen()) {
            // No existing session or the existing session is closed
            sessionList.put(restaurantId, session);
            log.info("Connection established for restaurant ID {}: {}", restaurantId, session.getId());
        } else {
            // There is an existing open session, close the duplicate session
            log.warn("Connection already exists for restaurant ID {}. Closing duplicate session: {}", restaurantId, session.getId());
            session.close();
        }

        // Log current session list and size for debugging purposes
        log.info("Current session list: {}", sessionList);
        log.info("Number of active sessions: {}", sessionList.size());
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        PayloadMessage payloadMessage = mapper.readValue(message.getPayload(), PayloadMessage.class);
        String payload = message.getPayload();


        long restaurantId = payloadMessage.getRestaurantId();
        WebSocketSession targetSession = sessionList.get(restaurantId);

        if (targetSession != null && targetSession.isOpen()) {
            targetSession.sendMessage(new TextMessage("Message from client: " + payloadMessage.getAction()));
            log.info("Sent message to restaurant ID {}: {}", restaurantId, payloadMessage.getAction());
            //if (payload.contains("Message from client:")) {
            // Execute your action here
            //performAction();
            // }
        } else {
            log.warn("No active session found for restaurant ID {}: Ignoring message", restaurantId);
        }
    }



    private void performAction() {
        // Implement the action you want to execute
        System.out.println("Action executed because the specific WebSocket message was received.");
        // You can also invoke a Spring service or any other component here
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionList.values().remove(session);
        log.info("Session closed: {}", session.getId());
    }
}



