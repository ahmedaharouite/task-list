package com.codurance.training.tasks.taskList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public final class TaskListRunnable implements Runnable {
    private TaskList taskList;

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        TaskReadWriter taskReadWriter = new TaskReadWriter(in, out);
        new TaskListRunnable(new TaskList(taskReadWriter)).run();
    }

    public TaskListRunnable(TaskList taskList) {
        TaskReadWriter taskReadWriter = taskList.taskReadWriter;
        this.taskList = new TaskList(new TaskReadWriter(taskReadWriter.in, taskReadWriter.out));
    }

    public void run() {
        TaskReadWriter taskReadWriter = taskList.taskReadWriter;
        BufferedReader in = taskReadWriter.in;
        PrintWriter out = taskReadWriter.out;

        while (true) {
            out.print("> ");
            out.flush();
            String command;
            try {
                command = in.readLine();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
            if (command.equals("quit")) {
                break;
            }
            taskList.execute(command);
        }
    }
}
