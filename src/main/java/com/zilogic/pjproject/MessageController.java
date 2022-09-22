package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;



public class MessageController implements Initializable {

    @FXML
    private JFXButton search;

    @FXML
    private Label userName;

    @FXML
    private JFXListView buddys;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            loadDefaultInfo();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }



    private void loadDefaultInfo() throws Exception {
        buddys.setItems(AddBuddyController.observer_buddy);
        buddys.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #00FF00, #FFFFFF)");
        buddys.setOnMouseClicked((m) -> {
            AddBuddy bdy = (AddBuddy) buddys.getSelectionModel().getSelectedItem();
            System.out.println("hello " + bdy.getUri());

        });

    }

}
