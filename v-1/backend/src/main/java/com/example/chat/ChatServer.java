package com.example.chat;

import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ChatServer {

    public static void main(String[] args) {

        int port = 8080;

        WebSocketHandler handler =
                new WebSocketHandler();

        WebSocketServer server =
                new WebSocketServer(
                        new InetSocketAddress(port)
                ) {

                    @Override
                    public void onOpen(
                            org.java_websocket.WebSocket connection,
                            org.java_websocket.handshake.ClientHandshake handshake) {

                        handler.onOpen(connection);
                    }

                    @Override
                    public void onClose(
                            org.java_websocket.WebSocket connection,
                            int code,
                            String reason,
                            boolean remote) {

                        handler.onClose(connection);
                    }

                    @Override
                    public void onMessage(
                            org.java_websocket.WebSocket connection,
                            String message) {

                        handler.onMessage(
                                connection,
                                message
                        );
                    }

                    @Override
                    public void onError(
                            org.java_websocket.WebSocket connection,
                            Exception exception) {

                        handler.onError(
                                connection,
                                exception
                        );
                    }

                    @Override
                    public void onStart() {

                        System.out.println(
                                "WebSocket server started on port "
                                        + port
                        );
                    }
                };

        server.start();
    }
}