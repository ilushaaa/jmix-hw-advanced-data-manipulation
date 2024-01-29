package com.sample.hotel.entity;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum BookingStatus implements EnumClass<String> {

    BOOKED("B"),
    CANCELLED("C");

    private String id;

    BookingStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static BookingStatus fromId(String id) {
        for (BookingStatus at : BookingStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}