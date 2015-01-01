package com.airhacks.xray.persistence.control;

import java.io.FileNotFoundException;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class FilePersistenceTest {

    @Test
    public void serializeAndDeserialize() throws IOException, FileNotFoundException, ClassNotFoundException {
        String fileName = "./target/file" + System.currentTimeMillis();
        String expected = "duke " + System.currentTimeMillis();
        FilePersistence.serialize(fileName, expected);
        String actual = (String) FilePersistence.deserialize(fileName);
        assertThat(actual, is(expected));
    }

}
