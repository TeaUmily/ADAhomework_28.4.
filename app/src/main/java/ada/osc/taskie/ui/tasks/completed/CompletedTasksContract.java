package ada.osc.taskie.ui.tasks.completed;

import java.util.List;

import ada.osc.taskie.model.Task;
import ada.osc.taskie.ui.tasks.all.AllTasksContract;

public interface CompletedTasksContract {

    interface View{

        void showTasks(List<Task> tasks);

        void updateView();

        void showMoreTasks(List<Task> tasks);

        void onTaskRemoved(String taskId);

        void onTaskFavoriteStateChanged(String taskId);

        void onTaskCompletedStateChange(String taskId);


    }

    interface Presenter{

        void setView(CompletedTasksContract.View allTasksView);

        void getTasks();

        void deleteTask(Task task);

        void setTaskFavorite(Task task);


    }
}
