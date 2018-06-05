package ada.osc.taskie.ui.tasks.favorite;

import java.util.List;

import ada.osc.taskie.model.Task;
import ada.osc.taskie.ui.tasks.all.AllTasksContract;

public interface FavoriteTasksContract {

    interface View {

        void showTasks(List<Task> tasks);

        void showMoreTasks(List<Task> tasks);

        void onFavouriteTaskStarClick(Task task);

        void onTaskRemoved();

        void onTaskFavoriteStateChanged(String taskId);

        void showTaskStateChangedToast();
    }

    interface Presenter {

        void setView(FavoriteTasksContract.View favoriteTasksView);

        void getTasks();

        void deleteTask(Task task);

        void setTaskToNotFavorite(Task task);

        void changeTaskCompleted(Task task);

        void editTask(Task task);
    }
}
