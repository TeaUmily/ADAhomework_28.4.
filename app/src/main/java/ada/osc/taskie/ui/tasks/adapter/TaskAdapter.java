package ada.osc.taskie.ui.tasks.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import ada.osc.taskie.persistance.TaskRepository;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.realm.Realm;
import io.realm.RealmQuery;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

	private List<Task> mTasks;
	private TaskClickListener mTaskClickListener;
	private ItemEventListener mItemEventListener;
	private Realm mRealm;

	public TaskAdapter(TaskClickListener taskClickListener, ItemEventListener itemEventListener) {
		this.mItemEventListener = itemEventListener;
		this.mTaskClickListener = taskClickListener;
		mTasks = new ArrayList<>();
		mRealm = Realm.getDefaultInstance();
	}

	public void updateTasks(List<Task> tasks) {
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

		holder.mDueDate.setText("Due date: " + current.getDueDate());
		holder.mCompleted.setChecked(current.isCompleted());

		setFavoriteStarImageResources(holder, current);

		holder.mPriority.setImageResource(getPriorityColor(current));

	}


	private int getPriorityColor(Task task) {
		int color = R.color.green;
		switch (task.getPriority()){
			case 1: color =R.color.green ; break;
			case 2: color = R.color.yellow; break;
			case 3: color = R.color.red; break;
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


	public void setFavoriteStarImageResources(TaskViewHolder holder, Task current) {
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
		@BindView(R.id.textview_task_dueDate) TextView mDueDate;
		@BindView(R.id.toggleButton) ToggleButton mCompleted;
		@BindView(R.id.imageview_task_priority) ImageView mPriority;
		@BindView(R.id.fav_star_imageview) ImageView mFavouriteTaskStar;

		public TaskViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}


		@OnClick
		public void onTaskClick(){
			// TODO može li se kako izbjeći ovaj dio ovdje s realmom?
			mRealm.beginTransaction();
			mTasks.get(getAdapterPosition()).setNextPriority();
			mRealm.commitTransaction();
			mPriority.setImageResource(getPriorityColor(mTasks.get(getAdapterPosition())));
			mTaskClickListener.onClick(mTasks.get(getAdapterPosition()));
		}

		@OnClick (R.id.fav_star_imageview)
		public void onFavTaskClick() {
			if(mTasks.get(getAdapterPosition()).isFavorite()){
				mRealm.beginTransaction();
			    mTasks.get(getAdapterPosition()).setFavorite(false);
			    mRealm.commitTransaction();
				mFavouriteTaskStar.setImageResource(R.drawable.fav_star_icon);
			}
			else{
				mRealm.beginTransaction();
			    mTasks.get(getAdapterPosition()).setFavorite(true);
			    mRealm.commitTransaction();
				mFavouriteTaskStar.setImageResource(R.drawable.fav_yellow_star_icon);
			}
			mItemEventListener.onFavouriteTaskStarClick(mTasks.get(getAdapterPosition()));
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

