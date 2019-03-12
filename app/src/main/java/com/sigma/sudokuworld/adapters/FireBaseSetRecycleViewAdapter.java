package com.sigma.sudokuworld.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.masterdetail.SetListFragment;
import com.sigma.sudokuworld.masterdetail.SetListFragment.OnFragmentInteractionListener;
import com.sigma.sudokuworld.persistence.firebase.FireBaseSet;

import java.util.ArrayList;
import java.util.List;


public class FireBaseSetRecycleViewAdapter extends RecyclerView.Adapter<FireBaseSetRecycleViewAdapter.ViewHolder> {

    private List<FireBaseSet> mSets;
    private final SetListFragment.OnFragmentInteractionListener mListener;

    public FireBaseSetRecycleViewAdapter(OnFragmentInteractionListener listener) {
        mSets = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mSetItem = mSets.get(position);
        holder.mIdView.setText(mSets.get(position).getName());
        holder.mContentView.setText(mSets.get(position).getDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFireBaseClick(holder.mSetItem);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFireBaseLongClick(holder.mView, holder.mSetItem);
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSets.size();
    }

    public void setItems(List<FireBaseSet> sets) {
        mSets = sets;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public FireBaseSet mSetItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}