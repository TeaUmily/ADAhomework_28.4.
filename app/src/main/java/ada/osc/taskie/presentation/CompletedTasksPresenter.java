package ada.osc.taskie.presentation;

import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.List;

import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.ui.tasks.completed.CompletedTasksContract;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedTasksPresenter implements CompletedTasksContract.Presenter {

    private final ApiInteractor apiInteractor;
    private final SharedPreferences preferences;

    private CompletedTasksContract.View completedTasksView;

    public CompletedTasksPresenter(ApiInteractor apiInteractor, SharedPreferences preferences) {
        this.apiInteractor = apiInteractor;
        this.preferences = preferences;
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

            }
        };
    }

    @Override
    public void deleteTask(Task task) {

    }

    @Override
    public void setTaskFavorite(Task task) {

    }


}
