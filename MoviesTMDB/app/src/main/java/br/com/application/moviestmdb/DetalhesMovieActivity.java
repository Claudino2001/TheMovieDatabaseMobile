package br.com.application.moviestmdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class DetalhesMovieActivity extends AppCompatActivity {

    private TextView titulo, original_title, release_date, overview, genre_ids, vote_average;
    private ImageView banner;
    Filme filme;
    List<Genero> generos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_movie);

        titulo = (TextView) findViewById(R.id.titulo);
        vote_average = (TextView) findViewById(R.id.vote_average);
        banner = (ImageView) findViewById(R.id.banner);
        original_title = (TextView) findViewById(R.id.original_title);
        release_date = (TextView) findViewById(R.id.release_date);
        overview = (TextView) findViewById(R.id.overview);
        genre_ids = (TextView) findViewById(R.id.genre_ids);

        Intent intent = getIntent();
        if (intent.hasExtra("filme_obj")) {
            // Recupere o objeto do Intent usando getSerializableExtra() ou getParcelableExtra()
            filme = (Filme) intent.getSerializableExtra("filme_obj");
            generos = (List<Genero>) intent.getSerializableExtra("generos_obj");
        }

        titulo.setText(filme.getTitle());
        original_title.setText(filme.getOriginal_title());
        release_date.setText("Data de lançamento:\n" + filme.getRelease_date());
        overview.setText("Descrição:\n" + filme.getOverview());
        vote_average.setText("Avaliação:\n" + filme.getVote_average());


        String str_generos_do_filme = "";
        for(int i = 0; i<filme.getGenre_ids().size(); i++){
            Integer id_genero_filme = filme.getGenre_ids().get(i);
            for(int j = 0; j<generos.size(); j++){
                if(Objects.equals(generos.get(j).getId(), id_genero_filme)){
                    str_generos_do_filme = str_generos_do_filme + "\n" + generos.get(j).getName();
                }
            }
        }
        genre_ids.setText("Genero(s):\n" + str_generos_do_filme);


        String url_p1 = "https://image.tmdb.org/t/p/w500";
        String url_p2 = filme.getBackdrop_path();
        String url_p3 = "?api_key=da0e4838c057baf77b75e5338ced2bb3";
        URL url = null;
        try {
            url = new URL(url_p1 + url_p2 + url_p3);
        } catch (MalformedURLException e) {
            Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show();
        }
        //GITHUB
        Glide.with(this).load(url).into(banner);
    }
}