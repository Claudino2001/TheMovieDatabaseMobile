package br.com.application.moviestmdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoritosActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ListView listFav;
    BancoDeDados banco;
    Details detalhes_filme;
    ArrayList<Details> filmes_favoritos;
    List<Genero> generos = new ArrayList<>();
    ProgressBar progressBar;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        listFav = (ListView) findViewById(R.id.list_fav);

        progressBarMetodo();

        implementationNavigationView();

        banco = new BancoDeDados(this);

        consultaRetrofitGeneros().thenRun(this::GetAPIDetails);

        listaFavoritosConfig();

    }

    private void listaFavoritosConfig() {
        listFav.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                excluirFilmeDosFavs(i);
                return true;
            }
        });

        listFav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FavoritosActivity.this, FavoriteFilmeDetails.class);
                intent.putExtra("detalhes_filme", filmes_favoritos.get(i));
                intent.putExtra("generos_obj", (Serializable) generos);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


    }

    private void progressBarMetodo() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.onVisibilityAggregated(true);
        // Obtendo uma referência para a cor desejada
        int cor = ContextCompat.getColor(this, R.color.ColorNavegationBar);

        // Criando uma instância de ColorStateList com a cor desejada
        ColorStateList colorStateList = ColorStateList.valueOf(cor);

        // Definindo a cor da ProgressBar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(colorStateList);
            progressBar.setSecondaryProgressTintList(colorStateList);
            progressBar.setIndeterminateTintList(colorStateList);
        } else {
            // Para versões mais antigas do Android
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mode = PorterDuff.Mode.MULTIPLY;
            }
            progressBar.getProgressDrawable().setColorFilter(cor, mode);
            progressBar.getIndeterminateDrawable().setColorFilter(cor, mode);
        }
    }

    private void excluirFilmeDosFavs(int number) {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setTitle("Excluir filme dos favoritos.");
        msgBox.setIcon(R.drawable.ic_delete24);
        msgBox.setMessage("Tem certeza que deseja excluir esse filme dos favoritos?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                banco.excluirDados(filmes_favoritos.get(number).getId());
                GetAPIDetails();
            }
        });
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(FavoritosActivity.this, "Operação cancelada.", Toast.LENGTH_SHORT).show();
            }
        });
        msgBox.show();
    }

    private CompletableFuture<Void> consultaRetrofitGeneros() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);
        Call<Genres> request = service.GetAPIGeneros("pt-BR", "da0e4838c057baf77b75e5338ced2bb3");

        request.enqueue(new Callback<Genres>() {
            @Override
            public void onResponse(Call<Genres> call, Response<Genres> response) {
                if (!response.isSuccessful()) {
                    Log.i("TAG", "Erro: " + response.code());
                } else {
                    Genres genres = response.body();

                    for (Genero genero : genres.getGenres()) {
                        Log.i("TAG", String.format("GENERO: %s %s", genero.getId(), genero.getName()));
                    }
                    generos = genres.getGenres();
                }
                future.complete(null); // Marcar o CompletableFuture como concluído
            }

            @Override
            public void onFailure(Call<Genres> call, Throwable t) {
                Log.e("TAG", "Erro: " + t.getMessage());
                future.completeExceptionally(t); // Marcar o CompletableFuture como concluído com exceção
            }
        });

        return future;
    }

    private void GetAPIDetails() {
        filmes_favoritos = new ArrayList<>();
        ArrayList lista_favs = banco.consultarFilmes();
        Log.i("LISTA DE FAVORITOS", String.format(lista_favs.toString()));
        if(!lista_favs.isEmpty()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Service.URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Service service = retrofit.create(Service.class);

            // Usar um array para armazenar o valor da variável
            final int[] chamadasAssincronasConcluidas = {0};

            // Fazer um loop para cada id de filme
            for (int i = 0; i < lista_favs.size(); i++) {
                Call<Details> request = service.GetDetails((Integer) lista_favs.get(i), "pt-BR", "da0e4838c057baf77b75e5338ced2bb3");
                request.enqueue(new Callback<Details>() {
                    @Override
                    public void onResponse(Call<Details> call, Response<Details> response) {
                        if (response.isSuccessful()) {
                            Details details = response.body();
                            detalhes_filme = details;
                            filmes_favoritos.add(detalhes_filme);
                            System.out.println(detalhes_filme.getOriginal_title());
                            progressBar.onVisibilityAggregated(false);
                        }

                        // Incrementar o valor no array
                        chamadasAssincronasConcluidas[0]++;

                        // Verificar se todas as chamadas assíncronas foram concluídas
                        if (chamadasAssincronasConcluidas[0] == lista_favs.size()) {
                            listar();
                        }
                    }

                    @Override
                    public void onFailure(Call<Details> call, Throwable t) {
                        Log.e("TAG erro", "Erro: " + t.getMessage());

                        // Incrementar o valor no array
                        chamadasAssincronasConcluidas[0]++;

                        // Verificar se todas as chamadas assíncronas foram concluídas
                        if (chamadasAssincronasConcluidas[0] == lista_favs.size()) {
                            listar();
                        }
                        progressBar.onVisibilityAggregated(false);
                    }
                });
            }
        }else progressBar.onVisibilityAggregated(false);
    }

    private void listar() {
        Adapter_filme_ById adapter = new Adapter_filme_ById(this, filmes_favoritos, generos);
        listFav.setAdapter(adapter);
    }


    private void implementationNavigationView() {
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_favoritos);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_main:
                        startActivity(new Intent(FavoritosActivity.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        return true;
                    case R.id.page_favoritos:
                        return true;
                    case R.id.page_search:
                        startActivity(new Intent(FavoritosActivity.this, SearchActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.page_favoritos);
        progressBarMetodo();
    }
}