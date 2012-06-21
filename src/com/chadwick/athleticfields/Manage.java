package com.chadwick.athleticfields;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Manage extends Activity {
	
	private DataAccess _DataAccess;
	
	private class DivisionInfo {
		private String Division;
		private String URL;
		public void setDivision(String division) {
			Division = division;
		}
		public String getDivision() {
			return Division;
		}
		public void setURL(String uRL) {
			URL = uRL;
		}
		public String getURL() {
			return URL;
		}
	}
	
	private class TeamInfo {
		private String Team;
		private String URL;
		public void setTeam(String team) {
			Team = team;
		}
		public String getTeam() {
			return Team;
		}
		public void setURL(String uRL) {
			URL = uRL;
		}
		public String getURL() {
			return URL;
		}
	}
	
	// some globals for temporarily storing the division/url or team/url pairs
	public ArrayList<DivisionInfo> divInfo = new ArrayList<DivisionInfo>();
	public ArrayList<TeamInfo> teamInfo = new ArrayList<TeamInfo>();
	private TeamInfo selectedTeam = new TeamInfo();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //final Object saveResult = getLastNonConfigurationInstance();       
        
        setContentView(R.layout.manage);
        
        
        /*if (saveResult == null) {
        	Downloader downloader = new Downloader();
            downloader.execute("");
        } else {
        	display((String)saveResult);
        }*/
        
        TableLayout table = (TableLayout) findViewById(R.id.manageDisplayTeams);
        TextView txt2 = (TextView) findViewById(R.id.teamName);
        SharedPreferences settings = getSharedPreferences("soccer.xml", 0);
        Map<String, ?> teams = settings.getAll();
        //txt2.setText(teams.get("TeamName").toString());
        for (Map.Entry<String, ?> entry : teams.entrySet())
        {
        	if (entry.getKey().toString() == "TeamName")
        	{
        		TableRow tr = new TableRow(this);
                tr.setLayoutParams(new LayoutParams(
                               LayoutParams.FILL_PARENT,
                               LayoutParams.WRAP_CONTENT));
                TextView txt = new TextView(this);
            	txt.setText(entry.getValue().toString());
            	txt.setLayoutParams(new LayoutParams(
        						LayoutParams.FILL_PARENT,
        						LayoutParams.WRAP_CONTENT));
            	tr.addView(txt);
                     /* Create a Button to be the row-content. */
                     //Button b = new Button(this);
                     //b.setText("Button");
                     //b.setLayoutParams(new LayoutParams(
                     //          LayoutParams.FILL_PARENT,
                     //          LayoutParams.WRAP_CONTENT));
                     /* Add Button to row. */
           /* Add row to TableLayout. */
                 table.addView(tr,new TableLayout.LayoutParams(
                 LayoutParams.FILL_PARENT,
                 LayoutParams.WRAP_CONTENT));
                 txt2.setText(entry.getValue().toString());
        	}
        	//txt2.setText("no for");
        }   
        DivisionDownloader divisionDownloader = new DivisionDownloader();
        divisionDownloader.execute("");
        
        
        final Button button = (Button) findViewById(R.id.addTeamButton);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//CharSequence status = "" + _DataAccess.insert(selectedTeam.getTeam(), selectedTeam.getURL());
            	
                SharedPreferences settings = getSharedPreferences("soccer.xml", 0);
                getSharedPreferences("soccer.xml", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("TeamName", selectedTeam.getTeam());
                editor.putString("TeamUrl", selectedTeam.getURL());
                // Don&#39;t forget to commit your edits!!!
                editor.commit();
                
            	Toast.makeText(Manage.this, "Added Team", Toast.LENGTH_LONG).show();
            	
            	TextView txt = (TextView) findViewById(R.id.teamName);
            	
            	settings = getSharedPreferences("soccer.xml", 0);
            	String team = settings.getString("TeamName", "Not found");
    	        txt.setText(team);
    	        
            }
        });
        
    }
	
	public class DivisionOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	teamInfo.clear();
	    	TeamDownloader teamDownloader = new TeamDownloader();
	    	teamDownloader.execute(divInfo.get(pos).getURL());
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	      // Do nothing.
	    }
	}
	
	public class TeamOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	selectedTeam.setTeam(teamInfo.get(pos).getTeam());
	    	selectedTeam.setURL(teamInfo.get(pos).getURL());
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	      // Do nothing.
	    }
	}
	
    public void display(String result) {
    	
    }
    
    @Override
    public Object onRetainNonConfigurationInstance() {
    	String text = null;
    	/*TextView date = (TextView) findViewById(R.id.date);
    	TextView status = (TextView) findViewById(R.id.status);

    	if (date.getText() != null && status.getText() != null)
    		text = date.getText().toString() + "\n" + status.getText().toString();*/

        return text;
    }
    
    private class TeamDownloader extends AsyncTask<String, Void, String> {
    	ProgressDialog progressDialog;
    	ArrayList<String> teamList = new ArrayList<String>();
    	ArrayList<String> urlList = new ArrayList<String>();
    	String status = null;
    	@Override
    	protected String doInBackground(String... arg0){
    		try {
    			String urlString = "http://www.reginasoccer.com" + arg0[0];
    			URL url = new URL(urlString);
                
                URLConnection ucon = url.openConnection();

                String LINKS = "//option";        
              
                HtmlCleaner cleaner = new HtmlCleaner();
                CleanerProperties props = cleaner.getProperties();
                props.setAllowHtmlInsideAttributes(true);
                props.setAllowMultiWordAttributes(true);
                props.setRecognizeUnicodeChars(true);
                props.setOmitComments(true);
                TagNode node;
                
                node = cleaner.clean(new InputStreamReader(ucon.getInputStream()));
                
                Object[] team_nodes = node.evaluateXPath(LINKS); 
                
                if (team_nodes.length > 0) {
            		for(int i = 0; i < team_nodes.length; i++){
                		TagNode team_node = (TagNode) team_nodes[i];
                		teamList.add(team_node.getChildren().iterator().next().toString().trim());
                		urlList.add(team_node.getAttributeByName("value"));
                	}
                }
                
                /*
                for(Object linkNode : team_nodes){   
                    TagNode[] links = ((TagNode)linkNode).getElementsHavingAttribute("value", true);
                    for(TagNode t: links){
                    	String temp = t.getAttributeByName("value");
                    	urlList.add(temp);
                    }
                }
                */
                
                for (int i = 0; i < teamList.size(); i++) {
                	TeamInfo temp = new TeamInfo();
                	temp.setTeam(teamList.get(i));
                	if (!urlList.isEmpty()) {
                		temp.setURL(urlList.get(i));
                	}else{
                		temp.setURL("noURL");
                	}
                	teamInfo.add(temp);
                }
	        }catch(Exception e){
            	status = e.getMessage();
            }
            return status;
	    }
        @Override
        protected void onPostExecute(String Result){
            progressDialog.dismiss();
	        if (Result != null) {
	        	Toast.makeText(Manage.this, "Failed to update teams.", Toast.LENGTH_LONG).show();
	        } else {
	        	ArrayList<String> teamsToDisplay = new ArrayList<String>();
	        	for (int i = 0; i < teamInfo.size(); i++) {
	        		teamsToDisplay.add(teamInfo.get(i).getTeam());
	        	}
		    	Spinner spinner = (Spinner) findViewById(R.id.TeamSpinner);
		        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Manage.this,android.R.layout.simple_spinner_item, teamsToDisplay);
		        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        spinner.setAdapter(adapter);
		        spinner.setOnItemSelectedListener(new TeamOnItemSelectedListener());
	        }
    	}
    
	    @Override
	    protected void onPreExecute() {
	        progressDialog = ProgressDialog.show(getParent(), null, "Grabbing Teams...", true);
	        super.onPreExecute();
	    }
	}
  
    private class DivisionDownloader extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        String status = null;
        ArrayList<String> divisionList = new ArrayList<String>();
        ArrayList<String> urlList = new ArrayList<String>();
        @Override
        protected String doInBackground(String... arg0) {
            try{

                URL myURL = new URL("http://www.reginasoccer.com/adult/adult-schedules");
                
                URLConnection ucon = myURL.openConnection();

                String DIVISIONS = "//blockquote/p/font/font";
                String LINKS = "//blockquote";

                HtmlCleaner cleaner = new HtmlCleaner();
                CleanerProperties props = cleaner.getProperties();
                props.setAllowHtmlInsideAttributes(true);
                props.setAllowMultiWordAttributes(true);
                props.setRecognizeUnicodeChars(true);
                props.setOmitComments(true);
                TagNode node;
                
                node = cleaner.clean(new InputStreamReader(ucon.getInputStream()));

                Object[] info_nodes = node.evaluateXPath(DIVISIONS);
                Object[] link_nodes = node.evaluateXPath(LINKS); 
                
                for(Object linkNode : link_nodes){   
                    TagNode[] links = ((TagNode)linkNode).getElementsHavingAttribute("href", true);
                    for(TagNode t: links){
                    	String temp = t.getAttributeByName("href");
                    	if (temp.contains("Default")){
                    		urlList.add(temp.replace("Default", "Index"));
                    	} 
                    }
                }
                
                if (info_nodes.length > 0) {
                	for(int i = 0; i < info_nodes.length; i++){
	                    TagNode info_node = (TagNode) info_nodes[i];
	                    TagNode info_node2 = null;
	                    String temp, temp2 = null;
	                    
	                    if (i < info_nodes.length-1)
	                    	info_node2 = (TagNode) info_nodes[i+1];
	                    
	                    if (info_node2 != null)
	                    	temp2 = info_node2.getChildren().iterator().next().toString().trim();

	                    temp = info_node.getChildren().iterator().next().toString().trim();
	                    
	                    if (temp.startsWith("Men") || temp.startsWith("Co-ed") || temp.startsWith("Women's"))
	                    	if (temp2 != null)
	                    		if (!temp2.startsWith("Men") && !temp2.startsWith("Co-ed") && !temp2.startsWith("Women's")) {
	                    			temp += temp2;
	                    			i++;
	                    		}
	                    if (!temp.contains("Botox") && !temp.contains("Masters")) {
		                    divisionList.add(temp);
	                    }
                	}
                }
                
                for (int i = 0; i < divisionList.size(); i++) {
                	DivisionInfo temp = new DivisionInfo();
                	temp.setDivision(divisionList.get(i));
                	temp.setURL(urlList.get(i));
                	divInfo.add(temp);
                }
                	
            }catch(Exception e){
            	status = e.getMessage();
            }
            return status;
        }
        @Override
	    protected void onPostExecute(String Result){
	        progressDialog.dismiss();
            
	        if (Result != null) {
	        	Toast.makeText(Manage.this, "Failed to update divisions.", Toast.LENGTH_LONG).show();
	        } else {
	        	ArrayList<String> divisionsToDisplay = new ArrayList<String>();
	        	for (int i = 0; i < divInfo.size(); i++) {
	        		divisionsToDisplay.add(divInfo.get(i).getDivision());
	        	}
		    	Spinner spinner = (Spinner) findViewById(R.id.DivisionSpinner);
		        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Manage.this,android.R.layout.simple_spinner_item, divisionsToDisplay);
		        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        spinner.setAdapter(adapter);
		        spinner.setOnItemSelectedListener(new DivisionOnItemSelectedListener());
	        }
	    }
	
	    @Override
	    protected void onPreExecute() {
	        progressDialog = ProgressDialog.show(getParent(), null, "Grabbing Divisions...", true);
	        super.onPreExecute();
	    }
    }
}