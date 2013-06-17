package id.ac.itats.skripsi.ziez.mapview;

import id.ac.itats.skripsi.ziez.astar.AStarRequest;
import id.ac.itats.skripsi.ziez.astar.AStarResponse;
import id.ac.itats.skripsi.ziez.astar.AStarRouter;
import id.ac.itats.skripsi.ziez.astar.PointList;
import id.ac.itats.skripsi.ziez.astar.graph.builder.GraphBuilder;
import id.ac.itats.skripsi.ziez.astar.model.Graph;
import id.ac.itats.skripsi.ziez.astar.util.StopWatch;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.ListOverlay;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import org.mapsforge.android.maps.overlay.Polyline;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.map.reader.header.FileOpenResult;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends MapActivity {
	private static final String TAG = "SKRIPSI";

	private MapView mapView;
	private static final File MAP_FILE = new File(Environment
			.getExternalStorageDirectory().getPath(), "surabaya.map");
	private static final String OSM_FILE = "file:///"
			+ Environment.getExternalStorageDirectory().getPath()
			+ "/surabaya.osm";
	
	private AStarRouter astar;

	private ListOverlay pathOverlay = new ListOverlay();

	// XXX volatile keyword ?
	private volatile boolean shortestPathRunning = false;

	private GeoPoint start;
	private GeoPoint end;

	// TODO start - end marker....
	private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent motionEvent) {

			if (shortestPathRunning) {
				logUser("Calculation still in progress");
				return false;
			}

			float x = motionEvent.getX();
			float y = motionEvent.getY();
			Projection p = mapView.getProjection();
			GeoPoint tmpPoint = p.fromPixels((int) x, (int) y);
			log(tmpPoint.toString());

			if (start != null && end == null) {
				end = tmpPoint;
				shortestPathRunning = true;
				Marker marker = createMarker(tmpPoint, R.drawable.ic_finish);
				if (marker != null) {
					pathOverlay.getOverlayItems().add(marker);
					mapView.redraw();
				}

				calcPath(start.latitude, start.longitude, end.latitude,
						end.longitude);
			}

			else {
				start = tmpPoint;
				end = null;
				pathOverlay.getOverlayItems().clear();
				Marker marker = createMarker(start, R.drawable.ic_start);
				if (marker != null) {
					pathOverlay.getOverlayItems().add(marker);
					mapView.redraw();
				}
			}
			return true;

		}

	};
	private GestureDetector gestureDetector = new GestureDetector(
			gestureListener);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		log(MAP_FILE.getPath());

		mapView = new MapView(this) {
			@Override
			public boolean onTouchEvent(MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return super.onTouchEvent(event);
			}
		};
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);

		FileOpenResult fileOpenResult = mapView.setMapFile(MAP_FILE);
		if (!fileOpenResult.isSuccess()) {
			logUser(fileOpenResult.getErrorMessage());
			finish();
		}

		setContentView(mapView);
		mapView.getOverlays().add(pathOverlay);
		
		
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		// TODO menu undo marker

		return true;
	}

	//FIXME BUILDGRAPH...
//	private void buildGraph(final String OSMFile) {
//		log("build graph ...");
//		
//		new AsyncTask<Void, Void, Graph>(){
//			
//			@Override
//			protected Graph doInBackground(Void... params) {
//			
//				Graph graph = null;
//				GraphBuilder graphBuilder;
//				try {
//					graphBuilder = new GraphBuilder(OSMFile);
//					graph = graphBuilder.getGraph();
//				} catch (Exception e) {
//				
//					e.printStackTrace();
//				}
//				
//				return graph;
//			}
//			
//			protected void onPostExecute(Graph graph) {
//				logUser(""+graph.getSize());
//			}
//		
//		}.execute();
//		
//	}
	
	// FIXME SHORTEST PATH
	public void calcPath(final double fromLat, final double fromLon,
			final double toLat, final double toLon) {
		log("calculating path ...");

		// TODO AStar request response
		new AsyncTask<Void, Void, Polyline>() {

			@Override
			protected Polyline doInBackground(Void... params) {

				Polyline polyline = createPolyline(new GeoPoint(fromLat,
						fromLon), new GeoPoint(toLat, toLon));

				return polyline;
			}

			protected void onPostExecute(Polyline result) {
				shortestPathRunning = false;

				pathOverlay.getOverlayItems().add(result);
				mapView.redraw();

				logUser("Shortest path finish...");
			}

		}.execute();
	}

	// HELPER
	private void logUser(String str) {
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	private void log(String str) {
		Log.i(TAG, str);
	}

	// MARKER-MARKER
	private Marker createMarker(GeoPoint geoPoint, int resourceIdentifier) {
		Drawable drawable = getResources().getDrawable(resourceIdentifier);
		return new Marker(geoPoint, Marker.boundCenterBottom(drawable));
	}

	private Polyline createPolyline(AStarResponse response) {
		int points = response.points().size();
		List<GeoPoint> geoPoints = new ArrayList<GeoPoint>(points);
		PointList tmp = response.points();
		for (int i = 0; i < response.points().size(); i++) {
			geoPoints.add(new GeoPoint(tmp.latitude(i), tmp.longitude(i)));
		}
		PolygonalChain polygonalChain = new PolygonalChain(geoPoints);
		Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintStroke.setStyle(Paint.Style.STROKE);
		paintStroke.setColor(Color.BLUE);
		paintStroke.setAlpha(128);
		paintStroke.setStrokeWidth(8);
		paintStroke
				.setPathEffect(new DashPathEffect(new float[] { 25, 15 }, 0));

		return new Polyline(polygonalChain, paintStroke);
	}

	private static Polyline createPolyline(GeoPoint from, GeoPoint to) {
		List<GeoPoint> geoPoints = Arrays.asList(from, to);
		PolygonalChain polygonalChain = new PolygonalChain(geoPoints);

		Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintStroke.setStyle(Paint.Style.STROKE);
		paintStroke.setColor(Color.MAGENTA);
		paintStroke.setAlpha(128);
		paintStroke.setStrokeWidth(7);
		paintStroke
				.setPathEffect(new DashPathEffect(new float[] { 25, 15 }, 0));

		return new Polyline(polygonalChain, paintStroke);
	}
}
