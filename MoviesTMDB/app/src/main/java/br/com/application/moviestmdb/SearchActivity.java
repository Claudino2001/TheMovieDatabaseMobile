package br.com.application.moviestmdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private SearchView search;
    List<Filme> filmes = new ArrayList<>();
    List<Genero> generos = new ArrayList<>();
    private ListView list;
    public BottomNavigationView bottomNavigationView;
    public BancoDeDados banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = (SearchView) findViewById(R.id.search);
        list = (ListView) findViewById(R.id.list_search);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        banco = new BancoDeDados(this);

        configSearch();

        bottomNavigationView.setSelectedItemId(R.id.page_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_main:
                        startActivity(new Intent(SearchActivity.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        return true;
                    case R.id.page_favoritos:
                        startActivity(new Intent(SearchActivity.this, FavoritosActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        return true;
                    case R.id.page_search:
                        return true;
                }
                return false;
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Integer filme_id = filmes.get(i).getId();
                String filme_name = filmes.get(i).getOriginal_title();
                if(!banco.searchMovie(filme_id)){
                    inserirAosFavs(filme_id, filme_name);
                }
                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this, DetalhesMovieActivity.class);
                intent.putExtra("filme_obj", (Serializable) filmes.get(i));
                intent.putExtra("generos_obj", (Serializable) generos);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        hideTeclado();

    }

    private void hideTeclado() {
        ViewGroup rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocusView = getCurrentFocus();
        if (currentFocusView != null) {
            imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
            currentFocusView.clearFocus();
        }
    }

    private void configSearch() {

        // Obtém o layout pai do SearchView
        ViewGroup searchViewParent = (ViewGroup) search.getParent();

        // Adiciona um OnClickListener ao layout pai
        searchViewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre a opção de busca quando clicar em qualquer lugar do SearchView
                search.onActionViewExpanded();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                buscarFilme(s);
                hideKeyboardOver();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                buscarFilme(s);
                return true;
            }
        });
        search.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                hideKeyboardOver();
                return true;
            }
            return false;
        });

        search.clearFocus();

        search.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    private void inserirAosFavs(int filme_id, String filme_nome) {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setTitle("Adicionar filme aos favoritos.");
        msgBox.setIcon(R.drawable.ic_add);
        msgBox.setMessage("Tem certeza que deseja adicionar esse filme aos favoritos?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                banco.inserirDados(filme_id, filme_nome);
            }
        });

        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SearchActivity.this, "Operação cancelada.", Toast.LENGTH_SHORT).show();
            }
        });
        msgBox.show();
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
                    Log.i("TAG", "Erro: " + response.code());
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
                Log.e("TAG", "Erro: " + t.getMessage());
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
                    Log.i("TAG", "Erro: " + response.code());
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
                Log.e("TAG", "Erro: " + t.getMessage());
            }
        });
    }
    public static void hideKeyboardClickBtn(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void hideKeyboardOver() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.page_search);
    }
}