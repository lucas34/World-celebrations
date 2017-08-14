package com.journeemondialelib;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Celebration extends RealmObject {

    @PrimaryKey
    public int id;

    public int day;

    public int month;

    public String name;

    public String image;

}
