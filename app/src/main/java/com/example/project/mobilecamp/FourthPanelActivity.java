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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class FourthPanelActivity extends AppCompatActivity {

    private static boolean searchNode(List<LocNode> init, LocNode source, LocNode dest, List<LocNode> res, HashSet<Integer> buff) {
//		System.out.println(source.getDescription());
        if(!dest.isContaining(source)) {
            System.out.println("=============1");
            //buff.add(source.getIdNode());
            source.setVisited(true);

            for(LocNode ln : source.getNeighbors()) {
                if(!ln.isVisited()) {
                //if(!buff.contains(ln.getIdNode())) {
                    System.out.println("=============2");
                    if(searchNode(init, ln, dest, res, buff)) {
                        System.out.println("=============3");
                        res.add(ln);
                        return true;
                    }
                }
            }
            return false;
        }
        else {
//			System.out.println("." +dest.getDescription());
            return true;
        }
    }

    private static boolean contain(Integer id, ArrayList<Integer> neighbour){
        for(Integer a : neighbour){
            if(id.equals(a)){
                return true;
            }
        }

        return false;

    }

    private static boolean searchTwo(Integer idSource, Integer idTarget, HashSet<Integer> buff, ArrayList<String> result, DataBaseHelper myDbHelper){


        if(!contain(idTarget,myDbHelper.getNeighbor(idSource))){
            buff.add(idSource);
            for(Integer id : myDbHelper.getNeighbor(idSource)){
                if(!buff.contains(id)){
                    //System.out.println("==============1");
                    if(searchTwo(id,idTarget,buff,result,myDbHelper)){
                        //System.out.println("=================2");
                        result.add(myDbHelper.getName(id));
                        return true;
                    }
                }

            }
            return false;
        }
        else{
            return true;
        }


    }




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




        setContentView(R.layout.fourth_panel); //there was activity_main
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
        final FourthPanelActivity fourthPanelActivity = this;

        //Pobieranie teskstu z textedit i na jego podstawie wyszukiwanie w bazie
        String searchText  = getIntent().getStringExtra("TARGET_NAME");
        Cursor cr = myDbHelper.getLocalization(searchText);

        //Ustawienie nazwy szukenj pracowni
        TextView textView3 = (TextView) findViewById(R.id.textview3);
        String localizationName = cr.getString(3);
        textView3.setText(localizationName);

        //Ladowanie odpowiedniego obrazka jako mapy
        ImageView map = (ImageView) findViewById(R.id.map);
        String pathImg = cr.getString(0);
        int resID = getResources().getIdentifier(pathImg,"drawable",getPackageName());
        map.setImageResource(resID);

        HashSet<Integer> buff = new HashSet<>();
        //ArrayList<LocNode> result = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        String source  = getIntent().getStringExtra("SOURCE_NAME");
        //searchNode(myDbHelper.createInitList(),myDbHelper.createLocNode(source),myDbHelper.createLocNode(searchText),result, buff);
        searchTwo(myDbHelper.getId(source),myDbHelper.getId(searchText),buff,result,myDbHelper);
        Collections.reverse(result);

        //System.out.println("TO JEST WIELKOSC LISTY ==========" + result.size());



        EditText description = (EditText) findViewById(R.id.opis);
        StringBuilder res = new StringBuilder();
        for(String s : result){
            //System.out.println("test :" + s.getDescription());
            //System.out.println("test==============" + s);
            res.append(s + "\n");

        }
        description.setText(res);

        Button buttonGoBack = (Button) findViewById(R.id.button8) ;
        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setContentView(R.layout.activity_main);
                Intent intent = new Intent(fourthPanelActivity,ThirdPanelActivity.class);
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



