package com.zilogic.pjproject;

import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;

class MyObserver implements MyAppObserver {

    MainStageController mainCon = new MainStageController();
    protected static MyCall currentCall = null;

    private boolean del_call_scheduled = false;

    public void check_call_deletion() {
        System.out.println("Check call deletion : ");
        if (this.del_call_scheduled && currentCall != null) {
            currentCall.delete();
            currentCall = null;
            this.del_call_scheduled = false;
        }
    }

    @Override
    public void notifyRegState(int paramInt, String paramString, long paramLong) {

        System.out.println(" Registartion status : " + paramString + "Param status code : " + paramInt);

    }

    @Override
    public void notifyIncomingCall(MyCall paramMyCall) {
        System.out.println(" INCOMING CALL  in NOTIFY METHOD");
        try {
            CallOpParam callOpParam = new CallOpParam();
            try {
                currentCall = paramMyCall;
            } catch (Exception exception) {
                System.out.println(exception);
            }
        } catch (Exception ex) {
            System.err.println(" Exception while loading at IncomingCall ui");
        }

    }

    @Override
    public void notifyCallMediaState(MyCall paramMyCall) {
        System.out.println("MEDIA STATE : " + paramMyCall.toString());
    }

    @Override
    public void notifyCallState(MyCall paramMyCall) {

        CallInfo callInfo;
        if (currentCall == null || paramMyCall.getId() != currentCall.getId()) {
            return;
        }
        try {
            callInfo = paramMyCall.getInfo();
        } catch (Exception exception) {
            callInfo = null;
        }
        if (callInfo.getState() == 6) {
            System.out.println(" Call state :  " + callInfo.getStateText()); // Logger.getLogger(MyObserver.class.getName()).log(Level.SEVERE, null, ex);
            this.del_call_scheduled = true;
            paramMyCall.delete();
            paramMyCall = null;
          
        }

    }

    @Override
    public void notifyBuddyState(MyBuddy paramMyBuddy) {
    }

    @Override
    public void notifyChangeNetwork() {
    }
}
