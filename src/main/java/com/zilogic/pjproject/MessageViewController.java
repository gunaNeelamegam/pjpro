package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.zilogic.pjproject.utils.MongoDb;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.SendTypingIndicationParam;
import org.pjsip.pjsua2.SipTxOption;

public class MessageViewController implements Initializable {

//    MyBuddy bdy =new MyBuddy();
    MyBuddy buddy = new MyBuddy();
    String BUDDYURI;
    @FXML
    private TextField message;
    @FXML
    private JFXButton sendBtn;

    @FXML
    private AnchorPane messageRoot;

    @FXML
    private JFXButton search;

    @FXML
    private Label userName;
    MongoDb mongodb = new MyApp().getMongodb();

    //Load the Message's from database 
    private void LoadMessages() {
    }
//This method is used to add the message to Database

    private void addMessageToDB() {
    }

    //intialize the load the data from the data base
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userName.setText(BUDDYURI);
        getSipUri();
    }

    //reading the data from the db
    private String getSipUri() {
        MongoDatabase database = this.mongodb.getDb();
        MongoCollection collection = database.getCollection("Buddy");
        MongoCursor cursor = collection.find(Filters.eq("user", userName.getText())).iterator();
        try {
            while (cursor.hasNext()) {
                BUDDYURI = (String) cursor.next();
                System.out.println(BUDDYURI);
            }
        } finally {
            cursor.close();
        }
        return BUDDYURI;
    }

    //Sending videos as the Data
    private void sendMultiPartData(String Data, File file) {

    }

    //Send the images as the Data
    private void sendMultiPartData(String data, String... d) throws Exception {
        MyBuddy buddy = new MyBuddy();
        SendInstantMessageParam prm = new SendInstantMessageParam();
        SipTxOption setting = prm.getTxOption();
        setting.setContentType("Image/media");
        setting.setMsgBody(data);
        setting.getTargetUri();
        Arrays.toString(d);
        buddy.sendInstantMessage(prm);
    }

    //Sending message to Buddy
    @FXML
    private void sendMessage() {

        Platform.runLater(() -> {
            try {
                SendInstantMessageParam prm = new SendInstantMessageParam();
                BUDDYURI = getSipUri();
                prm.setContent(message.getText());
                prm.setContentType("text/plain");
                prm.getTxOption().getMsgBody();
                MyAccount acc = MyApp.accList.get(0);
                prm.getTxOption().setTargetUri(acc.getInfo().getUri());
                buddy.sendInstantMessage(prm);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    //@METHOD is used for listening the TextField
    @FXML
    private void sendTypingMessage(ActionEvent event) throws Exception {
        MyAccount acc = MyApp.accList.get(0);
        MyBuddy buddy = new MyBuddy();
        SendTypingIndicationParam prm = new SendTypingIndicationParam();
        prm.setIsTyping(true);
        prm.getTxOption().setTargetUri(acc.getInfo().getUri());
        buddy.sendTypingIndication(prm);
        System.out.println("=========typing indication==============");
    }

    //return to the Back Message Fxml
    @FXML
    private void back() throws Exception {
        Stage stage = (Stage) sendBtn.getScene().getWindow();
        Scene scene =new Scene(FXMLLoader.load(getClass().getResource("Message.fxml")));
        stage.setScene(scene);
        stage.setTitle("Message");
        stage.show();
    }
}
