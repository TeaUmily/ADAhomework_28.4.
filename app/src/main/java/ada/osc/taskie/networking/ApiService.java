package ada.osc.taskie.networking;

import android.media.session.MediaSession;

import ada.osc.taskie.model.LoginResponse;
import ada.osc.taskie.model.RegistrationToken;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/register/")
    Call<RegistrationToken> registerUser(@Body RegistrationToken registrationToken);

    @POST("api/login/")
    Call<LoginResponse> loginUser(@Body RegistrationToken registrationToken);

    @POST("api/note/")
    Call<Task> postNewTask(@Header("authorization") String header, @Body Task task);

    @POST("api/note/delete")
    Call<Task> deleteTask(@Header("authorization") String header, @Query("id") String mId);

    @GET("api/note/")
    Call<TaskList> getTasks(@Header("authorization") String header);

    @GET("api/note/favorite")
    Call<TaskList> getFavoriteTasks(@Header("authorization") String token);

    @GET("api/note/completed")
    Call<TaskList> getCompletedTasks(@Header("authorization") String token);

    @POST ("api/note/complete")
    Call<Task> changeTaskCompletedState(@Header("authorization") String header, @Query("id") String id);

    @POST("api/note/favorite")
    Call<Task> changeTaskFavoriteState(@Header("authorization") String header, @Query("id") String id);

    @POST("api/note/edit")
    Call<Task> editTask(@Header("authorization") String header, @Body Task taskToEdit);

    @GET("api/note/{id}")
    Call<Task> getNoteById(@Header("authorization") String header, @Path("id") String id);
}
