package ada.osc.taskie.presentation;

import android.content.SharedPreferences;

import java.util.List;

import ada.osc.taskie.App;
import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.persistance.TaskRepository;
import ada.osc.taskie.ui.tasks.favorite.FavoriteTasksContract;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteTaskPresenter implements FavoriteTasksContract.Presenter {

    private final SharedPreferences preferences;
    private final ApiInteractor apiInteractor;
    private TaskRepository mCache;
    private FavoriteTasksContract.View favoriteTasksView;

    public FavoriteTaskPresenter(SharedPreferences preferences, ApiInteractor apiInteractor) {
        this.preferences = preferences;
        this.apiInteractor = apiInteractor;
        this.mCache = TaskRepository.getInstance();
    }

    @Override
    public void setView(FavoriteTasksContract.View favoriteTasksView) {
        this.favoriteTasksView = favoriteTasksView;
    }

    @Override
    public void getTasks() {
        apiInteractor.getFavoriteTasks(getFavoriteTasksCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<TaskList> getFavoriteTasksCallback() {
        return new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body().mTaskList;

                    favoriteTasksView.showTasks(tasks);
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
                favoriteTasksView.showTasks(mCache.filterFavoriteTasks());
            }
        };
    }

    @Override
    public void deleteTask(Task task) {
        if(App.isInternetConnection()){
            mCache.removeTaskById(task.getId());
        apiInteractor.deleteTask(task, deleteTaskCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
        }
        else {
            mCache.removeTaskById(task.getId());
        }
    }

    private Callback<Task> deleteTaskCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                favoriteTasksView.onTaskRemoved();
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
            }
        };
    }


    @Override
    public void setTaskToNotFavorite(Task task) {
        if(App.isInternetConnection()){
            mCache.changeFavoriteState(task);
            apiInteractor.chaneTaskFavouriteState(task, changeTaskFavoriteStateCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
        }
        else {
            mCache.changeFavoriteState(task);
        }

    }

    @Override
    public void changeTaskCompleted(Task task) {
        if(App.isInternetConnection()){
        mCache.updateTaskCompletedState(task);
        apiInteractor.changeTaskCompletedState(task, completedStateChangedCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }else {
            mCache.updateTaskCompletedState(task);
        }

    }

    private Callback<Task> completedStateChangedCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favoriteTasksView.showTaskStateChangedToast();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {

            }
        };
    }

    private Callback<Task> changeTaskFavoriteStateCallback() {
            return new Callback<Task>() {
                @Override
                public void onResponse(Call<Task> call, Response<Task> response) {
                }

                @Override
                public void onFailure(Call<Task> call, Throwable t) {
                }
            };
        }

    @Override
    public void editTask(Task task) {
        if(App.isInternetConnection()){
        mCache.updateTask(task);
        apiInteractor.editTask(task,editTaskCallback(),preferences.getString(SharedPrefsUtil.TOKEN,""));
        }
        else {
            mCache.updateTask(task);
        }
    }

    private Callback<Task> editTaskCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {

            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {

            }
        };
    }
}
