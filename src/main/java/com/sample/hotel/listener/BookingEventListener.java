package com.sample.hotel.listener;

import com.sample.hotel.app.BookingService;
import com.sample.hotel.entity.Booking;
import com.sample.hotel.entity.BookingStatus;
import io.jmix.core.DataManager;
import io.jmix.core.FluentLoader;
import io.jmix.core.event.EntityChangedEvent;
import io.jmix.core.event.EntitySavingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class BookingEventListener {

    private final BookingService bookingService;
    private final DataManager dataManager;

    public BookingEventListener(BookingService bookingService, DataManager dataManager) {
        this.bookingService = bookingService;
        this.dataManager = dataManager;
    }

    @EventListener
    public void onBookingSaving(final EntitySavingEvent<Booking> event) {
        Booking editedEntity = event.getEntity();
        LocalDate departureDate = editedEntity.getArrivalDate().plusDays(editedEntity.getNightsOfStay());
        editedEntity.setDepartureDate(departureDate);
    }

    @EventListener
    public void onBookingChangedBeforeCommit(final EntityChangedEvent<Booking> event) {
        if (event.getChanges().isChanged("status")) {
            Booking booking = dataManager.load(event.getEntityId()).one();
            if (BookingStatus.CANCELLED.equals(booking.getStatus())) {
                bookingService.cancelReservation(booking);
            }
        }
    }
}