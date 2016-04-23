package application.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.MapApp;
import application.MarkerManager;
import application.SelectManager;
import application.CLabel;
import application.services.GeneralService;
import application.services.RouteService;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.LatLongBounds;
import gmapsfx.javascript.object.MVCArray;
import gmapsfx.shapes.Polyline;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.util.StringConverter;

public class RouteController {
	// Strings for slider labels
	public static final int BFS = 3;
    public static final int A_STAR = 2;
    public static final int DIJ = 1;
    public static final int TSP_Greedy = 4;
    public static final int TSP_2_opt = 5;
	public static final int DISABLE = 0;
	public static final int START = 1;
	public static final int DESTINATION = 2;

    private int selectedToggle = DIJ;

    private RouteService routeService;
    private Button displayButton;
    private Button hideButton;
    private Button startButton;
    private Button resetButton;
    private Button destinationButton;
    //for TSP.
    private Button site1Buttion;
    private Button site2Buttion;
    private Button site3Buttion;
    private Button site4Buttion;
    //for TSP.
    private Button visualizationButton;

    private ToggleGroup group;
    private CLabel<geography.GeographicPoint> startLabel;
    private CLabel<geography.GeographicPoint> endLabel;
    private CLabel<geography.GeographicPoint> pointLabel;
    private CLabel<geography.GeographicPoint> pointLabel1;
    private CLabel<geography.GeographicPoint> pointLabel2;
    private CLabel<geography.GeographicPoint> pointLabel3;
    private CLabel<geography.GeographicPoint> pointLabel4;
    private List<CLabel<geography.GeographicPoint>> pointLabels;
    private SelectManager selectManager;
    private MarkerManager markerManager;



	public RouteController(RouteService routeService, Button displayButton, Button hideButton,
						   Button resetButton, Button startButton, Button destinationButton,
						   ToggleGroup group, List<RadioButton> searchOptions, Button visualizationButton,
						   CLabel<geography.GeographicPoint> startLabel, CLabel<geography.GeographicPoint> endLabel,
						   CLabel<geography.GeographicPoint> pointLabel, SelectManager manager, MarkerManager markerManager) {
        // save parameters
        this.routeService = routeService;
		this.displayButton = displayButton;
        this.hideButton = hideButton;
		this.startButton = startButton;
		this.resetButton = resetButton;
		this.destinationButton = destinationButton;
        this.group = group;
        this.visualizationButton = visualizationButton;

        // maybe don't need references to labels;
		this.startLabel = startLabel;
		this.endLabel = endLabel;
        this.pointLabel = pointLabel;
        this.selectManager = manager;
        this.markerManager = markerManager;

        setupDisplayButtons();
        setupRouteButtons();
        setupVisualizationButton();
        setupLabels();
        setupToggle();
        //routeService.displayRoute("data/sampleroute.map");
	}
	
	
	//@JackCathy for TSP solution.
	public RouteController(RouteService routeService, Button displayButton, Button hideButton,
			   Button resetButton, Button startButton, Button destinationButton, List<Button> siteButtons,
			   ToggleGroup group, List<RadioButton> searchOptions, Button visualizationButton,
			   CLabel<geography.GeographicPoint> startLabel, CLabel<geography.GeographicPoint> endLabel,
			   CLabel<geography.GeographicPoint> pointLabel, List<Label> pointLabels, SelectManager manager, MarkerManager markerManager) {
        // save parameters
		this.routeService = routeService;
		this.displayButton = displayButton;
		this.hideButton = hideButton;
		this.startButton = startButton;
		this.resetButton = resetButton;
		this.destinationButton = destinationButton;
		//for TSP
		this.site1Buttion = siteButtons.get(0);
		this.site2Buttion = siteButtons.get(1);
		this.site3Buttion = siteButtons.get(2);
		this.site4Buttion = siteButtons.get(3);
		//for TSP
		//this.destinationButton = destinationButton;
		this.group = group;
		this.visualizationButton = visualizationButton;

		// maybe don't need references to labels;
		this.startLabel = startLabel;
		this.endLabel = endLabel;
		this.pointLabel = pointLabel;
		//for TSP.
		this.pointLabel1 = (CLabel<geography.GeographicPoint>)pointLabels.get(0);
		this.pointLabel2 = (CLabel<geography.GeographicPoint>)pointLabels.get(1);
		this.pointLabel3 = (CLabel<geography.GeographicPoint>)pointLabels.get(2);
		this.pointLabel4 = (CLabel<geography.GeographicPoint>)pointLabels.get(3);
		pointLabels.add(pointLabel1);
	    pointLabels.add(pointLabel2);
	    pointLabels.add(pointLabel3);
	    pointLabels.add(pointLabel4);
		//for TSP.
		this.selectManager = manager;
		this.markerManager = markerManager;

        setupDisplayButtons();
        //setupRouteButtons();
        setupRouteButtonsForTSP();
        setupVisualizationButtonTSP();
        setupLabels();
        setupToggle();
        //routeService.displayRoute("data/sampleroute.map");
    }

