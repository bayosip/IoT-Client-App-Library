package inc.osips.bleproject.model.remote_comms.ble_comms;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import inc.osips.bleproject.interfaces.WirelessDeviceConnector;
import inc.osips.bleproject.model.remote_comms.DeviceConnectionFactory;
import inc.osips.bleproject.model.remote_comms.Util;
import inc.osips.bleproject.model.remote_comms.ble_comms.services.BleGattService;
import inc.osips.bleproject.utilities.GeneralUtil;
import inc.osips.bleproject.view.activities.Home;

public class BleConnection implements WirelessDeviceConnector {

    private volatile BleGattService gattService;
    private BluetoothDevice bleDevice;
    private Activity activity;
    private String baseUUID;

    private boolean mBound = false;
    private static final String TAG = "BLE Connection";

    public BleConnection(Activity activity, Parcelable bleDevice, String baseUUID) {
        Log.w("Connection+", baseUUID);
        this.baseUUID = baseUUID;
        this.bleDevice = (BluetoothDevice) bleDevice;
        this.activity = activity;
    }

    @Override
    public ServiceConnection getServiceConnection() {
        return mConnection;
    }

    @Override
    public void connectToDeviceWithDeviceInfoFrom(Intent intent) {

    }

    @Override
    public boolean isConnected() {
        return mBound;
    }

    //API 21 and Above
    private void ConnectToBleDevice(){
        if (makeConnectionBLE()) {
            return;
        } else {
            Util.message(activity,"Cannot Connect to Device");
            activity.sendBroadcast(new Intent(DeviceConnectionFactory.FAILED_DEVICE_CONNECTION));
        }
    }


    private boolean makeConnectionBLE() {

        if (gattService != null){
            if(gattService.init() && !TextUtils.isEmpty(baseUUID)){
                final boolean result = gattService.connect(bleDevice, baseUUID);
                return result;
            }
            return false;
        }
        else{
            Log.w(TAG, "no uuid");
            return false;
        }
    }

    @Override
    public void sendInstructionsToDevice(String instuctions) {
        gattService.writeLEDInstructions(instuctions);
    }

    private ServiceConnection mConnection =
            /*
             * Defines callbacks for service binding, passed to bindService()
             */
        new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName className,
                                               IBinder service) {
                    //Log.i(TAG, "binding services");
                    BleGattService.BTLeServiceBinder binder = (BleGattService.BTLeServiceBinder) service;
                    gattService = binder.getService();
                    mBound = true;
                    if (Build.VERSION.SDK_INT >= 21) {
                        ConnectToBleDevice();
                    } else {
                        Util.message(activity,"API too low for App!");
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                    mBound = false;
                    Log.i(TAG, "Service Disconnected");
                    activity.sendBroadcast(new Intent(DeviceConnectionFactory.DEVICE_CONNECTION_SERVICE_STOPPED));
                }
            };

}