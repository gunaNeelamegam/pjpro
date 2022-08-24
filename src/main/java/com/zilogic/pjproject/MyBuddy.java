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
  public BuddyConfig cfg;
  
  MyBuddy(BuddyConfig paramBuddyConfig) {
    this.cfg = paramBuddyConfig;
  }
  
  String getStatusText() {
    BuddyInfo buddyInfo;
    try {
      buddyInfo = getInfo();
    } catch (Exception exception) {
      return "?";
    } 
    String str = "";
    if (buddyInfo.getSubState() == 4)
      if (buddyInfo.getPresStatus().getStatus() == 1) {
        str = buddyInfo.getPresStatus().getStatusText();
        if (str == null || str.length() == 0)
          str = "Online"; 
      } else if (buddyInfo.getPresStatus().getStatus() == 2) {
        str = "Offline";
      } else {
        str = "Unknown";
      }  
    return str;
  }
  
  public void onBuddyState() {
    MyApp.observer.notifyBuddyState(this);
  }
}
