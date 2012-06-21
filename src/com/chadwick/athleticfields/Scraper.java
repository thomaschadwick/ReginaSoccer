package com.chadwick.athleticfields;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.xml.sax.SAXException;

public class Scraper {
	
    //<table class="contentpaneopen">
    //<tr> 
    //<td valign="top" colspan="2"> 
    //<strong><p>Today is <em><strong>Monday, May 30th</strong></em>, and all fields (including practice and passive green spaces) are open, and available for use as scheduled with the following exception:  Grassick Park Field.</p><p>Updated at 3:10pm</p><p>For Weekend Field Conditions - Please call the City of Regina at 777-7484 </p></strong><p>In the event of inclement weather and field play has not been cancelled, coaches and referees are responsible for insuring the safety of their participants, assessing field conditions and stopping play when damage to the field may occur. </p><p>Field closure information for City of Regina athletic fields is also available through the <a href="http://www.regina.ca/Page1030.aspx" target="_blank">City of Regina web site</a>. </p></td> 
    //</tr> 
    //</table> 

	
	private static final String STATUS_XPATH = "//div[@id='content_box'/h1/p/p";
    // example XPATH queries in the form of strings - will be used later
    //private static final String NAME_XPATH = "//div[@class='yfi_quote']/div[@class='hd']/h2";

    //private static final String TIME_XPATH = "//table[@id='time_table']/tbody/tr/td[@class='yfnc_tabledata1']";

    //private static final String PRICE_XPATH = "//table[@id='price_table']//tr//span";

    // TagNode object, its use will come in later
    private static TagNode node;

    // a method that helps me retrieve the stock option's data based off the name (i.e. GOUAA is one of Google's stock options)
    public String getProperty() throws XPatherException, ParserConfigurationException,SAXException, IOException, XPatherException {

        // the URL whose HTML I want to retrieve and parse
        String soccerSite = "http://www.regina.ca/Page1030.aspx";
        String name = "";
        
        // this is where the HtmlCleaner comes in, I initialize it here
        HtmlCleaner cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        props.setAllowHtmlInsideAttributes(true);
        props.setAllowMultiWordAttributes(true);
        props.setRecognizeUnicodeChars(true);
        props.setOmitComments(true);
        

        // open a connection to the desired URL
        URL url = new URL(soccerSite);
        URLConnection conn = url.openConnection();

        //use the cleaner to "clean" the HTML and return it as a TagNode object
        node = cleaner.clean(new InputStreamReader(conn.getInputStream()));

        // once the HTML is cleaned, then you can run your XPATH expressions on the node, which will then return an array of TagNode objects (these are returned as Objects but get casted below)
        Object[] info_nodes = node.evaluateXPath(STATUS_XPATH);


        // here I just do a simple check to make sure that my XPATH was correct and that an actual node(s) was returned
        if (info_nodes.length > 0) {
            // casted to a TagNode
            TagNode info_node = (TagNode) info_nodes[0];
            // how to retrieve the contents as a string
            name = info_node.getChildren().iterator().next().toString().trim();
        }

        /*if (time_nodes.length > 0) {
            TagNode time_node = (TagNode) time_nodes[0];
            String date = time_node.getChildren().iterator().next().toString().trim();
        }

        if (price_nodes.length > 0) {
            TagNode price_node = (TagNode) price_nodes[0];
            double price = Double.parseDouble(price_node.getChildren().iterator().next().toString().trim());
        }*/

        return name;
    }
}
