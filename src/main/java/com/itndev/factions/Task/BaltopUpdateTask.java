package com.itndev.factions.Task;

import com.itndev.factions.Main;
import com.itndev.factions.Utils.FactionList.FactionList;
import org.bukkit.scheduler.BukkitRunnable;

public class BaltopUpdateTask extends AsyncTask{

    public static BaltopUpdateTask build(BaltopUpdateTask task) {
        task.run();
        return task;
    }

    public void run() {
        setMinute(10);
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                FactionList.FactionTopExecute(100L);
                FactionList.BuildFactionTop();
            }
        };

    }



}
