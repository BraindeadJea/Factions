package com.itndev.factions.Faction;

import com.itndev.factions.Package.WrappedInfo;

import java.util.UUID;

public class FactionUUID {

    String STORAGE_FactionUUID = null;
    Long STORAGE_LONG = null;
    UUID STORAGE_UUID = null;

    public FactionUUID(String factionUUID) {
        STORAGE_FactionUUID = factionUUID;
    }

    public String toString() {
        return STORAGE_FactionUUID;
    }

    public Faction toFaction() {
        return new Faction(STORAGE_FactionUUID);
    }

    public WrappedInfo toSplittedInfo() {
        return new WrappedInfo(getLong(), getUUID());
    }

    public UUID getUUID() {
        STORAGE_UUID = UUID.fromString(STORAGE_FactionUUID.split("=")[1]);
        return STORAGE_UUID;
    }

    public Long getLong() {
        STORAGE_LONG = Long.parseLong(STORAGE_FactionUUID.split("=")[0]);
        return STORAGE_LONG;
    }
}
