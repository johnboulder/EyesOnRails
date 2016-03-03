package com.wherethismove.eyesonrails;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Uses fragments to display a list of
 */
public class MainActivity extends AppCompatActivity implements IssueFragment.OnListFragmentInteractionListener
{

    public final String ISSUES_FRAGMENT_BSTACK_NAME = "issues_fragment";
    public final String ERROR_FRAGMENT_BSTACK_NAME = "error_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Call to init the list of issues
        initializeView();
    }

    /**
     * Called from the onCreate() function to initialize the list fragment
     */
    protected void initializeView()
    {
        Bundle args = new Bundle();
        args.putString(IssueFragment.ARG_URL, "https://api.github.com/repos/rails/rails/issues");
        IssueFragment issuesFragment = new IssueFragment();
        issuesFragment.setArguments(args);

        // Set the list fragment up
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragment_container, issuesFragment, ISSUES_FRAGMENT_BSTACK_NAME).commit();
    }

    /**
     * Callback function used for when comments are clicked
     */
    @Override
    public void onListFragmentInteraction(final IssueListItem item)
    {
        // Get the comments URL and get all the comment data from the returned JSON object.
        GetPageJsonTask task = new GetPageJsonTask(
                new GetPageJsonTask.GetPageDataCallback()
                {

                    @Override
                    public void callback(JSONArray pageData)
                    {
                        try
                        {
                            // Get the first comment from the issue
                            String comments = item.getUsername()+": \n"+item.getBody()+"\n****************************\n";

                            // Simply iterate the JSONarray and add each username+comment to a text box in a popup
                            for(int i=0; i < pageData.length(); i++) {

                                JSONObject jObject = pageData.getJSONObject(i);

                                String body = jObject.getString("body");
                                JSONObject user = jObject.getJSONObject("user");
                                String login = user.getString("login");

                                comments = comments + login+": \n"+body+"\n****************************\n";
                            }

                            makeCommentsDialog(item.getTitle(), comments);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            makeCommentsDialog(item.getTitle(), "no comments to display, or rate limit reached");
                        }
                    }

                    @Override
                    public void error()
                    {
                        errorLoadingPage();
                    }
                }
        );

        // Call GetPageJsonTask with the comments url
        task.execute(item.getCommentsUrl());
    }

    /**
     * Causes a popup dialog to appear and list any and all comments passed to it
     * Intended to only be called by onListFragmentInteraction
     * @param title The title of the issue
     * @param comments The string of comments created by onListFragmentInteraction
     */
    private void makeCommentsDialog(String title, String comments)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(comments).setTitle(title);

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Replaces the current fragment with a fragment which contains an image
     * telling the user that there's been an issue loading the current page
     */
    @Override
    public void errorLoadingPage()
    {
        // Change the current fragment to an appropriate image
        ErrorFragment errorFragment = new ErrorFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, errorFragment, ERROR_FRAGMENT_BSTACK_NAME).addToBackStack(ERROR_FRAGMENT_BSTACK_NAME).commit();
    }
}
