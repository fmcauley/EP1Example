package com.example.ep1example

import java.util.*

class SampleEP1GattAttributes {
    private var attributes: Any? = HashMap<Any?, Any?>()
    var EP1_PALLET_LOCATION_COORDINATES = "00002a37-0000-1000-8000-00805f9b34fb"
    var CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb"

    fun lookup(uuid: String?, defaultName: String?): String? {
        val name = attributes[uuid]
        return name ?: defaultName
    }

    companion object EP1SampleServices {
        //sample data
        attributes["0000180d-0000-1000-8000-00805f9b34fb"] = "EP1 Lcoation Service"
        attributes["0000180a-0000-1000-8000-00805f9b34fb"] = "Device Information Service"
        // Sample Characteristics.
        attributes[EP1_LOCATION_MEASUREMENT] = "EP1 Coordinates"
        attributes["00002a29-0000-1000-8000-00805f9b34fb"] = "Manufacturer Name String"
    }
}

private operator fun Any?.get(uuid: String?): String? {
    //stub
    return null
}
