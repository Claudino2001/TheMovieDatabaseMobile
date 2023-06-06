package br.com.application.moviestmdb;

import android.annotation.SuppressLint;
import android.content.Context;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Adapter_filme_ById extends ArrayAdapter<Details> {
    private final Context context;
    private final List<Details> filmes_Details;
    private final List<Genero> generos;

    public Adapter_filme_ById(Context context, List<Details> filmes_details, List<Genero> generos){
        super(context, R.layout.item_filme_dois, filmes_details);
        this.context = context;
        filmes_Details = filmes_details;
        this.generos = generos;
    }

    @SuppressLint("SetTextI18n")
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
        String url_p2 = filmes_Details.get(position).getPoster_path();
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

        titulo.setText(filmes_Details.get(position).getTitle());
        overview.setText(filmes_Details.get(position).getOverview());
        release_date.setText(filmes_Details.get(position).getRelease_date());

        double voteAverage = filmes_Details.get(position).getVote_average().doubleValue();
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.0", decimalFormatSymbols);
        String formattedVoteAverage = decimalFormat.format(voteAverage);
        vote_average.setText("Avaliação: " + formattedVoteAverage);


        String str_generos_do_filme = "";
        if(filmes_Details.get(position).getGenres() != null){
            for(int i = 0; i<filmes_Details.get(position).getGenres().size(); i++){
                Integer id_genero_filme = filmes_Details.get(position).getGenres().get(i).getId();
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
