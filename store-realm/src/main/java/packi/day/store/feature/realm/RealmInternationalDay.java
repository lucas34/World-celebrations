package packi.day.store.feature.realm;

import org.jetbrains.annotations.NotNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import packi.day.store.CelebrationAdaptable;
import packi.day.store.InternationalDay;

/**
 * Created by lucas34990 on 12/2/18.
 */

public class RealmInternationalDay extends RealmObject implements CelebrationAdaptable {

    @PrimaryKey
    int id;

    String name;

    int day;

    int month;

    String image;

    @NotNull
    @Override
    public InternationalDay adapt() {
        return new InternationalDay(id, name, day, month, image);
    }
}
