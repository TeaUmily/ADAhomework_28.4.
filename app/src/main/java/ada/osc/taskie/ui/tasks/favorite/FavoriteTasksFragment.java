package ada.osc.taskie.ui.tasks.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.listener.ItemEventListener;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import ada.osc.taskie.presentation.FavoriteTaskPresenter;
import ada.osc.taskie.util.SharedPrefsUtil;
import ada.osc.taskie.ui.tasks.adapter.TaskAdapter;
import ada.osc.taskie.listener.TaskClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavoriteTasksFragment extends Fragment implements FavoriteTasksContract.View, TaskClickListener, ItemEventListener {

    @BindView(R.id.tasks)
    RecyclerView tasks;

    private TaskAdapter taskAdapter;

    private FavoriteTasksContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        presenter = new FavoriteTaskPresenter(App.getPreferences(), App.getApiInteractor());
        presenter.setView(this);

        taskAdapter = new TaskAdapter(this, this);
        tasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        tasks.setItemAnimator(new DefaultItemAnimator());
        tasks.setAdapter(taskAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getTasks();

    }


    @Override
    public void showTasks(List<Task> tasks) {
        taskAdapter.updateTasks(tasks);
    }

    @Override
    public void showMoreTasks(List<Task> tasks) {

    }

    @Override
    public void onTaskRemoved(String taskId) {

    }

    @Override
    public void onTaskFavoriteStateChanged(String taskId) {

    }

    @Override
    public void onTaskSwipeRight(Task task) {

    }

    @Override
    public void onTaskSwipeLeft(Task task) {

    }

    @Override
    public void onClick(Task task) {

    }

    @Override
    public void onToggleClick(Task task) {

    }

    @Override
    public void onPriorityColorClick(Task task) {

    }

    @Override
    public void onFavouriteTaskStarClick(Task task) {

    }

    @Override
    public void onLongClick(Task task) {

    }
}