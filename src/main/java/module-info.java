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
    exports com.snowdonia.lib.logging;
    exports com.snowdonia.lib.util;
    exports com.snowdonia.lib.cloud;
    exports com.snowdonia.lib.file;
    exports com.snowdonia.lib.ui;
}