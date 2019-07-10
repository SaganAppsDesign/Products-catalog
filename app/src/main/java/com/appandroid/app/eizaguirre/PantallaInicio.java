package com.appandroid.app.eizaguirre;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static com.appandroid.app.eizaguirre.MainActivity.esTablet;

public class PantallaInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
       // setContentView ( R.layout.activity_pantalla_inicio );


        if(esTablet(getApplicationContext())){ //tablet

            setContentView(R.layout.activity_pantalla_inicio_tablet );

        }else{ // telefono
            setContentView(R.layout.activity_pantalla_inicio );


        }

        new Handler (  ).postDelayed ( new Runnable () {
            @Override
            public void run() {

                Intent intent = new Intent (PantallaInicio.this, MainActivity.class);

                startActivity ( intent );

                finish ();

            }
        } , 1500);
    }
}
