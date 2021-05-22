package module1;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class HelloWorld extends PApplet
{
	private static final long serialVersionUID = 1L;

	public static String mbTilesString = "blankLight-1-3.mbtiles";
	private static final boolean offline = false;
	UnfoldingMap map1, map2;

	public void setup() {
		size(800, 600, P2D);  
		this.background(200, 200, 200);
		
		// Select a map provider
		AbstractMapProvider provider = new Google.GoogleTerrainProvider();
		// Set a zoom level
		int zoomLevel = 10;
		
		if (offline) {
			provider = new MBTilesMapProvider(mbTilesString);
			zoomLevel = 3;
		}
		
		// Create a new UnfoldingMap to be displayed in this window.  
		// The 2nd-5th arguments give the map's x, y, width and height
		// When you create your map we want you to play around with these 
		// arguments to get your second map in the right place.
		// The 6th argument specifies the map provider.  
		// There are several providers built-in.
		// Note if you are working offline you must use the MBTilesMapProvider
		map1 = new UnfoldingMap(this, 50, 50, 350, 500, provider);

		// The next line zooms in and centers the map at 
	    // 32.9 (latitude) and -117.2 (longitude)
	    map1.zoomAndPanTo(zoomLevel, new Location(32.9f, -117.2f));
		
		// This line makes the map interactive
		MapUtils.createDefaultEventDispatcher(this, map1);
		
		map2 = new UnfoldingMap(this, 450, 50, 350, 500, provider);
		map2.zoomAndPanTo(zoomLevel, new Location(23.77, 90.39));
		MapUtils.createDefaultEventDispatcher(this, map2);
		
		// TODO: Add code here that creates map2 
		// Then you'll modify draw() below

	}

	/** Draw the Applet window.  */
	public void draw() {
		// So far we only draw map1...
		// TODO: Add code so that both maps are displayed
		map1.draw();
		map2.draw();
	}

	
}
