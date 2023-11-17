package com.snowdonia.lib.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class JsonParser
{
    public static <T> T load_json_from_file(File jsonFile, Class<T> clazz)
    {
        try
        {
            String json = Files.readString(jsonFile.toPath());

            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        }
        catch (IOException e)
        {
            // file doesn't exist, create it?
        }
        return null;
    }

    public static void write_json_to_file(File jsonFile, Object obj)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(obj);

        try
        {
            Files.write(jsonFile.toPath(), json.getBytes());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
