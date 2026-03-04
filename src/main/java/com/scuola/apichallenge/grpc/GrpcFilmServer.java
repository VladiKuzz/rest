package com.scuola.apichallenge.grpc;

import com.scuola.apichallenge.film.Film;
import com.scuola.apichallenge.film.FilmRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class GrpcFilmServer {

    private final FilmRepository repo;
    private Server server;

    public GrpcFilmServer(FilmRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void start() throws Exception {
        server = ServerBuilder.forPort(9090)
                .addService(new FilmServiceGrpc.FilmServiceImplBase() {

                    @Override
                    public void listFilms(Empty request, StreamObserver<FilmList> responseObserver) {
                        var list = FilmList.newBuilder();
                        for (Film f : repo.findAll()) list.addFilms(toProto(f));
                        responseObserver.onNext(list.build());
                        responseObserver.onCompleted();
                    }

                    @Override
                    public void getFilm(FilmId request, StreamObserver<com.scuola.apichallenge.grpc.Film> responseObserver) {
                        var film = repo.findById(request.getId()).orElse(null);
                        if (film == null) {
                            responseObserver.onError(new IllegalArgumentException("Film non trovato"));
                            return;
                        }
                        responseObserver.onNext(toProto(film));
                        responseObserver.onCompleted();
                    }

                    @Override
                    public void createFilm(com.scuola.apichallenge.grpc.Film request, StreamObserver<com.scuola.apichallenge.grpc.Film> responseObserver) {
                        Film saved = repo.save(new Film(
                                request.getId().isBlank() ? null : request.getId(),
                                request.getTitolo(),
                                request.getAnno(),
                                request.getRegista()
                        ));
                        responseObserver.onNext(toProto(saved));
                        responseObserver.onCompleted();
                    }

                    private com.scuola.apichallenge.grpc.Film toProto(Film f) {
                        return com.scuola.apichallenge.grpc.Film.newBuilder()
                                .setId(f.getId())
                                .setTitolo(f.getTitolo())
                                .setAnno(f.getAnno())
                                .setRegista(f.getRegista())
                                .build();
                    }
                })
                .build()
                .start();

        System.out.println("gRPC server avviato su :9090");
    }

    @PreDestroy
    public void stop() {
        if (server != null) server.shutdown();
    }
}
