package at.fhjoanneum.android.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.Assert.*;

import android.location.Location;
import at.fhjoanneum.android.CompareableScanResult;

public class WlanMapXmlConverterUTest {

	@Test
	public void testEmptyMapToXMLAndBack() {
		WlanMapXmlConverter converter = new WlanMapXmlConverter();
		Map<CompareableScanResult, Location> sourceMap = new TreeMap<CompareableScanResult, Location>();
		String xml = converter.convertMapIntoXML(sourceMap);
		System.out.println("Converted xml: "+xml);
		Map<CompareableScanResult, Location> resultMap = converter.convertXMLIntoMap();
		assertEquals(sourceMap, resultMap);
	}
	
	@Test
	public void testMapWithTwoWlansToXMLAndBack() {
		WlanMapXmlConverter converter = new WlanMapXmlConverter();
		Map<CompareableScanResult, Location> sourceMap = new TreeMap<CompareableScanResult, Location>();
		CompareableScanResult wlan = loadWlan("wlan1");
		CompareableScanResult wlan = loadWlan("wlan2");
		Location location1 = loadLocation();
		Location location2 = loadLocation();
		sourceMap.put(wlan1, location1);
		sourceMap.put(wlan2, location2);
		String xml = converter.convertMapIntoXML(sourceMap);
		System.out.println("Converted xml: "+xml);
		Map<CompareableScanResult, Location> resultMap = converter.convertXMLIntoMap();
		assertEquals(sourceMap, resultMap);
	}
	
	private CompareableScanResult loadWlan(String ssid) {
		return new CompareableScanResult("BSSID", "[ESS]", 1, 1, ssid);
	}
	
	private Location loadLocation() {
		Location location = new Location("PROVIDER");
		location.setAccuracy(Float.getValueOf("1.0"));
		location.setAltitude(Double.getValueOf("1.0"));
		location.setBearing(Float.getValueOf("1.0"));
		location.setLatitude(Double.getValueOf("1.0"));
		location.setLongitude(Doulbe.getValueOf("1.0"));
		location.setSpeed(Float.getValueOf("1.0"));
		location.setTime(Long.getValueOf("1.0"));
		return location;
	}
}
