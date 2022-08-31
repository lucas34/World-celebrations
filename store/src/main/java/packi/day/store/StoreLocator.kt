package packi.day.store

import android.content.Context

interface StoreLocator {

    fun getStore(): InternationalDayRepository

}

fun Context.getStoreLocator(): StoreLocator {
    return if(this is StoreLocator) this else (this.applicationContext as StoreLocator)
}