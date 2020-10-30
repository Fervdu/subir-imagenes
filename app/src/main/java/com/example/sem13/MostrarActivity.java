package com.example.sem13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MostrarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView jlvAutos;
    ArrayList<Auto> lista;
    MostrarAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);

        jlvAutos = findViewById(R.id.lvMostrarAutos);
        lista = new ArrayList<>();
        adapter = new MostrarAdapter(this, R.layout.auto_item, lista);
        jlvAutos.setAdapter(adapter);

        MostrarAutos();
        jlvAutos.setOnItemClickListener(this);
    }

    private void MostrarAutos() {
        AsyncHttpClient ahcMostrar = new AsyncHttpClient();

        String sUrl = "http://brownieseria.atwebpages.com/Museapp/museosNombre.php";
        RequestParams params = new RequestParams();
        //params.add("ID", "");

        ahcMostrar.get(sUrl, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        lista.clear();

                        for(int i = 0; i < jsonArray.length(); i++){
                            lista.add(new Auto(jsonArray.getJSONObject(i).getInt("id"),
                                    jsonArray.getJSONObject(i).getString("nombre"),
                                    jsonArray.getJSONObject(i).getString("direccion"),
                                    jsonArray.getJSONObject(i).getString("telefono"),
                                    jsonArray.getJSONObject(i).getDouble("entradaPrecio"),
                                    jsonArray.getJSONObject(i).getString("imageReferencial")));
                        }
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {

            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent iDetalle = new Intent(getApplicationContext(), DetalleAutoActivity.class);
        iDetalle.putExtra("id", lista.get(i).getId());
        startActivity(iDetalle);
    }
}