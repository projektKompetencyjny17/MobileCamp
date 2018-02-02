package com.example.project.mobilecamp;

/**
 * Created by Ola on 2017-12-17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import java.sql.SQLException;
import java.util.ArrayList;


public class ThirdPanelActivity extends AppCompatActivity {

    //private EditText targetName;
    private Spinner sourceSpinner;
    private Spinner targetSpinner;

    private SpinnerActivity sourceActivity;
    private SpinnerActivity targetActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {;
        super.onCreate(savedInstanceState);

        DataBaseHelper myDbHelper = new DataBaseHelper(this);

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            //throw sqle;
            System.out.print("blad");


        }



        setContentView(R.layout.third_panel); //there was activity_main
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
        final ThirdPanelActivity thirdPanelActivity = this;


        //targetName = (EditText) findViewById(R.id.search1);
        sourceActivity = new SpinnerActivity();
        targetActivity = new SpinnerActivity();


        ArrayList<String> listSource = myDbHelper.getListOfLocation();

        sourceSpinner = (Spinner) findViewById(R.id.source_spinner);
        sourceSpinner.setOnItemSelectedListener(sourceActivity);

        // Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapterSource = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,listSource);
        // Specify the layout to use when the list of choices appears
        adapterSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sourceSpinner.setAdapter(adapterSource);




        targetSpinner = (Spinner) findViewById(R.id.target_spinner);
        targetSpinner.setOnItemSelectedListener(targetActivity);

        // Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapterTarget = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,listSource);
        // Specify the layout to use when the list of choices appears
        adapterTarget.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        targetSpinner.setAdapter(adapterTarget);





        Button buttonGoBack = (Button) findViewById(R.id.button7) ;
        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setContentView(R.layout.activity_main);
                Intent intent = new Intent(thirdPanelActivity,SecondPanelClass.class);
                // setContentView(R.layout.activity_main);
                startActivity(intent);

            }
        });

        Button buttonSearch = (Button) findViewById(R.id.button6) ;
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setContentView(R.layout.activity_main);

                Intent intent = new Intent(thirdPanelActivity,FourthPanelActivity.class);
                intent.putExtra("SOURCE_NAME",sourceActivity.getNameLocation());
                intent.putExtra("TARGET_NAME",targetActivity.getNameLocation());

                // setContentView(R.layout.activity_main);
                startActivity(intent);

            }
        });



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


