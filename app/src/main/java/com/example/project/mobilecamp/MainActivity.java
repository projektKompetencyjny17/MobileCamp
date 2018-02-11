package com.example.project.mobilecamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import java.io.*;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Towrzenie bazy w pamieci telefonu UWAGA wykonuje sie tylko raz, po zmianie bazy nalezy ja wpier usunac z pamieci telefonu
        DataBaseHelper myDbHelper = new DataBaseHelper(this);

        myDbHelper.delateDatabase(); //sluzy do usuwania bazy danych z pamieci telefonu

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }






        setContentView(R.layout.activity_main); //there was activity_main
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Previous there was action for floating button -> use it to learn setting action after click - about 'fab' - it's good example for actions
          /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        final MainActivity mainActivity = this;
        Button buttonGo = (Button) findViewById(R.id.button2) ;
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity,SecondPanelClass.class);
                // setContentView(R.layout.second_panel);
                startActivity(intent);
            }
        });

        Button buttonHelp = (Button) findViewById(R.id.button3) ;
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity,HelpActivity.class);
                // setContentView(R.layout.second_panel);
                startActivity(intent);
                //setContentView(R.layout.help_panel);




            }
        });
   /*
        Button buttonGoBack = (Button) findViewById(R.id.button5) ;
        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
