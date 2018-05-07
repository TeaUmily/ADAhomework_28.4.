package ada.osc.taskie.model;

import android.renderscript.RenderScript;

import java.io.Serializable;
import java.util.Date;

import ada.osc.taskie.R;


public class Task implements Serializable{

	private static int sID = 0;

	int mId;
	private String mTitle;
	private String mDescription;
	private boolean mCompleted;
	private TaskPriority mPriority;
	private Date mDueDate;

	public Task(String title, String description, TaskPriority priority, Date dueDate) {
		mId = sID++;
		mTitle = title;
		mDescription = description;
		mCompleted = false;
		mPriority = priority;
		mDueDate = dueDate;
	}

	public int getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public boolean isCompleted() {
		return mCompleted;
	}

	public void setCompleted(boolean completed) {
		mCompleted = completed;
	}

	public TaskPriority getPriority() {
		return mPriority;
	}

	public void setPriority(TaskPriority priority) {
		mPriority = priority;
	}

	public Date getDueDate() {
		return mDueDate;
	}

	public void setDueDate(Date mDueDate) {
		this.mDueDate = mDueDate;
	}

	public void setNextPriority(){
		switch(mPriority){
			case LOW: mPriority = TaskPriority.MEDIUM; break;
			case MEDIUM: mPriority = TaskPriority.HIGH; break;
			case HIGH: mPriority = TaskPriority.LOW; break;
		}
	}

}
