package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;
public class EarthquakeCityMap extends PApplet {
	private static final long serialVersionUID = 1L;
	private static final boolean offline = false;
	public static final float THRESHOLD_MODERATE = 5;
	public static final float THRESHOLD_LIGHT = 4;
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	private UnfoldingMap map;
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	public void setup() {
		size(950, 600, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			earthquakesURL = "2.5_week.atom";// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		}
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
	    List<Marker> markers = new ArrayList<Marker>(); 	    // The List you will populate with new SimplePointMarkers
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    for(PointFeature ft:earthquakes){//Add a loop here that calls createMarker (see below)
	    	SimplePointMarker pm= createMarker(ft);// to create a new SimplePointMarker for each PointFeature in  earthquakes
	    	markers.add(pm);//Add each new SimplePointMarker to the List markers add the markers to the map so that they are displayed
	    }
	    map.addMarkers(markers);
	}
		
	/* createMarker: A suggested helper method that takes in an earthquake 
	 * feature and returns a SimplePointMarker for that earthquake
	 * 
	 * TODO (Step 4): Add code to this method so that it adds the proper 
	 * styling to each marker based on the magnitude of the earthquake.  
	*/
	private SimplePointMarker createMarker(PointFeature feature)
	{  
		// To print all of the features in a PointFeature (so you can see what they are)
		// uncomment the line below.  Note this will only print if you call createMarker 
		// from setup
		//System.out.println(feature.getProperties());
		
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
		
	    int yellow = color(255, 255, 0),red=color(255,0,0),blue=color(0,0,255);
		if(mag>=5)marker.setColor(red);
		else if(mag>=4)marker.setColor(yellow);
		else marker.setColor(blue);
	    return marker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}
	private void addKey() 
	{	
		fill(255,255,255);
		rect(25,50,150,250);
		fill(0,0,0);
		text("5+ Magnitude",35,80);
		fill(255,0,0);
		ellipse(30,80,5,5);
	}
}
