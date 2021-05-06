package com.example.ep1example

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class EP1ControlActivity : AppCompatActivity() {

    private val TAG: String = EP1ControlActivity::class.java.name

    private var bluetoothService: BluetoothLEService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ep1_control_activity)

        val gattServiceIntent = Intent(this, BluetoothLEService::class.java)
        bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    // Code to manage Service lifecycle.
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            componentName: ComponentName,
            service: IBinder
        ) {
            bluetoothService = (service as BluetoothLEService.LocalBinder).getService()
            bluetoothService?.let { bluetooth ->
                if (!bluetooth.initialize()) {
                    Log.e(TAG, "Unable to initialize Bluetooth")
                    finish()
                } else {
                    // perform device connection
                }
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            bluetoothService = null
        }
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device. This can be a
    // result of read or notification operations.
    private val gattUpdateReceiver = object : BroadcastReceiver() {

        private lateinit var bluetoothLeService: BluetoothLEService

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when (action) {
                ACTION_GATT_CONNECTED -> {
                    connected = true
                    updateConnectionState(R.string.connected)
                    (context as? Activity).invalidateOptionsMenu()
                }
                ACTION_GATT_DISCONNECTED -> {
                    connected = false
                    updateConnectionState(R.string.disconnected)
                    (context as? Activity).invalidateOptionsMenu()
                    clearUI()
                }
                ACTION_GATT_SERVICES_DISCOVERED -> {
                    // Show all the supported services and characteristics on the
                    // user interface.
                    displayGattServices(bluetoothLeService.getSupportedGattServices())
                }
                ACTION_DATA_AVAILABLE -> {
                    displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA))
                }
            }
        }
    }

    // Demonstrates how to iterate through the supported GATT
    // Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the
    // ExpandableListView on the UI.
    private fun displayGattServices(gattServices: List<BluetoothGattService>?) {
        if (gattServices == null) return
        var uuid: String?
        val unknownServiceString: String = resources.getString(R.string.unknown_service)
        val unknownCharaString: String = resources.getString(R.string.unknown_characteristic)
        val gattServiceData: MutableList<HashMap<String, String>> = mutableListOf()
        val gattCharacteristicData: MutableList<ArrayList<HashMap<String, String>>> =
            mutableListOf()
        mGattCharacteristics = mutableListOf()

        // Loops through available GATT Services.
        gattServices.forEach { gattService ->
            val currentServiceData = HashMap<String, String>()
            uuid = gattService.uuid.toString()
            currentServiceData[LIST_NAME] = SampleGattAttributes.lookup(uuid, unknownServiceString)
            currentServiceData[LIST_UUID] = uuid.toString()
            gattServiceData += currentServiceData

            val gattCharacteristicGroupData: ArrayList<HashMap<String, String>> = arrayListOf()
            val gattCharacteristics = gattService.characteristics
            val charas: MutableList<BluetoothGattCharacteristic> = mutableListOf()

            // Loops through available Characteristics.
            gattCharacteristics.forEach { gattCharacteristic ->
                charas += gattCharacteristic
                val currentCharaData: HashMap<String, String> = hashMapOf()
                uuid = gattCharacteristic.uuid.toString()
                currentCharaData[LIST_NAME] =
                    SampleEP1GattAttributes.lookup(uuid, unknownCharaString)
                currentCharaData[LIST_UUID] = uuid.toString()
                gattCharacteristicGroupData += currentCharaData
            }
            mGattCharacteristics += charas
            gattCharacteristicData += gattCharacteristicGroupData
        }
    }
}
