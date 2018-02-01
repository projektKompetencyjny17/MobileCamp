package com.example.project.mobilecamp;

/**
 * Created by Ola on 2017-12-17.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import java.sql.SQLException;
import android.database.Cursor;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class FourthPanelActivity extends AppCompatActivity {


    private static boolean contain(Integer id, ArrayList<Integer> neighbour){
        for(Integer a : neighbour){
            if(id.equals(a)){
                return true;
            }
        }

        return false;

    }

    private static boolean searchTwo(Integer idSource, Integer idTarget, HashSet<Integer> buff, ArrayList<Integer> result, DataBaseHelper myDbHelper){


        if(!contain(idTarget,myDbHelper.getNeighbor(idSource))){
            buff.add(idSource);
            for(Integer id : myDbHelper.getNeighbor(idSource)){
                if(!buff.contains(id)){
                    //System.out.println("==============1");
                    if(searchTwo(id,idTarget,buff,result,myDbHelper)){
                        //System.out.println("=================2");
                        //result.add(myDbHelper.getName(id));
                        result.add(id);
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
       // TextView textView3 = (TextView) findViewById(R.id.textview3);
//        String localizationName = cr.getString(3);
       // textView3.setText(localizationName);





        //wyszukiwanie
        HashSet<Integer> visitedId = new HashSet<>();

        ArrayList<Integer> result = new ArrayList<>();

        String source  = getIntent().getStringExtra("SOURCE_NAME");
        searchTwo(myDbHelper.getId(source),myDbHelper.getId(searchText),visitedId,result,myDbHelper);
        Collections.reverse(result);


        //Ladowanie odpowiedniego obrazka jako mapy

        ImageView map = (ImageView) findViewById(R.id.map);
        String pathImg = cr.getString(0);
        int resID = getResources().getIdentifier(pathImg,"drawable",getPackageName());

        //zeby nie skalowalo obrazka
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        //obraz w oryginale
        Bitmap myMapOrg = BitmapFactory.decodeResource(getResources(),resID,opts);

        //obraz zeskalowany przez android studio
        Bitmap myMap = BitmapFactory.decodeResource(getResources(),resID);

        // obiekt paint do malowania drogi kolor czerowony grubosc 10
        Paint myPaint = new Paint();
        myPaint.setColor(Color.RED);
        myPaint.setStrokeWidth(5);
        myPaint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));

        // trzeba wyliczyc skale, zeby potem odpowiednio narysowac pkt w powiekszonym obrazie
        int scale = myMap.getHeight()/myMapOrg.getHeight();


        Bitmap tempBitmap = Bitmap.createBitmap(myMap.getWidth(), myMap.getHeight(), Bitmap.Config.RGB_565);//tworzenie bitmapy do zapisania w image view
        Canvas tempCanvas = new Canvas(tempBitmap);// canavs do tej mapy


        tempCanvas.drawBitmap(myMap, 0, 0, null);// wgranie obrazka na bitmape


        //obliczanie pkt do rysowania oraz rysowanie
        Integer startX = myDbHelper.getCordinates(myDbHelper.getId(source)).get(0)*scale;
        Integer startY = myDbHelper.getCordinates(myDbHelper.getId(source)).get(1)*scale;



        //buf wpolzednych pocztkoawych, potem sie przyda do wycentorwania
        final int bufStartX = startX, bufStartY=startY;

        Integer bufX = 0,bufY = 0;


        for(Integer id : result){
            ArrayList<Integer> buf = myDbHelper.getCordinates(id);
            bufX = buf.get(0)*scale;
            bufY = buf.get(1)*scale;
            tempCanvas.drawLine(startX,startY,bufX,bufY,myPaint);
            //tempCanvas.drawPoint(buf.get(0),buf.get(1),myPaint);
            startX = bufX;
            startY = bufY;

        }

        bufX = myDbHelper.getCordinates(myDbHelper.getId(searchText)).get(0)*scale;
        bufY = myDbHelper.getCordinates(myDbHelper.getId(searchText)).get(1)*scale;

        tempCanvas.drawLine(startX,startY,bufX,bufY,myPaint);

        // przekazanie bitmapy do wyswietlenia w imageview
        map.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

        //Centrowanie widoku
        final HorizontalScrollView mainScroll = (HorizontalScrollView) findViewById(R.id.scroll);

        ViewTreeObserver vto = mainScroll.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                mainScroll.scrollTo(bufStartX, bufStartX);
            }
        });








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



