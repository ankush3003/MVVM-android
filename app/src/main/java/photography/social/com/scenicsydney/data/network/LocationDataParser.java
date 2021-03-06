package photography.social.com.scenicsydney.data.network;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import photography.social.com.scenicsydney.data.database.LocationEntry;

/**
 * Parser for Location Data.
 */
public class LocationDataParser {
    private Context mContext;

    // For Singleton instantiation
    private static LocationDataParser sInstance;
    private static final Object LOCK = new Object();

    // Keys for json file parsing
    private final String MASTER_DATA_FILENAME = "sample_data.json";
    private final String LOCATION_ARRAY_KEY = "locations";
    private final String LAST_UPDATED_KEY = "updated";

    private final String LOCATION_NAME = "name";
    private final String LOCATION_LAT = "lat";
    private final String LOCATION_LONG = "lng";

    /**
     * private constructor for singleton initialization
     *
     * @param context context
     */
    private LocationDataParser(Context context) {
        this.mContext = context;
    }

    /**
     * Get the singleton for this class
     *
     * @param context context
     */
    public static LocationDataParser getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LocationDataParser(context);
            }
        }
        return sInstance;
    }

    /**
     * Parses data from json file -  assets/sample_data.json for now.
     *
     * @return LocationEntry[] result, null if error occurs
     */
    public LocationEntry[] parseFromFile() {
        try {
            return fromJson(new JSONObject(loadJSONFromAsset(mContext)));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Extracts LocationEntry[] from json.
     *
     * @param jsonObject source json object.
     * @return LocationEntry[] result
     * @throws JSONException
     */
    private LocationEntry[] fromJson(final JSONObject jsonObject) throws JSONException {
        JSONArray jsonLocationArray = jsonObject.getJSONArray(LOCATION_ARRAY_KEY);

        LocationEntry[] locationEntries = new LocationEntry[jsonLocationArray.length()];

        for (int i = 0; i < jsonLocationArray.length(); i++) {
            JSONObject jsonLocationObject = jsonLocationArray.getJSONObject(i);
            Location location = new Location("");
            location.setLatitude(Double.parseDouble(jsonLocationObject.optString(LOCATION_LAT, "0")));
            location.setLongitude(Double.parseDouble(jsonLocationObject.optString(LOCATION_LONG, "0")));
            locationEntries[i] = new LocationEntry(
                    jsonLocationObject.optString(LOCATION_NAME, ""),
                    location,
                    "");
        }
        return locationEntries;
    }

    /**
     * Loads json from assets folder
     *
     * @param context activity conext to resolve assets
     * @return String jsonstring
     */
    private String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = ((Activity)context).getAssets().open(MASTER_DATA_FILENAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}