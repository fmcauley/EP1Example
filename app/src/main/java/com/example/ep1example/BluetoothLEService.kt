package com.example.ep1example

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class BluetoothLEService : Service() {

    private val binder = LocalBinder()

    private var bluetoothManager: BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null

    private val TAG: String = BluetoothLEService::class.java.getName()

    fun initialize(): Boolean {
        // If bluetoothManager is null, try to set it
        if (bluetoothManager == null) {
            bluetoothManager = getSystemService(BluetoothManager::class.java)
            if (bluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.")
                return false
            }
        }
        // For API level 18 and higher, get a reference to BluetoothAdapter through
        // BluetoothManager.
        bluetoothManager?.let { manager ->
            bluetoothAdapter = manager.adapter
            if (bluetoothAdapter == null) {
                Log.e(TAG, "Unable to obtain a BluetoothAdapter.")
                return false
            }
            return true
        } ?: return false
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService() : BluetoothLEService {
            return this@BluetoothLEService
        }
    }

    private fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic) {
        val intent = Intent(action)

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is carried out as per profile specifications.

        //SAMPLE CODE
        when (characteristic.uuid) {
            EP1_PALLET_LOCATION_COORDINATES -> {
                val flag = characteristic.properties
                val format = when (flag and 0x01) {
                    0x01 -> {
                        Log.d(TAG, "EP1 location format UINT16.")
                        BluetoothGattCharacteristic.FORMAT_UINT16
                    }
                    else -> {
                        Log.d(TAG, "EP1 location format UINT8.")
                        BluetoothGattCharacteristic.FORMAT_UINT8
                    }
                }
                val ep1Coordinates = characteristic.getIntValue(format, 1)
                Log.d(TAG, String.format("Received EP1 coordinates: %d", ep1Coordinates))
                intent.putExtra(EXTRA_DATA, (ep1Coordinates).toString())
            }
            else -> {
                // For all other profiles, writes the data formatted in HEX.
                val data: ByteArray? = characteristic.value
                if (data?.isNotEmpty() == true) {
                    val hexString: String = data.joinToString(separator = " ") {
                        String.format("%02X", it)
                    }
                    intent.putExtra(EXTRA_DATA, "$data\n$hexString")
                }
            }
        }
        sendBroadcast(intent)
    }

    //make sure to close the connection
    fun close() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }
}
