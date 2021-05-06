
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


## Design
When the app opens it will scan for near by devices
When a device(s) are found a list will be populated
When an item from the list is selected a new activity/fragment will open:
If Unlock/Lock is selected those features will be accessible 
Else if GPS is selected the coordinate data will be displayed 

