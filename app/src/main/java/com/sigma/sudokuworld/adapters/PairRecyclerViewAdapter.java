package com.sigma.sudokuworld.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.db.views.WordPair;
import com.sigma.sudokuworld.masterdetail.PairListFragment;
import com.sigma.sudokuworld.masterdetail.PairListFragment.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;


public class PairRecyclerViewAdapter extends RecyclerView.Adapter<PairRecyclerViewAdapter.ViewHolder> {

    private final OnFragmentInteractionListener mListener;
    private List<WordPair> mWordPairs;

    public PairRecyclerViewAdapter(PairListFragment.OnFragmentInteractionListener listener) {
        mWordPairs = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mWordPair = mWordPairs.get(position);
        holder.mIdView.setText(mWordPairs.get(position).getNativeWord().getWord());
        holder.mContentView.setText(mWordPairs.get(position).getForeignWord().getWord());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onClickPairFragmentInteraction(holder.mWordPair);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWordPairs.size();
    }

    public void setItems(List<WordPair> wordPairs) {
        mWordPairs = wordPairs;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public WordPair mWordPair;

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