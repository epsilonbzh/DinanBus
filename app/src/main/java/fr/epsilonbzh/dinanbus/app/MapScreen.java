package fr.epsilonbzh.dinanbus.app;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

import fr.epsilonbzh.dinanbus.core.Bus;
import fr.epsilonbzh.dinanbus.core.Line;
import fr.epsilonbzh.dinanbus.core.Stop;

public class MapScreen extends AppCompatActivity {
    private MapView mapView;
    private IMapController mapController;
    private ArrayList<Marker> markerList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapscreen);

        this.context = getApplicationContext();
        this.mapView = findViewById(R.id.map);
        this.markerList = new ArrayList<>();
        this.mapController = mapView.getController();

        config();
        setMarkers();
    }

    private void config() {
        //replace in-build zoom buttons by double touch zoom
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mapView.setMultiTouchControls(true);

        //set source
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        //config Zoom presets
        mapController.setCenter(new GeoPoint(48.4541938, -2.0485565));
        mapController.setZoom(20.0);
        mapView.setMinZoomLevel(15.0);
        mapView.setMaxZoomLevel(23.0);

        //define map borders
        BoundingBox boundingBox = new BoundingBox( 48.5000000,-2.0000000,48.4000000,-2.1000000);
        mapView.setScrollableAreaLimitDouble(boundingBox);
    }

    private void setMarkers() {
        Line line1 = new Line(context,"Line-1.xml");
        Line line2 = new Line(context,"Line-2.xml");
        Line line3 = new Line(context,"Line-3.xml");
        Line line4 = new Line(context,"Line-4.xml");

        ArrayList<Line> line_list = new ArrayList<>();
        line_list.add(line1);
        line_list.add(line2);
        line_list.add(line3);
        line_list.add(line4);
        Bus bus = new Bus(line_list);

        bus.fixLinesWithSameCoordinates();

        line1 = bus.getLine(0);
        line2 = bus.getLine(1);
        line3 = bus.getLine(2);
        line4 = bus.getLine(3);

        buildMarker(line1);
        buildMarker(line2);
        buildMarker(line3);
        buildMarker(line4);

        mapView.getOverlays().addAll(markerList);

    }

    private void buildMarker(Line line) {
        setupMarker(line,line.getStops(),false);
        if(line.hasReverse()) {
            setupMarker(line, line.getStopsReverse(), true);
        }
    }

    private void setupMarker(Line line,ArrayList<Stop> stop_list, boolean reverse) {
        for(Stop s : stop_list) {
            Marker m = new Marker(mapView);
            m.setPosition(new GeoPoint(s.getLon(), s.getLat()));
            m.setTitle(s.getName());
            m.setSnippet(getResources().getString(R.string.remaining_time) + " : " + s.printNext());
            if(line.hasReverse()) {
                if(reverse) {
                    m.setSubDescription(getResources().getString(R.string.line) + " " + line.getLineNumber() + " - " + getResources().getString(R.string.to) + " " + line.getLastStopReverse().getName());
                }else {
                    m.setSubDescription(getResources().getString(R.string.line) + " " + line.getLineNumber() + " - " + getResources().getString(R.string.to) + " " + line.getLastStop().getName());
                }
            }
            switch(line.getLineNumber()) {
                case 1:
                    m.setImage(getResources().getDrawable(R.drawable.marker_image_1));
                    m.setIcon(getResources().getDrawable(R.drawable.marker_icon_1));
                    break;
                case 2:
                    m.setImage(getResources().getDrawable(R.drawable.marker_image_2));
                    m.setIcon(getResources().getDrawable(R.drawable.marker_icon_2));
                    break;
                case 3:
                    m.setImage(getResources().getDrawable(R.drawable.marker_image_3));
                    m.setIcon(getResources().getDrawable(R.drawable.marker_icon_3));
                    break;
                case 4:
                    m.setImage(getResources().getDrawable(R.drawable.marker_image_4));
                    m.setIcon(getResources().getDrawable(R.drawable.marker_icon_4));
                    break;
                default:
                    System.err.print("Unknow line number");
            }
            this.markerList.add(m);
        }
    }
}