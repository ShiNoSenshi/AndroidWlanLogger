package at.fhjoanneum.android;

import android.net.wifi.ScanResult;

public class CompareableScanResult implements Comparable<CompareableScanResult> {
	private ScanResult scanResult;

	public CompareableScanResult(ScanResult scanResult) {
		this.scanResult = scanResult;
	}

	public int compareTo(CompareableScanResult other) {
		return this.scanResult.SSID.compareTo(other.getScanResult().SSID);
	}

	public ScanResult getScanResult() {
		return scanResult;
	}

}
