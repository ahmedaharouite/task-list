package com.codurance.training.tasks.taskList;

public final class TaskListLastId {

    public long lastId = 0;

    public long nextId() {
        return ++lastId;
    }
}
