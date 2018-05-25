package ada.osc.taskie.interaction;

import ada.osc.taskie.model.LoginResponse;
import ada.osc.taskie.model.RegistrationToken;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.networking.ApiService;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class ApiInteractorImpl implements ApiInteractor{

    private final ApiService mApiService;

    public ApiInteractorImpl(ApiService apiService) {
        this.mApiService = apiService;
    }


    @Override
    public void registerUser(RegistrationToken registerRequest, Callback<RegistrationToken> callback) {
        mApiService.registerUser(registerRequest).enqueue(callback);
    }

    @Override
    public void loginUser(RegistrationToken loginRequest, Callback<LoginResponse> callback) {
        mApiService.loginUser(loginRequest).enqueue(callback);
    }

    @Override
    public void getTasks(Callback<TaskList> callback, String token) {
        mApiService.getTasks(token).enqueue(callback);
    }

    @Override
    public void getFavoriteTasks(Callback<TaskList> callback, String token) {
        mApiService.getFavoriteTasks(token).enqueue(callback);
    }

    @Override
    public void addNewTask(Task task, Callback<Task> callback, String token) {
        mApiService.postNewTask(token, task).enqueue(callback);
    }

    @Override
    public void deleteTask(Task task, Callback<Task> callback, String token) {
        mApiService.deleteTask(token, task.getId()).enqueue(callback);
    }

    @Override
    public void getCompletedTasks(Callback<TaskList> callback, String token) {
        mApiService.getCompletedTasks(token).enqueue(callback);
    }

    @Override
    public void changeTaskCompletedState(Task task, Callback<Task> callback, String token) {
        mApiService.changeTaskCompletedState(token, task.getId()).enqueue(callback);
    }

    @Override
    public void chaneTaskFavouriteState(Task task, Callback<Task> callback, String token) {
        mApiService.changeTaskFavoriteState(token, task.getId()).enqueue(callback);
    }

    @Override
    public void editTask(Task task, Callback<Task> callback, String token) {
        mApiService.editTask(token,task).enqueue(callback);
    }

}
