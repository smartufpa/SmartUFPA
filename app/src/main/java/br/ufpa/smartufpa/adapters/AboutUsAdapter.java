package br.ufpa.smartufpa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.models.AboutUsOption;

/**
 * Created by kaeuc on 07/12/2017.
 */

public class AboutUsAdapter extends RecyclerView.Adapter {

    private ArrayList<AboutUsOption> options;
    private Context parentContext;
    private AboutUsAdapter.OnItemClickListener onItemClickListener;

    public AboutUsAdapter(final ArrayList options, Context parentContext) {
        this.options = options;
        this.parentContext = parentContext;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Interface to mimic the OnItemClick from list view
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parentContext)
                .inflate(R.layout.item_about_option, parent, false);

        return new AboutUsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AboutUsViewHolder viewHolder = (AboutUsViewHolder) holder;

        viewHolder.title.setText(options.get(position).getTitle());
        viewHolder.subtitle.setText(options.get(position).getSubtitle());
        viewHolder.icon.setImageDrawable(options.get(position).getIcon());

    }

    @Override
    public int getItemCount() {
        return options.size();
    }


    private class AboutUsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title, subtitle;
        private ImageView icon;

        private AboutUsViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = view.findViewById(R.id.item_about_title);
            subtitle = view.findViewById(R.id.item_about_subtitle);
            icon = view.findViewById(R.id.item_about_img);

        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

}
