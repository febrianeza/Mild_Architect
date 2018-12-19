package com.lineup.mild.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.lineup.mild.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    private static final int UNSELECTED = -1;

    private RecyclerView recyclerView;
    private Context context;
    private int selectedItem = UNSELECTED;

    String[][] faqItems;

    public FaqAdapter(Context context, RecyclerView recyclerView, String[][] faqItems) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.faqItems = faqItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faq_recycler_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int number = position + 1;

        holder.expand_button.setText(number + ". " + faqItems[position][0]);
        holder.expanded_text.setText(faqItems[position][1]);
    }

    @Override
    public int getItemCount() {
        return faqItems.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ExpandableLayout.OnExpansionUpdateListener, View.OnClickListener {
        @BindView(R.id.expand_button)
        TextView expand_button;
        @BindView(R.id.expandable_layout)
        ExpandableLayout expandable_layout;
        @BindView(R.id.expanded_text)
        TextView expanded_text;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            expandable_layout.setInterpolator(new OvershootInterpolator());
            expandable_layout.setOnExpansionUpdateListener(this);

            expand_button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
            if (holder != null) {
                holder.expand_button.setSelected(false);
                holder.expandable_layout.collapse();
            }

            int position = getAdapterPosition();
            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            } else {
                expand_button.setSelected(true);
                expandable_layout.expand();
                selectedItem = position;
            }
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            Log.d("ExpandableLayout", "State: " + state);
            if (state == ExpandableLayout.State.EXPANDING) {
                recyclerView.smoothScrollToPosition(getAdapterPosition());
            }
        }
    }
}
