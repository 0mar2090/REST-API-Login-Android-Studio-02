package com.iingdev.webservices365;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText UserEditText;
    EditText passwordEditText;
    Button btnIngresarButtom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserEditText=findViewById(R.id.UserEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        btnIngresarButtom=findViewById(R.id.btnIngresarButtom);

        btnIngresarButtom.setOnClickListener(this);

    }

    public String GET(String Email,String Pass){

        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder result=null;
        try {
            // 1 - WINDOWS + R ==> CMD
            // 2 - ipconfig
            // 3 - Dirección IPv4 : 192.168.2.179 Ejemplo
            // 4 - OJO Android Studio no reconoce http://localhost
            // 5 - Bye 0mar2090
            url= new URL("http://192.168.2.179/WebServices/Login.php?Email="+Email+"&Password="+Pass);


            HttpURLConnection Conexion=(HttpURLConnection)url.openConnection();
            respuesta =Conexion.getResponseCode();

            result = new  StringBuilder();
            if (respuesta==HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(Conexion.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while ((linea=reader.readLine())!=null){
                    result.append(linea);
                }
            }

        }catch(Exception e) {
            e.printStackTrace();
        }

        assert result != null;
        return result.toString();

    }

    public int ObtenerJSON(String response) {
        int res = 0;
        try {
            JSONArray json = new JSONArray(response);
            if (json.length() > 0) {
                res = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return res;
    }

    @Override
    public void onClick(View view) {
         Thread tr = new Thread(){
             @Override
             public void run() {
                 final  String resultado=GET(UserEditText.getText().toString(),passwordEditText.getText().toString());
                 runOnUiThread(() -> {
                     int r = ObtenerJSON(resultado);
                     if(r>0){
                         Intent i= new Intent(getApplicationContext(),Menu.class);
                         i.putExtra("cod",UserEditText.getText().toString());
                         startActivity(i);
                     }else{
                         Toast.makeText(getApplicationContext(),"Usuario o Contraseña Incorrecto",Toast.LENGTH_LONG).show();
                     }
                 });
             }
         };
         tr.start();
    }


}