
# Bright Drop EP1 New Product


## Assumptions
GATT compliant device
Android device is the GATT client
The Android devices will have Bluetooth enabled
The Android’s app will have the correct manifest data
There will be encryption for the data being transmitted to and from the device
The EP1’s IoT device will be the GATT server
The EP1’s GATT server will have a Services for:
1. GPS data Characteristic
    1. Coordinate data will read only
2. Lock/Unlock Service
    1. Characteristic for Unlock
    2. Characteristic for Lock
    3. Will allow for data to be sent as well as read


## Design / UML
EP1ScanActivity
________________
BluetoothLeScanner
BluetoothAdapter
________________
scanLeDevice()
scanCallBack()
----------------

EP1ControlActivity                         
__________________    
BluetoothLEService
serviceConnection
gattUpdateReceiver
displayGattServices      
__________________                      
displayGattServices()
        |                           
        |    
        |                                
        |
        V
BluetoothLEService
___________________
BluetoothManager
BluetoothAdapter
________________
initialize()
broadcastUpdate()
close()


LockControlActivity  || GPSLocationActivity
gattConnection           gattConnection
___________________     ____________________        
sendLock()              viewLocation()
sendUnlock()
