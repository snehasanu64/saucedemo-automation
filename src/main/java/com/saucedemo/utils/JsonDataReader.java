package com.saucedemo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/** Turns a JSON array file (src/test/resources/testdata) into the Object[][] a @DataProvider needs. */
public class JsonDataReader {

    private JsonDataReader() {
    }

    /** Each row becomes one test run; the row is passed as a Map of field name to value. */
    public static Object[][] read(String fileName) {
        Type listType = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        try (InputStream in = JsonDataReader.class.getClassLoader().getResourceAsStream("testdata/" + fileName)) {
            if (in == null) {
                throw new IllegalArgumentException("Test data file not found: testdata/" + fileName);
            }
            Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
            List<Map<String, String>> rows = new Gson().fromJson(reader, listType);
            Object[][] data = new Object[rows.size()][1];
            for (int i = 0; i < rows.size(); i++) {
                data[i][0] = rows.get(i);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Could not read " + fileName, e);
        }
    }
}
