package com.sample.hotel.listener;

import com.sample.hotel.entity.Booking;
import io.jmix.core.event.EntitySavingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookingEventListener {

    @EventListener
    public void onBookingSaving(final EntitySavingEvent<Booking> event) {
        Booking editedEntity = event.getEntity();
        LocalDate departureDate = editedEntity.getArrivalDate().plusDays(editedEntity.getNightsOfStay());
        editedEntity.setDepartureDate(departureDate);
    }
}