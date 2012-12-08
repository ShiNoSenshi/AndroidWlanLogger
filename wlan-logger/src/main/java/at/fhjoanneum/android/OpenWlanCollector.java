package at.fhjoanneum.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.util.Log;
import at.fhjoanneum.android.constants.WlanLoggerConstants;

public class OpenWlanCollector implements Runnable {
	private List<ScanResult> newOpenWlans = new ArrayList<ScanResult>();
	private Activity activity;
	private Map<ScanResult, Location> openWlans = new TreeMap<ScanResult, Location>();

	public OpenWlanCollector(Activity activity) {
		this.activity = activity;
	}

	public void run() {
		while (true) {
			if (newOpenWlansAppeared()) {
				addThemToTheMap();
			} else {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					Log.e(WlanLoggerConstants.TAG, "Error while sleeping.", e);
				}
			}
		}
	}
	
	public List<ScanResult> getNewOpenWlans() {
		return newOpenWlans;
	}

	public Map<ScanResult, Location> getOpenWlans() {
		return openWlans;
	}

	private void addThemToTheMap() {
		LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		Location lastKnownLocation = findLastKnownLocation(lm);
		for(ScanResult newOpenWlan : newOpenWlans) {
			if (!openWlans.containsKey(newOpenWlan)) {
				openWlans.put(newOpenWlan, lastKnownLocation);
			}
		}
		newOpenWlans.clear();
	}

	private Location findLastKnownLocation(LocationManager lm) {
		Location lastKnownLocation = null;
		for (String provider :lm.getAllProviders()) {
			Location location = lm.getLastKnownLocation(provider);
			if(lastKnownLocation == null || lastKnownLocation.getTime()<location.getTime()) {
				lastKnownLocation = location;
			}
		}
		return lastKnownLocation;
	}

	private boolean newOpenWlansAppeared() {
		return !newOpenWlans.isEmpty();
	}

}
