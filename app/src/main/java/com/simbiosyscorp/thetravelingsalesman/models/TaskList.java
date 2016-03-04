package com.simbiosyscorp.thetravelingsalesman.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by D on 12/18/2015.
 */
public class TaskList {
    private List<TaskOld> mList;
    private static TaskList taskList;
    private Map<UUID, TaskOld> map;

    private TaskList(Context context) {
        mList = new ArrayList<>();
        map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            TaskOld task = new TaskOld();
            addTask(task);
        }
    }

    public static TaskList get(Context context) {
        if (taskList == null) {
            taskList = new TaskList(context);
        }

        return taskList;

    }

    public TaskOld getTask(UUID taskId) {
        return map.get(taskId);
    }

    public List<TaskOld> getTasks() {
        return mList;
    }

    public void addTask(TaskOld task) {
        mList.add(task);
        map.put(task.getId(), task);
    }

    public boolean deleteTask(UUID id) {
        TaskOld task = map.get(id);
        if (task != null) {
            map.remove(id);
            mList.remove(task);
            return true;
        }
        return false;

    }


}
