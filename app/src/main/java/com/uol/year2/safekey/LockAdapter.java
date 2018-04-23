package com.uol.year2.safekey;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

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

    //Switch listener
    final private SwitchCheckListener mOnCheckListener;

    public LockAdapter(PlusClickListener listener, SwitchCheckListener listener2)
    {
        mOnClickListener = listener;
        mOnCheckListener = listener2;
    }

    public interface PlusClickListener {
        void onPlusClicked(int clickedItemIndex);
    }

    public interface SwitchCheckListener {
        void onSwitchChanged(int clickedItemIndex, boolean b, Object tag);
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
        //Get parent activity where Recycler view is located
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Binding of the views into their positions
        switch (getItemViewType(position)) {
            case LOCK:              //Lock binding
                //Get index
                int idIndex = mCursor.getColumnIndex(LockListContract.LockListEntry._ID);
                //Verify index
                Log.d("Lock Adapter", "idIndex = " + idIndex);

                //Move Cursor from previous query to new position
                mCursor.moveToPosition(position);

                //Create view holder
                final LockViewHolder lockViewHolder = (LockViewHolder) holder;
                //Get lock name from Cursor then call view holder function to set name to text editor
                String name = mCursor.getString(mCursor.getColumnIndex(LockListContract.LockListEntry.COLUMN_LOCK_NAME));
                lockViewHolder.setName(name);

                //Get Id from cursor and set as Tag for the holder
                final int id = mCursor.getInt(idIndex);
                lockViewHolder.itemView.setTag(id);

                //Binds onSwitchChanged to view holder
                lockViewHolder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        //Function on SafeKey_Main_Page.java
                        mOnCheckListener.onSwitchChanged(position, b, lockViewHolder.itemView.getTag());
                    }
                });

                break;
            case PLUS:              //Plus binding
                //Create plus view holder
                //No special functions needed here as static value
                final PlusViewHolder plusViewHolder = (PlusViewHolder) holder;
                break;
        }
    }

    //Get number of items in recycler view
    @Override
    public int getItemCount() {
        //If empty cursor then 1 item is still there due to '+'
        if (mCursor == null) return 1;
        //Otherwise return Number of items in cursor + 1 for plus
        else return mCursor.getCount() + 1;
    }

    //Swap cursor function
    public Cursor swapCursor(Cursor c) {
        //If cursor is the same as previous no updates need to occur
       if (mCursor == c) return null;

       //Swap cursor values
       Cursor temp = mCursor;
       this.mCursor = c;

       //Data has changed so view must be restarted to show changed
       if (c != null) this.notifyDataSetChanged();
       return temp;
    }

    // Creation of two views with different purposes

    //Standard lock view
    class LockViewHolder extends RecyclerView.ViewHolder {
        //GUI elements
        TextView ListItemLockView;
        public Switch mSwitch;

        public LockViewHolder (View itemView)
        {
           super(itemView);
           //Link GUI items to variables
           ListItemLockView = (TextView) itemView.findViewById(R.id.tv_item_lock);
           mSwitch = (Switch) itemView.findViewById(R.id.sh_toggle);
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
            //Uses function on SafeKey_Main_Page.java
            mOnClickListener.onPlusClicked(clickedPosition);
        }
    }
}
