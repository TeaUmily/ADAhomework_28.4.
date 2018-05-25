package ada.osc.taskie.presentation;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.List;

import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.ui.addTask.NewTaskActivity;
import ada.osc.taskie.ui.tasks.all.AllTasksContract;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllTasksPresenter implements AllTasksContract.Presenter {

    private final ApiInteractor apiInteractor;
    private final SharedPreferences preferences;

    private AllTasksContract.View allTasksView;

    public AllTasksPresenter(ApiInteractor apiInteractor, SharedPreferences preferences) {
        this.apiInteractor = apiInteractor;
        this.preferences = preferences;
    }

    @Override
    public void setView(AllTasksContract.View allTasksView) {
        this.allTasksView = allTasksView;
    }

    @Override
    public void getTasks() {
        apiInteractor.getTasks(getAllTasksCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<TaskList> getAllTasksCallback() {
        return new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body().mTaskList;
                    allTasksView.showTasks(tasks);
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
                // TODO: 19/05/2018 add error handling
            }
        };
    }

    @Override
    public void deleteTask(Task task) {
        apiInteractor.deleteTask(task, deleteTaskCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<Task> deleteTaskCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {

            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
               //TODO:

            }
        };
    }

    @Override
    public void setTaskFavorite(Task task) {
        apiInteractor.chaneTaskFavouriteState(task, setTaskFavoriteCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<Task> setTaskFavoriteCallback() {
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
        apiInteractor.changeTaskCompletedState(task, completedStateChangedCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }


    private Callback<Task> completedStateChangedCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allTasksView.showTaskStateChangedToast();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                    allTasksView.showTaskStateChangedErrorToast();
            }
        };
    }

    }
