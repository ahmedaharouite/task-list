package com.codurance.training.tasks.taskList;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.codurance.training.tasks.task.Task;
import com.codurance.training.tasks.task.TaskDescription;
import com.codurance.training.tasks.task.TaskDone;
import com.codurance.training.tasks.task.TaskId;

public final class TaskList {
    public final Map<String, List<Task>> tasks = new LinkedHashMap<>();
    public TaskReadWriter taskReadWriter;
    private TaskListLastId lastId;

    public TaskList(TaskReadWriter taskReadWriter) {
        this.taskReadWriter = new TaskReadWriter(taskReadWriter.in, taskReadWriter.out);
    }

    public void execute(String commandLine) {
        String[] commandRest = commandLine.split(" ", 2);
        String command = commandRest[0];
        switch (command) {
            case "show":
                show();
                break;
            case "add":
                add(commandRest[1]);
                break;
            case "check":
                checkByBoolean(commandRest[1], true);
                break;
            case "uncheck":
                checkByBoolean(commandRest[1], false);
                break;
            case "help":
                taskReadWriter.help();
                break;
            default:
                taskReadWriter.error(command);
                break;
        }
    }

    private void show() {        
        PrintWriter out = taskReadWriter.out;
        for (Map.Entry<String, List<Task>> project : tasks.entrySet()) {
            out.println(project.getKey());
            for (Task task : project.getValue()) {
                TaskDone taskDone = task.done;
                TaskId taskId = task.id;
                TaskDescription taskDescription = task.description;
                out.printf("    [%c] %d: %s%n", (taskDone.done ? 'x' : ' '), taskId.id, taskDescription.description);
            }
            out.println();
        }
    }

    public void add(String commandLine) {
        String[] subcommandRest = commandLine.split(" ", 2);
        String subcommand = subcommandRest[0];
        if (subcommand.equals("project")) {
            addProject(subcommandRest[1]);
        } else if (subcommand.equals("task")) {
            String[] projectTask = subcommandRest[1].split(" ", 2);
            addTask(projectTask[0], projectTask[1]);
        }
    }

    private void addProject(String name) {
        tasks.put(name, new ArrayList<Task>());
    }

    private void addTask(String project, String description) {
        List<Task> projectTasks = tasks.get(project);
        PrintWriter out = taskReadWriter.out;
        if (projectTasks == null) {
            out.printf("Could not find a project with the name \"%s\".", project);
            out.println();
            return;
        }
        
        TaskId taskId = new TaskId(lastId.nextId());
        TaskDescription taskDescription = new TaskDescription(description);
        TaskDone taskDone = new TaskDone(false);
        Task task = new Task(taskId, taskDescription, taskDone);
        projectTasks.add(task);
    }

    public void checkByBoolean(String idString, boolean done) {
        setDone(idString, done);
    }

    private void setDone(String idString, boolean done) {        
        PrintWriter out = taskReadWriter.out;
        int id = Integer.parseInt(idString);
        for (Map.Entry<String, List<Task>> project : tasks.entrySet()) {
            for (Task task : project.getValue()) {
                TaskId taskId = task.id;
                TaskDone taskDone = task.done;
                if (taskId.id == id) {
                    taskDone.done = done;
                    return;
                }
            }
        }
        out.printf("Could not find a task with an ID of %d.", id);
        out.println();
    }
}
