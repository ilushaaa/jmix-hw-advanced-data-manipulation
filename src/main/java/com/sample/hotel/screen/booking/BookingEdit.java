package com.sample.hotel.screen.booking;

import com.sample.hotel.entity.BookingStatus;
import io.jmix.ui.screen.*;
import com.sample.hotel.entity.Booking;

import java.time.LocalDate;

@UiController("Booking.edit")
@UiDescriptor("booking-edit.xml")
@EditedEntityContainer("bookingDc")
public class BookingEdit extends StandardEditor<Booking> {

    @Subscribe
    public void onInitEntity(InitEntityEvent<Booking> event) {
        event.getEntity().setStatus(BookingStatus.BOOKED);
    }
}