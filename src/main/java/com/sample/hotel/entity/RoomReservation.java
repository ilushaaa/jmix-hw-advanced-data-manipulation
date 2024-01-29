package com.sample.hotel.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@JmixEntity
@Table(name = "ROOM_RESERVATION", indexes = {
        @Index(name = "IDX_ROOM_RESERVATION_BOOKING", columnList = "BOOKING_ID"),
        @Index(name = "IDX_ROOM_RESERVATION_ROOM", columnList = "ROOM_ID")
})
@Entity
public class RoomReservation {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BOOKING_ID", nullable = false)
    @NotNull
    private Booking booking;

    @JoinColumn(name = "ROOM_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Room room;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}