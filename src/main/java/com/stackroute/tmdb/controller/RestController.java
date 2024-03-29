package com.stackroute.tmdb.controller;

import com.stackroute.tmdb.model.Movie;
import com.stackroute.tmdb.service.MovieServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1")
public class RestController {

    private MovieServiceImpl movieService;

    @Autowired
    public RestController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public ResponseEntity getMovies(@RequestParam(name="name", defaultValue = "") String movieName) {
        if(!movieName.equals("")) {
            return ResponseEntity.ok(movieService.getMovieByName(movieName));
        }
        return ResponseEntity.ok(movieService.findAll());
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity getMovie(@PathVariable(value = "id") String id) {
        try {
            int movieId = Integer.parseInt(id);
            Movie movie = movieService.findById(movieId);
            return ResponseEntity.ok(movie);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie "+id+" not found", e);
        }
    }

    @GetMapping("/moviesByName/{name}")
    public ResponseEntity getMovieByName(@PathVariable(value = "name") String name) {
        return ResponseEntity.ok(movieService.getMovieByName(name));
    }

    @PostMapping("/movies")
    public ResponseEntity createMovie(@Valid @RequestBody Movie movie) {
        movieService.saveMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(movie);
    }

    @PutMapping("/movies")
    public ResponseEntity updateMovie(@Valid @RequestBody Movie movie) {
        if(movieService.existsById(movie.getId())) {
            movieService.saveMovie(movie);
            return ResponseEntity.status(HttpStatus.OK).body(movie);
        }
        else {
            movieService.saveMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body(movie);
        }
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity deleteMovie(@PathVariable(value = "id") String id) {
        try {
            int movieId = Integer.parseInt(id);
            movieService.deleteMovie(movieId);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie "+id+" not found", e);
        }
    }

}
