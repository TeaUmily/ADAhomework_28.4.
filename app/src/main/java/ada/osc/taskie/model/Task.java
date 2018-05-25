package ada.osc.taskie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class Task extends RealmObject  implements Serializable{

	private static int sID = 0;

	@Expose
	@SerializedName("id")
	private String mId;
	@Expose
	@SerializedName("title")
	private String mTitle;
	@Expose
	@SerializedName("content")
	private String mDescription;
	@Expose
	@SerializedName("isCompleted")
	private boolean mCompleted;
	@Expose
	@SerializedName("dueDate")
	private String mDueDate;
	@Expose
	@SerializedName("taskPriority")
	private int mPriority;
	@Expose
	@SerializedName("isFavorite")
	private boolean mFavorite;
	private String mCategory;

	public Task(String title, String description, TaskPriority priority, String dueDate) {
		mId = UUID.randomUUID().toString();
		mTitle = title;
		mFavorite= false;
		mDescription = description;
		mCompleted = false;
		mPriority = priority.getValue();
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


	public String getDueDate(){
		return mDueDate;
	}

	public void setDueDate(String dueDate) {
		this.mDueDate = dueDate;
	}


	public void setNextPriority(){
		switch(getPriority()){
			case 1: mPriority = TaskPriority.MEDIUM.getValue(); break;
			case 2: mPriority = TaskPriority.HIGH.getValue(); break;
			case 3: mPriority = TaskPriority.LOW.getValue(); break;
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

	public String getCategory() {
		return mCategory;
	}

	public void setCategory(String mCategory) {
		this.mCategory = mCategory;
	}

	public int getPriority() {
		return mPriority;
	}

	public void setPriority(int mPriority) {
		this.mPriority = mPriority;
	}

	public boolean isFavorite() {
		return mFavorite;
	}

	public void setFavorite(boolean mFavorite) {
		this.mFavorite = mFavorite;

	}
}
