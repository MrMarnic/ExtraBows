package me.marnic.extrabows.api.util;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Copyright (c) 01.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class TimerUtil {

    private static final LinkedList<TimeCommand> commands = new LinkedList<>();
    public static int currentTicks;

    private static Iterator<TimeCommand> iterator;

    public static void handleTickEvent(TickEvent.ServerTickEvent e) {
        if(!commands.isEmpty()) {
            iterator = commands.listIterator();
            while (iterator.hasNext()) {
                if(iterator.next().handle(currentTicks)) {
                    iterator.remove();
                }
            }
        }

        ++currentTicks;
    }

    public static TimeCommand forId(int id) {
        if(!commands.isEmpty()) {
            iterator = commands.listIterator();
            while (iterator.hasNext()) {
                TimeCommand command = iterator.next();
                if(command.getId()==id) {
                    return command;
                }
            }
        }

        return null;
    }

    public static void addTimeCommand(TimeCommand command) {
        command.setId(commands.size()+1);
        commands.listIterator().add(command);
    }
}

