package com.itndev.factions.Task;

public class TaskUtils {

    public static AsyncTask BuildTask(AsyncTask task) {
        AsyncTask newTask = new AsyncTask();
        newTask.setMinute(task.getMinute());
        newTask.setRunnable(task.getRunnable());
        return newTask;
    }
}
