package com.simbiosyscorp.thetravelingsalesman.utils;

import android.util.Log;

/**
 * Created by D on 012 02 12.
 */
public class LinkedInUtils {

    /**
     * My api key
     * **/
    private static final String CLIENT_ID = "75p7aicvxrcg9a";
    private static final String CLIENT_SECRET = "DeZOCCmJ0NSFfyrB";


    private static final String DEBUG_TAG = "oauth: ";
    private static final String NETWORK_NAME = "network name";
    private static final String REDIRECT_URI = "https://127.0.0.1";
    private static final String STATE = "state";
    private static final String RESPONSE_TYPE_VALUE ="code";
    private static final String SCOPE_BASIC_PROFILE = "r_basicprofile";
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String SCOPE_PARAM = "scope";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";

    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";


    private static final String SCOPES = SCOPE_BASIC_PROFILE;
    /**
     * Method that generates the url for get the authorization token from the Service
     * @return Url
     */
    private static String getAuthorizationUrl(){
        String URL = AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND  +CLIENT_ID_PARAM    +EQUALS + CLIENT_ID
                +AMPERSAND  +SCOPE_PARAM        +EQUALS + SCOPES
                +AMPERSAND  +STATE_PARAM        +EQUALS +STATE
                +AMPERSAND  +REDIRECT_URI_PARAM +EQUALS +REDIRECT_URI;
        Log.i("authorization URL",""+URL);
        return URL;
    }


    /**
     * Method that generates the url for get the access token from the Service
     * @return Url
     */
    private static String getAccessTokenUrl(String authorizationToken){
        String URL = ACCESS_TOKEN_URL
                +QUESTION_MARK
                +GRANT_TYPE_PARAM+EQUALS+GRANT_TYPE
                +AMPERSAND
                +RESPONSE_TYPE_VALUE+EQUALS+authorizationToken
                +AMPERSAND
                +CLIENT_ID_PARAM+EQUALS+CLIENT_ID
                +AMPERSAND
                +REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI
                +AMPERSAND
                +SECRET_KEY_PARAM+EQUALS+CLIENT_SECRET;
        Log.i("accessToken URL", "" + URL);
        return URL;
    }


}
