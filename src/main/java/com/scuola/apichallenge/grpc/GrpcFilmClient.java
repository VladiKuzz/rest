package com.scuola.apichallenge.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcFilmClient {
    public static void main(String[] args) {
        ManagedChannel ch = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        FilmServiceGrpc.FilmServiceBlockingStub stub = FilmServiceGrpc.newBlockingStub(ch);

        // 1) ListFilms
        FilmList list = stub.listFilms(Empty.newBuilder().build());
        System.out.println("LIST: " + list);

        // 2) CreateFilm
        Film created = stub.createFilm(Film.newBuilder()
                .setTitolo("Matrix")
                .setAnno(1999)
                .setRegista("Wachowski")
                .build());
        System.out.println("CREATED: " + created);

        ch.shutdown();
    }
}
