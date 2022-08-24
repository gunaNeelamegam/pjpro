/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zilogic.pjproject;

/**
 *
 * @author user
 */
import java.util.ArrayList;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.ContainerNode;

class MyAccountConfig {

    public AccountConfig accCfg = new AccountConfig();

    public ArrayList<BuddyConfig> buddyCfgs = new ArrayList<>();

    public void readObject(ContainerNode paramContainerNode) {
        try {
            ContainerNode containerNode1 = paramContainerNode.readContainer("Account");
            this.accCfg.readObject(containerNode1);
            ContainerNode containerNode2 = containerNode1.readArray("buddies");
            this.buddyCfgs.clear();
            while (containerNode2.hasUnread()) {
                BuddyConfig buddyConfig = new BuddyConfig();
                buddyConfig.readObject(containerNode2);
                this.buddyCfgs.add(buddyConfig);
            }
        } catch (Exception exception) {
        }
    }

    public void writeObject(ContainerNode paramContainerNode) {
        try {
            ContainerNode containerNode1 = paramContainerNode.writeNewContainer("Account");
            this.accCfg.writeObject(containerNode1);
            ContainerNode containerNode2 = containerNode1.writeNewArray("buddies");
            for (byte b = 0; b < this.buddyCfgs.size(); b++) {
                ((BuddyConfig) this.buddyCfgs.get(b)).writeObject(containerNode2);
            }
        } catch (Exception exception) {
        }
    }
}
