package fr.epita.assistants.item_producer.domain.entity;

import lombok.Value;

import java.util.ArrayList;

@Value
public class GameEntity {
    ArrayList<ArrayList<String>> map;
}
