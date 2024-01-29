package com.sample.hotel.screen.roomreservation;

import com.sample.hotel.entity.Client;
import com.sample.hotel.entity.RoomReservation;
import io.jmix.core.DataManager;
import io.jmix.ui.Dialogs;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UiController("ReservedRooms")
@UiDescriptor("reserved-rooms.xml")
@LookupComponent("roomReservationsTable")
public class ReservedRoomsScreen extends StandardLookup<RoomReservation> {
    @Autowired
    private GroupTable<RoomReservation> roomReservationsTable;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private DataManager dataManager;

    @Subscribe("roomReservationsTable.viewClientEmail")
    public void onRoomReservationsTableViewClientEmail(Action.ActionPerformedEvent event) {
        RoomReservation reservation = roomReservationsTable.getSingleSelected();
        if (reservation == null) {
            return;
        }
        Client client = reservation.getBooking().getClient();

        List<String> emails = dataManager
                .loadValue("select c.email from Client c where c.id = :clientId", String.class)
                .parameter("clientId", client.getId())
                .list();

        String email = emails.isEmpty() ? "Not set" : emails.get(0);

        dialogs.createMessageDialog()
                .withCaption("Client email")
                .withMessage(email)
                .show();
    }
}