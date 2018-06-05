package ada.osc.taskie.ui.tasks.mainActivity;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.SimpleItemTouchCallback;
import ada.osc.taskie.listener.ItemEventListener;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.persistance.TaskRepository;
import ada.osc.taskie.presentation.AllTasksPresenter;
import ada.osc.taskie.ui.addTask.NewTaskActivity;
import ada.osc.taskie.ui.tasks.adapter.TaskAdapter;
import ada.osc.taskie.ui.tasks.all.AllTasksContract;
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

		getSupportActionBar().setTitle("");
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setLogo(R.mipmap.launcher_taskie_icon);
		getSupportActionBar().setDisplayUseLogoEnabled(true);

	}


	@OnClick(R.id.fab_tasks_addNew)
	public void startNewTaskActivity() {
		Intent newTask = new Intent();
		newTask.setAction(ACTION_NEW_TASK);
		newTask.setClass(this, NewTaskActivity.class);
		startActivity(newTask);
	}

	private void setupTabLayout() {
	    mTabLayout.setTabTextColors(R.color.transparent_white_color, R.color.transparent_white_color);
		mTabLayout.addTab(mTabLayout.newTab().setText("TASKS"),true);
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.lowPriority_first_sort: {
				allTasksFragment.sortTasksHighPriorityFirst();
				return true;
			}
			case R.id.highPriority_first_sort: {
				allTasksFragment.sortTasksLowPriorityFirst();
				return true;
			}
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}