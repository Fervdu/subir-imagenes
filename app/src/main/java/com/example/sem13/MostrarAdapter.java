package com.example.sem13;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MostrarAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Auto> listAutos;

    public MostrarAdapter() {
    }

    public MostrarAdapter(Context context, int layout, ArrayList<Auto> listAutos) {
        this.context = context;
        this.layout = layout;
        this.listAutos = listAutos;
    }

    @Override
    public int getCount() {
        return listAutos.size();
    }

    @Override
    public Object getItem(int i) {
        return listAutos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView Marca, Modelo, Placa, Precio;
        ImageView fotoAuto;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View fila = view;
        ViewHolder holder = new ViewHolder();

        if(fila == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            fila = inflater.inflate(layout, null);
            holder.Marca = fila.findViewById(R.id.lblItemMarca);
            holder.Modelo = fila.findViewById(R.id.lblItemModelo);
            holder.Placa = fila.findViewById(R.id.lblItemPlaca);
            holder.Precio = fila.findViewById(R.id.lblItemPrecio);
            holder.fotoAuto = fila.findViewById(R.id.ivItemAuto);
            fila.setTag(holder);
        }
        else
            holder = (ViewHolder)fila.getTag();

        Auto auto = listAutos.get(i);

        holder.Marca.setText(auto.getMarca());
        holder.Modelo.setText(auto.getModelo());
        holder.Placa.setText(auto.getPlaca());
        holder.Precio.setText(String.valueOf(auto.getPrecio()));
        String sImagen  = auto.getImagen();
        byte[] bImagen = Base64.decode(sImagen, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(bImagen, 0, bImagen.length);
        holder.fotoAuto.setImageBitmap(decodedImage);
        return fila;
    }
}
