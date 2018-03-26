package com.uol.year2.safekey;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uol.year2.safekey.SQLiteDB.LockListContract;

/**
 * Created by Lottie on 08/03/2018.
 */

public class LockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor mCursor;

    //Views
    private static final int LOCK = 0;
    private static final int PLUS = 1;

    //Click Listener
    final private PlusClickListener mOnClickListener;

    public LockAdapter(PlusClickListener listener)
    {
        mOnClickListener = listener;
    }

    public interface PlusClickListener {
        void onPlusClicked(int clickedItemIndex);
    }

    @Override
    public int getItemViewType(int position) {
        //If cursor is null then set only a plus
        if (mCursor == null) return PLUS;

        //Sets the view type. Only last item in list can be a plus
        if (position < mCursor.getCount()) return LOCK;
        else return PLUS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        boolean shouldAttachToParent = false;

        RecyclerView.ViewHolder viewHolder = null;

        //Creates views based on the view type
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
        //Binding of the views into their positions
        switch (getItemViewType(position)) {
            case LOCK:              //Lock binding
                int idIndex = mCursor.getColumnIndex(LockListContract.LockListEntry._ID);

                Log.d("Lock Adapter", "idIndex = " + idIndex);
                mCursor.moveToPosition(position);

                final LockViewHolder lockViewHolder = (LockViewHolder) holder;
                String name = mCursor.getString(mCursor.getColumnIndex(LockListContract.LockListEntry.COLUMN_LOCK_NAME));
                lockViewHolder.setName(name);

                final int id = mCursor.getInt(idIndex);
                lockViewHolder.itemView.setTag(id);
                break;
            case PLUS:              //Plus binding
                final PlusViewHolder plusViewHolder = (PlusViewHolder) holder;
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 1;
        else return mCursor.getCount() + 1;
    }

    //Swap cursor function
    public Cursor swapCursor(Cursor c) {
       if (mCursor == c) return null;

       Cursor temp = mCursor;
       this.mCursor = c;

       if (c != null) this.notifyDataSetChanged();
       return temp;
    }

    // Creation of two views with different purposes

    class LockViewHolder extends RecyclerView.ViewHolder {
        TextView ListItemLockView;

        public LockViewHolder (View itemView)
        {
           super(itemView);
           ListItemLockView = (TextView) itemView.findViewById(R.id.tv_item_lock);
        }

        public void setName(String name)
        {
            //Sets the text as Lock + position number.
            ListItemLockView.setText(name);
        }
    }

    class PlusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public PlusViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // When button is clicked
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onPlusClicked(clickedPosition);
        }
    }
}
