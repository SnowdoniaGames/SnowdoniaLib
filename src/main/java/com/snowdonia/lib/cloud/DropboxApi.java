package com.snowdonia.lib.cloud;

import com.dropbox.core.*;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;
import com.snowdonia.lib.DummyApplication;
import com.snowdonia.lib.logging.SnowdoniaLogger;
import com.snowdonia.lib.util.FxmlUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.nio.file.Files;

public class DropboxApi
{
    private static final String APP_KEY = "ei8sd9nc1tskgft";
    private static File refresh_token_file = null;
    private static DbxClientV2 client = null;

    public static void connect(File base_dir)
    {
        if (!base_dir.exists())
            SnowdoniaLogger.fatal(DropboxApi.class, "Unable to find base directory");

        refresh_token_file = new File(base_dir + File.separator + "refresh_token");
        if (refresh_token_file.exists())
        {
            // attempt to auth using refresh token
            dropbox_connect_using_refresh_token();
        }
        else
        {
            // build request to get refresh token, using pkce and offline access type
            DbxRequestConfig config = DbxRequestConfig.newBuilder(APP_KEY).build();
            DbxAppInfo appInfo = new DbxAppInfo(APP_KEY);
            DbxPKCEWebAuth webAuth = new DbxPKCEWebAuth(config, appInfo);
            DbxWebAuth.Request webAuthRequest =  DbxWebAuth.newRequestBuilder().withNoRedirect().withTokenAccessType(TokenAccessType.OFFLINE).build();

            // use the auth URL and direct the user to auth through the default browser
            DropboxAuth_Controller.WEB_AUTH = webAuth;
            DropboxAuth_Controller.AUTH_URL = webAuth.authorize(webAuthRequest);

            // load UI allowing the user to setup auth
            SnowdoniaLogger.info(DropboxApi.class, "DropboxAuth : Loading FXML");
            FXMLLoader fxmlLoader = new FXMLLoader(DummyApplication.class.getResource("fxml/DropboxAuth.fxml"));
            Parent root = FxmlUtil.loadFXML(DropboxApi.class, fxmlLoader);
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setAlwaysOnTop(true);
            stage.show();
        }
    }

    public static void download_file(File localFile, String remoteFile)
    {
        try
        {
            try (OutputStream outFile = new FileOutputStream(localFile))
            {
                client.files().downloadBuilder(remoteFile).download(outFile);
            }
        }
        catch (DbxException | IOException e)
        {
            SnowdoniaLogger.fatal(DropboxApi.class, "Unable to download file : " + remoteFile);
        }
    }

    public static void upload_file(File localFile, String remoteFile)
    {
        try (InputStream inFile = new FileInputStream(localFile))
        {
            client.files().uploadBuilder(remoteFile).withMode(WriteMode.OVERWRITE).uploadAndFinish(inFile);
        }
        catch (DbxException | IOException e)
        {
            SnowdoniaLogger.fatal(DropboxApi.class, "Unable to upload file : " + remoteFile);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void dropbox_connect_using_refresh_token()
    {
        try
        {
            String refresh_token = Files.readString(refresh_token_file.toPath());
            DbxRequestConfig config = DbxRequestConfig.newBuilder(APP_KEY).build();
            DbxCredential credential = new DbxCredential("", 0L, refresh_token, APP_KEY);
            client = new DbxClientV2(config, credential);

            // get the account name to check that we have authed
            FullAccount account = null;
            try
            {
                account = client.users().getCurrentAccount();
            }
            catch (DbxException e)
            {
                SnowdoniaLogger.warn(DropboxApi.class, "Failed to authorize dropbox with provided refresh_token");

                // delete the refresh_token file to force the user to reauth
                refresh_token_file.delete();

                // check file was deleted
                if (refresh_token_file.exists())
                    SnowdoniaLogger.fatal(DropboxApi.class, "Refresh file still exists");

                // for now simply close the app, when run again auth should get fixed
                System.exit(0);
            }
            SnowdoniaLogger.debug(DropboxApi.class, "Logged into dropbox account : " + account.getName().getDisplayName());
        }
        catch (IOException e)
        {
            SnowdoniaLogger.fatal(DropboxApi.class, "DropboxApi IOException");
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public static void dropbox_fetch_refresh_token(DbxPKCEWebAuth webAuth, String authCode)
    {
        if (webAuth == null)
            SnowdoniaLogger.fatal(DropboxApi.class, "Webauth lost somehow");

        try
        {
            // attempt to get refresh token
            DbxAuthFinish result = webAuth.finishFromCode(authCode.trim());
            String refresh_token = result.getRefreshToken();

            // create refresh token file
            Files.write(refresh_token_file.toPath(), refresh_token.getBytes());

            // check file now exists
            if (!refresh_token_file.exists())
                SnowdoniaLogger.fatal(DropboxApi.class, "Refresh file still doesn't exist");

            // retry the normal connection method
            dropbox_connect_using_refresh_token();
        }
        catch (DbxException e)
        {
            SnowdoniaLogger.fatal(DropboxApi.class, "DropboxApi RefreshToken DbxException");
        }
        catch (IOException e)
        {
            SnowdoniaLogger.fatal(DropboxApi.class, "DropboxApi RefreshToken IOException");
        }
    }
}
