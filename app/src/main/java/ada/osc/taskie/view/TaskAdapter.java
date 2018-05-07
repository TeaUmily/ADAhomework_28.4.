package ada.osc.taskie.view;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

	private List<Task> mTasks;
	private TaskClickListener mListener;
	private ItemEventListener mEventListener;

	public TaskAdapter(TaskClickListener listener, ItemEventListener eventListener) {
		mListener = listener;
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




	@Override
	public void onBindViewHolder(@NonNull final TaskViewHolder holder, final int position) {

		final Task current = mTasks.get(position);

		holder.mTitle.setText(current.getTitle());
		holder.mDescription.setText(current.getDescription());

		Format formatter = new SimpleDateFormat("dd/MM/yyyy");
		String s= formatter.format(current.getDueDate());
		holder.mDueDate.setText("Due date: "+s);


		holder.mCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked){
					mEventListener.onToggleClick(mTasks.get(position));
				}
				else{
					mEventListener.onToggleClick(mTasks.get(position));
				}
			}
		});

		holder.mPriority.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				current.setNextPriority();
				holder.mPriority.setImageResource(getPriorityColor(current));
			}
		});


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
			mListener.onClick(mTasks.get(getAdapterPosition()));
		}

		@OnLongClick
		public boolean onTaskLongClick() {
            mListener.onLongClick(mTasks.get(getAdapterPosition()));
            return true;
        }



	}




}
