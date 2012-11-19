package at.fhjoanneum.android;

import android.os.Bundle;
import android.util.Log;
import at.fhjoanneum.android.constants.WlanLoggerConstants;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class HelloAndroidActivity extends MapActivity {

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Log a message (only on dev platform)
        Log.i(WlanLoggerConstants.TAG, "onCreate");

        setContentView(R.layout.main);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}

