package inc.osips.bleproject.interfaces;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;

public interface PresenterInterface {

    Activity getScanningActivity();
    void registerBroadCastReceiver(Activity activity);
    void unregisterBroadCastReceiver(Activity activity);
}
