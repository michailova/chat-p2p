package com.example.chat;

import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomManager {

    private final Map<String, List<WebSocket>> rooms =
            new HashMap<>();

    public synchronized boolean joinRoom(
            String roomName,
            WebSocket connection) {

        List<WebSocket> clients =
                rooms.computeIfAbsent(
                        roomName,
                        key -> new ArrayList<>()
                );

        if (clients.size() >= 2) {
            return false;
        }

        clients.add(connection);

        return true;
    }

    public synchronized void leaveRoom(
            String roomName,
            WebSocket connection) {

        List<WebSocket> clients = rooms.get(roomName);

        if (clients == null) {
            return;
        }

        clients.remove(connection);

        if (clients.isEmpty()) {
            rooms.remove(roomName);
        }
    }

    public synchronized List<WebSocket> getClients(
            String roomName) {

        return new ArrayList<>(
                rooms.getOrDefault(
                        roomName,
                        new ArrayList<>()
                )
        );
    }
}



