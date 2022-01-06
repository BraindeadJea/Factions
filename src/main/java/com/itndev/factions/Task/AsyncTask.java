package com.itndev.factions.Task;

import org.bukkit.scheduler.BukkitRunnable;

public class AsyncTask {
    private Integer Minute = 10;

    public BukkitRunnable runnable = null;

    public BukkitRunnable getRunnable() {
        return runnable;
    }

    public void setRunnable(BukkitRunnable r) {
        runnable = r;
    }

    public Integer getMinute() {
        return Minute;
    }

    public void setMinute(Integer m) {
        Minute = m;
    }


}
