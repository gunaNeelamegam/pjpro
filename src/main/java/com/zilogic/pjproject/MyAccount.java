package com.zilogic.pjproject;

/**
 *
 * @author user
 */
import static com.zilogic.pjproject.AddAccountController.exitRegThread;
import java.util.ArrayList;
import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.pjsip_status_code;

class MyAccount extends Account {

    static int INCOMINGCALL=0;
    int i = 0;
    static MyCall currentCall;
    public ArrayList<MyBuddy> buddyList = new ArrayList<>();

    public AccountConfig cfg;
    static int REGISTERSTATE = 0;

    public MyAccount() {
    }

    MyAccount(AccountConfig paramAccountConfig) {
        this.cfg = paramAccountConfig;
    }

    public MyBuddy addBuddy(BuddyConfig paramBuddyConfig) {
        MyBuddy myBuddy = new MyBuddy(paramBuddyConfig);
        try {
            myBuddy.create(this, paramBuddyConfig);
        } catch (Exception exception) {
            myBuddy.delete();
            myBuddy = null;
        }
        if (myBuddy != null) {
            this.buddyList.add(myBuddy);
            if (paramBuddyConfig.getSubscribe())
        try {
                myBuddy.subscribePresence(true);
            } catch (Exception exception) {
            }
        }
        return myBuddy;
    }

    public void delBuddy(MyBuddy paramMyBuddy) {
        this.buddyList.remove(paramMyBuddy);
        paramMyBuddy.delete();
    }

    public void delBuddy(int paramInt) {
        MyBuddy myBuddy = this.buddyList.get(paramInt);
        this.buddyList.remove(paramInt);
        myBuddy.delete();
    }

    public synchronized void onRegState(OnRegStateParam paramOnRegStateParam) {
        System.out.println(paramOnRegStateParam.getStatus());
        if (paramOnRegStateParam.getStatus() == pjsip_status_code.PJSIP_SC_OK) {
            REGISTERSTATE = 1;
            MyApp.observer.notifyRegState(paramOnRegStateParam.getCode(), paramOnRegStateParam.getReason(), paramOnRegStateParam
                    .getExpiration());

        } else {
            REGISTERSTATE = -1;
        }
    }

    boolean exitIncomingCall = false;

    public synchronized void onIncomingCall(OnIncomingCallParam paramOnIncomingCallParam) {
        System.out.println(" INCOMING CALL");
        INCOMINGCALL = 1;
        System.out.print(Thread.currentThread().getName());
        try {
            currentCall = new MyCall(this, paramOnIncomingCallParam.getCallId());
            System.out.println("Call id :" + paramOnIncomingCallParam.getCallId() + "" + paramOnIncomingCallParam.getRdata().getWholeMsg());
            CallOpParam prm = new CallOpParam(true);
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_RINGING);
            currentCall.answer(prm);
        } catch (Exception ex) {
        }
    }

    public synchronized void onInstantMessage(OnInstantMessageParam paramOnInstantMessageParam) {
        System.out.println("======== Incoming pager ======== ");
        System.out.println("From     : " + paramOnInstantMessageParam.getFromUri());
        System.out.println("To       : " + paramOnInstantMessageParam.getToUri());
        System.out.println("Contact  : " + paramOnInstantMessageParam.getContactUri());
        System.out.println("Mimetype : " + paramOnInstantMessageParam.getContentType());
        System.out.println("Body     : " + paramOnInstantMessageParam.getMsgBody());
    }

}
