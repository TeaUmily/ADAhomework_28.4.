package ada.osc.taskie.ui.tasks.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Icon;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.listener.ItemEventListener;
import ada.osc.taskie.listener.TaskClickListener;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.ui.tasks.all.AllTasksContract;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import retrofit2.http.Body;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

	private List<Task> mTasks;
	private TaskClickListener mTaskClickListener;
	private ItemEventListener mItemEventListener;


	public TaskAdapter(TaskClickListener taskClickListener, ItemEventListener itemEventListener) {
		this.mItemEventListener = itemEventListener;
		this.mTaskClickListener = taskClickListener;
		mTasks = new ArrayList<>();
	}

	public void updateTasks(List<Task> tasks){
		mTasks.clear();
		mTasks.addAll(tasks);
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_task, parent, false);
		return new TaskViewHolder(itemView);
	}


	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(@NonNull final TaskViewHolder holder, final int position) {

		final Task current = mTasks.get(position);

		holder.mTitle.setText(current.getTitle());
		holder.mDescription.setText(current.getDescription());

		holder.mDueDate.setText("Due date: "+ current.getDueDate());
		holder.mCompleted.setChecked(current.isCompleted());

		setFavoriteStarState(holder, current);

		holder.mPriority.setImageResource(getPriorityColor(current));
	}




	private int getPriorityColor(Task task) {
		int color = R.color.taskPriority_Unknown;
		switch (task.getPriority()){
			case 1: color = R.color.taskpriority_low; break;
			case 2: color = R.color.taskpriority_medium; break;
			case 3: color = R.color.taskpriority_high; break;
		}
		return color;
	}
	@Override
	public int getItemCount() {
		return mTasks.size();
	}


	public void removeTask(String taskId) {
		int index = -1;

		for (Task task : mTasks) {
			if (task.getId().equals(taskId)) {
				index = mTasks.indexOf(task);
			}
		}

		if (index != -1) {

			mTasks.remove(index);
			notifyItemRemoved(index);
		}
	}


	@Override
	public void onViewRecycled(@NonNull TaskViewHolder holder) {
		super.onViewRecycled(holder);
		holder.mCompleted.setOnCheckedChangeListener(null);
	}

	public void onTaskSwipeLeft(int position) {
		mItemEventListener.onTaskSwipeLeft(mTasks.get(position));
	}

	public void onTaskSwipeRight(int position){
		mItemEventListener.onTaskSwipeRight(mTasks.get(position));
	}

	public void onFavouriteTaskStarClick() {

	}

	public void setFavoriteStarState(TaskViewHolder holder, Task current) {
		if(current.isFavorite()){
			holder.mFavouriteTaskStar.setImageResource(R.drawable.fav_yellow_star_icon);
		}
		else{
			holder.mFavouriteTaskStar.setImageResource(R.drawable.fav_star_icon);
		}
	}


	class TaskViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.textview_task_title) TextView mTitle;
		@BindView(R.id.textview_task_description) TextView mDescription;
		@BindView(R.id.imageview_task_priority) ImageView mPriority;
		@BindView(R.id.textview_task_dueDate) TextView mDueDate;
		@BindView(R.id.toggleButton) ToggleButton mCompleted;
		@BindView(R.id.fav_star_imageview) ImageView mFavouriteTaskStar;

		public TaskViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		/*@OnClick(R.id.imageview_task_priority)
		public void onPriorityColorClick(){
			mEventListener.onPriorityColorClick(mTasks.get(getAdapterPosition()));
			mPriority.setImageResource(getPriorityColor(mTasks.get(getAdapterPosition())));
		}

		}*/

		@OnClick (R.id.fav_star_imageview)

		public void onFavTaskClick() {
			mItemEventListener.onFavouriteTaskStarClick(mTasks.get(getAdapterPosition()));
			mFavouriteTaskStar.setImageResource(R.drawable.fav_yellow_star_icon);
		}

		@OnLongClick
		public boolean onTaskLongClick() {
			mTaskClickListener.onLongClick(mTasks.get(getAdapterPosition()));
			return true;
		}

		@OnClick (R.id.toggleButton)
		public void onToggleClick(){
			mItemEventListener.onToggleClick(mTasks.get(getAdapterPosition()));
		}
	}
}

