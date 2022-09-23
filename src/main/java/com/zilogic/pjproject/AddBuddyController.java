package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zilogic.pjproject.utils.MongoDb;
import com.zilogic.pjproject.utils.PropertyFile;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.bson.Document;
import org.pjsip.pjsua2.BuddyConfig;

/**
 *
 * @author Guna
 */
public class AddBuddyController {

    private BuddyConfig bdy = new BuddyConfig();
    private MyApp app = new MyApp();
     ObservableList<AddBuddy> observer_buddy = FXCollections.observableArrayList();
     AddBuddy buddy = null;
    private MongoDb mongodb = new MyApp().getMongodb();
    private MyBuddy mybud = null;
    private MongoDatabase database = mongodb.getDb();
    private MongoCollection collection = database.getCollection("Buddy");

    public MongoCollection getCollection() {
        return collection;
    }

    public void setCollection(MongoCollection collection) {
        this.collection = collection;
    }

    @FXML
    public TextField buddyName;

    @FXML
    private JFXButton closeBtn;

    @FXML
    private JFXButton saveBtn;

    @FXML
    void close(ActionEvent event) {
        MainStageController.add_buddy_stage.close();
    }

    //SAVE INTO THE PJSUA2 ACCOUNT
    @FXML
    void save() {
        Platform.runLater(() -> {
            try {
                add_Buddy();
            } catch (Exception ex) {
            }
        });

        MainStageController.add_buddy_stage.close();
    }

    //Adding buddy to MongoDb
    private void AddingDocs() {
        try {
            Document buddy = new Document("_id", bdy.getUri());
            buddy.append(" domain", bdy.getUri().substring(9));
            buddy.append("user", buddyName.getText());
            buddy.append("subscribe", bdy.getSubscribe());
            this.collection.insertOne(buddy);
            System.out.println("Document added  into Collection");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void add_Buddy() throws Exception {
        MyAccount acc = MyApp.accList.get(0);
        buddy=new AddBuddy();
        bdy.setUri("sip:" + buddyName.getText().trim() + "@" + acc.getInfo().getUri().substring(9));
        bdy.setSubscribe(true);
        observer_buddy.add(buddy);
        mybud = acc.addBuddy(bdy);
        System.out.println("Buddy added Successfully");
        this.AddingDocs();
        app.saveConfig("pjsua2.json");
    }
}

// @FXML
/* synchronized void save(ActionEvent event) throws Exception {

        createBuddy = new Thread(() -> {
            Runnable updater = () -> {
                System.out.println("creating the Buddy Account ");
                BuddyConfig bdy = new BuddyConfig();
                try {
                    MyAccount acc = MyApp.accList.get(0);
                    bdy.setUri("sip:" + buddyName.getText().trim() + "@" + acc.getInfo().getUri().substring(9));
                    bdy.setSubscribe(true);
                    AddBuddy buddy = new AddBuddy(buddyName.getText());
                    buddyDetails.add(buddy);
                    mybud = acc.addBuddy(bdy);
                    MyApp.ep.libHandleEvents(10L);
                    System.out.println(" Buddy status : " + bdy.getUri());
                    var app = new MyApp();
                    if (mybud.getId() != -1) {
                        exitCreateBuddy = true;
                        app.saveConfig("pjsua2.json");
                        MainStageController.add_buddy_stage.close();
                    }
                } catch (Exception e) {
                    System.err.println(" BUDDY CREATION FAILED");
                }
            };
            while (!exitCreateBuddy) {
                try {
                    Thread.sleep(100);
                    Platform.runLater(updater);
                } catch (Exception e) {
                }
            }
        });
        createBuddy.start();
        System.out.println(" Create buddy is Alive : " + createBuddy.isAlive());
    }*/
