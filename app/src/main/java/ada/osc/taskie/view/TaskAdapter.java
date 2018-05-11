package ada.osc.taskie.view;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

	private List<Task> mTasks;
	private ItemEventListener mEventListener;

	public TaskAdapter(ItemEventListener eventListener) {
		mEventListener = eventListener;
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

		Format formatter = new SimpleDateFormat("dd/MM/yyyy");
		String s= formatter.format(current.getDueDate());
		holder.mDueDate.setText("Due date: "+s);

		holder.mCompleted.setChecked(current.isCompleted());

		holder.mPriority.setImageResource(getPriorityColor(current));
	}

	private int getPriorityColor(Task task) {
		int color = R.color.taskPriority_Unknown;
		switch (task.getPriority()){
			case LOW: color = R.color.taskpriority_low; break;
			case MEDIUM: color = R.color.taskpriority_medium; break;
			case HIGH: color = R.color.taskpriority_high; break;
		}
		return color;
	}

	@Override
	public int getItemCount() {
		return mTasks.size();
	}


	public void removeTask(int position) {
		mEventListener.onTaskSwipeRight(mTasks.get(position));
		mTasks.remove(position);
		notifyItemRemoved(position);
	}


	@Override
	public void onViewRecycled(@NonNull TaskViewHolder holder) {
		super.onViewRecycled(holder);
		holder.mCompleted.setOnCheckedChangeListener(null);
	}


	public void editTask(int position) {
		mEventListener.onTaskSwipeLeft(mTasks.get(position));
	}

	class TaskViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.textview_task_title) TextView mTitle;
		@BindView(R.id.textview_task_description) TextView mDescription;
		@BindView(R.id.imageview_task_priority) ImageView mPriority;
		@BindView(R.id.textview_task_dueDate) TextView mDueDate;
		@BindView(R.id.toggleButton) ToggleButton mCompleted;

		public TaskViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		@OnClick
		public void onTaskClick(){
			mEventListener.onClick(mTasks.get(getAdapterPosition()));
		}

		@OnClick(R.id.imageview_task_priority)
        public void onPriorityColorClick(){
		    mEventListener.onPriorityColorClick(mTasks.get(getAdapterPosition()));
            mPriority.setImageResource(getPriorityColor(mTasks.get(getAdapterPosition())));
        }

        @OnClick (R.id.toggleButton)
		    public void onToggleClick(){
		        mEventListener.onToggleClick(mTasks.get(getAdapterPosition()));
		    }
        }
    }

