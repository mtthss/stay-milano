package com.staymilano.wifidirect;

import com.staymilano.wifidirect.DeviceDetailFragment;
import com.staymilano.wifidirect.DeviceListFragment;
import com.staymilano.R;
import com.staymilano.WiFiActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private Channel mChannel;
    private WiFiActivity mActivity;

    
    
    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, WiFiActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	/* THIS METHOD IS CALLED WHEN AN INTENT MATCHES THE INTENT FILTER ASSOCIATED TO this IN THE WI-FI-ACTIVITY */
    	String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
        	/* updates the variable indicating whether the wi-fi is enabled or not */
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            mActivity.setIsWifiP2pEnabled(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED);
            
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // request available peers from the wifi p2p manager. This is an asynchronous call 
        	// the calling activity is notified with a callback of the method onPeersAvailable() on DeviceListFragment 
            if (mManager != null) {
                mManager.requestPeers(mChannel, (PeerListListener) mActivity.getFragmentManager().findFragmentById(R.id.frag_list));
            }
            
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if (mManager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                // we are connected with the other device, request connection
                // info to find group owner IP
                DeviceDetailFragment fragment = (DeviceDetailFragment) mActivity.getFragmentManager().findFragmentById(R.id.frag_detail);
                mManager.requestConnectionInfo(mChannel, fragment);
                
            } else {
                // It's a disconnect
                mActivity.resetData();
            }
        	
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            DeviceListFragment fragment = (DeviceListFragment) mActivity.getFragmentManager().findFragmentById(R.id.frag_list);
            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
            
        }
    }
   
    
    
}