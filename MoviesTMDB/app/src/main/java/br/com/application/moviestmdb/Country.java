package br.com.application.moviestmdb;

import java.util.List;

public class Country {
    private String iso_3166_1;
    private List<Classificacoes> release_dates;

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public List<Classificacoes> getRelease_dates() {
        return release_dates;
    }

    public void setRelease_dates(List<Classificacoes> release_dates) {
        this.release_dates = release_dates;
    }
}
