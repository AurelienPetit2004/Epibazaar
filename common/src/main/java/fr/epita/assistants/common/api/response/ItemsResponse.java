package fr.epita.assistants.common.api.response;

import lombok.Value;

import java.util.ArrayList;

@Value
public class ItemsResponse {
    ArrayList<ItemResponse> itemsResponse;
}
