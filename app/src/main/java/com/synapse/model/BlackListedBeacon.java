package com.synapse.model;

public class BlackListedBeacon {
    String BeaconName;
    String BeaconAddress;
    String BeaconUUID;
    String BeaconType;
    String Id;
    public BlackListedBeacon() {
    }

    public BlackListedBeacon(String beaconName, String beaconAddress, String beaconUUID, String beaconType, String id) {
        BeaconName = beaconName;
        BeaconAddress = beaconAddress;
        BeaconUUID = beaconUUID;
        BeaconType = beaconType;
        Id = id;
    }


    public String getBeaconName() {
        return BeaconName;
    }

    public void setBeaconName(String beaconName) {
        BeaconName = beaconName;
    }

    public String getBeaconAddress() {
        return BeaconAddress;
    }

    public void setBeaconAddress(String beaconAddress) {
        BeaconAddress = beaconAddress;
    }

    public String getBeaconUUID() {
        return BeaconUUID;
    }

    public void setBeaconUUID(String beaconUUID) {
        BeaconUUID = beaconUUID;
    }

    public String getBeaconType() {
        return BeaconType;
    }

    public void setBeaconType(String beaconType) {
        BeaconType = beaconType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
