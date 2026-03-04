package com.scuola.apichallenge.film;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmRestController {

    private final FilmRepository repo;

    public FilmRestController(FilmRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Film> getFilms() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable String id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        Film created = repo.save(film);
        return ResponseEntity.created(URI.create("/films/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable String id, @RequestBody Film film) {
        return repo.update(id, film)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable String id) {
        return repo.delete(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
