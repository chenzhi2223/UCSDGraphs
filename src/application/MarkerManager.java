/** Class to manage Markers on the Map
 * 
 * @author UCSD MOOC development team
 *
 */

package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import gmapsfx.javascript.event.UIEventType;
import gmapsfx.javascript.object.Animation;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.Marker;
import gmapsfx.javascript.object.MarkerOptions;
import javafx.scene.control.Button;
import gmapsfx.javascript.object.LatLongBounds;
import netscape.javascript.JSObject;

public class MarkerManager {

    private static final double DEFAULT_Z = 2;
    private static final double SELECT_Z = 1;
    private static final double STRTDEST_Z = 3;

    private HashMap<geography.GeographicPoint, Marker> markerMap;
    private ArrayList<geography.GeographicPoint> markerPositions;
    private GoogleMap map;
    protected static String startURL = "http://maps.google.com/mapfiles/kml/pal3/icon40.png";
    protected static String destinationURL = "http://maps.google.com/mapfiles/kml/pal2/icon5.png";
    protected static String SELECTED_URL = "http://maps.google.com/mapfiles/kml/paddle/ltblu-circle.png";
    protected static String markerURL = "http://maps.google.com/mapfiles/kml/paddle/blu-diamond-lv.png";
	protected static String visURL = "http://maps.google.com/mapfiles/kml/paddle/red-diamond-lv.png";
    private Marker startMarker;
    private Marker destinationMarker;
    //for TSP
    private Marker site1Marker;
    private Marker site2Marker;
    private Marker site3Marker;
    private Marker site4Marker;
    //for TSP
    private Marker selectedMarker;
    private DataSet dataSet;
    private LatLongBounds bounds;
    private SelectManager selectManager;
    private RouteVisualization rv;
    private Button vButton;
    private boolean selectMode = true;

    public MarkerManager() {
    	markerMap = new HashMap<geography.GeographicPoint, Marker>();
    	map = null;
    	selectManager = null;
        rv = null;
        markerPositions = null;
    }
    //TODO -- parameters?
    public MarkerManager(GoogleMap map, SelectManager selectManager) {
    	// TODO -- parameters?
    	markerMap = new HashMap<geography.GeographicPoint, Marker>();
        dataSet = null;
        this.map=map;
        this.selectManager = selectManager;
        rv = null;
        markerPositions = null;
    }

    /**
     * Used to set reference to visualization button. Manager will be responsible
     * for disabling button
     *
     * @param vButton
     */
    public void setVisButton(Button vButton) {
    	this.vButton = vButton;
    }

    public void setSelect(boolean value) {
    	selectMode = value;
    }
    public RouteVisualization getVisualization() { return rv; }



    public GoogleMap getMap() { return this.map; }
    public void setMap(GoogleMap map) { this.map = map; }
    public void setSelectManager(SelectManager selectManager) { this.selectManager = selectManager; }

    public void putMarker(geography.GeographicPoint key, Marker value) {
    	markerMap.put(key, value);

    }

    /** Used to initialize new RouteVisualization object
     *
     */
    public void initVisualization() {
    	rv = new RouteVisualization(this);
    }

    public void clearVisualization() {
        rv.clearMarkers();
    	rv = null;
    }

    // TODO -- protect against this being called without visualization built
    public void startVisualization() {
    	if(rv != null) {
	    	rv.startVisualization();
    	}
    }
    public void startVisualizationTSP() {
    	if(rv != null) {
	    	rv.startVisualizationTSP();
    	}
    }

    public void setStart(geography.GeographicPoint point) {
    	if(startMarker!= null) {
            changeIcon(startMarker, markerURL);
//            startMarker.setZIndex(DEFAULT_Z);
    	}
        startMarker = markerMap.get(point);
//        startMarker.setZIndex(STRTDEST_Z);
        changeIcon(startMarker, startURL);
    }
    public void setDestination(geography.GeographicPoint point) {
    	if(destinationMarker != null) {
    		destinationMarker.setIcon(markerURL);
//            destinationMarker.setZIndex(DEFAULT_Z);
    	}
        destinationMarker = markerMap.get(point);
//        destinationMarker.setZIndex(STRTDEST_Z);
        changeIcon(destinationMarker, destinationURL);
    }
    
    //for TSP setting with 4 sites.
    public void setSite1(geography.GeographicPoint point) {
    	if(site1Marker!= null) {
            changeIcon(site1Marker, markerURL);
//            startMarker.setZIndex(DEFAULT_Z);
    	}
        site1Marker = markerMap.get(point);
//        startMarker.setZIndex(STRTDEST_Z);
        changeIcon(site1Marker, SELECTED_URL);
    }   
    public void setSite2(geography.GeographicPoint point) {
    	if(site2Marker!= null) {
            changeIcon(site2Marker, markerURL);
//            startMarker.setZIndex(DEFAULT_Z);
    	}
        site2Marker = markerMap.get(point);
//        startMarker.setZIndex(STRTDEST_Z);
        changeIcon(site2Marker, SELECTED_URL);
    }
    public void setSite3(geography.GeographicPoint point) {
    	if(site3Marker!= null) {
            changeIcon(site3Marker, markerURL);
//            startMarker.setZIndex(DEFAULT_Z);
    	}
        site3Marker = markerMap.get(point);
//        startMarker.setZIndex(STRTDEST_Z);
        changeIcon(site3Marker, SELECTED_URL);
    }    
    public void setSite4(geography.GeographicPoint point) {
    	if(site4Marker!= null) {
            changeIcon(site4Marker, markerURL);
//            startMarker.setZIndex(DEFAULT_Z);
    	}
        site4Marker = markerMap.get(point);
//        startMarker.setZIndex(STRTDEST_Z);
        changeIcon(site4Marker, SELECTED_URL);
    }
    //for TSP setting.
    
