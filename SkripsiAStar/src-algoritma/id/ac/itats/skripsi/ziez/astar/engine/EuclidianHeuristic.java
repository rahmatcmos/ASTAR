package id.ac.itats.skripsi.ziez.astar.engine;

import id.ac.itats.skripsi.ziez.astar.model.Vertex;
import id.ac.itats.skripsi.ziez.util.parsing.osm.OSMNode;

public class EuclidianHeuristic implements AStarHeuristic {

	@Override
	public double getNilaiHeuristic(Vertex source, Vertex goal) {
		OSMNode start = source.getNode();
		OSMNode finish = source.getNode();

		double x = start.getPoint().getX() - finish.getPoint().getX();
		double y = start.getPoint().getY() - finish.getPoint().getY();

		return Math.sqrt(x * x + y * y);
	}

}
