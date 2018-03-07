package packi.day.common

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

object Keyboard {

    fun hide(activity: Activity) {
        val manager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val windowToken = activity.currentFocus?.windowToken ?: return
        manager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

}

object Wear {

    fun sendNotification(context: Context, message: String) {
        val apiClient = GoogleApiClient.Builder(context)
                // TODO depreacted
                .addApi(Wearable.API)
                .build()

        if (apiClient.isConnected) {
            sendMessage(apiClient, message)
        } else {
            // TODO don't do blocking connect
            apiClient.blockingConnect()

            if (apiClient.isConnected) {
                sendMessage(apiClient, message)
            }
        }
    }

    private fun sendMessage(googleApiClient: GoogleApiClient, message: String) {
        if (googleApiClient.isConnected) {

            val datas = PutDataMapRequest.create(Constants.PATH_NOTIFICATION)
            datas.dataMap.putString(Constants.KEY_TITLE, message)
            val request = datas.asPutDataRequest()

            // TODO depreacted
            Wearable.DataApi.putDataItem(googleApiClient, request)
        }
    }

}

object Constants {

    const val PATH_NOTIFICATION = "/ongoingnotification"
    const val PATH_DISMISS = "/ondismiss"
    const val KEY_TITLE = "title"

}