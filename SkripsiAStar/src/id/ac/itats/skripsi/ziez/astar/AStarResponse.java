package id.ac.itats.skripsi.ziez.astar;

import java.util.ArrayList;
import java.util.List;

public class AStarResponse {

	private PointList list;
	private double distance;
	private long time;
	private String debugInfo = "";
	private List<Throwable> errors = new ArrayList<Throwable>(4);

	public boolean hasError() {
		// TODO Auto-generated method stub
		return !errors.isEmpty();
	}

	public double distance() {
		// TODO Auto-generated method stub
		return distance;
	}

	public PointList points() {
		// TODO Auto-generated method stub
		return list;
	}

	public String debugInfo() {
		// TODO Auto-generated method stub
		return debugInfo;
	}

	public long time() {
		// TODO Auto-generated method stub
		return time;
	}

	public String errors() {
		// TODO Auto-generated method stub
		return null;
	}

}
