package com.scuola.apichallenge.grpc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;

public class SizeCompare {
    public static void main(String[] args) throws Exception {
        Film protoFilm = Film.newBuilder()
                .setId("123")
                .setTitolo("Interstellar")
                .setAnno(2014)
                .setRegista("Christopher Nolan")
                .build();

        var jsonObj = new java.util.LinkedHashMap<String, Object>();
        jsonObj.put("id", protoFilm.getId());
        jsonObj.put("titolo", protoFilm.getTitolo());
        jsonObj.put("anno", protoFilm.getAnno());
        jsonObj.put("regista", protoFilm.getRegista());

        ObjectMapper om = new ObjectMapper();
        byte[] jsonBytes = om.writerWithDefaultPrettyPrinter().writeValueAsBytes(jsonObj);
        byte[] pbBytes = protoFilm.toByteArray();

        Files.createDirectories(Path.of("data"));
        Files.write(Path.of("data", "film.json"), jsonBytes);
        Files.write(Path.of("data", "film.pb"), pbBytes);

        System.out.println("JSON bytes: " + jsonBytes.length);
        System.out.println("Protobuf bytes: " + pbBytes.length);
    }
}
