package me.marnic.extrabows.api.util;

public class TimeCommand {
    private int endTick;
    private int startTick;
    private Runnable execute;
    private int id;

    public TimeCommand(int time,Runnable execute) {
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public class EndableTimeCommand extends TimeCommand{

        public boolean end;

        public EndableTimeCommand(int time, Runnable execute) {
            super(time, execute);
        }

        @Override
        public boolean handle(int current) {
            return end;
        }

        public void setEnd(boolean end) {
            this.end = end;
        }
    }
}