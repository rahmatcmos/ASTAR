package id.ac.itats.skripsi.ziez.astar.engine;

import id.ac.itats.skripsi.ziez.astar.model.Edge;
import id.ac.itats.skripsi.ziez.astar.model.Graph;
import id.ac.itats.skripsi.ziez.astar.model.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AStar {

	private final List<Edge> edges;

	private Set<Vertex> openList;
	private Set<Vertex> closedList;
	private Vertex goal;

	private AStarHeuristic heuristic;

	private Map<Vertex, Vertex> predecessors; // parent vertex
	private Map<Vertex, Double> g_score;

	public AStar(Graph graph, AStarHeuristic heuristic) {
		new ArrayList<Vertex>(graph.getVertexs());
		this.edges = new ArrayList<Edge>(graph.getEdges());
		this.heuristic = heuristic;
	}

	public void execute(Vertex source, Vertex goal) {
		this.goal = goal;

		closedList = new HashSet<Vertex>(); // close
		openList = new HashSet<Vertex>(); // open

		g_score = new HashMap<Vertex, Double>();

		predecessors = new HashMap<Vertex, Vertex>();

		g_score.put(source, 0d);
		// TODO hitung heuristic

		openList.add(source);

		while (openList.size() != 0) {
			Vertex current = getMinimumFn(openList); // by f(n)=g(n)+h(n) values

			closedList.add(current);
			openList.remove(current);

			evaluasiSuksessor(current, goal);
		}
	}

	private Vertex getMinimumFn(Set<Vertex> openList) {
		// the node in openset having the lowest f_score[] value
		Vertex minimum = null;
		for (Vertex current : openList) {
			if (minimum == null) {
				minimum = current;
				// minimum != null
			} else {
				// evaluasi fn
				if (getShortestDistance(current) < getShortestDistance(minimum)) {
					minimum = current;
				}
			}
		}
		return minimum;
	}

	private double getShortestDistance(Vertex target) {
		Double g = g_score.get(target);
		Double h = heuristic.getNilaiHeuristic(target, goal);

		if (g == null) {
			return Integer.MAX_VALUE;
		} else {
			return g + h; // f = g + h
		}
	}

	// sorting
	private void evaluasiSuksessor(Vertex current, Vertex goal) {
		List<Vertex> suksesors = getNeighbors(current); // bangkitkan semua
														// suksesor S

		for (Vertex target : suksesors) {

			if (getShortestDistance(target) > getShortestDistance(current)
					+ getDistance(current, target)) {

				g_score.put(
						target,
						getShortestDistance(current)
								+ getDistance(current, target));

				predecessors.put(target, current);

				openList.add(target); // OPEN = [A, B, C, D, E] CLOSED = [S]
			}
		}

	}

	private double getDistance(Vertex node, Vertex target) {
		for (Edge edge : edges) {
			if (edge.getFromVertex().equals(node)
					&& edge.getToVertex().equals(target)) {
				return edge.getJarak();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		for (Edge edge : edges) {
			if (edge.getFromVertex().equals(node)
					&& !isClosed(edge.getToVertex())) {
				neighbors.add(edge.getToVertex());
			}
		}
		return neighbors;
	}

	private boolean isClosed(Vertex vertex) {
		return closedList.contains(vertex);
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public LinkedList<Vertex> getPath() {
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		Vertex step = goal;
		// Check if a path exists
		if (predecessors.get(step) == null) {
			System.out.println("no path");
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}

}
