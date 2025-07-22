package fr.epita.assistants.common.command;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class ResetInventoryCommand {
    public ResetInventoryCommand() {
    }
}