package com.snowdonia.lib.util;

import com.snowdonia.lib.logging.SnowdoniaLogger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

@SuppressWarnings("rawtypes")
public class FxmlUtil
{
    public static Parent loadFXML(Class c, FXMLLoader fxmlLoader)
    {
        Parent root = null;
        try
        {
            root = fxmlLoader.load();
        }
        catch (IOException e)
        {
            SnowdoniaLogger.fatal(c, "Unable to load fxml");
        }
        return root;
    }
}
