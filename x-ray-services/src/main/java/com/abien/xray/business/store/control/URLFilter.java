package com.abien.xray.business.store.control;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class URLFilter {
    public final static String[] MUST_NOT_ENDS_WITH = {".css",".gif",".jpg",".js"};
    public final static String[] BLACK_LIST = {"/roller/CommentAuthenticatorServlet"};

    public boolean ignore(String uri){
        if(uri == null || uri.isEmpty()){
            return true;
        }
        if(mustNotEndsWith(uri)){
            return true;
        }
        if(isBlackListed(uri)){
            return true;
        }
        return false;
    }

    boolean mustNotEndsWith(String uri) {
        for (String ending : MUST_NOT_ENDS_WITH) {
            if(uri.endsWith(ending)){
                return true;
                }
        }
        return false;
    }

    boolean isBlackListed(String uri) {
        for (String ending : BLACK_LIST) {
            if(uri.endsWith(ending)){
                return true;
                }
        }
        return false;
    }
}