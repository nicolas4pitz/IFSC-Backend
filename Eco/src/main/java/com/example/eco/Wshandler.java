package com.example.eco;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Wshandler extends TextWebSocketHandler {

    final ObjectMapper mapper = new ObjectMapper();
    ConcurrentHashMap<String, WebSocketSession> clientes = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Cria a mensagem de resposta, que contém o texto recebido por meio deste websock
            try {
                for (int j = 0; j < clientes.size(); j++) {
                    // cria um objeto Dado
                    Dado dado = new Dado(j, message.getPayload());

                    // converte o objeto Dado para JSON
                    String conteudo = mapper.writeValueAsString(dado);

                    // encapsula a string JSON em uma mensagem a ser enviada para o cliente
                    for (var client: clientes.values()){
                        client.sendMessage(message);
                    }
                }
            } catch (Exception e) {
                return;
            }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        // chama o método herdado ... ele deve fazer algo importante
        super.afterConnectionEstablished(session);
        clientes.put(session.getId(), session);
        // faz algo de interessante quando o websocket estiver conectado
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        // chama o método herdado ... também deve fazer algo importante
        super.afterConnectionClosed(session, status);
        // e faz algo necessário (?!) quando esse websocket for fechado
        clientes.remove(session.getId());
    }

}
