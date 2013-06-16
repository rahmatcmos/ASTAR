package id.ac.itats.skripsi.ziez.geocoding.engine;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Geocode {

	private static StringBuilder get(String q) {
		StringBuilder builder = new StringBuilder();
		try {
			URL url = new URL(q);
			URLConnection urlc = url.openConnection();

			BufferedInputStream buffer = new BufferedInputStream(
					urlc.getInputStream());

			int byteRead;
			while ((byteRead = buffer.read()) != -1) {
				builder.append((char) byteRead);
			}

			buffer.close();

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return builder;
	}

	public static String[] request() {
		StringBuilder r = get("http://icanhazip.com");
		String getip = (r.toString());
		StringBuilder ipUser = get("http://api.ipinfodb.com/v3/ip-city/?key=e4e58c6483128846d238bd69bbe6c608dd29749af8d5a4d4086606f518b9e96b&ip="
				+ getip);

		// System.out.println(ipUser.toString());
		String str = ipUser.toString();
		String[] temp = str.split(";");

		String lati = temp[8];
		String longi = temp[9];
		String contri = temp[4];
		String citi = temp[6];

		String response[] = { lati, longi };
		// System.out.println(response[0]);
		// System.out.println(response[1]);

		return response;
	}
}
