package ada.osc.taskie.ui.tasks.all;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.SimpleItemTouchCallback;
import ada.osc.taskie.listener.ItemEventListener;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.presentation.AllTasksPresenter;
import ada.osc.taskie.ui.addTask.NewTaskActivity;
import ada.osc.taskie.ui.tasks.adapter.TaskAdapter;
import ada.osc.taskie.listener.TaskClickListener;
import ada.osc.taskie.ui.tasks.divider.SimpleDividerItemDecoration;
import ada.osc.taskie.util.SharedPrefsUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AllTasksFragment extends Fragment implements AllTasksContract.View, MenuOptions ,TaskClickListener, ItemEventListener {

    private String ACTION_EDIT_TASK = "edit task action";

    @BindView(R.id.tasks)
    RecyclerView tasks;

    private TaskAdapter taskAdapter;
    private AllTasksContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(App.isInternetConnection()){
            presenter.updateTasksFromLocalBase();
        }
        presenter.getTasks();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        presenter = new AllTasksPresenter(App.getApiInteractor(), App.getPreferences());
        presenter.setView(this);


        taskAdapter = new TaskAdapter(this, this);

        tasks.addItemDecoration(new SimpleDividerItemDecoration(getContext(), android.R.color.darker_gray));

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
    public void showMoreTasks(List<Task> tasks) {
        // TODO: 19/05/2018 add lazy loading/pagination
    }

    @Override
    public void onTaskRemoved() {
        Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskFavoriteStateChanged(String taskId) {
        // TODO: 19/05/2018 add logic for this
    }

    @Override
    public void onTaskSwipeLeft(Task task) {
        displayAlertDialog(task);
    }

    private void displayAlertDialog(final Task task) {

        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setTitle(R.string.confirm_delete);
        ad.setMessage(R.string.are_you_sure_you_want_to_delete_this_task);

        ad.setPositiveButton(
                R.string.delete,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        taskAdapter.removeTask(task.getId());
                        presenter.deleteTask(task);


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
        startNewTaskActivityForEdit(task);
    }

    public void startNewTaskActivityForEdit(Task task){
        Intent editTask = new Intent();
        editTask.setAction(ACTION_EDIT_TASK);
        editTask.setClass(getContext(), NewTaskActivity.class);
        editTask.putExtra(NewTaskActivity.EXTRA_TASK, task);
        startActivity(editTask);
    }

    @Override
    public void onClick(Task task) {
        presenter.editTask(task);
    }

    @Override
    public void onToggleClick(Task task) {
        if(!task.isCompleted()){
            presenter.changeTaskCompleted(task);
        }
        else {
            presenter.changeTaskCompleted(task);
            }
    }

    @Override
    public void onFavouriteTaskStarClick(Task task) {
        taskAdapter.removeTask(task.getId());
        presenter.setTaskToFavorite(task);

    }

    @Override
    public void onLongClick(Task task) {

    }

    @Override
    public void showTaskStateChangedToast() {
        Toast.makeText(getContext(), "Task changed state", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sortTasksLowPriorityFirst() {
        presenter.sortTasksLowPriorityFirst();
    }

    @Override
    public void sortTasksHighPriorityFirst() {
        presenter.sortTasksHighPriorityFirst();
    }
}
