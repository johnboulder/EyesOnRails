package com.wherethismove.eyesonrails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wherethismove.eyesonrails.IssueFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IssueListItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyIssueRecyclerViewAdapter extends RecyclerView.Adapter<MyIssueRecyclerViewAdapter.ViewHolder>
{

    private final List<IssueListItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyIssueRecyclerViewAdapter(List<IssueListItem> items, OnListFragmentInteractionListener listener)
    {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_issue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);

        holder.mIdView.setText("Issue #"+mValues.get(position).getId());
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mAuthor.setText("by "+mValues.get(position).getUsername());

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != mListener)
                {
                    // Notify the active callbacks interface that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mIdView;
        public final TextView mTitleView;
        public final TextView mAuthor;
        public IssueListItem mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mAuthor = (TextView) view.findViewById(R.id.author);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
