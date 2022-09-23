package com.zilogic.pjproject;

import com.zilogic.pjproject.utils.MongoDb;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.CodecInfo;
import org.pjsip.pjsua2.CodecInfoVector2;
import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.IpChangeParam;
import org.pjsip.pjsua2.JsonDocument;
import org.pjsip.pjsua2.LogConfig;
import org.pjsip.pjsua2.LogWriter;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.UaConfig;

class MyApp {

    final MongoDb mongodb = new MongoDb();

    public MongoDb getMongodb() {
        return mongodb;
    }

    static {
        System.loadLibrary("pjsua2");
        System.loadLibrary("openh264");
    }
    public static Endpoint ep = new Endpoint();

    public static MyAppObserver observer;

    public static ArrayList<MyAccount> accList = new ArrayList<>();

    public static ArrayList<MyAccountConfig> accCfgs = new ArrayList<>();

    EpConfig epConfig = new EpConfig();

    private TransportConfig sipTpConfig = new TransportConfig();

    private String appDir;

    private MyLogWriter logWriter;

    private final String configName = "pjsua2.json";

    // private final int SIP_PORT = 5080;
    private final int LOG_LEVEL = 4;

    MyAccount account;

    public MyAccount getDefaultAccount() {
        if (this.accList.size() != 0) {
            this.account = this.accList.get(0);
            return this.account;
        } else {
            return null;
        }
    }

    public void init(MyAppObserver paramMyAppObserver, String paramString) throws Exception {
        init(paramMyAppObserver, paramString, false);
    }

    public void init(MyAppObserver paramMyAppObserver, String paramString, boolean paramBoolean) throws Exception {
        observer = paramMyAppObserver;
        this.appDir = paramString;
        try {
            ep.libCreate();
        } catch (Exception exception) {
            return;
        }
        String str = this.appDir + "/" + "pjsua2.json";
        File file = new File(str);
        if (file.exists()) {
            loadConfig(str);
        } else {
            this.sipTpConfig.setPort(6000L);
        }
        this.epConfig.getLogConfig().setLevel(LOG_LEVEL);
        this.epConfig.getLogConfig().setConsoleLevel(LOG_LEVEL);
        LogConfig logConfig = this.epConfig.getLogConfig();
        this.logWriter = new MyLogWriter();
        logConfig.setWriter((LogWriter) this.logWriter);
        logConfig.setDecor(logConfig.getDecor() & 0xFFFFFFFFFFFFFE7FL);
        UaConfig uaConfig = this.epConfig.getUaConfig();
        uaConfig.setUserAgent("Guna Application " + ep.libVersion().getFull());
        if (paramBoolean) {
            uaConfig.setThreadCnt(1L);
            uaConfig.setMainThreadOnly(false);
        }
        try {
            ep.libInit(this.epConfig);
//            ep.audDevManager().setNullDev();

        } catch (Exception exception) {
            return;
        }
        try {
            CodecInfoVector2 codeinfo = ep.codecEnum2();
            for (CodecInfo c : codeinfo) {
                System.out.println(" code : " + c.getCodecId() + " " + c.getPriority());
            }
            ep.codecSetPriority("speex/32000/1", (short) 130);
            ep.transportCreate(1, this.sipTpConfig);
        } catch (Exception exception) {
            System.out.println(exception);
        }
        try {
            ep.transportCreate(2, this.sipTpConfig);
        } catch (Exception exception) {
            System.out.println(exception);
        }
        try {
            ep.transportCreate(3, this.sipTpConfig);
        } catch (Exception exception) {
            System.out.println(exception);
        }
        this.sipTpConfig.setPort(6000L);
        accCfgs.clear();
        accList.clear();
        for (int b = 0; b < accCfgs.size(); b++) {
            MyAccountConfig myAccountConfig = accCfgs.get(b);
            myAccountConfig.accCfg.getNatConfig().setIceEnabled(true);
            myAccountConfig.accCfg.getVideoConfig().setAutoTransmitOutgoing(true);
            myAccountConfig.accCfg.getVideoConfig().setAutoShowIncoming(true);
            myAccountConfig.accCfg.getMediaConfig().setSrtpUse(1);
            myAccountConfig.accCfg.getMediaConfig().setSrtpSecureSignaling(0);
            MyAccount myAccount = addAcc(myAccountConfig.accCfg);
            if (myAccount != null) {
                for (int b1 = 0; b1 < myAccountConfig.buddyCfgs.size(); b1++) {
                    BuddyConfig buddyConfig = myAccountConfig.buddyCfgs.get(b1);
                    myAccount.addBuddy(buddyConfig);
                }
            }
        }
        try {
            ep.libStart();
        } catch (Exception exception) {
            return;
        }
    }

