package id.ac.itats.skripsi.ziez.astar.engine;

import id.ac.itats.skripsi.ziez.astar.model.Edge;
import id.ac.itats.skripsi.ziez.astar.model.Graph;
import id.ac.itats.skripsi.ziez.astar.model.Vertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Dijkstra {


	private final List<Vertex> nodes;
	private final List<Edge> edges;
	private Set<Vertex> settledNodes;
	private Set<Vertex> unSettledNodes;
	private Map<Vertex, Vertex> predecessors; // parent node
	private Map<Vertex, Double> distance;

	public Dijkstra(Graph graph) {
		// Create a copy of the array so that we can operate on this array

		this.nodes = new ArrayList<Vertex>(graph.getVertexs());
		this.edges = new ArrayList<Edge>(graph.getEdges());

	}

	public void execute(Vertex source) {
		settledNodes = new HashSet<Vertex>(); // close
		unSettledNodes = new HashSet<Vertex>(); // open
		distance = new HashMap<Vertex, Double>();
		predecessors = new HashMap<Vertex, Vertex>();
		distance.put(source, 0d);
		unSettledNodes.add(source);

		while (unSettledNodes.size() > 0) {
			// TODO getMinimum = pass

			Vertex node = getMinimum(unSettledNodes);

			// TODO settle-unsettledNodes = pass
			settledNodes.add(node);
			unSettledNodes.remove(node);

			// TODO findMinimalDistances = pass
			findMinimalDistances(node);

		}

	}

	private Vertex getMinimum(Set<Vertex> vertexes) {
		Vertex minimum = null;
		for (Vertex vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getDistance(vertex) < getDistance(minimum)) {
					minimum = vertex;
				}
			}
		}

		return minimum;
	}

	private double getDistance(Vertex destination) {
		Double d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	private void findMinimalDistances(Vertex node) {

		List<Vertex> adjacentNodes = getNeighbors(node);

		for (Vertex target : adjacentNodes) {

			if (getDistance(target) > getDistance(node)
					+ getDistance(node, target)) {
				distance.put(target,
						getDistance(node) + getDistance(node, target));

				predecessors.put(target, node);

				unSettledNodes.add(target);

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

	private List<Vertex> getNeighbors(Vertex vertex) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		for (Edge edge : edges) {

			if (edge.getFromVertex().equals(vertex)
					&& !isSettled(edge.getToVertex())) {

				neighbors.add(edge.getToVertex());

			}

		}

		return neighbors;
	}

	private boolean isSettled(Vertex vertex) {
		return settledNodes.contains(vertex);
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public LinkedList<Vertex> getPath(Vertex target) {
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		Vertex step = target;
		// Check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);

		// System.err.println("\n"+predecessors);
		return path;
	}

	public Collection<Vertex> getPredecessors() {
		return predecessors.values();
	}

}