package packi.day.lib

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

class SupportNavigationHandler<FRAGMENT : Fragment> {

    private val manager: FragmentManager
    private val layout: Int
    private var changeListener: ((FRAGMENT) -> Unit)? = null

    val deepness: Int
        get() = manager.backStackEntryCount

    val currentFragment: FRAGMENT
        get() = manager.findFragmentByTag("NAVIGATION_FRAGMENT_HANDLER$deepness") as FRAGMENT

    private val previousFragment: FRAGMENT
        get() = if (deepness == 0) {
            manager.findFragmentByTag("NAVIGATION_FRAGMENT_HANDLER0") as FRAGMENT
        } else {
            manager.findFragmentByTag("NAVIGATION_FRAGMENT_HANDLER" + (deepness - 1)) as FRAGMENT
        }

    constructor(activity: FragmentActivity, layout: Int) {
        manager = activity.supportFragmentManager
        this.layout = layout
    }

    constructor(fragmentManager: FragmentManager, layout: Int) {
        manager = fragmentManager
        this.layout = layout
    }

    @JvmOverloads
    fun showMain(target: FRAGMENT, args: Bundle? = null) {
        notifyChange(target)
        removeAllBackStack(manager)
        target.arguments = args
        val ft = manager.beginTransaction()
        ft.replace(layout, target, generateTag(0))
        ft.commit()
    }

    @JvmOverloads
    fun replaceContent(target: FRAGMENT, args: Bundle? = null) {
        notifyChange(target)
        removeBackStack(manager)
        target.arguments = args
        transactionReplace(target, generateTag(1))
    }

    @JvmOverloads
    fun pushContent(target: FRAGMENT, args: Bundle? = null) {
        notifyChange(target)
        target.arguments = args
        transactionReplace(target, generateTag(deepness + 1))
    }

    fun popCurrentFragment() {
        notifyChange(previousFragment)
        manager.popBackStackImmediate()
    }

    private fun removeBackStack(fm: FragmentManager) {
        for (i in 1 until manager.backStackEntryCount) {
            manager.popBackStack()
        }
    }

    private fun removeAllBackStack(fm: FragmentManager) {
        for (i in 0 until manager.backStackEntryCount) {
            manager.popBackStackImmediate()
        }
    }

    fun setOnFragmentChangeListener(listener: (FRAGMENT) -> Unit) {
        changeListener = listener
    }

    private fun generateTag(offset: Int): String {
        return "NAVIGATION_FRAGMENT_HANDLER$offset"
    }

    private fun transactionReplace(target: FRAGMENT, tag: String) {
        val ft = manager.beginTransaction()
        ft.replace(layout, target, tag)
        ft.addToBackStack(target.toString())
        ft.commit()
    }

    private fun notifyChange(fragment: FRAGMENT) {
        changeListener?.invoke(fragment)
    }

}