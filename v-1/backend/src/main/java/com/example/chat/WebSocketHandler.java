package com.example.chat;

import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSocketHandler {

    private final RoomManager roomManager =
            new RoomManager();

    private final Map<WebSocket, String> clientRooms =
            new HashMap<>();


    public void onOpen(WebSocket connection) {

        System.out.println(
                "Client connected: "
                        + connection.getRemoteSocketAddress()
        );
    }


    public void onMessage(
            WebSocket connection,
            String message) {

        System.out.println(
                "Received: " + message
        );

        // Клиент хочет войти в комнату
        if (message.startsWith("JOIN ")) {

            String roomName =
                    message.substring(5).trim();

            joinRoom(
                    connection,
                    roomName
            );

            return;
        }

        // Определяем комнату клиента
        String roomName =
                clientRooms.get(connection);

        if (roomName == null) {

            connection.send(
                    "ERROR: Join a room first"
            );

            return;
        }

        // Все остальные сообщения считаем
        // signaling-сообщениями
        forwardToOtherClient(
                roomName,
                connection,
                message
        );
    }


    private void joinRoom(
            WebSocket connection,
            String roomName) {

        boolean joined =
                roomManager.joinRoom(
                        roomName,
                        connection
                );

        if (!joined) {

            connection.send(
                    "ERROR: Room is full"
            );

            return;
        }

        clientRooms.put(
                connection,
                roomName
        );

        List<WebSocket> clients =
                roomManager.getClients(roomName);

        if (clients.size() == 1) {

            connection.send(
                    "JOINED " + roomName + " CALLER"
            );

        } else {

            connection.send(
                    "JOINED " + roomName + " RECEIVER"
            );
        }

        System.out.println(
                "Client joined room: " + roomName
        );
    }


    private void forwardToOtherClient(
            String roomName,
            WebSocket sender,
            String message) {

        List<WebSocket> clients =
                roomManager.getClients(roomName);

        for (WebSocket client : clients) {

            if (client != sender) {

                client.send(message);
            }
        }
    }


    public void onClose(WebSocket connection) {

        String roomName =
                clientRooms.remove(connection);

        if (roomName != null) {

            roomManager.leaveRoom(
                    roomName,
                    connection
            );
        }

        System.out.println(
                "Client disconnected"
        );
    }


    public void onError(
            WebSocket connection,
            Exception exception) {

        System.err.println(
                "WebSocket error: "
                        + exception.getMessage()
        );
    }
}