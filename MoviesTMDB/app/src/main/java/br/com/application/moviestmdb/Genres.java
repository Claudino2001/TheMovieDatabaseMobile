package br.com.application.moviestmdb;

import java.util.List;

public class Genres {
    List<Genero> genres;

    public Genres(List<Genero> genres) {
        this.genres = genres;
    }

    public List<Genero> getGenres() {
        return genres;
    }

    public void setGenres(List<Genero> genres) {
        this.genres = genres;
    }
}
