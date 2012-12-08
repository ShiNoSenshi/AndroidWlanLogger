package at.fhjoanneum.android.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.location.Location;
import at.fhjoanneum.android.CompareableScanResult;

public class WlanMapXmlConverter {

	public static  String convertMapIntoXML(
			Map<CompareableScanResult, Location> openWlans) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<MAP>\n");
		Iterator<CompareableScanResult> iterator = openWlans.keySet().iterator();
		while(iterator.hasNext()) {
			CompareableScanResult listedWlan = iterator.next();
			Location location = openWlans.get(listedWlan);
			stringBuilder.append("\t<ELEMENT>\n");
			stringBuilder.append("\t\t<KEY>\n");
			stringBuilder.append("\t\t\t<BSSID>").append(listedWlan.getBssid()).append("</BSSID>\n");
			stringBuilder.append("\t\t\t<SSID>").append(listedWlan.getSsid()).append("</SSID>\n");
			stringBuilder.append("\t\t\t<LEVEL>").append(listedWlan.getLevel()).append("</LEVEL>\n");
			stringBuilder.append("\t\t\t<CAPABILITIES>").append(listedWlan.getCapabilities()).append("</CAPABILITIES>\n");
			stringBuilder.append("\t\t\t<FREQUENCY>").append(listedWlan.getFrequency()).append("</FREQUENCY>\n");
			stringBuilder.append("\t\t</KEY>\n");
			stringBuilder.append("\t\t<VALUE>\n");
			stringBuilder.append("\t\t\t<ACCURACY>").append(location.getAccuracy()).append("</ACCURACY>\n");
			stringBuilder.append("\t\t\t<ALTITUDE>").append(location.getAltitude()).append("</ALTITUDE>\n");
			stringBuilder.append("\t\t\t<BEARING>").append(location.getBearing()).append("</BEARING>\n");
			stringBuilder.append("\t\t\t<LATITUDE>").append(location.getLatitude()).append("</LATITUDE>\n");
			stringBuilder.append("\t\t\t<LONGITUDE>").append(location.getLongitude()).append("</LONGITUDE>\n");
			stringBuilder.append("\t\t\t<PROVIDER>").append(location.getProvider()).append("</PROVIDER>\n");
			stringBuilder.append("\t\t\t<SPEED>").append(location.getSpeed()).append("</SPEED>\n");
			stringBuilder.append("\t\t\t<TIME>").append(location.getTime()).append("</TIME>\n");
			stringBuilder.append("\t\t</VALUE>\n");
			stringBuilder.append("\t</ELEMENT>\n");
		}
		stringBuilder.append("</MAP>\n");
		return stringBuilder.toString();
	}

	public static Map<CompareableScanResult, Location> convertXMLIntoMap(
			Document xml) {
		Map<CompareableScanResult, Location> map = new TreeMap<CompareableScanResult, Location>();

		NodeList elements = xml.getElementsByTagName("ELEMENT");
		if(elements != null) {
			for(int i = 0 ; i < elements.getLength();i++) {
				Element element = (Element)elements.item(i);
				CompareableScanResult key = getKey(element.getElementsByTagName("KEY").item(0));
				Location value = getValue(element.getElementsByTagName("VALUE").item(0));
				map.put(key, value);
				
			}
		}
		
		return map;
	}

	private static Location getValue(Node node) {
		String provider = getTextValue(node, "PROVIDER");
		float accuracy = getFloatValue(node, "ACCURACY");
		double altitude = getDoubleValue(node, "ALTITUDE");
		float bearing = getFloatValue(node, "BEARING");
		double latitude = getDoubleValue(node, "LATITUDE");
		double longitude = getDoubleValue(node, "LONGITUDE");
		float speed = getFloatValue(node, "SPEED");
		long time = getLongValue(node, "TIME");
		Location location = new Location(provider);
		location.setAccuracy(accuracy);
		location.setAltitude(altitude);
		location.setBearing(bearing);
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setSpeed(speed);
		location.setTime(time);
		return null;
	}



	private static CompareableScanResult getKey(Node node) {
		String bssid = getTextValue(node, "BSSID");
		String capabilities = getTextValue(node, "CAPABILITIES");
		int frequency = getIntValue(node, "FREQUENCY");
		int level = getIntValue(node, "LEVEL");
		String ssid = getTextValue(node, "SSID");
		CompareableScanResult compareableScanResult = new CompareableScanResult(bssid, capabilities, frequency, level, ssid);
		return compareableScanResult;
	}
	
	private static String getTextValue(Node node, String tagName) {
		return getNode(tagName, node.getChildNodes()).getNodeValue();
	}
	
	private static float getFloatValue(Node value, String string) {
		return Float.valueOf(getTextValue(value, string));
	}
	
	private static double getDoubleValue(Node value, String string) {
		return Double.valueOf(getTextValue(value, string));
	}
	
	private static long getLongValue(Node value, String string) {
		return Long.valueOf(getTextValue(value, string));
	}
	
	private static int getIntValue(Node value, String string) {
		return Integer.valueOf(getTextValue(value, string));
	}
	
	private static Node getNode(String tagName, NodeList nodes) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            return node;
	        }
	    }
	 
	    return null;
	}
}
