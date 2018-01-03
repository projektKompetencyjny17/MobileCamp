package com.example.project.mobilecamp;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by adino on 03.01.2018.
 */

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private String nameLocation;

    public String getNameLocation() {
        return nameLocation;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        nameLocation = (String)parent.getItemAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
