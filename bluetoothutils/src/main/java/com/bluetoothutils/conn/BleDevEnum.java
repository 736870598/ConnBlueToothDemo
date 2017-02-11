package com.bluetoothutils.conn;

/**
 * Created by sunxiaoyu on 2017/2/10.
 */

public enum BleDevEnum {

    HMSOTF("HMSoft", "0000ffe1-0000-1000-8000-00805f9b34fb", "0000ffe1-0000-1000-8000-00805f9b34fb");


    private String name;
    private String readUUID;
    private String writeUUID;

    BleDevEnum(String name, String readUUID, String writeUUID){
        this.name = name;
        this.readUUID = readUUID;
        this.writeUUID = writeUUID;
    }

    public String getName() {
        return name;
    }

    public String getReadUUID() {
        return readUUID;
    }

    public String getWriteUUID() {
        return writeUUID;
    }
}
