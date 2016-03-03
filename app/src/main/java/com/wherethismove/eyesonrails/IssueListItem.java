package com.wherethismove.eyesonrails;

/**
 * Created by stockweezie on 3/2/2016.
 */
public class IssueListItem
{
    private String mId; // id
    private String mUrl;// url
    private String mCommentsUrl; // comments_url
    private String mBody; // body
    private String mUsername; // login
    private String mTitle; // title

    IssueListItem(String id, String url, String commentsUrl, String body, String username, String title)
    {
        mId = id;
        mUrl = url;
        mCommentsUrl = commentsUrl;
        mBody = body;
        mUsername = username;
        mTitle = title;
    }

    public String getId()
    {
        return mId;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public String getCommentsUrl()
    {
        return mCommentsUrl;
    }

    public String getBody()
    {
        return mBody;
    }

    public String getUsername()
    {
        return mUsername;
    }

    public String getTitle()
    {
        return mTitle;
    }
}
