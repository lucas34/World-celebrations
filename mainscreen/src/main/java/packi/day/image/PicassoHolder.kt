package packi.day.image

import android.content.Context
import com.squareup.picasso3.Picasso

object PicassoHolder {
    lateinit var appContext: Context
    private val instance: Picasso by lazy {
        Picasso
            .Builder(appContext)
            .build()
    }
    fun get() = instance
}