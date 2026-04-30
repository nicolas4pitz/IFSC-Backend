package com.example.grpc;

import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class GreeterService {
    public void adiciona(Operandos request, StreamObserver<Resultado> responseObserver) {
        double p1 = request.getP1();
        double p2 = request.getP1();
        Resultado response = Resultado.newBuilder().setResultado(p1+p2).build();
        // envia a resposta
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void subtrai(Operandos request, StreamObserver<Resultado> responseObserver) {
        double p1 = request.getP1();
        double p2 = request.getP1();
        Resultado response = Resultado.newBuilder().setResultado(p1-p2).build();
        // envia a resposta
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void multiplica(Operandos request, StreamObserver<Resultado> responseObserver) {
        double p1 = request.getP1();
        double p2 = request.getP1();
        Resultado response = Resultado.newBuilder().setResultado(p1*p2).build();
        // envia a resposta
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void divide(Operandos request, StreamObserver<Resultado> responseObserver) {
        double p1 = request.getP1();
        double p2 = request.getP1();
        Resultado response = Resultado.newBuilder().setResultado(p1/p2).build();
        // envia a resposta
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