    public MyAccount addAcc(AccountConfig paramAccountConfig) {
        MyAccount myAccount = new MyAccount(paramAccountConfig);
        try {
            myAccount.create(paramAccountConfig);
        } catch (Exception exception) {
            System.out.println(exception);
            myAccount = null;
            return null;
        }
        accList.add(myAccount);
        return myAccount;
    }

    public void delAcc(MyAccount paramMyAccount) {
        accList.remove(paramMyAccount);
    }

    private void loadConfig(String paramString) {
        JsonDocument jsonDocument = new JsonDocument();
        try {
            jsonDocument.loadFile(paramString);
            ContainerNode containerNode1 = jsonDocument.getRootContainer();
            this.epConfig.readObject(containerNode1);
            ContainerNode containerNode2 = containerNode1.readContainer("SipTransport");
            this.sipTpConfig.readObject(containerNode2);
            accCfgs.clear();
            ContainerNode containerNode3 = containerNode1.readArray("accounts");
            while (containerNode3.hasUnread()) {
                MyAccountConfig myAccountConfig = new MyAccountConfig();
                myAccountConfig.readObject(containerNode3);
                accCfgs.add(myAccountConfig);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
        jsonDocument.delete();
    }

    private void buildAccConfigs() {
        accCfgs.clear();
        for (byte b = 0; b < accList.size(); b++) {
            MyAccount myAccount = accList.get(b);
            MyAccountConfig myAccountConfig = new MyAccountConfig();
            myAccountConfig.accCfg = myAccount.cfg;
            myAccountConfig.buddyCfgs.clear();
            for (byte b1 = 0; b1 < myAccount.buddyList.size(); b1++) {
                MyBuddy myBuddy = myAccount.buddyList.get(b1);
                myAccountConfig.buddyCfgs.add(myBuddy.cfg);
            }
            accCfgs.add(myAccountConfig);
        }
    }

    void saveConfig(String paramString) {
        JsonDocument jsonDocument = new JsonDocument();
        try {
            jsonDocument.writeObject((PersistentObject) this.epConfig);
            ContainerNode containerNode1 = jsonDocument.writeNewContainer("SipTransport");
            this.sipTpConfig.writeObject(containerNode1);
            buildAccConfigs();
            ContainerNode containerNode2 = jsonDocument.writeNewArray("accounts");
            for (byte b = 0; b < accCfgs.size(); b++) {
                ((MyAccountConfig) accCfgs.get(b)).writeObject(containerNode2);
            }
            jsonDocument.saveFile(paramString);
        } catch (Exception exception) {
        }
        jsonDocument.delete();
    }

    public void handleNetworkChange() {
        try {
            System.out.println("Network change detected");
            IpChangeParam ipChangeParam = new IpChangeParam();
            ep.handleIpChange(ipChangeParam);
            new Alert(Alert.AlertType.ERROR, " Network error", ButtonType.CANCEL).show();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public void deinit() {
        String str = this.appDir + "/" + "pjsua2.json";
        saveConfig(str);
        Runtime.getRuntime().gc();
        try {
            ep.libDestroy();
        } catch (Exception exception) {
        }
        ep.delete();
        ep = null;
    }
}
