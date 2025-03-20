package com.btec.chessgame_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
//
//@Controller
//public class ChessGameController {
//
//    @MessageMapping("/move") // Clients send moves here
//    @SendTo("/game/moves") // Broadcast to all subscribers
//    public ChessMove processMove(ChessMove move) {
//        System.out.println("Received move: " + move.getFrom() + " to " + move.getTo());
//        return move; // Sends back move details to clients
//    }
//}

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.annotation.SendToUser;

@Controller
public class ChessGameController {
    private final GameRoomManager gameRoomManager;
    private final SimpMessagingTemplate messagingTemplate;


    @Autowired
    public ChessGameController(GameRoomManager gameRoomManager, SimpMessagingTemplate messagingTemplate) {
        this.gameRoomManager = gameRoomManager;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/joinRoom")
    public void joinRoom(@Payload JoinRequest request) {
        String roomId = request.getRoomId();
        String playerId = request.getPlayerId();

        boolean roomReady = gameRoomManager.addPlayerToRoom(roomId, playerId);

        if (roomReady) {
            System.out.println("Room " + roomId + " is now full. Players ready!");
            messagingTemplate.convertAndSend("/room/" + roomId, new GameMessage("connected"));
        }
    }

    @MessageMapping("/move")
    @SendTo("/room/{roomId}")
    public ChessMove receiveMove(@Payload ChessMove move) {
        return move; // Broadcast move to opponent
    }
}


