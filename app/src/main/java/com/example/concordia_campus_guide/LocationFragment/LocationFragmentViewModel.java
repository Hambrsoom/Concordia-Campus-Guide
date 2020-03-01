package com.example.concordia_campus_guide.LocationFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.lifecycle.ViewModel;

import com.example.concordia_campus_guide.BuildingCode;
import com.example.concordia_campus_guide.Global.ApplicationState;
import com.example.concordia_campus_guide.Models.Building;
import com.example.concordia_campus_guide.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPolygonStyle;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static java.lang.Double.parseDouble;

public class LocationFragmentViewModel extends ViewModel {
    private HashMap<String, GroundOverlayOptions> buildingsGroundOverlayOptions = new HashMap<>();
    /**
     * @return return the map style
     */
    public int getMapStyle(){
        return R.raw.mapstyle_retro;
    }

    /**
     * The purpose of this method to load the overlay polygon on the map.
     * @param map is the map used in our application.
     * @param applicationContext is the Context of the LocationFragmentView page
     * @return It will return the layer to the LocationFragmentView to display on the map
     */
    public GeoJsonLayer loadPolygons(GoogleMap map, Context applicationContext){
        GeoJsonLayer layer = initLayer(map, applicationContext);
        setPolygonStyle(layer,map, applicationContext);
        return  layer;
    }

