package br.com.application.moviestmdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetalhesMovieActivity extends AppCompatActivity {

    private TextView titulo, original_title, release_date, overview, genre_ids, vote_average, txtDetalhes, txtTime, txtAge;
    private ImageView banner;
    private RecyclerView recyclerCast;
    Filme filme;
    List<Genero> generos;
    List<Cast> casts;
    Details detalhes_filme = new Details();

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
        txtDetalhes = (TextView) findViewById(R.id.textoDetalhes);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtAge = (TextView) findViewById(R.id.txtAge);
        recyclerCast = (RecyclerView) findViewById(R.id.recyclerCast);

        Intent intent = getIntent();
        if (intent.hasExtra("filme_obj")) {
            // Recupere o objeto do Intent usando getSerializableExtra() ou getParcelableExtra()
            filme = (Filme) intent.getSerializableExtra("filme_obj");
            generos = (List<Genero>) intent.getSerializableExtra("generos_obj");
        }

        showInfoMovie();

        GetCreditsCast();

        GetAPIDetails();

        GetCertification(); //Pegar a classificação indicativa do filme

    }

    private void GetCertification() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);

        Call<ReleaseDates> request = service.GetReleaseDates(filme.getId(), "da0e4838c057baf77b75e5338ced2bb3");

        request.enqueue(new Callback<ReleaseDates>() {
            @Override
            public void onResponse(Call<ReleaseDates> call, Response<ReleaseDates> response) {
                if(response.isSuccessful()){
                    String c = null;
                    ReleaseDates releaseDates = response.body();
                    for(int i = 0; i < releaseDates.getResults().size(); i++){
                        System.out.print("Iso_3166_1: ");
                        System.out.println(releaseDates.getResults().get(i).getIso_3166_1());
                        if(releaseDates.getResults().get(i).getIso_3166_1().equals("BR")){
                            for(int j = 0; j < releaseDates.getResults().get(i).getRelease_dates().size(); j++){
                                System.out.println("ACHEI UMA CLASSIFICAÇÃO NACIONAL: ");
                                System.out.println("-> " + releaseDates.getResults().get(i).getRelease_dates().get(j).getCertification().toString());
                                if(!Objects.equals(releaseDates.getResults().get(i).getRelease_dates().get(j).getCertification(), "")){
                                    c = releaseDates.getResults().get(i).getRelease_dates().get(j).getCertification();
                                    break;
                                }
                            }
                        }
                    }
                    if(c != null){
                        txtAge.setText("[" + c + "]");
                    }else {
                        txtAge.setText("[N/A]");
                    }
                }
            }

            @Override
            public void onFailure(Call<ReleaseDates> call, Throwable t) {
                System.out.println("\n\n\ndeu merda\n\n\n");
            }
        });
    }

    private void GetAPIDetails() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);
        Call<Details> request = service.GetDetails(filme.getId(), "da0e4838c057baf77b75e5338ced2bb3");

        request.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {
                if(response.isSuccessful()){
                    Details details = response.body();
                    detalhes_filme = details;
                    showDetalhes();
                }
            }

            @Override
            public void onFailure(Call<Details> call, Throwable t) {
                Log.e("TAG erro","Erro: " + t.getMessage());
            }
        });

    }

    private void showDetalhes() {
        System.out.println("\n\n\n\n");
        System.out.println("::::: " + detalhes_filme.getBudget().toString());
        System.out.println("::::: " + detalhes_filme.getRevenue().toString());
        System.out.println("::::: " + detalhes_filme.getStatus().toString());
        System.out.println("::::: " + detalhes_filme.getOriginal_language().toString());
        System.out.println("\n\n\n\n");

        DecimalFormat dFormat = new DecimalFormat("###,###,###,###,###.##");
        //System.out.println("$" + dFormat.format(detalhes_filme.getBudget()));

        txtDetalhes.setText( "Orçamento: $" + dFormat.format(detalhes_filme.getBudget()) + "\n" +
                "" + "Receita: $" + dFormat.format(detalhes_filme.getRevenue()) + "\n" +
                "" + "Status: " + detalhes_filme.getStatus() + "\n" +
                "" + "Original Language: " + detalhes_filme.getOriginal_language().toUpperCase());

        if (detalhes_filme.getRuntime().toString() != null) {
            txtTime.setText(detalhes_filme.getRuntime().toString() + "min");
        }else{
            txtTime.setText("[N/A] min");
        }
    }

    private void GetCreditsCast() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);
        Call<Credits> request = service.GetCredits(filme.getId(), "da0e4838c057baf77b75e5338ced2bb3");

        request.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                if(!response.isSuccessful()){
                    Log.i("TAG 1", "Erro: " + response.code());
                }else{
                    Credits credits = response.body();
                    casts = credits.getCast();
                    showCast();
                }
            }
            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                Log.e("TAG 2","Erro: " + t.getMessage());
            }
        });

    }

    private void showCast() {
        RecyclerView recyclerCast = findViewById(R.id.recyclerCast);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCast.setLayoutManager(layoutManager);

        Adapter_item_cast adapter_item_cast = new Adapter_item_cast(this, casts);
        recyclerCast.setAdapter(adapter_item_cast);

//        for(int i =0; i<casts.size(); i++){
//            System.out.println("Cast " + i + ": " + casts.get(i).getName() + " -> " + casts.get(i).getCharacter());
//        }
    }

    private void showInfoMovie() {
        titulo.setText(filme.getTitle());
        original_title.setText(filme.getOriginal_title());
        release_date.setText(filme.getRelease_date());
        overview.setText(filme.getOverview());
        vote_average.setText(filme.getVote_average().toString());


        String str_generos_do_filme = "";
        ArrayList<String> str_teste = new ArrayList<>();
        for(int i = 0; i<filme.getGenre_ids().size(); i++){
            Integer id_genero_filme = filme.getGenre_ids().get(i);
            for(int j = 0; j<generos.size(); j++){
                if(Objects.equals(generos.get(j).getId(), id_genero_filme)){
                    str_teste.add(generos.get(j).getName());
                    str_generos_do_filme = str_generos_do_filme + "\n" + generos.get(j).getName();
                }
            }
        }
        //genre_ids.setText(str_generos_do_filme);
        genre_ids.setText(str_teste.toString());


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

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}