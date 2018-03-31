package aditya.nayanda.shippingmanager.activities.helper;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nayanda on 27/03/18.
 */

public class MapHelper {

    private static final String BASE_API_URL = "https://maps.googleapis.com/maps/api/directions/";
    private static final String BASE_MAP_URL = "https://www.google.com/maps/dir/?api=1";
    private static final String BASE_MAP_URI = "google.navigation:q=";
    private static final String OPTIMIZED_PARAMETER = "&waypoints=optimize:true|";

    public static Uri createGoogleMapRouteIntentUri(LatLng destinations) {

        StringBuilder stringBuilder = new StringBuilder(BASE_MAP_URI)
                .append((float) destinations.latitude)
                .append(",")
                .append((float) destinations.longitude);
        return Uri.parse(stringBuilder.toString());
    }

    public static Uri createGoogleMapRouteIntentUri(LatLng origin, LatLng lastDestination, List<LatLng> destinations) {
        if (destinations.size() == 0 || destinations.size() > 9)
            throw new IllegalArgumentException("google restricted the max destination from 1 to 10");
        StringBuilder stringBuilder = new StringBuilder(BASE_MAP_URL)
                .append("&origin=")
                .append((float) origin.latitude)
                .append(",")
                .append((float) origin.longitude)
                .append("&destination=")
                .append((float) lastDestination.latitude)
                .append(",")
                .append((float) lastDestination.longitude)
                .append("&waypoints=");
        for (LatLng destination : destinations) {
            stringBuilder.append((float) destination.latitude)
                    .append(",")
                    .append((float) destination.longitude)
                    .append("|");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1)
                .append("&travelmode=driving");
        return Uri.parse(stringBuilder.toString());
    }

    private static URL createUrlForRoute(LatLng origin, LatLng lastDestination, List<LatLng> destinations, RouteOrder order, String apiKey) throws MalformedURLException {
        if (destinations.size() == 0 || destinations.size() > 9)
            throw new IllegalArgumentException("google restricted the max destination from 1 to 10");

        StringBuilder stringBuilder = new StringBuilder(BASE_API_URL)
                .append("json?origin=")
                .append((float) origin.latitude)
                .append(",")
                .append((float) origin.longitude)
                .append("&destination=");

        switch (order) {
            case OPTIMIZE:
                stringBuilder.append((float) lastDestination.latitude)
                        .append(",")
                        .append((float) lastDestination.longitude)
                        .append(OPTIMIZED_PARAMETER);
                break;
            default:
                break;
        }

        for (LatLng destination : destinations) {
            stringBuilder.append((float) destination.latitude)
                    .append(",")
                    .append((float) destination.longitude)
                    .append("|");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1)
                .append("&key=")
                .append(apiKey);
        String url = stringBuilder.toString();
        return new URL(url);
    }

    public static JSONObject requestRoute(LatLng origin, LatLng lastDestination, List<LatLng> destinations, RouteOrder order, String apiKey)
            throws IOException, JSONException {
        String data = null;
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = createUrlForRoute(origin, lastDestination, destinations, order, apiKey);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            data = stringBuilder.toString();
            Log.d("DOWNLOADED", data);
            bufferedReader.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            if (inputStream != null)
                inputStream.close();
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        if (data == null) return null;
        else return new JSONObject(data);
    }

    public static List<LatLng> parseJsonRoute(JSONObject routeResponse) {
        List<LatLng> routes = new ArrayList<>();

        try {
            JSONArray jsonRoutes = routeResponse.getJSONArray("routes");

            for (int i = 0; i < jsonRoutes.length(); i++) {
                JSONArray jsonLegs = ((JSONObject) jsonRoutes.get(i)).getJSONArray("legs");

                for (int j = 0; j < jsonLegs.length(); j++) {
                    JSONArray jsonSteps = ((JSONObject) jsonLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k < jsonSteps.length(); k++) {
                        String polyline;
                        polyline = (String) ((JSONObject) ((JSONObject) jsonSteps.get(k))
                                .get("polyline"))
                                .get("points");

                        List<LatLng> list = decodePolyline(polyline);
                        routes.addAll(list);
                    }
                }
            }

        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
        return routes;
    }

    public static List<Integer> getWayPointsOrder(JSONObject routeResponse) {
        List<Integer> order = new ArrayList<>();
        try {
            JSONArray jsonRoutes = routeResponse.getJSONArray("routes");

            for (int i = 0; i < jsonRoutes.length(); i++) {
                JSONArray jsonSteps = ((JSONObject) jsonRoutes.get(i)).getJSONArray("waypoint_order");

                for (int j = 0; j < jsonSteps.length(); j++) {
                    String num = jsonSteps.get(j).toString();
                    int parsedInt = Integer.parseInt(num);
                    order.add(parsedInt);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
        return order;
    }

    /**
     * Method to decode polyline points
     * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private static List<LatLng> decodePolyline(String encoded) {

        List<LatLng> polyLine = new ArrayList<>();
        int index = 0, len = encoded.length();
        int latitude = 0, longitude = 0;

        while (index < len) {
            int encodedChar, shift = 0, result = 0;
            do {
                encodedChar = encoded.charAt(index++) - 63;
                result |= (encodedChar & 0x1f) << shift;
                shift += 5;
            } while (encodedChar >= 0x20);
            int dLatitude = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            latitude += dLatitude;

            shift = 0;
            result = 0;
            do {
                encodedChar = encoded.charAt(index++) - 63;
                result |= (encodedChar & 0x1f) << shift;
                shift += 5;
            } while (encodedChar >= 0x20);
            int dLongitude = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            longitude += dLongitude;

            LatLng point = new LatLng((((double) latitude / 1E5)),
                    (((double) longitude / 1E5)));
            polyLine.add(point);
        }
        return polyLine;
    }


    public enum RouteOrder {
        OPTIMIZE, ORDERED
    }

}
