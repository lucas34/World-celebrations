package packi.day.store.feature.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lucas34990 on 12/2/18.
 */

public class RealmInternationalDay extends RealmObject {

    @PrimaryKey
    int id;

    String name;

    int day;

    int month;

    String image;

}
