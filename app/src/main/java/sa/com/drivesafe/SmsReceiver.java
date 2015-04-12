package sa.com.drivesafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
    Boolean checkBoxChecked;
    @Override
    public void onReceive(Context context, Intent intent) {
        //---get the SMS message passed in---

        if(checkBoxChecked) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String messageReceived = "";
            if (bundle != null) {
                //---retrieve the SMS message received---
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    messageReceived += msgs[i].getMessageBody().toString();
                    messageReceived += "\n";
                }
                // Get the Sender Phone Number
                String senderPhoneNumber = msgs[0].getOriginatingAddress();
                sendBack(senderPhoneNumber, "I am currently driving..");
            }
        }
    }

    public void sendBack(String sender, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sender, null, msg, null, null);
    }

    public void setCheckBoxChecked(Boolean checked){
        checkBoxChecked = checked;
    }
}
