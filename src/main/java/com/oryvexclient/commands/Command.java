package com.oryvexclient.commands;

import com.oryvexclient.commands.CommandManager;

public abstract class Command {
    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void execute(String[] args);
    
    protected void sendMessage(String msg) {
        // Forward to command manager to handle chat sending
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
}
