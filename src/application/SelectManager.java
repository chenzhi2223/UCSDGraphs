/** Class to manage items selected in the GUI
 * 
 * @author UCSD MOOC development team
 *
 */

package application;
import application.services.GeneralService;
import geography.GeographicPoint;
import gmapsfx.javascript.object.Marker;

public class SelectManager {
	private CLabel<GeographicPoint> pointLabel;
	//for TSP.
    private CLabel<GeographicPoint> pointLabel1;
    private CLabel<GeographicPoint> pointLabel2;
    private CLabel<GeographicPoint> pointLabel3;
    private CLabel<GeographicPoint> pointLabel4;
    //for TSP.
    private CLabel<GeographicPoint> startLabel;
    private CLabel<GeographicPoint> destinationLabel;
    private Marker startMarker;
    private Marker destinationMarker;
    private Marker selectedMarker;
    private Marker site1Marker;
    private Marker site2Marker;
    private Marker site3Marker;
    private Marker site4Marker;
    private MarkerManager markerManager;
    private DataSet dataSet;


    public SelectManager() {
        startMarker = null;
        destinationMarker = null;
        site1Marker = null;
        site2Marker = null;
        site3Marker = null;
        site4Marker = null;
        selectedMarker = null;
        pointLabel = null;
        //for TSP
        pointLabel1 = null;
        pointLabel2 = null;
        pointLabel3 = null;
        pointLabel4 = null;
        //for TSP.
        startLabel = null;
        destinationLabel = null;
        dataSet = null;
    }


    public void resetSelect() {
        markerManager.setSelectMode(true);
    }
    public void clearSelected() {
    	selectedMarker = null;
    	pointLabel.setItem(null);
    }

    public void setAndDisplayData(DataSet data) {
    	setDataSet(data);
        //TODO - maybe if markerManager!= null?
        if(markerManager != null) {
            markerManager.displayDataSet();
        }
        else {
        	System.err.println("Error : Marker Manager is null.");
        }
    }

    public void setMarkerManager(MarkerManager manager) { this.markerManager = manager; }
    public void setPoint(GeographicPoint point, Marker marker) {
        // System.out.println("inSetPoint.. passed : " + point);
    	pointLabel.setItem(point);
        selectedMarker = marker;
    }
    public void setDataSet(DataSet dataSet) {
    	this.dataSet = dataSet;
    	if(markerManager != null) {
    		markerManager.setDataSet(dataSet);
    	}
    }

    public void setPointLabel(CLabel<GeographicPoint> label) { this.pointLabel = label; }
    public void setStartLabel(CLabel<GeographicPoint> label) { this.startLabel = label; }
    public void setDestinationLabel(CLabel<GeographicPoint> label) { this.destinationLabel = label; }
    //for TSP
    public void setPointLabel1(CLabel<GeographicPoint> label) { this.pointLabel1 = label; }
    public void setPointLabel2(CLabel<GeographicPoint> label) { this.pointLabel2 = label; }
    public void setPointLabel3(CLabel<GeographicPoint> label) { this.pointLabel3 = label; }
    public void setPointLabel4(CLabel<GeographicPoint> label) { this.pointLabel4 = label; }
    //for TSP
    
    public GeographicPoint getPoint() { return pointLabel.getItem(); }
    //for TSP.
    public GeographicPoint getSite1() { return pointLabel1.getItem(); }
    public GeographicPoint getSite2() { return pointLabel2.getItem(); }
    public GeographicPoint getSite3() { return pointLabel3.getItem(); }
    public GeographicPoint getSite4() { return pointLabel4.getItem(); }
    //for TSP.
    
	public GeographicPoint getStart(){return startLabel.getItem();}
	public GeographicPoint getDestination(){return destinationLabel.getItem();}
	
    public void setStart() {
		if(pointLabel.getItem() != null) {
        	GeographicPoint point = pointLabel.getItem();
    		startLabel.setItem(point);
            markerManager.setStart(point);
		}
	}

	public void setDestination() {
		if(pointLabel.getItem() != null) {
        	GeographicPoint point = pointLabel.getItem();
    		destinationLabel.setItem(point);
    		markerManager.setDestination(point);
		}
	}
	
	//for TSP solution.
	public void setSite1() {
		if(pointLabel.getItem() != null) {
        	GeographicPoint point = pointLabel.getItem();
        	pointLabel1.setItem(point);
            markerManager.setSite1(point);
		}
	}
	public void setSite2() {
		if(pointLabel.getItem() != null) {
        	GeographicPoint point = pointLabel.getItem();
        	pointLabel2.setItem(point);
            markerManager.setSite2(point);
		}
	}
	public void setSite3() {
		if(pointLabel.getItem() != null) {
        	GeographicPoint point = pointLabel.getItem();
        	pointLabel3.setItem(point);
            markerManager.setSite3(point);
		}
	}
	public void setSite4() {
		if(pointLabel.getItem() != null) {
        	GeographicPoint point = pointLabel.getItem();
        	pointLabel4.setItem(point);
            markerManager.setSite4(point);
		}
	}
    //for TSP solution.


}