package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AddAccountController {
    
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
    
    @FXML
    void save() throws Exception {
        MyApp.accList.clear();
        System.out.println("Thread name : " + Thread.currentThread().getName());
        var addaccount = new AddAccount();
        domainAddress = domain.getText();
        username = user.getText();
        password = pass.getText();
        addaccount.setUsername(username);
        addaccount.setDomainAddress(domainAddress);
        addaccount.setPassword(password);
        userdetails.add(addaccount);
        observer_account.add(addaccount);
        task = new Task() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    try {
                        System.out.println("Account ");
                        MyAccount account = MainStageController.createAccount();
                        MyApp.ep.libHandleEvents(10L);
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
    
    @FXML
    public void close() {
        MainStageController.addAccountStage.close();
        System.out.println(" add Account close SuccessFully");
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
