package id.ac.itats.skripsi.ziez.astar.test;

import static org.junit.Assert.assertNotNull;
import id.ac.itats.skripsi.ziez.astar.graph.builder.GraphBuilder;
import id.ac.itats.skripsi.ziez.astar.model.Graph;
import id.ac.itats.skripsi.ziez.astar.model.Vertex;
import id.ac.itats.skripsi.ziez.geocoding.engine.Geocode;
import id.ac.itats.skripsi.ziez.util.parsing.LatLongUtil;
import id.ac.itats.skripsi.ziez.util.parsing.osm.OSMNode;

import java.util.Collection;

import org.junit.Test;


public class TestGraph {

	@Test
	public void testGraphBuilder() throws Exception {
		GraphBuilder builder = new GraphBuilder("data/surabaya.osm");
		Graph graph = builder.getGraph();
		assertNotNull("graph loaded !", graph);

		String[] location = Geocode.request();
		System.out.println(location[0] + ", " + location[1]);

		OSMNode matchNode = doMatching(graph.getVertexs(),
				Double.parseDouble("-7.2517240"),
				Double.parseDouble("112.6501230"));

		/*
		 * MATCH 1
		 */

		// int i = 0;
		// OSMNode hasilAkjhir = null;
		// for (Vertex vertex : graph.getVertexs()) {
		// OSMNode node = vertex.getNode();
		// double jarak = 0;
		// if (i != 0) {
		// if (jarak < LatLongUtil.distance(lat, lon,
		// Double.parseDouble(node.lat),
		// Double.parseDouble(node.lon))) {
		// hasilAkjhir = node;
		// jarak = LatLongUtil.distance(lat, lon,
		// Double.parseDouble(node.lat),
		// Double.parseDouble(node.lon));
		// System.out.println("x" + jarak);
		// }
		// } else {
		// jarak = LatLongUtil.distance(lat, lon,
		// Double.parseDouble(node.lat),
		// Double.parseDouble(node.lon));
		// System.out.println("a" + jarak);
		// }
		// i++;
		// }
		//
		// System.out.println(hasilAkjhir.id);

	}

	/*
	 * MATCH 2
	 */

	private OSMNode doMatching(Collection<Vertex> vertexs, double gpsLat,
			double gpsLon) {
		OSMNode minNode = null;

		double minJarak = Double.MAX_VALUE;
		for (Vertex vertex : vertexs) {

			OSMNode node2 = vertex.getNode();

			double jarak = LatLongUtil.distance(gpsLat, gpsLon,
					Double.parseDouble(node2.lat),
					Double.parseDouble(node2.lon));

			// System.out.println(jarak);

			if (jarak < minJarak) {
				minJarak = jarak;
				minNode = node2;
			}

		}

		// TEST
		System.out.println("======================================");

		System.out.println(" minJarak : " + minJarak);

		System.out.println(" minNode id : " + minNode.id);

		System.out.println(" minNode lat : " + minNode.lat);
		System.out.println(" minNode lon : " + minNode.lon);

		System.out.println(" minNode jarak : "
				+ LatLongUtil.distance(gpsLat, gpsLon,
						Double.parseDouble(minNode.lat),
						Double.parseDouble(minNode.lon)));

		return minNode;
	}

}
