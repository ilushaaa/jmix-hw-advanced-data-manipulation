package com.sample.hotel.screen.booking;

import com.sample.hotel.app.BookingService;
import com.sample.hotel.entity.Room;
import com.sample.hotel.entity.RoomReservation;
import io.jmix.ui.Dialogs;
import io.jmix.ui.Notifications;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.Action;
import io.jmix.ui.action.DialogAction;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.LinkButton;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.sample.hotel.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Booking.roomAssignment")
@UiDescriptor("booking-room-assignment.xml")
@LookupComponent("bookingsTable")
public class BookingRoomAssignment extends StandardLookup<Booking> {
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private Table<Booking> bookingsTable;
    @Autowired
    private CollectionContainer<Booking> bookingsDc;
    @Autowired
    private CollectionContainer<Room> roomDc;
    @Autowired
    private Table<Room> roomTable;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private Notifications notifications;

    @Install(to = "roomTable.assign", subject = "columnGenerator")
    private Component roomsTableAssignColumnGenerator(Room room) {
        Booking booking = bookingsTable.getSingleSelected();
        if (booking == null) {
            return null;
        }

        if (!bookingService.isSuitable(booking, room)) {
            return new Table.PlainTextCell("-");
        }

        LinkButton link = uiComponents.create(LinkButton.class);
        link.setCaption("Assign");
        link.addClickListener(e -> {
            confirmReserve(room, booking);
        });
        return link;
    }

    private void confirmReserve(Room room, Booking booking) {
        dialogs.createOptionDialog()
                .withCaption("Please confirm")
                .withMessage("Reserve room #" + room.getNumber() + " to the booking?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY)
                                .withHandler(e -> {
                                    doReserveBooking(room, booking);
                                }),
                        new DialogAction(DialogAction.Type.NO)
                )
                .show();
    }

    private void doReserveBooking(Room room, Booking booking) {
        RoomReservation result = bookingService.reserveRoom(booking, room);
        if (result == null) {
            notifications.create(Notifications.NotificationType.ERROR)
                    .withCaption("Reserve failed")
                    .show();
            return;
        }
        roomDc.getMutableItems().remove(room);
        bookingsDc.getMutableItems().remove(booking);
    }

    @Subscribe(id = "bookingsDc", target = Target.DATA_CONTAINER)
    public void onBookingsDcItemChange(InstanceContainer.ItemChangeEvent<Booking> event) {
        roomTable.repaint();
    }
}