package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MessageController implements Initializable {
    
    @FXML
    private JFXButton search;
    
    @FXML
    private JFXButton userAccount1;
    
    @FXML
    private Label userName;
    
    @FXML
    private JFXListView Accounts;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadDefaultInfo();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    static int i = 0;
    
    private void loadDefaultInfo() throws Exception {
        
        Accounts.setItems(AddAccountController.observer_account);
   
         userName.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #00FF00, #FFFFFF)");
    }
    
}
