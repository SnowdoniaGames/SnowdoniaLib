module com.snowdonia.lib {
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires javafx.graphics;
    requires javafx.fxml;
    requires dropbox.core.sdk;
    requires javafx.controls;
    requires java.desktop;
    requires com.google.gson;
    requires com.sun.jna.platform;
    requires google.http.client.jackson2;
    requires google.api.client;
    requires google.api.services.youtube.v3.rev222;
    requires google.http.client;
    exports com.snowdonia.lib.cloud;
    exports com.snowdonia.lib.file;
    exports com.snowdonia.lib.imagerecognition;
    exports com.snowdonia.lib.imagerecognition.data;
    exports com.snowdonia.lib.input;
    exports com.snowdonia.lib.logging;
    exports com.snowdonia.lib.ui;
    exports com.snowdonia.lib.util;
    exports com.snowdonia.lib.api.youtube;
}