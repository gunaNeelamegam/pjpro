/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zilogic.pjproject;

/**
 *
 * @author user
 */
class AddBuddy {

    public AddBuddy(String buddyUserName) {
        this.buddyUserName = buddyUserName;
    }

    @Override
    public String toString() {
        return "AddBuddy{" + "buddyUserName=" + buddyUserName + '}';
    }

    public String getBuddyUserName() {
        return buddyUserName;
    }

    public void setBuddyUserName(String buddyUserName) {
        this.buddyUserName = buddyUserName;
    }

    String buddyUserName;
}