    public void changeIcon(Marker marker, String url) {
        marker.setVisible(false);
        marker.setIcon(url);
        marker.setVisible(true);
    }

    /**
     * TODO -- Might need to create all new markers and add them??
     */
    public void restoreMarkers() {
    	Iterator<geography.GeographicPoint> it = markerMap.keySet().iterator();
        while(it.hasNext()) {
            Marker marker = markerMap.get(it.next());
            // destination marker needs to be added because it is added in javascript
            if(marker != startMarker) {
                marker.setVisible(false);
                marker.setVisible(true);
            }
        }
        selectManager.resetSelect();
    }

    public void refreshMarkers() {

    	Iterator<geography.GeographicPoint> it = markerMap.keySet().iterator();
        while(it.hasNext()) {
        	Marker marker = markerMap.get(it.next());
        	marker.setVisible(true);
        }
    }
    public void clearMarkers() {
        if(rv != null) {
        	rv.clearMarkers();
        	rv = null;
        }
    	Iterator<geography.GeographicPoint> it = markerMap.keySet().iterator();
    	while(it.hasNext()) {
    		markerMap.get(it.next()).setVisible(false);
    	}
    }

    public void setSelectMode(boolean value) {
        if(!value) {
        	selectManager.clearSelected();
        }
    	selectMode = value;
    }

    public boolean getSelectMode() {
    	return selectMode;
    }
    public static MarkerOptions createDefaultOptions(LatLong coord) {
        	MarkerOptions markerOptions = new MarkerOptions();
        	markerOptions.animation(null)
        				 .icon(markerURL)
        				 .position(coord)
                         .title(null)
                         .visible(true);
        	return markerOptions;
    }

    public void hideIntermediateMarkers() {
        Iterator<geography.GeographicPoint> it = markerMap.keySet().iterator();
        while(it.hasNext()) {
            Marker marker = markerMap.get(it.next());
            if(marker != startMarker && marker != destinationMarker
            		&& marker != site1Marker && marker != site2Marker
            		&& marker != site3Marker && marker != site4Marker) {
                marker.setVisible(false);
            }
//        	map.addMarker(marker);
        }
    }

    public void hideDestinationMarker() {
    	destinationMarker.setVisible(false);
    }

    public void displayMarker(geography.GeographicPoint point) {
    	if(markerMap.containsKey(point)) {
        	Marker marker = markerMap.get(point);
            marker.setVisible(true);
            // System.out.println("Marker : " + marker + "set to visible");
    	}
    	else {
    		// System.out.println("no key found for MarkerManager::displayMarker");
    	}
    }
    public void displayDataSet() {
        markerPositions = new ArrayList<geography.GeographicPoint>();
        dataSet.initializeGraph();
    	Iterator<geography.GeographicPoint>it = dataSet.getIntersections().iterator();
        bounds = new LatLongBounds();
        while(it.hasNext()) {
        	geography.GeographicPoint point = it.next();
            LatLong ll = new LatLong(point.getX(), point.getY());
        	MarkerOptions markerOptions = createDefaultOptions(ll);
            bounds.extend(ll);
        	Marker marker = new Marker(markerOptions);
            registerEvents(marker, point);
        	map.addMarker(marker);
        	putMarker(point, marker);
        	markerPositions.add(point);
//            marker.setZIndex(DEFAULT_Z);
        }
        map.fitBounds(bounds);
        // System.out.println("End of display Intersections");

    }


    private void registerEvents(Marker marker, geography.GeographicPoint point) {
        /*map.addUIEventHandler(marker, UIEventType.mouseover, (JSObject o) -> {
           marker.setVisible(true);
           //marker.setAnimation(Animation.BOUNCE);
        });

        map.addUIEventHandler(marker, UIEventType.mouseout, (JSObject o) -> {
        	marker.setAnimation(null);
        });*/

        map.addUIEventHandler(marker, UIEventType.click, (JSObject o) -> {
            //System.out.println("Clicked Marker : " + point.toString());
            if(selectMode) {
                	if(selectedMarker != null && selectedMarker != startMarker
                	   && selectedMarker != destinationMarker
                	   && selectedMarker != site1Marker
                	   && selectedMarker != site2Marker
                	   && selectedMarker != site3Marker
                	   && selectedMarker != site4Marker
                			) {
                		selectedMarker.setIcon(markerURL);
//                		selectedMarker.setZIndex(DEFAULT_Z);
                	}
            	selectManager.setPoint(point, marker);
                selectedMarker = marker;
                selectedMarker.setIcon(SELECTED_URL);
//                selectedMarker.setZIndex(SELECT_Z);

                // re add markers to map
                // slightly glitchy
//                refreshMarkers();
            }
        });
    }

    public void disableVisButton(boolean value) {
    	if(vButton != null) {
	    	vButton.setDisable(value);
    	}
    }
	public void setDataSet(DataSet dataSet) {
		this.dataSet= dataSet;
	}


    public DataSet getDataSet() { return this.dataSet; }
}
