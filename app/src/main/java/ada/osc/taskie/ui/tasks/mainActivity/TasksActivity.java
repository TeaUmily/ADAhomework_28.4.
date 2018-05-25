package ada.osc.taskie.ui.tasks.mainActivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ada.osc.taskie.R;
import ada.osc.taskie.ui.addTask.NewTaskActivity;
import ada.osc.taskie.ui.tasks.all.AllTasksFragment;
import ada.osc.taskie.ui.tasks.completed.CompletedTasksFragment;
import ada.osc.taskie.ui.tasks.favorite.FavoriteTasksFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksActivity extends AppCompatActivity {

	private String ACTION_NEW_TASK= "new task action";

	@BindView(R.id.fab_tasks_addNew)
	FloatingActionButton mNewTask;

	@BindView(R.id.tabLayout)TabLayout mTabLayout;

	private AllTasksFragment allTasksFragment;
	private FavoriteTasksFragment favoriteTasksFragment;
	private CompletedTasksFragment completedTasksFragment;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);
		ButterKnife.bind(this);

		allTasksFragment = new AllTasksFragment();
		favoriteTasksFragment = new FavoriteTasksFragment();
		completedTasksFragment = new CompletedTasksFragment();


		bindWidgetsWithAnEvent();
		setupTabLayout();
	}


	@OnClick(R.id.fab_tasks_addNew)
	public void startNewTaskActivity() {
		Intent newTask = new Intent();
		newTask.setAction(ACTION_NEW_TASK);
		newTask.setClass(this, NewTaskActivity.class);
		startActivity(newTask);
	}

	private void setupTabLayout() {
		mTabLayout.addTab(mTabLayout.newTab().setText("ALL"),true);
		mTabLayout.addTab(mTabLayout.newTab().setText("COMPLETED"));
		mTabLayout.addTab(mTabLayout.newTab().setText("FAVORITE"));
	}

	private void bindWidgetsWithAnEvent()
	{
		mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				setCurrentTabFragment(tab.getPosition());
			}
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
	}

	private void setCurrentTabFragment(int tabPosition)
	{
		switch (tabPosition)
		{
			case 0 :
				replaceFragment(allTasksFragment);
				break;
			case 1 :
				replaceFragment(completedTasksFragment);
				break;
			case 2:
				replaceFragment(favoriteTasksFragment);
				break;
		}
	}
	public void replaceFragment(Fragment fragment) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.frame_container, fragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();
	}
}


