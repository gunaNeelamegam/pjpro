/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zilogic.pjproject;

/**
 *
 * @author user
 */
import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.BuddyInfo;

class MyBuddy extends Buddy {

    int STATUS;

    public BuddyConfig cfg;

    public MyBuddy(BuddyConfig paramBuddyConfig) {
        super();
        this.cfg = paramBuddyConfig;
    }

    public MyBuddy() {
        super();
    }

    String getStatusText() {
        BuddyInfo buddyInfo;
        try {
            buddyInfo = getInfo();
        } catch (Exception exception) {
            return "?";
        }
        String str = "";
        if (buddyInfo.getSubState() == 4) {
            if (buddyInfo.getPresStatus().getStatus() == 1) {
                System.out.println("================================================================================================");
                System.out.println("presence Status : " + buddyInfo.getSubStateName() + buddyInfo.getUri() + buddyInfo.getPresStatus());
                System.out.println("===================================================================================================");
                str = buddyInfo.getPresStatus().getStatusText();
                if (str == null || str.length() == 0) {
                    str = "Online";
                }
            } else if (buddyInfo.getPresStatus().getStatus() == 2) {
                str = "Offline";
            } else {
                str = "Unknown";
            }
        }
        return str;
    }

    public void onBuddyState() {
        MyApp.observer.notifyBuddyState(this);
    }

}
