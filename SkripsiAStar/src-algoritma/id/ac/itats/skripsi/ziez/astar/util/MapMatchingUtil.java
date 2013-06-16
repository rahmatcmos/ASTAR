package id.ac.itats.skripsi.ziez.astar.util;

import id.ac.itats.skripsi.ziez.astar.model.Vertex;
import id.ac.itats.skripsi.ziez.util.parsing.LatLongUtil;
import id.ac.itats.skripsi.ziez.util.parsing.osm.OSMNode;

import java.util.Collection;

public class MapMatchingUtil {

	public static OSMNode doMatching(Collection<Vertex> vertexs, String gpsLat,
			String gpsLon) {
		OSMNode minNode = null;

		double minJarak = Double.MAX_VALUE;
		for (Vertex vertex : vertexs) {

			OSMNode node2 = vertex.getNode();

			double jarak = LatLongUtil.distance(Double.parseDouble(gpsLat),
					Double.parseDouble(gpsLon), Double.parseDouble(node2.lat),
					Double.parseDouble(node2.lon));

			// System.out.println(jarak);

			if (jarak < minJarak) {
				minJarak = jarak;
				minNode = node2;
			}

		}

		// TEST
		// System.out.println("======================================");
		//
		// System.out.println(" minJarak : " + minJarak);
		//
		// System.out.println(" minNode id : " + minNode.id);
		//
		// System.out.println(" minNode jarak : "
		// + LatLongUtil.distance(Double.parseDouble(gpsLat),
		// Double.parseDouble(gpsLon),
		// Double.parseDouble(minNode.lat),
		// Double.parseDouble(minNode.lon)));

		return minNode;
	}

}
