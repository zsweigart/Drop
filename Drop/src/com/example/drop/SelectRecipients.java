package com.example.drop;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SelectRecipients extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_recipients);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_recipients, menu);
        return true;
    }
}
