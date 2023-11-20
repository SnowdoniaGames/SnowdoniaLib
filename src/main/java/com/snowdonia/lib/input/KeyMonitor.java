package com.snowdonia.lib.input;

public class KeyMonitor
{
    private final String name;
    private final Integer key;
    private final Integer trigger;
    private final Float cooldown;

    private long timer = System.currentTimeMillis();

    public KeyMonitor(String name, Integer key, Integer trigger, Float cooldown)
    {
        this.name = name;
        this.key = key;
        this.trigger = trigger;
        this.cooldown = cooldown;
    }

    public String getName() {
        return name;
    }

    public Integer getKey() {
        return key;
    }

    public Integer getTrigger() {
        return trigger;
    }

    public Float getCooldown() {
        return cooldown + 0.1f;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }
}
