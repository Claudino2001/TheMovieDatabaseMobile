package br.com.application.moviestmdb;

import java.util.List;

public class ReleaseDates {
    private Integer id;
    private List<Country> results;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Country> getResults() {
        return results;
    }

    public void setResults(List<Country> results) {
        this.results = results;
    }
}
