package com.sample.hotel.screen.client;

import io.jmix.ui.screen.*;
import com.sample.hotel.entity.Client;

@UiController("Client.browse")
@UiDescriptor("client-browse.xml")
@LookupComponent("table")
public class ClientBrowse extends MasterDetailScreen<Client> {
}