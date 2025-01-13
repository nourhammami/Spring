package com.example.pfe;

import ch.qos.logback.classic.Logger;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
public class WebSocketController extends TextWebSocketHandler {
    //  private final SimpMessagingTemplate messagingTemplate;
//
//    @MessageMapping("/send-action")
//    @SendTo("/topic/actions")
//    public String handleAction(String action) {
//        // Process the action received from the client
//        System.out.println("Received action: " + action);
//        return action;  // Echo the action back to subscribers
//    }
    @Autowired
    private TutorialHandler tutorialHandler;
    //List<Long> idsResto = new ArrayList<>();


    @GetMapping(value= "/nbre")
    public int handleNbre(){
        return tutorialHandler.sessionList.size();
    }

//    @GetMapping(value = "/test")
//    public ResponseEntity<Object> handleTextMessage(String text) {
//        Set<Long> idsResto = tutorialHandler.sessionList.keySet();
//
//        return new ResponseEntity<>(idsResto, HttpStatus.ACCEPTED) ;
//    }
@GetMapping("/test")
public ResponseEntity<List<Long>> getConnectedRestaurants() {
    Set<Long> connectedRestaurants = tutorialHandler.sessionList.keySet(); // assuming sessionList holds your connections
    return ResponseEntity.ok(new ArrayList<>(connectedRestaurants));
}
    private static final Logger logger = (Logger) LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/sendMessage")
    public void handleMessage(@Payload PayloadMessage message) {
        logger.info("Received message: restaurantId={}, action={}", message.getRestaurantId(), message.getAction());
        // Add additional processing logic here if needed
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        // Process your message here
        System.out.println("Received message: " + payload);
    }

    @MessageMapping("/processMessage")
    public void processIncomingMessage(@Payload PayloadMessage message) {
        logger.info("Processing incoming message: restaurantId={}, action={}", message.getRestaurantId(), message.getAction());
        // Add your processing logic here
    }

    @MessageMapping("/sendState")
    @SendTo("/topic/states")
    public String sendState(String state) {
        System.out.println("Received state: " + state);
        return state;
    }
    @MessageMapping("/websocket")
    @SendTo("/topic/updates")
    public String handleWebSocketMessage(@Payload String message, @Header("id_restaurant") String idRestaurant) {
        System.out.println("Received message: " + message + " for restaurant: " + idRestaurant);
        return "Message processed";
    }
    @GetMapping("/")
    public String home() {
        return "Welcome to Spring Boot!";
    }
//    @GetMapping(value = "/test")
//    public ResponseEntity<Set<Long>> handleTextMessage(@RequestParam("text") String text) {
//        Set<Long> idsResto = tutorialHandler.sessionList.keySet();
//        return new ResponseEntity<>(idsResto, HttpStatus.ACCEPTED);
//    }

}

