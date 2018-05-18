package ada.osc.taskie.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class Task extends RealmObject  implements Serializable{

	@Required
	@PrimaryKey
	private String mId;
	private String mTitle;
	private String mDescription;
	private boolean mCompleted;
	private String mPriority;
	private Date mDueDate;
	private String mCategory;

	public Task(String title, String description, TaskPriority priority, Date dueDate, String mCategory) {
		this.mCategory = mCategory;
		mId = UUID.randomUUID().toString();
		mTitle = title;
		mDescription = description;
		mCompleted = false;
		mPriority = convertTaskPriorityEnumToString(priority);
		mDueDate = dueDate;

	}

	public Task() {
	}

	public String getId() {
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


	public Date getDueDate(){
		return mDueDate;
	}

	public void setDueDate(Date dueDate) {
		this.mDueDate = dueDate;
	}

	public TaskPriority getTaskPriorityEnum() {
		return TaskPriority.valueOf(mPriority);
	}

	public void setTaskPriorityEnum(TaskPriority taskPriority) {
		this.mPriority = taskPriority.toString();
	}

	public void setNextPriority(){
		switch(getTaskPriorityEnum()){
			case LOW: mPriority = TaskPriority.MEDIUM.toString(); break;
			case MEDIUM: mPriority = TaskPriority.HIGH.toString(); break;
			case HIGH: mPriority = TaskPriority.LOW.toString(); break;
		}
	}

	public void changeState() {
		if(this.isCompleted()){
			this.setCompleted(false);
		}
		else {
			this.setCompleted(true);
		}
	}

	public String convertTaskPriorityEnumToString(TaskPriority taskPriority) {
		return String.valueOf(taskPriority.toString());
	}


	public String getCategory() {
		return mCategory;
	}

	public void setCategory(String mCategory) {
		this.mCategory = mCategory;
	}
}
