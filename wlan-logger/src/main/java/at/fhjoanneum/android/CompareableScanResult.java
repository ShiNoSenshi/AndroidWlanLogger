package at.fhjoanneum.android;

import android.net.wifi.ScanResult;

public class CompareableScanResult implements Comparable<CompareableScanResult> {
	private String bssid;
	private String capabilities;
	private int frequency;
	private int level;
	private String ssid;

	public CompareableScanResult(ScanResult scanResult) {
		bssid = scanResult.BSSID;
		capabilities = scanResult.capabilities;
		frequency = scanResult.frequency;
		level = scanResult.level;
		ssid = scanResult.SSID;
		
	}
	
	public CompareableScanResult(String bssid, String capabilities,
			int frequency, int level, String ssid) {
		super();
		this.bssid = bssid;
		this.capabilities = capabilities;
		this.frequency = frequency;
		this.level = level;
		this.ssid = ssid;
	}



	public int compareTo(CompareableScanResult other) {
		return this.ssid.compareTo(other.ssid);
	}

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public String getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(String capabilities) {
		this.capabilities = capabilities;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append("BSSID: ").append(bssid).append("\n")
				.append("Capabilities: ").append(capabilities).append("\n")
				.append("Frequency: ").append(frequency).append("\n")
				.append("Level: ").append(level).append("\n")
				.append("SSID: ").append(ssid).toString();
	}

	
	
	
}
