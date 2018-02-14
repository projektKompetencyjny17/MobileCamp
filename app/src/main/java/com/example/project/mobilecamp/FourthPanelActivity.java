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
import android.support.annotation.NonNull;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class FourthPanelActivity extends AppCompatActivity {


    private static boolean contain(Integer id, ArrayList<LocNode> neighbour){
        for(LocNode a : neighbour){
            if(id.equals(a.id)){
                return true;
            }
        }

        return false;

    }
    private float countDistance(LocNode ln1, LocNode ln2){
        float a = ((ln1.lokX - ln2.lokX)*(ln1.lokX - ln2.lokX) + (ln1.lokY - ln2.lokY)*(ln1.lokY - ln2.lokY));
        //System.out.println("ln1= " + ln1.id + " ln2= " + ln2.id +"       l1x: " + ln1.lokX + " l2x: " + ln2.lokX + " l1y: " + ln1.lokY + " l2y: " + ln2.lokY + "    res = " + a );
        return (float)Math.sqrt(a);
    }
    private boolean searchNode(LocNode source, LocNode target, HashSet<Integer> visited, ArrayList<LocNode> result, DataBaseHelper myDbHelper){

        if(!contain(target.id, myDbHelper.getNeighbor(source))){
            visited.add(source.id);
            ArrayList<LocNode> neighbors = myDbHelper.getNeighbor(source);
            System.out.println("Visited: " + visited.toString());
            String str = "";
            for(LocNode ln : neighbors){
                str += ln.id + " ";
            }
            //System.out.println("neighbors: " + str);
            if(neighbors.size() > 1){
                float bestDist = 10000;
                LocNode bestNode = neighbors.get(0);
                for(LocNode node : neighbors){
                    //System.out.println("====== " + visited.contains(node.id) + "   size: " + myDbHelper.getNeighbor(node).size());
                    if(!visited.contains(node.id) && myDbHelper.getNeighbor(node).size() > 1) {
                        if (bestDist > countDistance(node, target)) {
                            bestDist = countDistance(node, target);
                            bestNode = node;
                        }
                    }

                }
                //System.out.println("**" + bestNode.id + "-> " + str);
                //System.out.println("bestnode: " + bestNode.id);
                if(!visited.contains(bestNode.id)) {
                    //System.out.println("**" + bestNode.id);
                    result.add(bestNode);
                    searchNode(bestNode,target, visited,result,myDbHelper);
                    return true;
                }
                searchNode(bestNode,target, visited,result,myDbHelper);
                return false;

            }
            else if(neighbors.size() == 1){
                LocNode current = neighbors.get(0);
                if(!visited.contains(current.id)) {
                    System.out.println("*" + neighbors.get(0).id);
                    result.add(current);
                    searchNode(current, target, visited, result, myDbHelper);

                    return true;
                }
                return false;
            }
            else
                return false;

        }
        else{
            return true;
        }



    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        String targetName  = getIntent().getStringExtra("TARGET_NAME");
        Cursor cr = myDbHelper.getLocalizationData(targetName);

        //Ustawienie nazwy szukenj pracowni
       // TextView textView3 = (TextView) findViewById(R.id.textview3);
//        String localizationName = cr.getString(3);
       // textView3.setText(localizationName);






        //wyszukiwanie

        String source  = getIntent().getStringExtra("SOURCE_NAME");
        LocNode sourceId = myDbHelper.getLocnode(source);

        ArrayList<LocNode> result = getResultOfSearch(myDbHelper, targetName, sourceId);

        //System.out.println("=======-----------------=======" + result + "================================");


        //Ladowanie odpowiedniego obrazka jako mapy
        ArrayList<Integer> resultInt = new ArrayList<>();
        for(LocNode ln : result)
            resultInt.add(ln.id);

        printingMap(myDbHelper, targetName, resultInt, sourceId.id);








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

    @NonNull
    private ArrayList<LocNode> getResultOfSearch(DataBaseHelper myDbHelper, String targetName, LocNode sourceId) {
        HashSet<Integer> visitedId = new HashSet<>();

        ArrayList<LocNode> result = new ArrayList<>();


        searchNode(sourceId,myDbHelper.getLocnode(targetName),visitedId,result,myDbHelper);
        //Collections.reverse(result);
        String str = "";
        for(LocNode ln : result)
            str += ln.id + " ";
        //System.out.println("sting: " + str);
        return result;
    }

    private void printingMap(DataBaseHelper myDbHelper, String targetName, List<Integer> result, Integer sourceId) {
        ImageView map = (ImageView) findViewById(R.id.map);
        String pathImg = myDbHelper.getPathOfImg(sourceId);
        int resID = getResources().getIdentifier(pathImg,"drawable",getPackageName());

        //zeby nie skalowalo obrazka
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        //obraz w oryginale
        Bitmap myMapOrg = BitmapFactory.decodeResource(getResources(),resID,opts);

        //obraz zeskalowany przez android studio
        Bitmap myMap = BitmapFactory.decodeResource(getResources(),resID);

        // obiekt paint do malowania drogi kolor czerowony grubosc 5
        Paint myPaint = setLineProperties();

        // trzeba wyliczyc skale, zeby potem odpowiednio narysowac pkt w powiekszonym obrazie
        int scale = getScale(myMapOrg, myMap);


        Bitmap tempBitmap = Bitmap.createBitmap(myMap.getWidth(), myMap.getHeight(), Bitmap.Config.RGB_565);//tworzenie bitmapy do zapisania w image view
        Canvas tempCanvas = new Canvas(tempBitmap);// canavs do tej mapy


        tempCanvas.drawBitmap(myMap, 0, 0, null);// wgranie obrazka na bitmape

        //rysowanie pkt
        drawingPoints(myDbHelper, targetName, result, sourceId, myPaint, scale, tempCanvas);


        // przekazanie bitmapy do wyswietlenia w imageview
        map.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

        //Centrowanie widoku
        Integer bufStartX = myDbHelper.getCordinates(sourceId).get(0)*scale;
        Integer bufStartY = myDbHelper.getCordinates(sourceId).get(1)*scale;
        centeringView(bufStartX, bufStartY);
    }

    @NonNull
    private Paint setLineProperties() {
        Paint myPaint = new Paint();
        myPaint.setColor(Color.RED);
        myPaint.setStrokeWidth(5);
        myPaint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
        return myPaint;
    }

    private int getScale(Bitmap myMapOrg, Bitmap myMap) {
        return myMap.getHeight()/myMapOrg.getHeight();
    }

    private void drawingPoints(DataBaseHelper myDbHelper, String targetName, List<Integer> result, Integer sourceId, Paint myPaint, int scale, Canvas tempCanvas) {
        //obliczanie pkt do rysowania oraz rysowanie
        Integer startX = myDbHelper.getCordinates(sourceId).get(0)*scale;
        Integer startY = myDbHelper.getCordinates(sourceId).get(1)*scale;


        //iterator
        int i =0;
        Integer bufX = 0,bufY = 0;
        boolean forWasBroken=false;
        for(Integer id : result){
            if(!myDbHelper.getPathOfImg(id).equals(myDbHelper.getPathOfImg(sourceId))){
                forWasBroken = true;
                List<Integer> newResult = result.subList(i,result.size());
                createButton(bufX, bufY, myDbHelper, targetName, newResult, id);
                break;
            }
            ArrayList<Integer> buf = myDbHelper.getCordinates(id);
            bufX = buf.get(0)*scale;
            bufY = buf.get(1)*scale;
            tempCanvas.drawLine(startX,startY,bufX,bufY,myPaint);
            printDescription(myDbHelper, id);
            //System.out.println(id);
            //tempCanvas.drawPoint(buf.get(0),buf.get(1),myPaint);
            startX = bufX;
            startY = bufY;
            //result.remove(i);
            i++;



        }

        if(!forWasBroken) {
            bufX = myDbHelper.getCordinates(myDbHelper.getId(targetName)).get(0) * scale;
            bufY = myDbHelper.getCordinates(myDbHelper.getId(targetName)).get(1) * scale;

            tempCanvas.drawLine(startX, startY, bufX, bufY, myPaint);
        }


    }

    private void printDescription(DataBaseHelper myDbHelper, Integer sourceId) {
        TextView descriptionView = (TextView) findViewById(R.id.textview3);

        for(Integer id : myDbHelper.getNeighbor(sourceId)) {
            String description = null;
            if ((description = myDbHelper.getDescription(id)) != null) {
                //System.out.println("==================" + description + "===============");
                CharSequence buff = descriptionView.getText();
                descriptionView.setText(buff + description+"\n");


            }
        }
    }

    private void createButton(Integer bufX, Integer bufY, final DataBaseHelper myDbHelper, final String targetName, final List<Integer> result, final Integer sourceId) {
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.realtiveLayout);

        final TextView descriptionView = (TextView) findViewById(R.id.textview3);
        CharSequence buff = descriptionView.getText();
        descriptionView.setText(buff + "Kliknij w przycisk na mapie po dalsza droge");

        final Button nextLineButton = new Button(this);
        //nextLineButton.setMaxHeight(10);
        //nextLineButton.setMaxWidth(10);
        //int size = 10*scale;

        //int size = 8*scale;
        //int size2 = size;
        //dynamicznie cos nei dziala
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(80,80);
        parms.leftMargin = bufX-40;
        parms.topMargin = bufY-40;

        nextLineButton.setLayoutParams(parms);

        nextLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descriptionView.setText("");
                printingMap(myDbHelper,targetName,result,sourceId);
                relativeLayout.removeView(nextLineButton);


            }
        });

        relativeLayout.addView(nextLineButton);
    }

    private void centeringView(final int bufStartX, final int bufStartY) {
        final HorizontalScrollView horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        final MaxHeightScrollView verticalScroll = (MaxHeightScrollView) findViewById(R.id.verticalScroll);

        final int constVar = 50;

        ViewTreeObserver firstVto = horizontalScroll.getViewTreeObserver();
        firstVto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                horizontalScroll.scrollTo(bufStartX-constVar, bufStartY- constVar);
            }
        });

        ViewTreeObserver secondVto = verticalScroll.getViewTreeObserver();
        secondVto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                verticalScroll.scrollTo(bufStartX- constVar, bufStartY- constVar);
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



