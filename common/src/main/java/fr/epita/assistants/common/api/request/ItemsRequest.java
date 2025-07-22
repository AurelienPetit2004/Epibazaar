package fr.epita.assistants.common.api.request;

import lombok.Value;

import java.util.ArrayList;

@Value
public class ItemsRequest {
    ArrayList<ItemRequest> itemsRequest;
}
