package com.example.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class GreeterClient {
    // aqui é o stub que possibilita o acesso ao serviço gRPC
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    public GreeterClient() {
        // um channel define a localização do serviço gRPC
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        // cria o stub de forma a usar o channel
        this.blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public double adiciona(double p1, double p2) {
        Operandos req1 = Operandos.newBuilder().setP1(p1).build();
        Operandos req2 = Operandos.newBuilder().setP1(p2).build();

        // faz a chamada a sayHello, e obtém a reposta
        Resultado response = Resultado.newBuilder().setResultado(p1+p2).build();

        return response.getResultado();
    }
}