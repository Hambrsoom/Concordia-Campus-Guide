package com.example.concordia_campus_guide.view_models;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.concordia_campus_guide.R;
import com.example.concordia_campus_guide.database.AppDatabase;
import com.example.concordia_campus_guide.global.SelectingToFromState;
import com.example.concordia_campus_guide.helper.BuildingCodeMap;
import com.example.concordia_campus_guide.helper.CurrentLocation;
import com.example.concordia_campus_guide.helper.FloorPlan;
import com.example.concordia_campus_guide.helper.ManipulateWalkingPoints;
import com.example.concordia_campus_guide.helper.POIIcon;
import com.example.concordia_campus_guide.helper.PolygonsDrawer;
import com.example.concordia_campus_guide.models.Building;
import com.example.concordia_campus_guide.models.Place;
import com.example.concordia_campus_guide.models.PoiType;
import com.example.concordia_campus_guide.models.RoomModel;
import com.example.concordia_campus_guide.models.WalkingPoint;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Logger;

public class LocationFragmentViewModel extends ViewModel {

    private AppDatabase appDatabase;
    private MutableLiveData<PriorityQueue<WalkingPoint>> poiList = new MutableLiveData<>();
    private FloorPlan floorPlan;
    private PolygonsDrawer polygonsDrawer;
    private BuildingCodeMap buildingCodeMap;

    private CurrentLocation currentLocation;
    private POIIcon poiIcon;
    private ManipulateWalkingPoints manipulateWalkingPoints;

    //EV centerCoordinates
    private LatLng initialZoomLocation = new LatLng(45.495638 ,-73.578258);

    public static final Logger LOGGER = Logger.getLogger("LocationFragmentViewModel");

    private HashMap<String, List<WalkingPoint>> walkingPointsMap = new HashMap<>();

    public FloorPlan getFloorPlan() {
        return floorPlan;
    }
    public  Map<String, List<WalkingPoint>> getWalkingPointsMap(){
        return walkingPointsMap;
    }

    public LocationFragmentViewModel(AppDatabase appDb) {
        this.appDatabase = appDb;
        this.poiIcon = new POIIcon();
        this.floorPlan = new FloorPlan(appDatabase);
        this.manipulateWalkingPoints = new ManipulateWalkingPoints();
        this.buildingCodeMap = new BuildingCodeMap();
    }

    /**
     * The purpose of this method to load the overlay polygon on the map.
     *
     * @param map                is the map used in our application.
     * @param applicationContext is the Context of the LocationFragmentView page
     * @return It will return the layer to the LocationFragmentView to display on the map
     */
    public GeoJsonLayer loadPolygons(GoogleMap map, Context applicationContext) {
        polygonsDrawer = new PolygonsDrawer();
        return polygonsDrawer.loadPolygons(map,applicationContext, buildingCodeMap.getBuildings());
    }

    public Building getBuildingFromGeoJsonFeature(GeoJsonFeature feature) {
        polygonsDrawer = new PolygonsDrawer();
        return polygonsDrawer.getBuildingFromGeoJsonFeature(feature);
    }

    /**
     * @param buildingCode it represents which building we will be covering
     * @return Int of drawable resource's bitmap representation
     */
    public void setFloorPlan(GroundOverlay groundOverlay, String buildingCode, String floor, GoogleMap mMap) {
        floorPlan.setFloorPlan(groundOverlay,buildingCode,floor,mMap,walkingPointsMap);
    }

    public LatLng getZoomLocation(String location) {
        return buildingCodeMap.getZoomLocation(location);
    }

    public Building getBuildingFromCode(String buildingCode) {
        return buildingCodeMap.getBuildingFromCode(buildingCode);
    }

    public Map<String, Building> getBuildings() {
        return buildingCodeMap.getBuildings();
    }

    public void setBuildings(Map<String, Building> buildings){ buildingCodeMap.setBuildings(buildings);}

    public LatLng getInitialZoomLocation() {
        return initialZoomLocation;
    }

    public void setInitialZoomLocation(LatLng latLng){
        initialZoomLocation = latLng;
    }

    public void setListOfPOI(@PoiType String poiType, Context context) {
        List<WalkingPoint> allPOI = appDatabase.walkingPointDao().getAllPointsForPointType(poiType);
        poiIcon.setCurrentPOIIcon(poiType, context);
        currentLocation = new CurrentLocation(context);
        currentLocation.setMyLocation(SelectingToFromState.getMyCurrentLocation());
        this.poiList.postValue(poiIcon.getPOIinOrder(appDatabase,allPOI,currentLocation));
    }


    public BitmapDescriptor getCustomSizedIcon(String filename, Context context, int height, int width) {
        return poiIcon.getCustomSizedIcon(filename,context,height,width);
    }

    public BitmapDescriptor getCurrentPOIIcon(){
        return poiIcon.getCurrentPOIIcon();
    }

    public LiveData<List<RoomModel>> getListOfRoom() {
        return floorPlan.getRoomsInAFloor().getListOfRoom();
    }

    public LiveData<PriorityQueue<WalkingPoint>> getListOfPOI() {
        return poiList;
    }

    public RoomModel getRoomByRoomCodeAndFloorCode(String roomCode, String floorCode){
        return appDatabase.roomDao().getRoomByRoomCodeAndFloorCode(roomCode, floorCode);
    }

    public void parseWalkingPointList(AppDatabase appDatabase, SharedPreferences sharedPreferences, Place from, Place to){
        manipulateWalkingPoints.parseWalkingPointList(appDatabase,sharedPreferences, from, to, walkingPointsMap);
    }

    public List<WalkingPoint> getWalkingPointsList(){
       return manipulateWalkingPoints.getWalkingPointsList();
    }

    public void drawShuttlePath(GoogleMap mMap, String polyline) {
        List<LatLng> route = PolyUtil.decode(polyline);
        mMap.addPolyline(new PolylineOptions().addAll(route).color(R.color.colorAppTheme).width(20));
    }

    /**
     * @return return the map style
     */
    public int getMapStyle() {
        return R.raw.mapstyle_retro;
    }
}