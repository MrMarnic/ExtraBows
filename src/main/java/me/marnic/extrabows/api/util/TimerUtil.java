package me.marnic.extrabows.api.util;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Copyright (c) 01.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class TimerUtil {

    private static final LinkedList<TimeCommand> commands = new LinkedList<>();
    public static int currentTicks;

    static ArrayList<TimeCommand> toAdd = new ArrayList<>();
    static ArrayList<TimeCommand> toRemove = new ArrayList<>();

    public static void handleTickEvent(TickEvent.ServerTickEvent e) {

        if (!toAdd.isEmpty()) {
            commands.addAll(toAdd);
            toAdd.clear();
        }

        if (!toRemove.isEmpty()) {
            commands.removeAll(toRemove);
            toRemove.clear();
        }

        if (!commands.isEmpty()) {
            for (TimeCommand t : commands) {
                if (t.handle(currentTicks)) {
                    toRemove.add(t);
                }
            }
        }

        ++currentTicks;
    }

    public static TimeCommand forId(int id) {
        for (TimeCommand command : commands) {
            if (command.getId() == id) {
                return command;
            }
        }

        return null;
    }

    public static void addTimeCommand(TimeCommand command) {
        toAdd.add(command);
    }
}

