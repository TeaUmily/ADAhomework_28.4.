package ada.osc.taskie;

import android.view.ViewGroup;

import java.util.List;

import ada.osc.taskie.model.Task;
import ada.osc.taskie.persistance.FakeDatabase;
import ada.osc.taskie.model.TaskGenerator;

public class TaskRepository {

	private static TaskRepository sRepository = null;
	private static final int INITIAL_TASK_COUNT = 10;

	private FakeDatabase mDatabase;

	private TaskRepository(){
		mDatabase = new FakeDatabase();
		mDatabase.save(TaskGenerator.generate(INITIAL_TASK_COUNT));
	}

	public static synchronized TaskRepository getInstance(){
		if(sRepository == null){
			sRepository = new TaskRepository();
		}
		return sRepository;
	}

	public Task getTaskById(int id){
		return mDatabase.getTaskById(id);
	}
	public List<Task> getTasks() {
		return mDatabase.getTasks();
	}

	public void saveTask(Task task) {
		mDatabase.save(task);
	}

	public void removeTask(Task task) {
		mDatabase.delete(task);
	}

	public void sortTasksHighFirst(){
		mDatabase.sortHighPriortiyFirst();
	}

	public void sortTasksLowFirst(){
		mDatabase.sortLowPriorityFirst();
	}

	public List<Task> filterUncompletedTasks(){
		return mDatabase.filterUncompletedTasks();
	}

    public void updateTaskState(Task task) {
		mDatabase.updateTaskState(task);
    }

	public void changeTaskPriority(Task task) {
		mDatabase.changeTaskPriority(task);
	}
}
