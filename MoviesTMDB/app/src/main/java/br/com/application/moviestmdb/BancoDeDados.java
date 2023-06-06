package br.com.application.moviestmdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BancoDeDados extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "meu_banco.db";
    private static final int DATABASE_VERSION = 1;

    public BancoDeDados(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação das tabelas do banco de dados
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_filmes_favoritos (_id INTEGER PRIMARY KEY, filme_id INTEGER NOT NULL, filme_nome VARCHAR (100) NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Lógica para atualização do banco de dados, caso haja uma nova versão
        // Aqui você pode implementar a lógica para migrar os dados existentes para a nova versão do esquema do banco de dados
    }

    public ArrayList<Integer> consultarFilmes() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_filmes_favoritos ORDER BY filme_nome ", null);
        ArrayList<Integer> arrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt((int) cursor.getColumnIndex("filme_id"));
                arrayList.add(id);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return arrayList;
    }

    public boolean searchMovie(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT filme_id FROM tb_filmes_favoritos WHERE filme_id = "+ id +";", null);
        if (cursor.moveToFirst()) {
            // O cursor tem resultados, o filme foi encontrado
            cursor.close();
            return true;
        } else {
            // O cursor está vazio, o filme não foi encontrado
            cursor.close();
            return false;
        }
    }


    public void inserirDados(int filme_id, String filme_nome) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO tb_filmes_favoritos (filme_id, filme_nome) VALUES (?, ?);", new Object[]{filme_id, filme_nome});
    }

    public void excluirDados(int filme_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM tb_filmes_favoritos WHERE filme_id = ?;", new Object[]{filme_id});
    }
}
