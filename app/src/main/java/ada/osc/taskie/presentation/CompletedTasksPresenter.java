package ada.osc.taskie.presentation;

import android.content.SharedPreferences;

import java.util.List;

import ada.osc.taskie.App;
import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.persistance.TaskRepository;
import ada.osc.taskie.ui.tasks.completed.CompletedTasksContract;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedTasksPresenter implements CompletedTasksContract.Presenter {

    private final ApiInteractor apiInteractor;
    private final SharedPreferences preferences;
    private TaskRepository mCache;
    private CompletedTasksContract.View completedTasksView;

    public CompletedTasksPresenter(ApiInteractor apiInteractor, SharedPreferences preferences) {
        this.apiInteractor = apiInteractor;
        this.preferences = preferences;
        this.mCache = TaskRepository.getInstance();
    }

    @Override
    public void setView(CompletedTasksContract.View completedTasksView) {
        this.completedTasksView = completedTasksView;
    }

    @Override
    public void getTasks() {
        apiInteractor.getCompletedTasks(completedTasksCallback(),preferences.getString(SharedPrefsUtil.TOKEN, "" ));
    }

    private Callback<TaskList> completedTasksCallback() {
        return new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body().mTaskList;

                    completedTasksView.showTasks(tasks);
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {

                completedTasksView.showTasks(mCache.filterCompletedTasks());
            }
        };
    }

    @Override
    public void deleteTask(Task task) {
        if(App.isInternetConnection()){
        mCache.removeTaskById(task.getId());
        apiInteractor.deleteTask(task, deleteTaskCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }else {
            mCache.removeTaskById(task.getId());
        }

    }

    private Callback<Task> deleteTaskCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                completedTasksView.onTaskRemoved();
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                completedTasksView.onTaskRemoved();
            }
        };
    }


    @Override
    public void changeTaskFavorite(Task task) {
        if(App.isInternetConnection()){
        mCache.changeFavoriteState(task);
        apiInteractor.chaneTaskFavouriteState(task, changeTaskFavoriteStateCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
        }else {
            mCache.changeFavoriteState(task);
        }

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
    public void changeTaskCompleted(Task task) {
        if (App.isInternetConnection()) {
            mCache.updateTaskCompletedState(task);
            apiInteractor.changeTaskCompletedState(task, completedStateChangedCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
        }
        else {
            mCache.updateTaskCompletedState(task);
        }
    }
    private Callback<Task> completedStateChangedCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                completedTasksView.onTaskCompletedStateChange();
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
