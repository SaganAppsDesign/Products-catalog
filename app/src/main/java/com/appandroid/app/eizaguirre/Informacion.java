package com.appandroid.app.eizaguirre;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import static com.appandroid.app.eizaguirre.MainActivity.esTablet;

public class Informacion extends AppCompatActivity {

    private Toolbar toolbar; //Declaración de la clase toolbar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

        if(esTablet(getApplicationContext())){ //tablet

            setContentView(R.layout.activity_informacion_tablet );

        }else{ // telefono
            setContentView(R.layout.activity_informacion );


        }

        toolbar = (Toolbar) findViewById ( R.id.tool_bar);
        setSupportActionBar ( toolbar );

        getSupportActionBar().setDisplayShowTitleEnabled(false);



    }

}
