package com.sample.hotel.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@JmixEntity
@Table(name = "BOOKING", indexes = {
        @Index(name = "IDX_BOOKING_CLIENT", columnList = "CLIENT_ID")
})
@Entity
public class Booking {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinColumn(name = "CLIENT_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Client client;

    @NotNull
    @Column(name = "NUMBER_OF_GUESTS", nullable = false)
    private Integer numberOfGuests;

    @Column(name = "ARRIVAL_DATE", nullable = false)
    @NotNull
    private LocalDate arrivalDate;

    @Column(name = "NIGHTS_OF_STAY", nullable = false)
    @NotNull
    private Integer nightsOfStay;

    @Column(name = "DEPARTURE_DATE")
    private LocalDate departureDate;

    @Column(name = "STATUS", nullable = false)
    @NotNull
    private String status;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "booking")
    private RoomReservation roomReservation;

    @JmixProperty
    @DependsOnProperties({"arrivalDate"})
    public Long getCountdownDays() {
        if (arrivalDate == null) {
            return 0L;
        }
        if (arrivalDate.isBefore(LocalDate.now())) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), arrivalDate);
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public RoomReservation getRoomReservation() {
        return roomReservation;
    }

    public void setRoomReservation(RoomReservation roomReservation) {
        this.roomReservation = roomReservation;
    }

    public BookingStatus getStatus() {
        return status == null ? null : BookingStatus.fromId(status);
    }

    public void setStatus(BookingStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public Integer getNightsOfStay() {
        return nightsOfStay;
    }

    public void setNightsOfStay(Integer nightsOfStay) {
        this.nightsOfStay = nightsOfStay;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}