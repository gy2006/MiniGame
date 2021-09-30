package com.minigame.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public abstract class ResourceHelper {

    public  static void load(String name, Consumer<String> onLine) {
        try (InputStream is = ResourceHelper.class.getClassLoader().getResourceAsStream(name)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    onLine.accept(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
