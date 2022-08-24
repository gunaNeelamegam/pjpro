module com.zilogic.pjproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires pjsip;
    requires com.jfoenix;
    opens com.zilogic.pjproject to javafx.fxml;
    exports com.zilogic.pjproject;
}
