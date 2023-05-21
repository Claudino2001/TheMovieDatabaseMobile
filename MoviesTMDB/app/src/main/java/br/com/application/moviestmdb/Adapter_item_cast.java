package br.com.application.moviestmdb;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class Adapter_item_cast extends RecyclerView.Adapter<Adapter_item_cast.ViewHolder> {
    private final Context context;
    private final List<Cast> casts;

    public Adapter_item_cast(Context context, List<Cast> casts){
        this.context = context;
        this.casts = casts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Configurar os dados do item na posição
        Cast cast = casts.get(position);
        // Definir os valores nos elementos de layout
        // holder.imageProfile...
        // holder.txtOriginalName...
        // holder.txtCharacter...
        holder.txtOriginalName.setText(casts.get(position).getName());
        holder.txtCharacter.setText(casts.get(position).getCharacter());

        String s = casts.get(position).getProfile_path();

        if(!TextUtils.isEmpty(s)){
            String url_p1 = "https://image.tmdb.org/t/p/w500";
            String url_p2 = casts.get(position).getProfile_path();
            String url_p3 = "?api_key=da0e4838c057baf77b75e5338ced2bb3";
            URL url;
            try {
                url = new URL(url_p1 + url_p2 + url_p3);
            } catch (MalformedURLException e) {
                Toast.makeText(context, "erro: img" + position, Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }

            // Carregar a imagem usando uma biblioteca de carregamento de imagens, como o Glide
            Glide.with(context)
                    .load(url)
                    .into(holder.imageProfile);
        }

    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Elementos de layout do item
        ImageView imageProfile;
        TextView txtOriginalName;
        TextView txtCharacter;

        public ViewHolder(View itemView) {
            super(itemView);
            // Inicializar os elementos de layout do item
            imageProfile = itemView.findViewById(R.id.imageProfile);
            txtOriginalName = itemView.findViewById(R.id.txtOriginalName);
            txtCharacter = itemView.findViewById(R.id.txtCharacter);
        }
    }
}


//public class Adapter_item_cast extends ArrayAdapter<Cast> {
//    private final Context context;
//    private final List<Cast> casts;
//
//    public Adapter_item_cast(Context context, List<Cast> casts){
//        super(context, R.layout.item_cast, casts);
//        this.context = context;
//        this.casts = casts;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView = inflater.inflate(R.layout.item_cast, parent, false);
//        rowView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//        ImageView imageProfile = (ImageView) rowView.findViewById(R.id.imageProfile);
//        TextView txtOriginalName = (TextView) rowView.findViewById(R.id.txtOriginalName);
//        TextView txtCharacter = (TextView) rowView.findViewById(R.id.txtCharacter);
//
//        String url_p1 = "https://image.tmdb.org/t/p/w500";
//        String url_p2 = casts.get(position).getProfile_path();
//        String url_p3 = "?api_key=da0e4838c057baf77b75e5338ced2bb3";
//        URL url;
//        try {
//            url = new URL(url_p1 + url_p2 + url_p3);
//        } catch (MalformedURLException e) {
//            Toast.makeText(context, "erro: img" + position, Toast.LENGTH_SHORT).show();
//            throw new RuntimeException(e);
//        }
//
//        Glide.with(context).load(url).into(imageProfile);
//
//        txtOriginalName.setText(casts.get(position).getName());
//        txtCharacter.setText(casts.get(position).getCharacter());
//
//        return rowView;
//    }
//    @Override
//    public int getCount() {
//        return casts.size();
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 1;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//    @Override
//    public boolean isEnabled(int position) {
//        return false;
//    }
//
//
//}
