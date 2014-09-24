package com.airhacks.xray.persistence.control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author airhacks.com
 */
public class FilePersistence {

    public static void serialize(String file, Serializable object) throws FileNotFoundException, IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream)) {
                oos.writeObject(object);
            }
        }

    }

    public static Serializable deserialize(String file) throws FileNotFoundException, IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(file)) {
            try (ObjectInputStream oos = new ObjectInputStream(fis)) {
                return (Serializable) oos.readObject();
            }
        }

    }
}
