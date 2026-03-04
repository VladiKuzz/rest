package com.scuola.apichallenge.film;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class FilmGraphqlController {

    private final FilmRepository repo;

    public FilmGraphqlController(FilmRepository repo) {
        this.repo = repo;
    }

    @QueryMapping
    public List<Film> films() {
        return repo.findAll();
    }

    @QueryMapping
    public Film film(@Argument String id) {
        return repo.findById(id).orElse(null);
    }

    public record FilmInput(String titolo, int anno, String regista) {}

    @MutationMapping
    public Film createFilm(@Argument FilmInput input) {
        return repo.save(new Film(null, input.titolo(), input.anno(), input.regista()));
    }

    @MutationMapping
    public boolean deleteFilm(@Argument String id) {
        return repo.delete(id);
    }
}
