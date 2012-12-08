package at.fhjoanneum.android;

import java.util.List;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import at.fhjoanneum.android.constants.WlanLoggerConstants;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity {

	private boolean isTrackingOn = false;
	private WlanListenerThread wlanListener;
	private OpenWlanCollector collector;
	private WlanItemizedOverlay itemizedoverlay;

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
		
		setupWlanLogging();
		
		// Log a message (only on dev platform)
		Log.i(WlanLoggerConstants.TAG, "onCreate");

		setContentView(R.layout.main);
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.wlanmarker);
		itemizedoverlay = new WlanItemizedOverlay(drawable, this);
		
		GeoPoint point = new GeoPoint(19240000,-99120000);
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");

		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
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
	
	public boolean syncMap(View v) {
		Log.i(WlanLoggerConstants.TAG, "Sync Map with Data.");
		itemizedoverlay.clear();
		Map<CompareableScanResult, Location> wlanList = collector.getOpenWlans();
		if(!wlanList.isEmpty()) {
			addWlansToMap(wlanList);
			Toast.makeText(getApplicationContext(),
					"Open wlans added to the map.", Toast.LENGTH_LONG)
					.show();
			redrawMap();
		} else {
			Log.i(WlanLoggerConstants.TAG, "No openwlans in the set yet.");
			Toast.makeText(getApplicationContext(),
					"No open wlans found yet.", Toast.LENGTH_LONG)
					.show();
		}
		return true;
	}

	private void redrawMap() {
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.postInvalidate();
	}

	private void addWlansToMap(Map<CompareableScanResult, Location> wlanList) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("This Wlans: \n");
		for (CompareableScanResult scanResult : wlanList.keySet()) {
			stringBuilder.append("SSID: ").append(scanResult.getScanResult().SSID).append(",\n");
			addWlanLocationToOverlay(wlanList, scanResult);
		}
		stringBuilder.append("got added to the map.");
		Log.i(WlanLoggerConstants.TAG, stringBuilder.toString());
	}

	private void addWlanLocationToOverlay(Map<CompareableScanResult, Location> wlanList,
			CompareableScanResult scanResult) {
		Location location = wlanList.get(scanResult);
		GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
		itemizedoverlay.addOverlay(new OverlayItem(point, scanResult.getScanResult().SSID, String.valueOf(scanResult.getScanResult().level)));
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private void setupWlanLogging() {
		collector = new OpenWlanCollector(this);
		Thread collectorThread = new Thread(collector);
		collectorThread.start();
		wlanListener = new WlanListenerThread(this, collector);
	}

	private void turnOnTracking() {
		Thread trackingThread = new Thread(wlanListener);
		trackingThread.start();
	}

	private void turnOffTracking() {
		wlanListener.setRunning(false);
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
