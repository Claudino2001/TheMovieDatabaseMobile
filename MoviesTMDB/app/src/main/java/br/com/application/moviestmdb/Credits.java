package br.com.application.moviestmdb;

import java.util.List;

public class Credits {

    private Integer id;
    private List<Cast> cast;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }
}
