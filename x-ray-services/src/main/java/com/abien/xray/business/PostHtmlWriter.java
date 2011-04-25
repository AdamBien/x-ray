package com.abien.xray.business;

import com.abien.xray.business.store.entity.Post;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Provider
@Produces(MediaType.APPLICATION_XHTML_XML)
public class PostHtmlWriter implements
       MessageBodyWriter<List<Post>> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return (List.class.isAssignableFrom(type));
    }

    @Override
    public long getSize(List<Post> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        String serialized = serialize(t);
        return serialized.length();
    }

    @Override
    public void writeTo(List<Post> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        String serialized = serialize(t);
        entityStream.write(serialized.getBytes());
    }

    public String serialize(List<Post> posts){
        StringBuilder result = new StringBuilder();
        result.append("<ol class=\"posts\">");
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            result.append("<li class=\"post\">");
            result.append(serialize(post));
            result.append("</li>");
            result.append("\n");
        }
        result.append("</ol>");
        return result.toString();
    }

    public String serialize(Post post){
        StringBuilder serialized = new StringBuilder();
        serialized.append("<a class=\"postLink\" href=\"http://www.adam-bien.com/roller/abien");
        serialized.append(post.getUri());
        serialized.append("\">");
        serialized.append(post.getShortenedTitle(80));
        serialized.append("</a>");
        serialized.append("<div class=\"hits\">");
        serialized.append(post.getNumberOfHits());
        serialized.append("</div>");
        return serialized.toString();
    }

}
