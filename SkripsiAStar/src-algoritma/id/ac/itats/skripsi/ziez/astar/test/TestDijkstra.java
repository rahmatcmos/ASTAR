package id.ac.itats.skripsi.ziez.astar.test;

import id.ac.itats.skripsi.ziez.astar.engine.Dijkstra;
import id.ac.itats.skripsi.ziez.astar.graph.builder.GraphBuilder;
import id.ac.itats.skripsi.ziez.astar.model.Graph;
import id.ac.itats.skripsi.ziez.astar.model.Key;
import id.ac.itats.skripsi.ziez.astar.model.Vertex;
import id.ac.itats.skripsi.ziez.astar.util.MapMatchingUtil;
import id.ac.itats.skripsi.ziez.util.parsing.osm.OSMNode;

import java.util.LinkedList;

import org.junit.Test;


public class TestDijkstra {

	@Test
	public void testAlgoritmaDijkstra() throws Exception {
		GraphBuilder builder = new GraphBuilder("data/surabaya.osm");

		Graph graph = builder.getGraph();

		Dijkstra dijkstra = new Dijkstra(graph);

		// String location[] = Geocode.request();
		// System.out.println(location[0] + ", " + location[1]);

		// buat ngetes, biar < geocoding = gag adoh2 ;
		String unMatchLatLon[] = { "-7.329749", "112.804808" }; // LatLon ini
																// tidak ada di
																// graph, cek
																// google maps
		OSMNode startNode = MapMatchingUtil.doMatching(graph.getVertexs(),
				unMatchLatLon[0], unMatchLatLon[1]);

		// Vertex src = graph.fromVertex(new Key("-7.33035","112.804049"));
		Vertex src = graph.fromVertex(new Key(startNode.lat, startNode.lon));
		System.out.println("Src vertex = id : " + src + " latlon : "
				+ src.getNode().lat + " , " + src.getNode().lon);

		dijkstra.execute(src);

		Vertex dst = graph.toVertex(new Key("-7.3299528", "112.8038842"));
		System.out.println("Dst vertex = id : " + dst + " latlon : "
				+ dst.getNode().lat + " , " + dst.getNode().lon);

		LinkedList<Vertex> path = dijkstra.getPath(dst);
		System.out.println(dijkstra.getPredecessors());

		// fixed path not found
		if (path == null) {
			OSMNode goalNode = MapMatchingUtil.doMatching(
					dijkstra.getPredecessors(), dst.getNode().lat,
					dst.getNode().lon);
			dst = graph.toVertex(new Key(goalNode.lat, goalNode.lon));
			path = dijkstra.getPath(dst);
		}

		System.out.print("path : ");
		for (Vertex vertex : path) {
			System.out.print(vertex + " --> ");
		}
		System.out.println();

	}

}
