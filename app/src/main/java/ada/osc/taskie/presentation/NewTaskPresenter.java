package ada.osc.taskie.presentation;

import android.content.SharedPreferences;

import java.util.List;

import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.ui.addTask.NewTaskContract;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTaskPresenter implements NewTaskContract.Presenter {

    private final SharedPreferences mPreferences;
    private final ApiInteractor mApiInteractor;

    private NewTaskContract.View mNewTaskView;

    public NewTaskPresenter(SharedPreferences mPreferences, ApiInteractor mApiInteractor) {
        this.mPreferences = mPreferences;
        this.mApiInteractor = mApiInteractor;
    }

    @Override
    public void setView(NewTaskContract.View newTaskView) {
        this.mNewTaskView = newTaskView;
    }

    @Override
    public void createTask(Task task) {
        if(task != null){
            mApiInteractor.addNewTask(task, getNewTaskCallback(), mPreferences.getString(SharedPrefsUtil.TOKEN, ""));
        }
        else{
            mNewTaskView.showTaskError();
        }


    }

    @Override
    public void editTask(Task task) {
        mApiInteractor.editTask(task, editTaskCallback(), mPreferences.getString(SharedPrefsUtil.TOKEN, ""));
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

    private Callback<Task> getNewTaskCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mNewTaskView.onTaskCreated();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                mNewTaskView.showNetworkError();
            }
        };
    }
}
