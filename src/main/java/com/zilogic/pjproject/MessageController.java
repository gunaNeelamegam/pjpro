package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.zilogic.pjproject.utils.MongoDb;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.bson.Document;

public class MessageController implements Initializable {

    ArrayList<String> buddyData = new ArrayList<String>();

    @FXML
    private JFXButton search;

    @FXML
    private Label userName;

    @FXML
    private JFXListView buddys;

    MongoDb mongodb = new MyApp().getMongodb();

    MongoDatabase database = this.mongodb.getDb();

    MongoCollection collection = database.getCollection("Buddy");

    private void loadAllData() {
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                buddyData.add((String) cursor.next().get("user"));

            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            loadAllData();
            loadDefaultInfo();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    AddBuddyController buddyControl = new AddBuddyController();

    private void loadDefaultInfo() throws Exception {
        ObservableList list = FXCollections.observableArrayList(buddyData);
        buddys.setItems(list);
        buddys.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #FFFFFF,#b5086b)");
        buddys.setOnMouseClicked((event) -> {
            String username = buddys.getSelectionModel().getSelectedItem().toString();
            System.out.println("hello " + username);
            try {
                Stage stage = (Stage) search.getScene().getWindow();
//                Stage stage = MainStageController.message_Stage;
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("MessageView.fxml")));
                stage.setTitle("message");
                stage.setTitle(username);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.out.println(e.getCause() + e.getMessage());
            }
        });

    }

}
