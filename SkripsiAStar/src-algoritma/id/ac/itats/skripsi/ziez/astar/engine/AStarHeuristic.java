package id.ac.itats.skripsi.ziez.astar.engine;

import id.ac.itats.skripsi.ziez.astar.model.Vertex;

public interface AStarHeuristic {

	public double getNilaiHeuristic(Vertex source, Vertex goal);
}
