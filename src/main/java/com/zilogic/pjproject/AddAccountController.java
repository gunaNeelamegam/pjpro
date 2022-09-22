package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zilogic.pjproject.utils.MongoDb;
import com.zilogic.pjproject.utils.PropertyFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;

public class AddAccountController implements Initializable {

    private PropertyFile propertyFile = new PropertyFile();
    private final MongoClient client = new MongoDb().getClient();
    private MongoDatabase database = null;
    private MongoCollection collection = null;
    private MongoDb mongodb = new MongoDb();
    String domainAddress;
    String username;
    String password;
    private MainStageController mainstage = new MainStageController();
    @FXML
    private JFXButton closebtn;
    @FXML
    private JFXButton saveBtn;
    @FXML
    TextField domain;
    @FXML
    TextField user;
    @FXML
    PasswordField pass;
    Thread accountRegThread;
    static boolean exitRegThread = false;
    String callStatus;
    public static ArrayList<AddAccount> userdetails = new ArrayList<AddAccount>();
    static ObservableList<AddAccount> observer_account = FXCollections.observableArrayList();

    Task<Void> task;

    private void AddingDocs() {
        try {
            Document account = new Document();
            account.append(" domain", domain.getText());
            account.append("user", user.getText());
            account.append("password", pass.getText());
            this.database = this.mongodb.establishConnection("pjsua2");
            this.collection = this.database.getCollection("Account");
            this.collection.insertOne(account);
            System.out.println("Document added  into Collection");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void save() throws Exception {
        if (!(domain.getText().equals("") || user.getText().equals("") || pass.getText().equals(""))) {
            MyApp.accList.clear();
            var addaccount = new AddAccount();
            domainAddress = domain.getText();
            username = user.getText();
            password = pass.getText();
            addaccount.setUsername(username);
            addaccount.setDomainAddress(domainAddress);
            addaccount.setPassword(password);
            AddingDocs();
            userdetails.add(addaccount);
            observer_account.add(addaccount);
            this.AddingDocs();
            task = new Task() {
                @Override
                protected Void call() throws Exception {
                    Platform.runLater(() -> {
                        try {
                            System.out.println("Account ");
                            MyAccount account = MainStageController.createAccount();
                            if (account.getId() != -1) {
                                MainStageController.addAccountStage.close();
                                task.cancel();
                            }
                        } catch (Exception e) {
                        }
                    });
                    return null;
                }
            };
            Thread t1 = new Thread(task);
            t1.start();
        }
    }

    @FXML
    public void close() {
        MainStageController.addAccountStage.close();
        MainStageController.registerThread.stop();
        System.out.println(" add Account close SuccessFully");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainstage.registerAlert();
        MainStageController.registerThread.start();
    }

}

//    @FXML
//    public synchronized void save() throws IOException, Exception {
//        MyApp.accList.clear();
//        System.out.println("Thread name : " + Thread.currentThread().getName());
//        var addaccount = new AddAccount();
//        domainAddress = domain.getText();
//        username = user.getText();
//        password = pass.getText();
//        addaccount.setUsername(username);
//        addaccount.setDomainAddress(domainAddress);
//        addaccount.setPassword(password);
//        userdetails.add(addaccount);
//        accountRegThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                var updater = new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            MainStageController.createAccount();
//                            MyApp.ep.libHandleEvents(10L);
//                            if (MainStageController.account.getId() != -1) {
//                                if (MainStageController.account.getInfo().getRegStatusText().equalsIgnoreCase("OK"));
//                                MainStageController.addAccountStage.close();
//                                exitRegThread = true;
//                            }
//                        } catch (Exception ex) {
//                            System.err.println("Exception while Runlater Method....");
//                        }
//                    }
//                };
//                while (!exitRegThread) {
//                    try {
//                        Thread.sleep(10);
//                    } catch (Exception e) {
//                        System.out.println(e);
//                    }
//                    Platform.runLater(updater);
//                }
//            }
//        });
//        accountRegThread.setName("AccountReg");
//        accountRegThread.start();
//    }
