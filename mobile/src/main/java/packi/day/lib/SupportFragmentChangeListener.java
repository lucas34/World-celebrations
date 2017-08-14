package packi.day.lib;

import android.support.v4.app.Fragment;


public interface SupportFragmentChangeListener<FRAGMENT extends Fragment> {

    void onChangeContent(FRAGMENT fragment);

}