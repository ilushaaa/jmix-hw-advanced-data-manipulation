package com.sample.hotel.screen.room;

import io.jmix.ui.screen.*;
import com.sample.hotel.entity.Room;

@UiController("Room.edit")
@UiDescriptor("room-edit.xml")
@EditedEntityContainer("roomDc")
public class RoomEdit extends StandardEditor<Room> {
}