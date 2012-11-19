package at.fhjoanneum.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import at.fhjoanneum.android.constants.WlanLoggerConstants;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class HelloAndroidActivity extends MapActivity {

	private boolean isTrackingOn = false;

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 *            is null.</b>
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

	public boolean switchState(View v) {
		if (isTrackingOn) {
			Log.i(WlanLoggerConstants.TAG, "Switch Tracking To OFF.");
			turnOffTracking();
			changeOnOffButton(v, false);
		} else {
			Log.i(WlanLoggerConstants.TAG, "Switch Tracking To ON.");
			turnOnTracking();
			changeOnOffButton(v, true);
		}
		isTrackingOn = !isTrackingOn;
		return true;
	}

	private void turnOnTracking() {
		// TODO Auto-generated method stub
	}

	private void turnOffTracking() {
		// TODO Auto-generated method stub
	}

	private void changeOnOffButton(View v, boolean switchToOff) {
		Button button = (Button) v.findViewById(R.id.onOffButton);
		String state;
		if (switchToOff) {
			state = getString(R.string.off);
		} else {
			state = getString(R.string.on);
		}
		button.setText(state);
	}

}
