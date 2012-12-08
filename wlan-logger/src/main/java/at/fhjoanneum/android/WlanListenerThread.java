package at.fhjoanneum.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;
import at.fhjoanneum.android.constants.WlanLoggerConstants;

public class WlanListenerThread implements Runnable {

	private boolean running;
	private Activity activity;
	private List<ScanResult> openWlans;
	private OpenWlanCollector collector;

	public WlanListenerThread(Activity activity, OpenWlanCollector collector) {
		this.activity = activity;
		this.collector = collector;
	}

	public void run() {
		Log.i(WlanLoggerConstants.TAG, "WlanListener started.");
		running = true;
		while (running) {
			try {
				lookForFreeWlans();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				Log.e(WlanLoggerConstants.TAG, "Error while sleeping.", e);
				running = false;
			}
		}
		Log.i(WlanLoggerConstants.TAG, "WlanListener stopped.");
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	private void lookForFreeWlans() throws InterruptedException {
		WifiManager wifi = (WifiManager) activity
				.getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled() == false) {
			Toast.makeText(activity.getApplicationContext(),
					"wifi is disabled..making it enabled", Toast.LENGTH_LONG)
					.show();
			wifi.setWifiEnabled(true);
		}
		Log.i(WlanLoggerConstants.TAG, "Start Scanning.... ");
		wifi.startScan();
		Thread.sleep(10000);
		List<ScanResult> scanResults = wifi.getScanResults();
		Log.i(WlanLoggerConstants.TAG, "Found " + scanResults.size()
				+ " AccessPoints.");
		this.openWlans = findAllOpenWlans(scanResults);
		notifyObserver();
	}

	private List<ScanResult> findAllOpenWlans(List<ScanResult> scanResults) {
		List<ScanResult> openWlans = new ArrayList<ScanResult>();
		for (ScanResult scanResult : scanResults) {
			Log.i(WlanLoggerConstants.TAG, "Found AccessPoint, SSID: "
					+ scanResult.SSID + ", capabilities: "
					+ scanResult.capabilities);
//			if ("[ESS]".equals(scanResult.capabilities)) {
//				Log.i(WlanLoggerConstants.TAG, "Found open AccessPoint, SSID: "
//						+ scanResult.SSID + ", capabilities: "
//						+ scanResult.capabilities);
				openWlans.add(scanResult);
//			}
		}
		return openWlans;
	}

	private void notifyObserver() {
		collector.getNewOpenWlans().addAll(openWlans);
	}
}
