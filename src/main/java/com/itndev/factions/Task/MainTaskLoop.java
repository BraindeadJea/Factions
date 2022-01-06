package com.itndev.factions.Task;

import com.itndev.factions.Main;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class MainTaskLoop {

    public static void AsyncLoopThread() {
        ArrayList<AsyncTask> TaskList = new ArrayList<>();
        Thread AsyncThread = new Thread( () -> {
            int time = 1;
            TaskList.add(TaskUtils.BuildTask((AsyncTask) BaltopUpdateTask.build(new BaltopUpdateTask())));
            while(true) {
                try {
                    Thread.sleep(60 * 1000);
                    time = time + 1;
                    if(time >= 1441) {
                        time = 1;
                    }

                    new Thread( () -> {
                        for (AsyncTask task : TaskList) {
                            TaskUtils.BuildTask(task).getRunnable().runTaskAsynchronously(Main.getInstance());
                        }
                    }).start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
