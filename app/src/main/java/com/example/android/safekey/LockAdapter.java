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

public class LockAdapter extends RecyclerView.Adapter<LockAdapter.LockViewHolder> {

    private int mNumberItems;

    public LockAdapter(int numberOfItems)
    {
        mNumberItems = numberOfItems;
    }

    @Override
    public LockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        int layoutIdForListItem = R.layout.locks_list_item;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        boolean shouldAttachToParent = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParent);
        LockViewHolder viewHolder = new LockViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LockViewHolder holder, int position) {
        holder.bind(position);
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
}
