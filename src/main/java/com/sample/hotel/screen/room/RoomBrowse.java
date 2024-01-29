package com.sample.hotel.screen.room;

import io.jmix.ui.screen.*;
import com.sample.hotel.entity.Room;

@UiController("Room.browse")
@UiDescriptor("room-browse.xml")
@LookupComponent("roomsTable")
public class RoomBrowse extends StandardLookup<Room> {
}