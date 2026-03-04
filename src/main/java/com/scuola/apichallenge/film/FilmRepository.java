package com.scuola.apichallenge.film;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FilmRepository {
    private final Map<String, Film> store = new ConcurrentHashMap<>();
    private final ObjectMapper om = new ObjectMapper();
    private final Path jsonPath = Paths.get("data", "films.json");

    public FilmRepository() {
        try {
            Files.createDirectories(jsonPath.getParent());
            if (Files.exists(jsonPath)) {
                List<Film> films = om.readValue(Files.readAllBytes(jsonPath), new TypeReference<>() {});
                for (Film f : films) store.put(f.getId(), f);
            } else {
                // Seed iniziale
                save(new Film(UUID.randomUUID().toString(), "Interstellar", 2014, "Christopher Nolan"));
                save(new Film(UUID.randomUUID().toString(), "Il Padrino", 1972, "Francis Ford Coppola"));
            }
        } catch (Exception ignored) {
        }
    }

    public List<Film> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(Film::getTitolo))
                .toList();
    }

    public Optional<Film> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public Film save(Film film) {
        if (film.getId() == null || film.getId().isBlank()) {
            film.setId(UUID.randomUUID().toString());
        }
        store.put(film.getId(), film);
        persist();
        return film;
    }

    public Optional<Film> update(String id, Film film) {
        if (!store.containsKey(id)) return Optional.empty();
        film.setId(id);
        store.put(id, film);
        persist();
        return Optional.of(film);
    }

    public boolean delete(String id) {
        Film removed = store.remove(id);
        if (removed != null) persist();
        return removed != null;
    }

    private void persist() {
        try {
            byte[] bytes = om.writerWithDefaultPrettyPrinter().writeValueAsBytes(findAll());
            Files.write(jsonPath, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Errore scrittura films.json", e);
        }
    }
}
