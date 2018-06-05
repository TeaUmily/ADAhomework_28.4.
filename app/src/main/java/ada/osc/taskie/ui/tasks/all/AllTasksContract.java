package ada.osc.taskie.ui.tasks.all;

import java.util.List;

import ada.osc.taskie.model.Task;

public interface AllTasksContract {

    interface View {

        void showTasks(List<Task> tasks);

        void showMoreTasks(List<Task> tasks);

        void onTaskRemoved();

        void onTaskFavoriteStateChanged(String taskId);

        void showTaskStateChangedToast();



    }

    interface Presenter {

        void setView(View allTasksView);

        void getTasks();

        void deleteTask(Task task);

        void setTaskToFavorite(Task task);

        void changeTaskCompleted(Task task);

        void sortTasksLowPriorityFirst();

        void sortTasksHighPriorityFirst();

        void editTask(Task task);

        void updateTasksFromLocalBase();

        void updateTask(Task task);
    }
}
