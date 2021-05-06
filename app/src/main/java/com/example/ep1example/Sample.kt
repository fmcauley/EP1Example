package com.example.ep1example

/*
How to connect to the Gatt server

var bluetoothGatt: BluetoothGatt? = null
bluetoothGatt = device.connectGatt(this, false, gattCallback)

How to ask for notifications when there is a change on the device

lateinit var bluetoothGatt: BluetoothGatt
lateinit var characteristic: BluetoothGattCharacteristic
var enabled: Boolean = true
...
bluetoothGatt.setCharacteristicNotification(characteristic, enabled)
val uuid: UUID = UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)
val descriptor = characteristic.getDescriptor(uuid).apply {
    value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
}
bluetoothGatt.writeDescriptor(descriptor)


 */