package br.com.application.moviestmdb;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Adapter_item_filme extends ArrayAdapter<Filme> {
    private final Context context;
    private final List<Filme> filmes;

    private final List<Genero> generos;

    public Adapter_item_filme(Context context, List<Filme> filmes, List<Genero> generos){
        super(context, R.layout.item_filme_dois, filmes);
        this.context = context;
        this.filmes = filmes;
        this.generos = generos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_filme_dois, parent, false);

        ImageView cartaz = (ImageView) rowView.findViewById(R.id.cartaz);
        TextView titulo = (TextView) rowView.findViewById(R.id.title);
        TextView overview = (TextView) rowView.findViewById(R.id.overview);
        TextView release_date = (TextView) rowView.findViewById(R.id.release_date);
        TextView vote_average = (TextView) rowView.findViewById(R.id.vote_average);
        TextView genero = (TextView) rowView.findViewById(R.id.genero);

        String url_p1 = "https://image.tmdb.org/t/p/w500";
        String url_p2 = filmes.get(position).getPoster_path();
        String url_p3 = "?api_key=da0e4838c057baf77b75e5338ced2bb3";
        URL url;
        try {
            url = new URL(url_p1 + url_p2 + url_p3);
        } catch (MalformedURLException e) {
            Toast.makeText(context, "erro: img" + position, Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
        //GITHUB
        Glide.with(context).load(url).into(cartaz);

        titulo.setText(filmes.get(position).getTitle());
        overview.setText(filmes.get(position).getOverview());
        release_date.setText(filmes.get(position).getRelease_date());
        vote_average.setText("Avaliação: " + filmes.get(position).getVote_average().toString());

        String str_generos_do_filme = "";
        if(filmes.get(position).getGenre_ids() != null){
            for(int i = 0; i<filmes.get(position).getGenre_ids().size(); i++){
                Integer id_genero_filme = filmes.get(position).getGenre_ids().get(i);
                for(int j = 0; j<generos.size(); j++){
                    if(Objects.equals(generos.get(j).getId(), id_genero_filme)){
                        str_generos_do_filme = str_generos_do_filme + " " + generos.get(j).getName();
                    }
                }
            }
        }
        genero.setText(str_generos_do_filme);

        return rowView;
    }
}
