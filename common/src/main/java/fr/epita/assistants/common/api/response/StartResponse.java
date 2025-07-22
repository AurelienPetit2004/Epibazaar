package fr.epita.assistants.common.api.response;

import lombok.Value;

import java.util.ArrayList;

@Value
public class StartResponse {
    ArrayList<ArrayList<String>> map;
}
