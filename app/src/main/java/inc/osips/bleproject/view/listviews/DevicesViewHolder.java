package inc.osips.bleproject.view.listviews;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import inc.osips.bleproject.R;
import inc.osips.bleproject.interfaces.OnDiscoveredDevicesClickListener;
import inc.osips.bleproject.model.Devices;
import inc.osips.bleproject.model.utilities.GeneralUtil;

public class DevicesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private View layout;
    private TextView deviceName, deviceAddress;
    private OnDiscoveredDevicesClickListener listener;
    private List<Devices> devices;

    public DevicesViewHolder(@NonNull View itemView) {
        super(itemView);
        initialiseWidgets(itemView);
    }

    public void setItems(List<Devices> discoveredDevices){
        this.devices = discoveredDevices;
        deviceName.setText(discoveredDevices.get(getAdapterPosition()).getDeviceName());
        deviceAddress.setText(discoveredDevices.get(getAdapterPosition()).getDeviceAddress());
    }

    public void setListener(OnDiscoveredDevicesClickListener listener) {
        this.listener = listener;
    }

    private void initialiseWidgets(View v){
        layout =  v.findViewById(R.id.layoutItem);
        layout.setOnClickListener(this);
        deviceName = v.findViewById(R.id.textDeviceName);
        deviceAddress = v.findViewById(R.id.textDeviceAddress);
    }

    @Override
    public void onClick(View view) {
        if (getAdapterPosition()< devices.size())
            listener.selectDeviceToConnectTo(devices.get(getAdapterPosition()));
        deviceName.setTextColor(Color.WHITE);

        GeneralUtil.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                deviceName.setTextColor(ContextCompat.getColor(listener.getListenerContext(), R.color.title_color));
            }
        }, 200);
    }
}