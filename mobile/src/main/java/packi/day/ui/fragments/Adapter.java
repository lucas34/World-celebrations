package packi.day.ui.fragments;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import packi.day.R;
import packi.day.WorldApplication;
import packi.day.common.SomeTools;
import packi.day.store.InternationalDay;
import packi.day.store.StoreData;
import packi.day.ui.ActivityMain;


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

    private static final int HEADER = 0;
    private static final int ITEM = 2;
    private final StoreData viewModel;
    private final ActivityMain activity;
    private final Picasso picasso;
    private List<InternationalDay> celebrations;
    private Set<Integer> headers;

    public Adapter(ActivityMain activity, @Nullable String filter) {
        this.viewModel = ((WorldApplication) activity.getApplication()).getCelebrationHelper();
        this.activity = activity;

        picasso = Picasso.with(activity);

        celebrations = viewModel.find(filter);
        headers = viewModel.count(filter);
    }

    public void setFilter(String filter) {
        celebrations = viewModel.find(filter);
        headers = viewModel.count(filter);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return headers.contains(position) ? HEADER : ITEM;
    }

    @Override
    public int getItemCount() {
        return celebrations.size() + headers.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case HEADER: {
                View root = inflater.inflate(R.layout.section, parent, false);
                return new HeaderHolder(root);
            }
            case ITEM:
            default: {
                View root = inflater.inflate(R.layout.cell_celebration, parent, false);
                return new CelebrationHolder(root);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HEADER: {
                HeaderHolder headerHolder = (HeaderHolder) holder;
                InternationalDay celebration = celebrations.get(position - findNumberOfItemsBellow(position));
                headerHolder.title.setText(celebration.getDate().monthOfYear().getAsText(Locale.getDefault()));
                break;
            }
            case ITEM:
            default: {
                CelebrationHolder celebrationHolder = (CelebrationHolder) holder;
                int childPosition = position - findNumberOfItemsBellow(position);
                InternationalDay celebration = celebrations.get(childPosition);

                celebrationHolder.root.setTag(R.id.position, childPosition);
                picasso.load(celebration.getDrawable()).into(celebrationHolder.image);
                celebrationHolder.text.setText(Html.fromHtml("<b>" + celebration.getDate().getDayOfMonth() + "</b> : " + celebration.name));
                celebrationHolder.root.setOnClickListener(this);
                break;
            }

        }
    }

    // Need to implement dichotomy on this
    private int findNumberOfItemsBellow(int position) {
        int count = 0;
        for (Integer header : headers) {
            if (header < position) {
                count++;
            } else {
                return count;
            }
        }
        return count;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.position);
        InternationalDay celebration = celebrations.get(position);
        SomeTools.hideKeyboard(activity);
        activity.showFocus(celebration.getDate());
    }

    private static final class HeaderHolder extends RecyclerView.ViewHolder {

        private final TextView title;

        /* package */ HeaderHolder(View root) {
            super(root);
            title = root.findViewById(R.id.section_text);
        }
    }

    private static final class CelebrationHolder extends RecyclerView.ViewHolder {

        private final View root;
        private final TextView text;
        private final ImageView image;

        /* package */ CelebrationHolder(View root) {
            super(root);
            this.root = root;
            text = root.findViewById(R.id.text);
            image = root.findViewById(R.id.image);
        }
    }

}
