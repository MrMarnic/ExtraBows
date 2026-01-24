package me.marnic.extrabows.api.util;

public class TimeCommand {
    protected Runnable execute;
    private int endTick;
    private int startTick;
    private int id;

    public TimeCommand(int time, Runnable execute, int id) {
        this.startTick = TimerUtil.currentTicks;
        this.endTick = startTick + time;
        this.execute = execute;
        this.id = id;
    }

    public TimeCommand(int time, int id) {
        this.startTick = TimerUtil.currentTicks;
        this.endTick = startTick + time;
        this.id = id;
    }

    public TimeCommand(int time, Runnable execute) {
        this.startTick = TimerUtil.currentTicks;
        this.endTick = startTick + time;
        this.execute = execute;
        this.id = -1;
    }

    public boolean handle(int current) {
        if (current >= endTick) {
            execute.run();
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setExecute(Runnable execute) {
        this.execute = execute;
    }

    public static class EndableTimeCommand extends TimeCommand {

        public boolean end = false;

        public EndableTimeCommand(int time, Runnable execute, int id) {
            super(time, execute, id);
        }

        public EndableTimeCommand(int time, int id) {
            super(time, id);
        }

        @Override
        public boolean handle(int current) {
            boolean a = end;
            execute.run();
            return a;
        }

        public void setEnd(boolean end) {
            this.end = end;
        }
    }
}