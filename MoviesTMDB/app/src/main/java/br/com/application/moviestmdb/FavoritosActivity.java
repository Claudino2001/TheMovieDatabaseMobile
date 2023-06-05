package br.com.application.moviestmdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FavoritosActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView listFav;
    public ArrayList listaFavoritos;
    BancoDeDados banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        listFav = (ListView) findViewById(R.id.list_fav);

        implementationNavigationView();

        banco = new BancoDeDados(this);

        listar();

    }

    private void listar() {
        ArrayList meusFilmesFavoritos = banco.consultarFilmes();
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, meusFilmesFavoritos);
        listFav.setAdapter(adapter);
    }


    private void implementationNavigationView() {
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_main:
                        startActivity(new Intent(FavoritosActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        return true;
                    case R.id.page_favoritos:
                        return false;
                }
                return false;
            }
        });
    }
}