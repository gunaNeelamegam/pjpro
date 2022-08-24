/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.zilogic.pjproject;

/**
 *
 * @author user
 */
interface MyAppObserver {

    void notifyRegState(int paramInt, String paramString, long paramLong);

    void notifyIncomingCall(MyCall paramMyCall);

    void notifyCallState(MyCall paramMyCall);

    void notifyCallMediaState(MyCall paramMyCall);

    void notifyBuddyState(MyBuddy paramMyBuddy);

    void notifyChangeNetwork();
}
