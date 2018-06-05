package ada.osc.taskie.presentation;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ada.osc.taskie.App;
import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.persistance.TaskRepository;
import ada.osc.taskie.ui.tasks.all.AllTasksContract;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllTasksPresenter implements AllTasksContract.Presenter {

    private final ApiInteractor apiInteractor;
    private final SharedPreferences preferences;
    private TaskRepository mCache;

    private AllTasksContract.View mAllTasksView;
    private List<Task> tasks;

    public AllTasksPresenter(ApiInteractor apiInteractor, SharedPreferences preferences) {
        this.apiInteractor = apiInteractor;
        this.preferences = preferences;
        mCache = TaskRepository.getInstance();
    }

    @Override
    public void setView(AllTasksContract.View allTasksView) {
        this.mAllTasksView = allTasksView;
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
                    tasks = response.body().mTaskList;
                    mAllTasksView.showTasks(tasks);
                }
            }


            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
               tasks = mCache.getTasks();
               mAllTasksView.showTasks(tasks);
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
                mAllTasksView.onTaskRemoved();
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
               //TODO:
            }
        };
    }

    @Override
    public void setTaskToFavorite(Task task) {
        if(App.isInternetConnection()){
            mCache.changeFavoriteState(task);
            apiInteractor.chaneTaskFavouriteState(task, changeTaskFavoriteStateCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
        }
        else{
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
        if(App.isInternetConnection()){
            mCache.updateTaskCompletedState(task);
            apiInteractor.changeTaskCompletedState(task, completedStateChangedCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
        }
        else {
            mCache.updateTaskCompletedState(task);
        }

    }

    @Override
    public void sortTasksLowPriorityFirst() {
        if(App.isInternetConnection()){

            sortTasksHighPriorityFirst();
            Collections.reverse(tasks);
            mAllTasksView.showTasks(tasks);
        }
        else{
            mAllTasksView.showTasks(mCache.getSortedTasksLowPriorityFirst());
        }

    }

    @Override
    public void sortTasksHighPriorityFirst() {
        if(App.isInternetConnection()){

            Collections.sort(tasks, new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    Integer o1Priority = o1.getPriority();
                    Integer o2Priority = o2.getPriority();
                    return o1Priority.compareTo(o2Priority);
                }
            });
            mAllTasksView.showTasks(tasks);
        }
       else{
            mAllTasksView.showTasks(mCache.getSortedTasksHighPriorityFirst());
        }

    }

    @Override
    public void editTask(Task task) {
        if(App.isInternetConnection()){
            mCache.updateTask(task);
            apiInteractor.editTask(task,editTaskCallback(),preferences.getString(SharedPrefsUtil.TOKEN,""));
        }
        else{
            mCache.updateTask(task);
        }

    }

    @Override
    public void updateTasksFromLocalBase() {
        for (Task task:mCache.getTasks()
             ) { this.updateTask(task);
        }
    }

    @Override
    public void updateTask(Task task) {
        apiInteractor.editTask(task,editTaskCallback(),preferences.getString(SharedPrefsUtil.TOKEN,""));
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

    private Callback<Task> completedStateChangedCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mAllTasksView.showTaskStateChangedToast();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {

            }
        };
    }

}