	private void setupDisplayButtons() {
		displayButton.setOnAction(e -> {
            if(startLabel.getItem() != null && endLabel.getItem() != null) {
        			routeService.displayRoute(startLabel.getItem(), endLabel.getItem(), selectedToggle);
            }
            else if(ISTSPSet()) {
            	    List<geography.GeographicPoint> sites = getItems();
            	    routeService.displayRouteTSP(startLabel.getItem(), sites, selectedToggle);
            }
            else {
            	MapApp.showErrorAlert("Route Display Error", "Make sure to choose points for both start and destination.");
            }
		});

        hideButton.setOnAction(e -> {
        	routeService.hideRoute();
        });

        //TODO -- implement
        resetButton.setOnAction( e -> {

            routeService.reset();
        });
	}
	
	//@author JackCathy.
	private boolean ISTSPSet() {
		boolean startSign = (startLabel.getItem() != null);
		System.out.println("point1Label");
		System.out.println(pointLabel1.getItem().toString());
		boolean point1Sign = (pointLabel1.getItem()!=null);
		System.out.println("pointLabe2");
		System.out.println(pointLabel2.getItem().toString());
		System.out.println("pointLabe3");
		System.out.println(pointLabel3.getItem().toString());
		
		System.out.println("pointLabe4");
		System.out.println(pointLabel4.getItem().toString());
		
		boolean point2Sign = (pointLabel2.getItem()!=null);
		
		boolean point3Sign = (pointLabel3.getItem()!=null);
		boolean point4Sign = (pointLabel4.getItem()!=null);
		if (startSign && (point1Sign && point2Sign && point3Sign && point4Sign) ) {
			System.out.println(true);
			return true;
		}
		return false;
	}
	
	private List<geography.GeographicPoint> getItems() {
		System.out.println("getItems for each TSP sites:"); 
		List<geography.GeographicPoint> sites;
		 /*if(pointLabels==null) {
			 return null;
		 }*/
		 sites = new ArrayList<geography.GeographicPoint>();
		 /*for (CLabel<geography.GeographicPoint> label: pointLabels) {
			 //@SuppressWarnings("unchecked")
			 //CLabel<geography.GeographicPoint> clabel = (CLabel<geography.GeographicPoint>)label;
			 geography.GeographicPoint site = label.getItem();
			 System.out.println(site.toString());
			 sites.add(site);
		 }*/
		 sites.add(pointLabel1.getItem());
		 System.out.println("point 1"+ pointLabel1.getItem().toString());
		 sites.add(pointLabel2.getItem());
		 System.out.println("point 2" + pointLabel2.getItem().toString());			
		 sites.add(pointLabel3.getItem());
		 System.out.println("point 3" + pointLabel3.getItem().toString());		
		 sites.add(pointLabel4.getItem());
		 System.out.println("point 4" + pointLabel4.getItem().toString());
		 System.out.println(sites.size());
		 return sites;
	}

    private void setupVisualizationButton() {
    	visualizationButton.setOnAction( e -> {
    		markerManager.startVisualization();
    	});
    }
    private void setupVisualizationButtonTSP() {
    	visualizationButton.setOnAction( e -> {
    		markerManager.startVisualizationTSP();
    	});
    }
    private void setupRouteButtons() {
    	startButton.setOnAction(e -> {
            //System.out.println();
            selectManager.setStart();
    	});

        destinationButton.setOnAction( e-> {
            selectManager.setDestination();
        });
    }
    private void setupRouteButtonsForTSP() {
    	startButton.setOnAction(e -> {
            //System.out.println();
            selectManager.setStart();
    	});

        /*destinationButton.setOnAction( e-> {
            selectManager.setDestination();
        });*/
        //for TSP solution.
        site1Buttion.setOnAction( e-> {
            selectManager.setSite1();
        });
        site2Buttion.setOnAction( e-> {
            selectManager.setSite2();
        });
        site3Buttion.setOnAction( e-> {
            selectManager.setSite3();
        });
        site4Buttion.setOnAction( e-> {
            selectManager.setSite4();
        });
        //for TSP solution.
    }


    private void setupLabels() {


    }

    private void setupToggle() {
    	group.selectedToggleProperty().addListener( li -> {
            if(group.getSelectedToggle().getUserData().equals("Dijkstra")) {
            	selectedToggle = DIJ;
            }
            else if(group.getSelectedToggle().getUserData().equals("A*")) {
            	selectedToggle = A_STAR;
            }
            else if(group.getSelectedToggle().getUserData().equals("BFS")) {
            	selectedToggle = BFS;
            }
            else if(group.getSelectedToggle().getUserData().equals("TSP Greedy")) {
            	selectedToggle = TSP_Greedy;
            }
            else if(group.getSelectedToggle().getUserData().equals("TSP 2-opt")) {
            	selectedToggle = TSP_2_opt;
            }
            else {
            	System.err.println("Invalid radio button selection");
            }
    	});
    }




}
