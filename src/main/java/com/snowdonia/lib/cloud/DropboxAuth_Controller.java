package com.snowdonia.lib.cloud;

import com.dropbox.core.DbxPKCEWebAuth;
import com.snowdonia.lib.logging.SnowdoniaLogger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DropboxAuth_Controller
{
    public static DbxPKCEWebAuth WEB_AUTH = null;
    public static String AUTH_URL = "http://google.com";

    @FXML private AnchorPane root;
    @FXML public TextField textfield_input;
    @FXML public Button button_auth;

    @FXML
    void initialize()
    {
        // only allow auth button when code is entered
        textfield_input.textProperty().addListener((observable, oldVal, newVal) -> button_auth.setDisable(newVal.isEmpty()));
    }

    public void linkClicked() throws IOException
    {
        Desktop desk = Desktop.getDesktop();
        try
        {
            desk.browse(new URI(AUTH_URL));
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void auth_button_clicked()
    {
        // close this window
        Stage stage = (Stage) root.getScene().getWindow();
        if (stage != null) stage.close();

        // try to auth dropbox with provided code
        SnowdoniaLogger.info(this.getClass(), "Authorizing dropbox");
        DropboxApi.dropbox_fetch_refresh_token(WEB_AUTH, textfield_input.getText());
    }

    public void offline_button_clicked()
    {
        //run in offline mode
        SnowdoniaLogger.info(this.getClass(), "Offline mode selected");
    }

    public void exit_button_clicked()
    {
        SnowdoniaLogger.info(this.getClass(), "Exit : Auth declined");
        System.exit(0);
    }
}
