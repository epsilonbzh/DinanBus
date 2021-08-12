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

import fr.epsilonbzh.dinanbus.core.Line;

public class MapScreen extends AppCompatActivity {
    private MapView mapView;
    private IMapController mapController;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapscreen);

        this.context = getApplicationContext();
        this.mapView = findViewById(R.id.map);
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
    }
}