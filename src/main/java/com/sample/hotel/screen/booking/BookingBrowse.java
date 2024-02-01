package com.sample.hotel.screen.booking;

import com.sample.hotel.app.BookingService;
import com.sample.hotel.entity.Booking;
import com.sample.hotel.entity.BookingStatus;
import io.jmix.core.DataManager;
import io.jmix.ui.Dialogs;
import io.jmix.ui.action.Action;
import io.jmix.ui.action.DialogAction;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Booking.browse")
@UiDescriptor("booking-browse.xml")
@LookupComponent("bookingsTable")
public class BookingBrowse extends StandardLookup<Booking> {
    @Autowired
    private Table<Booking> bookingsTable;
    @Autowired
    private CollectionContainer<Booking> bookingsDc;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private BookingService bookingService;

    @Install(to = "bookingsTable.cancel", subject = "enabledRule")
    private boolean bookingsTableCancelEnabledRule() {
        Booking booking = bookingsTable.getSingleSelected();
        return booking != null && booking.getStatus() == BookingStatus.BOOKED;
    }

    @Subscribe("bookingsTable.cancel")
    public void onBookingsTableCancel(Action.ActionPerformedEvent event) {
        Booking booking = bookingsTable.getSingleSelected();
        if (booking == null) {
            return;
        }

        dialogs.createOptionDialog()
                .withCaption("Please confirm")
                .withMessage("The booking will be cancelled.")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY)
                                .withHandler(e -> cancelBooking(booking)),
                        new DialogAction(DialogAction.Type.NO)
                )
                .show();
    }

    private void cancelBooking(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = dataManager.save(booking);
        bookingsDc.replaceItem(updatedBooking);
    }
}