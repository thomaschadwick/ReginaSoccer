package com.chadwick.athleticfields;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class AthleticFields extends TabActivity {

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, Status.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("status").setIndicator("Status").setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, Schedule.class);
        spec = tabHost.newTabSpec("schedule").setIndicator("Schedule").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Manage.class);
        spec = tabHost.newTabSpec("manage").setIndicator("Manage").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
}