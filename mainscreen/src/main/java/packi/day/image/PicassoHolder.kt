package packi.day.image

import android.content.Context
import com.squareup.picasso3.Picasso

/**
 * Created by lucas.nelaupe on 30/8/22
 * @author lucas.nelaupe@bytedance.com
 */
object PicassoHolder {
    lateinit var appContext: Context
    private val instance: Picasso by lazy {
        Picasso
            .Builder(appContext)
            .build()
    }
    fun get() = instance
}