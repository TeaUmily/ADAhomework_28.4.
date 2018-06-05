package ada.osc.taskie.presentation;

import android.content.SharedPreferences;

import ada.osc.taskie.App;
import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.persistance.TaskRepository;
import ada.osc.taskie.ui.addTask.NewTaskContract;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTaskPresenter implements NewTaskContract.Presenter {

    private final SharedPreferences mPreferences;
    private final ApiInteractor mApiInteractor;
    private TaskRepository mCache;

    private NewTaskContract.View mNewTaskView;

    public NewTaskPresenter(SharedPreferences mPreferences, ApiInteractor mApiInteractor) {
        this.mPreferences = mPreferences;
        this.mApiInteractor = mApiInteractor;
        mCache = TaskRepository.getInstance();
    }

    @Override
    public void setView(NewTaskContract.View newTaskView) {
        this.mNewTaskView = newTaskView;
    }

    @Override
    public void getTaskById(String id) {
        mApiInteractor.getNoteById(id, getTaskByIdCallback(), mPreferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<Task> getTaskByIdCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                   Task task = response.body();
                   mNewTaskView.onGetTaskById(task);
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {

            }
        };
    }


    @Override
    public void createTask(Task task) {
        if(task != null){
            if(App.isInternetConnection()){
                mApiInteractor.addNewTask(task, createNewTaskCallback(), mPreferences.getString(SharedPrefsUtil.TOKEN, ""));
            }
            else {
                mCache.saveTask(task);
            }
            }

    }

    private Callback<Task> createNewTaskCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Task task = response.body();
                    mCache.saveTask(task);
                    mNewTaskView.onTaskCreated();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                mNewTaskView.showNetworkError();
            }
        };
    }

    @Override
    public void editTask(Task task) {
        if(App.isInternetConnection()){
            mCache.updateTask(task);
            mApiInteractor.editTask(task, editTaskCallback(), mPreferences.getString(SharedPrefsUtil.TOKEN, ""));
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
