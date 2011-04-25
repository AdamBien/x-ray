package com.abien.xray.business.store.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Post implements Comparable<Post>{
    private String uri;
    private String title;
    private long numberOfHits;
    public final static String EMPTY = "-";


    public Post(String uri, String title, long numberOfHits) {
        this.uri = uri;
        this.title = title;
        this.numberOfHits = numberOfHits;
    }

    public Post(String uri, long numberOfHits) {
        this.uri = uri;
        this.numberOfHits = numberOfHits;
    }

    public Post() {   /* for JAXB*/ }

    public long getNumberOfHits() {
        return numberOfHits;
    }

    public String getTitle() {
        return title;
    }

    public String getShortenedTitle(int totalLength){
        if(title == null){
            return null;
        }
        int length = title.length();
        if(length < totalLength){
            return title;
        }
        String shortened = title.substring(0, totalLength - 5);
        return shortened + "[...]";
    }

    public boolean isTitleEmpty(){
        return (title == null || EMPTY.equals(title));
    }

    public String getUri() {
        return uri;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Post{" + "uri=" + uri + "title=" + title + "numberOfHits=" + numberOfHits + '}';
    }

    @Override
    public int compareTo(Post other) {
        if(numberOfHits == other.numberOfHits){
            return 0;
        }
        if(numberOfHits > other.numberOfHits){
            return 1;
        }
        return -1;
    }
}
