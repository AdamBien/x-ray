package com.abien.xray.business.store.control;

/**
 *
 * @author abien
 */
public class URLPathExtractor {

    private String begin = null;
    private String end = null;

    public URLPathExtractor(String begin, String end) {
        this.begin = begin;
        this.end = end;
    }

    public URLPathExtractor() {
        this.begin = "/roller/abien";
        this.end = "#";
    }

    public String extractPathSegmentFromURL(String pathCandidate) {
        String path = extractURI(pathCandidate);
        int beginIndex = path.indexOf(begin);
        if (beginIndex == -1) {
            return path;
        }
        beginIndex += begin.length();
        int endIndex = path.indexOf(end);
        if (endIndex == -1) {
            endIndex = path.length();
        }
        String result = path.substring(beginIndex, endIndex);
        if (result == null || result.isEmpty()) {
            return "/";
        } else {
            if (!isEntry(result)) {
                return result;
            }
            return dropPlusGetSuffix(dropTrailingSlash(result));
        }
    }

    /**
     * To ignore: /entry/field_vs_property_based_access++GET+http://www.java-framework.com/roller/abien/entry/field_vs_property_based_access+%5B0,15749,46211%5D+-%3E+ 
     * @param pathCandidate
     * @return
     */
    public String dropPlusGetSuffix(String pathCandidate) {
        int index = pathCandidate.indexOf('+');
        if (index == -1) {
            return pathCandidate;
        } else {
            return pathCandidate.substring(0, index);
        }
    }

    public boolean isEntry(String uri) {
        return uri.contains("/entry");
    }

    public String dropTrailingSlash(String pathCandidate) {
        int indexOfLastChar = pathCandidate.length() - 1;
        if (indexOfLastChar > 0 && pathCandidate.charAt(indexOfLastChar) == '/') {
            return pathCandidate.substring(0, indexOfLastChar);
        }
        return pathCandidate;
    }

    public String extractReferer(String referer) {
        if (referer == null || !referer.contains("|")) {
            return null;
        }
        int pipeIndex = referer.lastIndexOf('|');
        return referer.substring(pipeIndex + 1, referer.length());
    }

    String extractURI(String path) {
        if (path == null || !path.contains("|")) {
            return path;
        }
        int pipeIndex = path.indexOf('|');
        return path.substring(0, pipeIndex);
    }
}
