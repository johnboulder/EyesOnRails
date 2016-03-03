package com.wherethismove.eyesonrails;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A fragment representing a list of issues from the ruby on rails repository.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 *
 * NOTE, most everything in this class is default generated until you get to the populateList function
 */
public class IssueFragment extends Fragment
{
    public static final String ARG_URL = "url";
    private String mUrl;
    private List<IssueListItem> issuesListItems;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IssueFragment()
    {
        // initialize the list so it doesn't cause issues
        issuesListItems = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public static IssueFragment newInstance(String url)
    {
        IssueFragment fragment = new IssueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mUrl = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_issue_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView)
        {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            // Note issuesListItems is empty here so nothing is added until we update the list
            recyclerView.setAdapter(new MyIssueRecyclerViewAdapter(issuesListItems, mListener));
            // Call populate list so that the list will be populated
            populateList();
        }
        return view;
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener)
        {
            mListener = (OnListFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onListFragmentInteraction(IssueListItem item);

        void errorLoadingPage();
    }

    /**
     * Calls the GetPageJsonTask and uses the callback to populate the issuesListItems list.
     * Then notifys the adapter that the data has changed.
     */
    public void populateList()
    {
        // TODO
        GetPageJsonTask task = new GetPageJsonTask(
                new GetPageJsonTask.GetPageDataCallback()
                {

                    @Override
                    public void callback(JSONArray pageData)
                    {
                        try
                        {
                            // Iterate across the list of JSON objects and put
                            // pertinent data into issuesListItems
                            for(int i=0; i < pageData.length(); i++) {
                                // Get the JSON object at i
                                JSONObject jObject = pageData.getJSONObject(i);

                                // Get the relevant fields
                                String id = jObject.getString("number");
                                String url = jObject.getString("url");
                                String comments_url = jObject.getString("comments_url");
                                String body = jObject.getString("body");

                                JSONObject user = jObject.getJSONObject("user");
                                String login = user.getString("login");

                                String title = jObject.getString("title");

                                // Add those fields to an object, and add it to issuesListItems
                                issuesListItems.add(new IssueListItem(id, url, comments_url, body, login, title));
                            }

                            RecyclerView recyclerView = (RecyclerView) getView();
                            // Notify the list of the data change
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error()
                    {
                        mListener.errorLoadingPage();
                    }
                }
        );

        // Call GetPageJsonTask with the given URL
        task.execute(mUrl);
    }
}
