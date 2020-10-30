package com.example.sem13;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText jtxtMarca, jtxtModelo, jtxtPlaca, jtxtPrecio;
    Button jbtnElegir, jbtnRegistrar, jbtnMostrar;
    ImageView jimgFotoAuto;
    private static final int REQUEST_CODE_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jtxtMarca = findViewById(R.id.txtMarca);
        jtxtModelo = findViewById(R.id.txtModelo);
        jtxtPlaca = findViewById(R.id.txtPlaca);
        jtxtPrecio = findViewById(R.id.txtPrecio);
        jbtnElegir = findViewById(R.id.btnElegir);
        jbtnRegistrar = findViewById(R.id.btnRegistrar);
        jbtnMostrar = findViewById(R.id.btnMostrar);
        jimgFotoAuto = findViewById(R.id.imgFotoAuto);

        jbtnElegir.setOnClickListener(this);
        jbtnRegistrar.setOnClickListener(this);
        jbtnMostrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnElegir:
                ElegirFoto();
                break;
            case R.id.btnRegistrar:
                //if(!ExisteAuto())
                RegistrarAuto();
                break;
            case R.id.btnMostrar:
                MostrarAutos();
                break;
        }
    }

    private void ElegirFoto() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent iFileChooser = new Intent(Intent.ACTION_PICK);
                iFileChooser.setType("image/*");
                startActivityForResult(iFileChooser, REQUEST_CODE_GALLERY);
            }
        }
        else
            Toast.makeText(this, "Error al acceder al almacenamiento",
                    Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY){
            if(resultCode == RESULT_OK && data != null){
                Uri uri = data.getData();
                jimgFotoAuto.setImageURI(uri);
            }
            else{
                Toast.makeText(this, "Debe elegir una foto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void RegistrarAuto() {
        AsyncHttpClient ahcRegistrar = new AsyncHttpClient();
        //importante la ruta debe ser valida
        String sUrl = "http://brownieseria.atwebpages.com/Museapp/actualizarImgReferencialMuseo.php";
        //llenar parametros
        RequestParams params = new RequestParams();
        params.add("id", jtxtMarca.getText().toString().trim());
        /*params.add("marca", jtxtMarca.getText().toString().trim());
        params.add("modelo", jtxtModelo.getText().toString().trim());
        params.add("placa", jtxtPlaca.getText().toString().trim());
        params.add("precio", jtxtPrecio.getText().toString().trim());*/
        params.add("imageReferencial", ImageViewToBase64(jimgFotoAuto));

        ahcRegistrar.post(sUrl, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    int iRetVal = (rawJsonResponse.length() == 0 ? 0 : Integer.parseInt(rawJsonResponse));
                    if(iRetVal == 1){
                        Toast.makeText(getApplicationContext(), "Registro Agregado", Toast.LENGTH_LONG).show();
                        jtxtMarca.setText("");
                        jtxtModelo.setText("");
                        jtxtPlaca.setText("");
                        jtxtPrecio.setText("");
                        jimgFotoAuto.setImageResource(R.mipmap.ic_launcher);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getApplicationContext(), "Error al agregar Registro", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }

    private String ImageViewToBase64(ImageView jimgFotoAuto){
        String sImagen = null;
        Bitmap bitmap = ((BitmapDrawable)jimgFotoAuto.getDrawable()).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] byteArray = stream.toByteArray();

        sImagen = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return sImagen;
    }

    private void MostrarAutos() {
        Intent iMostrar = new Intent(this, MostrarActivity.class);
        startActivity(iMostrar);
    }

}