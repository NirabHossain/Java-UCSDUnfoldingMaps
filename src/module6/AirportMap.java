package module6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	private static final long serialVersionUID = 1L;
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;	
	private HashMap<Integer, Location> airports;
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	
	public void setup() {
		size(800,600, OPENGL);
		
		//map = new UnfoldingMap(this, 50, 50, 750, 550, new Google.GoogleMapProvider());
		map = new UnfoldingMap(this, 50, 50, 750, 550);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		airportList = new ArrayList<Marker>();
		airports = new HashMap<Integer, Location>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			m.setRadius(5);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			//System.out.println(m.getProperties());
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());		
			//System.out.println(sl.getProperties());
			routeList.add(sl);
		}
		
		
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		
	}
	
	public void draw() {
		background(100,100,100);
		map.draw();
		
	}

	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(airportList);
		//loop();
	}
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}	

	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			unhideMarkers();
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
			checkClick();
		}
	}

	private void checkClick()
	{
		if (lastClicked != null) {
			lastClicked.setRadius(15);
			return;
		}
		// Loop over the earthquake markers to see if one of them is selected
		for (Marker marker : airportList) {
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker)marker;
				// Hide all the other earthquakes and hide
				for (Marker mhide : airportList) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
					}
				}
				ArrayList<Integer>locArr=new ArrayList<Integer>();
				int src=0;
				for (Marker routeHide : routeList) {
					src=Integer.parseInt(routeHide.getStringProperty("source"));
					if(airports.containsKey(src)){
						break;
					}
				}
				
				for(Marker routeHide : routeList){
					if(src==Integer.parseInt(routeHide.getStringProperty("source"))){
						int dest=Integer.parseInt(routeHide.getStringProperty("destination"));
						System.out.println(dest);
						if(airports.containsKey(dest))locArr.add(dest);
					}
				}
				for(Marker routeHide: routeList){
					if(!(src==Integer.parseInt(routeHide.getStringProperty("source"))) || !locArr.contains(Integer.parseInt(routeHide.getStringProperty("destination"))))
						routeHide.setHidden(true);
				}
				return;
				
			}
		}		
	}
	private void unhideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
			
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
	}


}
