package com.chadwick.athleticfields;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class Status extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Object saveResult = getLastNonConfigurationInstance();       
        
        setContentView(R.layout.status);
        
        if (saveResult == null) {
        	Downloader downloader = new Downloader();
            downloader.execute("");
        } else {
        	display((String)saveResult);
        }
        
        final Button button = (Button) findViewById(R.id.refresh);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Downloader downloader = new Downloader();
                downloader.execute("");
            }
        });
    }

    public void display(String result) {
        TextView date = (TextView) findViewById(R.id.date);
        TextView status = (TextView) findViewById(R.id.status);
        date.setText(result.substring(0, result.indexOf("\n")));
        status.setText(result.substring(result.indexOf("\n")+1,result.length()));
    }
    
    @Override
    public Object onRetainNonConfigurationInstance() {
    	String text = null;
    	TextView date = (TextView) findViewById(R.id.date);
    	TextView status = (TextView) findViewById(R.id.status);

    	if (date.getText() != null && status.getText() != null)
    		text = date.getText().toString() + "\n" + status.getText().toString();

        return text;
    }

    private class Downloader extends AsyncTask<String,Void,String> {
        String myString = null;
        ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... arg0) {
            try{
                URL myURL = new URL("http://www.regina.ca/Page1030.aspx");
                //URL coedURL = new URL("http://www.reginasoccer.com/11Outdoor/Adult/Schedules/TS_Co-ed_1035_Orange.htm");
                URLConnection ucon = myURL.openConnection();
                //URLConnection coedCon = coedURL.openConnection();

                String DATE = "//strong[1]";
                String FIELDS = "//p[3]";
                String FIELDS2 = "//strong";
                // String COED = "//"
                
                HtmlCleaner cleaner = new HtmlCleaner();
                CleanerProperties props = cleaner.getProperties();
                props.setAllowHtmlInsideAttributes(true);
                props.setAllowMultiWordAttributes(true);
                props.setRecognizeUnicodeChars(true);
                props.setOmitComments(true);
                TagNode node;
                //TagNode coed;
                
                
                node = cleaner.clean(new InputStreamReader(ucon.getInputStream()));
                //coed = cleaner.clean(new InputStreamReader(coedCon.getInputStream()));
                

                // once the HTML is cleaned, then you can run your XPATH expressions on the node, which will then return an array of TagNode objects (these are returned as Objects but get casted below)
                Object[] info_nodes = node.evaluateXPath(DATE);
                Object[] field_nodes = node.evaluateXPath(FIELDS);
                Object[] field2_nodes = node.evaluateXPath(FIELDS2);


                // here I just do a simple check to make sure that my XPATH was correct and that an actual node(s) was returned
                if (info_nodes.length > 0) {
                    // casted to a TagNode
                    TagNode info_node = (TagNode) info_nodes[0];
                    // how to retrieve the contents as a string
                    myString = info_node.getChildren().iterator().next().toString().trim();
                }
                
                if (field_nodes.length > 0) {
                    // casted to a TagNode
                    TagNode info_node = (TagNode) field_nodes[0];
                    // how to retrieve the contents as a string
                    myString += "\n";
                    myString += info_node.getChildren().iterator().next().toString().trim();
                }
                
                if (field2_nodes.length > 0) {
                    // casted to a TagNode
                    TagNode info_node = (TagNode) field2_nodes[2];
                    // how to retrieve the contents as a string
                    myString += " ";
                    myString += info_node.getChildren().iterator().next().toString().trim();
                }
                
            }catch(Exception e){
                myString = e.getMessage();
            }
            return myString;
        }
        @Override
	    protected void onPostExecute(String result){
	        progressDialog.dismiss();
	        display(result);
	    }
	
	    @Override
	    protected void onPreExecute() {
	        progressDialog = ProgressDialog.show(getParent(), null, "Updating...", true);
	        super.onPreExecute();
	    }
    }
}
