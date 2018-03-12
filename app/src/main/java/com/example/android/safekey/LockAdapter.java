package com.example.android.safekey;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Lottie on 08/03/2018.
 */

public class LockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mNumberItems;

    //Views
    private static final int LOCK = 0;
    private static final int PLUS = 1;

    public LockAdapter(int numberOfItems)
    {
        mNumberItems = numberOfItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mNumberItems - 1) return LOCK;
        else return PLUS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        boolean shouldAttachToParent = false;

        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case LOCK:
                View lockView = inflater.inflate(R.layout.locks_list_item, parent, shouldAttachToParent);
                viewHolder = new LockViewHolder(lockView);
                break;
            case PLUS:
                View plusView = inflater.inflate(R.layout.end_of_view, parent, shouldAttachToParent);
                viewHolder = new PlusViewHolder(plusView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case LOCK:
                final LockViewHolder lockViewHolder = (LockViewHolder) holder;
                lockViewHolder.bind(position);
                break;
            case PLUS:
                final PlusViewHolder plusViewHolder = (PlusViewHolder) holder;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class LockViewHolder extends RecyclerView.ViewHolder {
        TextView ListItemLockView;

        public LockViewHolder (View itemView)
        {
           super(itemView);
           ListItemLockView = (TextView) itemView.findViewById(R.id.tv_item_lock);
        }

        public void bind(int listIndex)
        {
            ListItemLockView.setText("Lock " + String.valueOf(listIndex));
        }
    }

    class PlusViewHolder extends RecyclerView.ViewHolder {

        public PlusViewHolder(View itemView) {
            super(itemView);
        }
    }
}
