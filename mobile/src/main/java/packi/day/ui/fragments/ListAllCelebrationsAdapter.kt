package packi.day.ui.fragments

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import packi.day.R
import packi.day.store.InternationalDay
import packi.day.ui.ActivityMain
import java.util.*

internal class ListAllCelebrationsAdapter(fragment: Fragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    private val picasso: Picasso = Picasso.with(fragment.context)

    private var listData: Pair<Set<Int>, List<InternationalDay>> = Pair(emptySet(), emptyList())

    fun setData(data: Pair<Set<Int>, List<InternationalDay>>) {
        listData = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (listData.component1().contains(position)) HEADER else ITEM
    }

    override fun getItemCount(): Int {
        return listData.component1().size + listData.component2().size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            HEADER -> {
                val root = inflater.inflate(R.layout.section, parent, false)
                return HeaderHolder(root)
            }
            ITEM -> {
                val root = inflater.inflate(R.layout.cell_celebration, parent, false)
                return CelebrationHolder(root)
            }
            else -> {
                val root = inflater.inflate(R.layout.cell_celebration, parent, false)
                return CelebrationHolder(root)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            HEADER -> {
                val headerHolder = holder as HeaderHolder
                val celebration = listData.component2()[position - findNumberOfItemsBellow(position)]
                headerHolder.title.text = celebration.date.monthOfYear().getAsText(Locale.getDefault())
            }
            else -> {
                val celebrationHolder = holder as CelebrationHolder
                val childPosition = position - findNumberOfItemsBellow(position)
                val celebration = listData.component2()[childPosition]
                celebrationHolder.root.setTag(R.id.position, childPosition)
                picasso.load(celebration.drawable).into(celebrationHolder.image)
                celebrationHolder.text.text = Html.fromHtml("<b>" + celebration.date.dayOfMonth + "</b> : " + celebration.name)
                celebrationHolder.root.setOnClickListener(this)
            }
        }
    }

    // Need to implement binary search
    private fun findNumberOfItemsBellow(position: Int): Int {
        var count = 0
        for (header in listData.component1()) {
            if (header < position) {
                count++
            } else {
                return count
            }
        }
        return count
    }

    override fun onClick(v: View) {
        val position = v.getTag(R.id.position) as Int
        val celebrationDate = listData.component2()[position].date

        val target = Intent(v.context, ActivityMain::class.java)
        target.putExtra("date", celebrationDate)
        v.context.startActivity(target)
    }

    private class HeaderHolder internal constructor(root: View) : RecyclerView.ViewHolder(root) {

        val title: TextView

        init {
            title = root.findViewById(R.id.section_text)
        }
    }

    private class CelebrationHolder internal constructor(internal val root: View) : RecyclerView.ViewHolder(root) {

        val text: TextView
        val image: ImageView

        init {
            text = root.findViewById(R.id.text)
            image = root.findViewById(R.id.image)
        }
    }

    companion object {

        private const val HEADER = 0
        private const val ITEM = 2
    }

}