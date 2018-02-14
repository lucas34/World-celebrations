package packi.day.store.feature;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.MonthDay;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import packi.day.store.DayStore;
import packi.day.store.InternationalDay;

/**
 * Created by lucas34990 on 12/2/18.
 */

public class HashMapDayStore implements DayStore {

    private Map<MonthDay, InternationalDay> store = new HashMap<>(365);

    public HashMapDayStore(Context context) {
        fillData(context.getResources().openRawResource(packi.day.store.feature.R.raw.celebration));
    }

    @Nullable
    @Override
    public InternationalDay get(MonthDay date) {
        return store.get(date);
    }

    @Override
    public Set<Integer> count(String criteria) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public List<InternationalDay> find(String criteria) {
        List<InternationalDay> result = new ArrayList<>(store.size());
        for(InternationalDay celebration : store.values()) {
            if (celebration.name.contains(criteria)) {
                result.add(celebration);
            }
        }
        return result;
    }

    private void fillData(InputStream in) {
        Scanner scanner = null;
        try {
            scanner = getFullStringScanner(in);
            JSONArray jsonArray = new JSONArray(scanner.next());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                InternationalDay internationalDay = new InternationalDay(jsonobject);
                store.put(internationalDay.getDate(), internationalDay);
            }
        } catch (JSONException e) {
            throw new RuntimeException("Failed to read JSON", e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private Scanner getFullStringScanner(InputStream in) {
        return new Scanner(in, "UTF-8").useDelimiter("\\A");
    }

}