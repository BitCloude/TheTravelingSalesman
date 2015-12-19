package com.appers.ayvaz.thetravelingsalesman.Model;

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
    private List<Task> mList;
    private static TaskList taskList;
    private Map<UUID, Task> map;

    private TaskList(Context context) {
        mList = new ArrayList<>();
        map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            Task task = new Task();
            mList.add(task);
            map.put(task.getId(), task);
        }
    }

    public static TaskList get(Context context) {
        if (taskList == null) {
            taskList = new TaskList(context);
        }

        return taskList;

    }

    public Task getTask(UUID taskId) {
        return map.get(taskId);
    }

    public List<Task> getTasks() {
        return mList;
    }
}