/*import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.List;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.SimpleItemTouchCallback;
import ada.osc.taskie.persistance.TaskRepository;
import ada.osc.taskie.listener.ItemEventListener;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.presentation.AllTasksPresenter;
import ada.osc.taskie.ui.addTask.NewTaskActivity;
import ada.osc.taskie.ui.tasks.AllTasksContract;
import ada.osc.taskie.ui.tasks.TaskAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/


/*
public class TasksActivity extends AppCompatActivity implements AllTasksContract.View {

	private static final String TAG = TasksActivity.class.getSimpleName();
	private String ACTION_NEW_TASK = "new task action";
	private String ACTION_EDIT_TASK = "edit task action";


	TaskRepository mRepository = TaskRepository.getInstance();
	TaskAdapter mTaskAdapter;

	private AllTasksContract.Presenter mPresenter;


	@BindView(R.id.fab_tasks_addNew) FloatingActionButton mNewTask;
	@BindView(R.id.recycler_tasks) RecyclerView mTasksRecycler;


	ItemEventListener mEventListener = new ItemEventListener() {
		@Override
		public void onClick(Task task) {
			toastTask(task);
		}

		@Override
		public void onToggleClick(Task task) {
			if(!task.isCompleted()){
				mRepository.updateTaskState(task);
				Log.i("Terezija:", "task <"+task.getTitle()+"> changes state to completed");
			}
			else{
				mRepository.updateTaskState(task);
				Log.i("Terezija:", "task <"+task.getTitle()+"> changes state to uncompleted");
			}
		}

		@Override
		public void onPriorityColorClick(Task task) {
			mRepository.changeTaskPriority(task);
		}


		@Override
		public void onTaskSwipeRight(Task task) {
			displayAlertDialog(task);
		}

		@Override
		public void onTaskSwipeLeft(Task task) {
			startNewTaskActivityForEdit(task);
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);


		mPresenter = new AllTasksPresenter(App.getApiInteractor(), App.getPreferences());
		mPresenter.setView(this);

		AlertDialog.Builder ad = new AlertDialog.Builder(this);

		ButterKnife.bind(this);
		setUpRecyclerView();
		updateTasksDisplay();

		initSwipe();
	}




	@Override
	protected void onResume() {
		super.onResume();
		updateTasksDisplay();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.lowPriority_first_sort: {
				mTaskAdapter.updateTasks(mRepository.getSortedTasksLowPriorityFirst());
				return true;
			}
			case R.id.highPriority_first_sort: {
				mTaskAdapter.updateTasks(mRepository.getSortedTasksHighPriorityFirst());
				return true;
			}
			case R.id.uncompleted_filter: {
				mTaskAdapter.updateTasks(mRepository.filterUncompletedTasks());
				return true;

			}case R.id.all_filter: {
				updateTasksDisplay();
				return true;
			}
			default:return super.onOptionsItemSelected(item);
		}
	}


	private void setUpRecyclerView() {

		int orientation = LinearLayoutManager.VERTICAL;

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
				this,
				orientation,
				false
		);

		RecyclerView.ItemDecoration decoration =
				new DividerItemDecoration(this, orientation);

		RecyclerView.ItemAnimator animator = new DefaultItemAnimator();

		mTaskAdapter = new TaskAdapter(mEventListener);

		mTasksRecycler.setLayoutManager(layoutManager);
		mTasksRecycler.addItemDecoration(decoration);
		mTasksRecycler.setItemAnimator(animator);
		mTasksRecycler.setAdapter(mTaskAdapter);
	}

	private void initSwipe() {
		SimpleItemTouchCallback simpleItemTouchCallback = new SimpleItemTouchCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
		simpleItemTouchCallback.setAdapter(mTaskAdapter);
		simpleItemTouchCallback.setRes(getResources());

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		itemTouchHelper.attachToRecyclerView(mTasksRecycler);
	}

	private void updateTasksDisplay() {
		List<Task> tasks = mRepository.getTasks();
		mTaskAdapter.updateTasks(tasks);
		for (Task t : tasks){
			Log.d(TAG, t.getTitle());
		}
	}

	private void toastTask(Task task) {
		Toast.makeText(
				this,
				task.getTitle() + "\n" + task.getDescription(),
				Toast.LENGTH_SHORT
		).show();
	}

	@OnClick(R.id.fab_tasks_addNew)
	public void startNewTaskActivity(){
		Intent newTask = new Intent();
		newTask.setAction(ACTION_NEW_TASK);
		newTask.setClass(this, NewTaskActivity.class);
		startActivity(newTask);
	}


	public void startNewTaskActivityForEdit(Task task){
		Intent editTask = new Intent();
		editTask.setAction(ACTION_EDIT_TASK);
		editTask.setClass(this, NewTaskActivity.class);
		editTask.putExtra(NewTaskActivity.EXTRA_TASK_ID, task.getId());
		startActivity(editTask);
	}

	@Override
	public void onStart() {
		super.onStart();
		mPresenter.getTasks();
	}


	@Override
	public void showTasks(List<Task> tasks) {
		mTaskAdapter.updateTasks(tasks);
	}

	@Override
	public void showMoreTasks(List<Task> tasks) {

	}

	@Override
	public void onTaskRemoved(String taskId) {
		mTaskAdapter.removeTask(taskId);
	}

	@Override
	public void onTaskFavoriteStateChanged(String taskId) {

	}
}
*/
