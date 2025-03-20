package com.btec.chessgame_demo;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GameRoomManager {
    private final Map<String, List<String>> rooms = new ConcurrentHashMap<>();

    public synchronized boolean addPlayerToRoom(String roomId, String playerId) {
        rooms.putIfAbsent(roomId, new CopyOnWriteArrayList<>());

        List<String> players = rooms.get(roomId);
        if (players.size() < 2 && !players.contains(playerId)) {
            players.add(playerId);
            return players.size() == 2; // Returns true if both players have joined
        }
        return false;
    }

    public List<String> getPlayersInRoom(String roomId) {
        return rooms.getOrDefault(roomId, new CopyOnWriteArrayList<>());
    }
}