    /**
     * The purpose of this method is to initiate the layer
     * @param map is the map used in our application.
     * @param applicationContext is the Context of the LocationFragmentView page
     * @return the initiated layer or it will throw an exception if it didn't find the
     *  GeoJson File
     */
    private GeoJsonLayer initLayer(GoogleMap map, Context applicationContext){
        GeoJsonLayer layer = null;

        try {
            JSONObject geoJsonLayer = ApplicationState.getInstance(applicationContext).getBuildings().getGeoJson();
            layer = new GeoJsonLayer(map, geoJsonLayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layer;
    }

    public void setBuildingGroundOverlayOptions(GeoJsonFeature feature){
        Building building = getBuildingFromGeoJsonFeature(feature);
        buildingsGroundOverlayOptions.put(feature.getProperty("code"), new GroundOverlayOptions()
                .position(building.getCenterCoordinates(), building.getWidth(), building.getHeight())
                .image(BitmapDescriptorFactory.fromAsset("buildings_floorplans/"+building.getBuildingCode()+"_"+building.getAvailableFloors()[building.getAvailableFloors().length-1].toLowerCase()+".png"))
                .bearing(Float.parseFloat(feature.getProperty("bearing"))));
    }

    public Building getBuildingFromGeoJsonFeature(GeoJsonFeature feature){
        LatLng centerPos = getCenterPositionBuildingFromGeoJsonFeature(feature);
        String[] floorsAvailable = feature.getProperty("floorsAvailable").split(",");
        float building_width = Float.parseFloat(feature.getProperty("width"));
        float building_height = Float.parseFloat(feature.getProperty("height"));
        String building_code = feature.getProperty("code").toLowerCase();

        return new Building(centerPos, floorsAvailable, building_code, building_width, building_height);
    }


    public LatLng getCenterPositionBuildingFromGeoJsonFeature(GeoJsonFeature feature){
        String[] coordinates = feature.getProperty("center").split(", ");
        LatLng centerPos = new LatLng(parseDouble(coordinates[1]), parseDouble(coordinates[0]));
        return centerPos;
    }
  
    public HashMap<String, GroundOverlayOptions> getBuildingGroundOverlaysOptions(){
        return buildingsGroundOverlayOptions;
    }

    /**
     * @param layer the GeoJson layer containing features to style.
     * @param map the google map where layer will be displayed and markers will be added.
     */
    private void setPolygonStyle(GeoJsonLayer layer, GoogleMap map, Context context){
        for (GeoJsonFeature feature : layer.getFeatures()){
            feature.setPolygonStyle(getPolygonStyle());

            if(feature.getProperty("floorsAvailable") != null)
                setBuildingGroundOverlayOptions(feature);

            String[] coordinates = feature.getProperty("center").split(", ");
            LatLng centerPos = new LatLng(parseDouble(coordinates[1]), parseDouble(coordinates[0]));
            addBuildingMarker(map, centerPos, feature.getProperty("code"), context);
        }
    }

    /**
     * The purpose of this method is to add a marker on the specified building.
     * @param map is the map used in our application.
     * @param centerPos is the latitude and longitude of the building's center
     * @param buildingLabel is the Building on which the method will add a marker
     */
    public void addBuildingMarker(GoogleMap map, LatLng centerPos, String buildingLabel, Context context) {
        Marker marker = map.addMarker(
                new MarkerOptions()
                        .position(centerPos)
                        .icon(styleMarker(buildingLabel,context))
                        .flat(true)
                        .anchor(0.5f,0.5f)
                        .alpha(0.90f)
                        //This line should be included whenever we test the UI for the marker:
                        .title(buildingLabel)
        );
        marker.setTag(buildingLabel);
    }

    /**
     * The purpose of this method is setup the style of the marker.
     * @param buildingLabel the label of the building
     * @param context the context of the LocationFragment
     * @return it will BitmapDescriptor object to use it as an icon for the marker on the map.
     */
    public BitmapDescriptor styleMarker(String buildingLabel, Context context) {
        int height = 150;
        int width = 150;
        InputStream deckFile = null;
        try {
            deckFile = context.getAssets().open("BuildingLabels/" + buildingLabel.toLowerCase()+".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap b = BitmapFactory.decodeStream(deckFile);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        return  smallMarkerIcon;
    }

//    /**
//     * Add classroom markers to map
//     * @param map
//     * @param centerPos
//     * @param classNumber
//     */
//    private void addClassroomMarker(GoogleMap map, LatLng centerPos, String classNumber) {
//        Marker marker = map.addMarker(
//                new MarkerOptions()
//                        .position(centerPos)
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.h))
//                        .flat(true)
//                        .anchor(0.5f,0.5f)
//                        .alpha(0.0f)
//        );
//        marker.setTag(classNumber);
//    }

    /**
     * The purpose of this method is the polygons style after setting their
     * FillColor, StrokeColor and StrokeWidth
     * @return it returns the polygon style.
     */
    public GeoJsonPolygonStyle getPolygonStyle() {
        GeoJsonPolygonStyle geoJsonPolygonStyle = new GeoJsonPolygonStyle();
        geoJsonPolygonStyle.setFillColor(Color.argb(51, 18, 125, 159));
        geoJsonPolygonStyle.setStrokeColor(Color.argb(255, 18, 125, 159));
        geoJsonPolygonStyle.setStrokeWidth(6);
        return geoJsonPolygonStyle;
    }

    /**
     * @param buildingCode it represents which building we will be covering
     * @return Int of drawable resource's bitmap representation
     */
    public int getIcon(BuildingCode buildingCode){
        switch (buildingCode) {
            case H:
                return R.drawable.h;
            case LB:
                return R.drawable.lb;
            case CJ:
                return R.drawable.cj;
            case AD:
                return R.drawable.ad;
            case CC:
                return R.drawable.cc;
            case EV:
                return R.drawable.ev;
            case FG:
                return R.drawable.fg;
            case GM:
                return R.drawable.gm;
            case MB:
                return R.drawable.mb;
            case FB:
                return R.drawable.fb;
            case SP:
                return R.drawable.sp;
            case BB:
                return R.drawable.bb;
            case FC:
                return R.drawable.fc;
            case DO:
                return R.drawable.dome;
            case GE:
                return R.drawable.ge;
            case HA:
                return R.drawable.ha;
            case HB:
                return R.drawable.hb;
            case HC:
                return R.drawable.hc;
            case BH:
                return R.drawable.bh;
            case JR:
                return R.drawable.jr;
            case PC:
                return R.drawable.pc;
            case PS:
                return R.drawable.ps;
            case PT:
                return R.drawable.pt;
            case PY:
                return R.drawable.py;
            case QA:
                return R.drawable.qa;
            case RA:
                return R.drawable.ra;
            case RF:
                return R.drawable.rf;
            case SC:
                return R.drawable.sc;
            case SH:
                return R.drawable.sh;
            case SI:
                return R.drawable.si;
            case TA:
                return R.drawable.ta;
            case VE:
                return R.drawable.ve;
            case VL:
                return R.drawable.vl;
            default:
                return -1;
        }
    }
  
    public void setFloorPlan(GroundOverlay groundOverlay, String buildingCode, String floor, Context context) {
        groundOverlay.setImage(BitmapDescriptorFactory.fromAsset("buildings_floorplans/"+buildingCode.toLowerCase()+"_"+floor.toLowerCase()+".png"));
    }

}