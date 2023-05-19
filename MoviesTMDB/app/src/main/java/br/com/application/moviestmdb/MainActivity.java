package br.com.application.moviestmdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private SearchView search;
    private ListView list;
    private static final String TAG = " MINHA TAG";
    List<Filme> filmes;
    List<Genero> generos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (SearchView) findViewById(R.id.search);
        list = (ListView) findViewById(R.id.list);

        consultaRetrofitGeneros();

        consultaRetrofitPopularMovies();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetalhesMovieActivity.class);
                intent.putExtra("filme_obj", (Serializable) filmes.get(i));
                intent.putExtra("generos_obj", (Serializable) generos);
                startActivity(intent);
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                buscarFilme(s);
                if(TextUtils.isEmpty(s)){
                    consultaRetrofitPopularMovies();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                buscarFilme(s);
                if(TextUtils.isEmpty(s)){
                    consultaRetrofitPopularMovies();
                }
                return true;
            }
        });

    }

    private void consultaRetrofitGeneros() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);
        Call<Genres> request = service.GetAPIGeneros("pt-BR", "da0e4838c057baf77b75e5338ced2bb3");

        request.enqueue(new Callback<Genres>() {
            @Override
            public void onResponse(Call<Genres> call, Response<Genres> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "Erro: " + response.code());
                }else{
                    Genres genres = response.body();

                    for(Genero genero: genres.getGenres()){
                        Log.i(TAG, String.format("GENERO: %s %s", genero.getId(), genero.getName()));
                    }
                    generos = genres.getGenres();
                }
            }

            @Override
            public void onFailure(Call<Genres> call, Throwable t) {
                Log.e(TAG, "Erro: " + t.getMessage());
            }
        });

    }


    private void mostrarFilmes() {
        Adapter_item_filme adapter = new Adapter_item_filme(this, filmes, generos);
        list.setAdapter(adapter);
    }
    private void buscarFilme(String consulta){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);
        Call<GetPopularMovies> request = service.GetAPISearchMovie(consulta, false, "pt-BR", 1, "da0e4838c057baf77b75e5338ced2bb3");

        request.enqueue(new Callback<GetPopularMovies>() {
            @Override
            public void onResponse(Call<GetPopularMovies> call, Response<GetPopularMovies> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "Erro: " + response.code());
                }
                else{
                    GetPopularMovies getPopularMovies = response.body();

                    for(Filme f: getPopularMovies.getResults()){
                        Log.i("TAG",String.format("consulta: %s",f.getTitle()));
                    }
                    filmes.clear();
                    filmes = getPopularMovies.getResults();
                }
                mostrarFilmes();
            }

            @Override
            public void onFailure(Call<GetPopularMovies> call, Throwable t) {
                Log.e(TAG, "Erro: " + t.getMessage());
            }
        });
    }

    private void consultaRetrofitPopularMovies() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);
        Call<GetPopularMovies> request =  service.GetAPIPopularMovies("pt-BR","da0e4838c057baf77b75e5338ced2bb3");

        request.enqueue(new Callback<GetPopularMovies>() {
            @Override
            public void onResponse(Call<GetPopularMovies> call, Response<GetPopularMovies> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "Erro: " + response.code());
                }else{
                    //A requisição foi realizada com sucesso
                    GetPopularMovies getPopularMovies = response.body();

                    for(Filme f: getPopularMovies.getResults()){
                        Log.i("TAG",String.format("%s : %s",f.getTitle(), f.getOriginal_title()));
                    }
                    filmes = getPopularMovies.getResults();
                }
                mostrarFilmes();
            }

            @Override
            public void onFailure(Call<GetPopularMovies> call, Throwable t) {
                Log.e(TAG, "Erro: " + t.getMessage());
            }
        });
    }
}