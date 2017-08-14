package packi.day.lib;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class SupportNavigationHandler<FRAGMENT extends Fragment> {

    private final FragmentManager manager;
    private final int layout;
    private SupportFragmentChangeListener<FRAGMENT> changeListener;

    public SupportNavigationHandler(FragmentActivity activity, int layout) {
        manager = activity.getSupportFragmentManager();
        this.layout = layout;
    }

    public SupportNavigationHandler(FragmentManager fragmentManager, int layout) {
        manager = fragmentManager;
        this.layout = layout;
    }

    public void showMain(FRAGMENT target) {
        notifyChange(target);
        removeAllBackStack(manager);
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(layout, target, generateTag(0));
        ft.commit();
    }

    public void replaceContent(FRAGMENT target) {
        replaceContent(target, null);
    }

    public void replaceContent(FRAGMENT target, Bundle args) {
        notifyChange(target);
        removeBackStack(manager);
        target.setArguments(args);
        transactionReplace(target, generateTag(1));
    }

    public void pushContent(FRAGMENT target) {
        pushContent(target, null);
    }

    public void pushContent(FRAGMENT target, Bundle args) {
        notifyChange(target);
        target.setArguments(args);
        transactionReplace(target, generateTag(getDeepness() + 1));
    }

    public void popCurrentFragment() {
        notifyChange(getPreviousFragment());
        manager.popBackStackImmediate();
    }

    private void removeBackStack(FragmentManager fm) {
        for (int i = 1; i < manager.getBackStackEntryCount(); ++i) {
            manager.popBackStack();
        }
    }

    private void removeAllBackStack(FragmentManager fm) {
        for (int i = 0; i < manager.getBackStackEntryCount(); ++i) {
            manager.popBackStack();
        }
    }

    public int getDeepness() {
        return manager.getBackStackEntryCount();
    }

    public void setOnFragmentChangeListener(SupportFragmentChangeListener<FRAGMENT> listener) {
        changeListener = listener;
    }

    public FRAGMENT getCurrentFragment() {
        return (FRAGMENT) manager.findFragmentByTag("NAVIGATION_FRAGMENT_HANDLER" + (getDeepness()));
    }

    private String generateTag(int offset) {
        return "NAVIGATION_FRAGMENT_HANDLER" + (offset);
    }

    private FRAGMENT getPreviousFragment() {
        if (getDeepness() == 0) {
            return (FRAGMENT) manager.findFragmentByTag("NAVIGATION_FRAGMENT_HANDLER0");
        } else {
            return (FRAGMENT) manager.findFragmentByTag("NAVIGATION_FRAGMENT_HANDLER" + (getDeepness() - 1));
        }
    }

    private void transactionReplace(FRAGMENT target, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(layout, target, tag);
        ft.addToBackStack(target.toString());
        ft.commit();
    }

    private void notifyChange(FRAGMENT fragment) {
        if (changeListener != null) {
            changeListener.onChangeContent(fragment);
        }
    }

}