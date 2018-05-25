package ada.osc.taskie.persistance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ada.osc.taskie.model.Task;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class TaskRepository {

	private static TaskRepository sRepository = null;
	private Realm mRealm;

	private TaskRepository(){
		mRealm = Realm.getDefaultInstance();
	}

	public static synchronized TaskRepository getInstance(){
		if(sRepository == null){
			sRepository = new TaskRepository();
		}
		return sRepository;
	}

	public Task getTaskById(String id){
		Task task= mRealm
				.where(Task.class)
				.equalTo("mId",id)
				.findFirst();
		return task;
	}

	public List<Task> getTasks() {
		RealmResults<Task> tasks = mRealm.where(Task.class).findAll();
		return tasks;
	}

	public void saveTask(Task task) {
		mRealm.beginTransaction();
		mRealm.copyToRealm(task);
		mRealm.commitTransaction();
	}

	public void removeTask(String id) {
		mRealm.beginTransaction();
		Task task = mRealm.where(Task.class).equalTo("mId", id).findFirst();
		task.deleteFromRealm();
		mRealm.commitTransaction();
	}

/*	public List<Task> getSortedTasksHighPriorityFirst(){

		RealmResults<Task> realmResults = mRealm.where(Task.class).findAll();
		List<Task> sortedTasks = new ArrayList<>();

		for(Task results : realmResults) {
			sortedTasks.add(results);
		}

		Collections.sort(sortedTasks, new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
				return o1.getPriority() > o2.getPriority();
			}
		});

		return sortedTasks;
	}*/

	/*public List<Task> getSortedTasksLowPriorityFirst(){
		List<Task> tasks = getSortedTasksHighPriorityFirst();
		Collections.reverse(tasks);
		return tasks;
	}*/

	public List<Task> filterUncompletedTasks(){
		RealmQuery<Task> query = mRealm.where(Task.class);
		query.equalTo("mCompleted", false);
		List<Task> tasks = query.findAll();
		return tasks;
	}

	public void updateTaskState(Task task) {
		mRealm.beginTransaction();
		Task taskToUpdate = mRealm.where(Task.class).equalTo("mId", task.getId()).findFirst();
		taskToUpdate.changeState();
		mRealm.commitTransaction();
	}

	public void changeTaskPriority(Task task) {
		mRealm.beginTransaction();
		Task taskToChangePriority= mRealm.where(Task.class).equalTo("mId", task.getId()).findFirst();
		taskToChangePriority.setNextPriority();
		mRealm.commitTransaction();
	}

	public void updateTask(Task task) {
		mRealm.beginTransaction();
		Task taskToUpdate = mRealm.where(Task.class).equalTo("mId", task.getId()).findFirst();
		taskToUpdate.setTitle(task.getTitle());
		taskToUpdate.setPriority(task.getPriority());
		taskToUpdate.setDescription(task.getDescription());
		taskToUpdate.setDueDate(task.getDueDate());
		mRealm.commitTransaction();
	}

}
