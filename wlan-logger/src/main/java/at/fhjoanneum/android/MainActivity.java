package at.fhjoanneum.android;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import at.fhjoanneum.android.constants.WlanLoggerConstants;
import at.fhjoanneum.android.utils.WlanMapXmlConverter;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity {

	private static final String FILENAME = "wlanLocations.xml";
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

		loadData();
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
		Map<CompareableScanResult, Location> wlanList = collector
				.getOpenWlans();
		if (!wlanList.isEmpty()) {
			addWlansToMap(wlanList);
			Toast.makeText(getApplicationContext(),
					"Open wlans added to the map.", Toast.LENGTH_LONG).show();
			redrawMap();
		} else {
			Log.i(WlanLoggerConstants.TAG, "No openwlans in the set yet.");
			Toast.makeText(getApplicationContext(), "No open wlans found yet.",
					Toast.LENGTH_LONG).show();
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
			stringBuilder.append("SSID: ")
					.append(scanResult.getSsid()).append(",\n");
			addWlanLocationToOverlay(wlanList, scanResult);
		}
		stringBuilder.append("got added to the map.");
		Log.i(WlanLoggerConstants.TAG, stringBuilder.toString());
	}

	private void addWlanLocationToOverlay(
			Map<CompareableScanResult, Location> wlanList,
			CompareableScanResult scanResult) {
		Location location = wlanList.get(scanResult);
		GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6),
				(int) (location.getLongitude() * 1E6));
		getItemizedoverlay().addOverlay(new OverlayItem(point, scanResult
				.getSsid(), String.valueOf(scanResult
				.getLevel())));
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

	@Override
	protected void onPause() {
		super.onPause();
		saveData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		saveData();
	}

	private void loadData() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			FileInputStream file = openFileInput(FILENAME);
			if(file != null) {
				Log.i(WlanLoggerConstants.TAG, "Load Data from file: "+FILENAME);
				Document xml = db.parse(file);
				Map<CompareableScanResult, Location> savedMap = WlanMapXmlConverter.convertXMLIntoMap(xml);
				Log.i(WlanLoggerConstants.TAG,	"loaded "+savedMap.size()+" AccessPoints.");
				collector.getOpenWlans().putAll(savedMap);
			} else {
				Log.i(WlanLoggerConstants.TAG, "No data do load yet.");
			}
		} catch (FileNotFoundException e) {
			Log.w(WlanLoggerConstants.TAG, "No data do load yet.");
		} catch (Exception e) {
			Log.e(WlanLoggerConstants.TAG, "Error during Data loading.", e);
		}
	}

	private void saveData() {
		try {
			Log.i(WlanLoggerConstants.TAG, "Save Data to file: "+FILENAME);
			String xml = WlanMapXmlConverter.convertMapIntoXML(collector
					.getOpenWlans());
			Log.d(WlanLoggerConstants.TAG, "xml: "+xml);
			FileOutputStream fos;
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(xml.getBytes());
			fos.close();
		} catch (Exception e) {
			Log.e(WlanLoggerConstants.TAG, "Error during Data saving.", e);
		}
	}
	
	private void addWlanOverlay() {
		MapView mapView = (MapView) findViewById(R.id.mapview);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.wlanmarker);
		itemizedoverlay = new WlanItemizedOverlay(drawable, this);
		mapOverlays.add(itemizedoverlay);
	}

	private WlanItemizedOverlay getItemizedoverlay() {
		if (itemizedoverlay == null) {
			addWlanOverlay();
		}
		return itemizedoverlay;
	}

	
}
