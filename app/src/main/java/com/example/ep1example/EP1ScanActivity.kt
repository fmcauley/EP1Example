package com.example.ep1example

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EP1ScanActivity : AppCompatActivity() {

    private val handler = Handler(Looper.myLooper()!!)

    // should have some sort of activity indicator when searching...
    private var scanning = false
    lateinit var bluetoothLeScanner: BluetoothLeScanner
    lateinit var bluetoothAdapter: BluetoothAdapter
    private val REQUEST_ENABLE_BT = 1
    private val SCAN_PERIOD: Long = 10000
    lateinit var scanButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager?.adapter!!

        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner!!

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
            finish()
        }

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }



        scanButton = findViewById(R.id.startScan)

        scanButton.setOnClickListener {
            scanLeDevice()
        }
    }

    override fun onResume() {
        super.onResume()

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(
                enableBtIntent,
                REQUEST_ENABLE_BT
            )
        }
    }

    // have the button call this function from action
    private fun scanLeDevice() {
        bluetoothLeScanner.let { scanner ->
            if (!scanning) {
                handler.postDelayed({
                    scanning = false
                    scanner.stopScan(scanCallback)
                }, SCAN_PERIOD)
                scanning = true
                scanner.startScan(scanCallback)
            } else {
                scanning = false
                scanner.stopScan(scanCallback)
            }
        }
    }


    // Device scan callback.
    private val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            //UI Updates
            // Notifications
        }
    }

}