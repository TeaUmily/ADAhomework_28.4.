package ada.osc.taskie.ui.tasks.completed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.SimpleItemTouchCallback;
import ada.osc.taskie.listener.ItemEventListener;
import ada.osc.taskie.listener.TaskClickListener;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.presentation.AllTasksPresenter;
import ada.osc.taskie.presentation.CompletedTasksPresenter;
import ada.osc.taskie.ui.tasks.adapter.TaskAdapter;
import ada.osc.taskie.ui.tasks.all.AllTasksContract;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedTasksFragment extends Fragment implements CompletedTasksContract.View, ItemEventListener, TaskClickListener {

    @BindView(R.id.tasks)
    RecyclerView tasks;

    private TaskAdapter taskAdapter;

    private CompletedTasksContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        presenter = new CompletedTasksPresenter(App.getApiInteractor(), App.getPreferences());
        presenter.setView(this);


        taskAdapter = new TaskAdapter(this, this);

        tasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        tasks.setItemAnimator(new DefaultItemAnimator());
        tasks.setAdapter(taskAdapter);

        initSwipe();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getTasks();
    }


    private void initSwipe() {
        SimpleItemTouchCallback simpleItemTouchCallback = new SimpleItemTouchCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        simpleItemTouchCallback.setAdapter(taskAdapter);
        simpleItemTouchCallback.setRes(getResources());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(tasks);
    }


    @Override
    public void showTasks(List<Task> tasks) {
        taskAdapter.updateTasks(tasks);
    }

    @Override
    public void updateView() {
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMoreTasks(List<Task> tasks) {
        // TODO: 19/05/2018 add lazy loading/pagination
    }

    @Override
    public void onTaskRemoved(String taskId) {
        taskAdapter.removeTask(taskId);
    }

    @Override
    public void onTaskFavoriteStateChanged(String taskId) {
        // TODO: 19/05/2018 add logic for this
    }

    @Override
    public void onTaskCompletedStateChange(String taskId) {

    }


    @Override
    public void onTaskSwipeLeft(Task task) {
        displayAlertDialog(task);
    }


    private void displayAlertDialog(final Task task) {


        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setTitle("Confirm delete");
        ad.setMessage("Are you sure you want to delete this task?");

        ad.setPositiveButton(
                R.string.delete,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        presenter.deleteTask(task);
                        taskAdapter.removeTask(task.getId());

                    }
                }
        );

        ad.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int arg1) {
                        presenter.getTasks();
                    }
                }
        );

        ad.show();
    }

    @Override
    public void onTaskSwipeRight(Task task) {

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
        taskAdapter.onFavouriteTaskStarClick();
    }

    @Override
    public void onLongClick(Task task) {

    }



}
