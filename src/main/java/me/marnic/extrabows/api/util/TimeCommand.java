package me.marnic.extrabows.api.util;

public class TimeCommand {
    private int endTick;
    private int startTick;
    private Runnable execute;

    public TimeCommand(int time, Runnable execute) {
        this.startTick = TimerUtil.currentTicks;
        this.endTick = startTick + time;
        this.execute = execute;
    }

    public boolean handle(int current) {
        if(current>=endTick) {
            execute.run();
            return true;
        }
        return false;
    }
}