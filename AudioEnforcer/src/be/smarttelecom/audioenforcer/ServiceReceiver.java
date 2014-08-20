package be.smarttelecom.audioenforcer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class ServiceReceiver extends BroadcastReceiver {
    TelephonyManager telephony;
    AEPhoneStateListener _phoneListener;

    public void onReceive(Context context, Intent intent) {
        _phoneListener = new AEPhoneStateListener(context);
        telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(_phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void onDestroy() {
        telephony.listen(_phoneListener, PhoneStateListener.LISTEN_NONE);
    }

}