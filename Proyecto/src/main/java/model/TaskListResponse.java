package model;

import java.util.List;

public class TaskListResponse extends BaseResponse{
    List<Task> result;

    public List<Task> getTasks() {
        return result;
    }
}
