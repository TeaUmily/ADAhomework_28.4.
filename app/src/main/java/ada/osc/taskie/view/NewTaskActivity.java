package ada.osc.taskie.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ada.osc.taskie.R;
import ada.osc.taskie.TaskRepository;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTaskActivity extends AppCompatActivity {

	public static String EXTRA_TASK_ID ="task id";

	@BindView(R.id.edittext_newtask_title)	EditText mTitleEntry;
	@BindView(R.id.edittext_newtask_description) EditText mDescriptionEntry;
	@BindView(R.id.spinner_newtask_priority) Spinner mPriorityEntry;
	@BindView(R.id.edittext_newtask_dueDate) EditText mDueDate;

	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	TaskRepository mRepository = TaskRepository.getInstance();
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);
		ButterKnife.bind(this);
		setUpSpinnerSource();

		intent = getIntent();
		if(checkIntent()){
            setViews();
        }

	}

    private boolean checkIntent() {
	    if (intent.hasExtra(EXTRA_TASK_ID)){
	        return true;
        }
        else {
	        return false;
	    }
    }


    private void setUpSpinnerSource() {
		mPriorityEntry.setAdapter(
				new ArrayAdapter<TaskPriority>(
						this, android.R.layout.simple_list_item_1, TaskPriority.values()
				)
		);
		mPriorityEntry.setSelection(0);
	}

		public void setViews(){

	    int id= intent.getIntExtra(EXTRA_TASK_ID, 10);

		Task task = mRepository.getTaskById(id);

		mTitleEntry.setText(task.getTitle());
		mDescriptionEntry.setText(task.getDescription());
		mPriorityEntry.setSelection(task.getPriority().ordinal());
		String date_str= formatter.format(task.getDueDate());
		mDueDate.setText(date_str);
	}



	@OnClick(R.id.imagebutton_newtask_savetask)
	public void saveTask(){

		try{
		checkInput();


		String title = mTitleEntry.getText().toString();
		String description = mDescriptionEntry.getText().toString();
		TaskPriority priority = (TaskPriority) mPriorityEntry.getSelectedItem();
		Date date = formatter.parse(mDueDate.getText().toString());

		checkDateInput(date);

		if(checkIntent()){
		    Task task = mRepository.getTaskById(intent.getIntExtra(EXTRA_TASK_ID,10));
		    task.setTitle(title);
		    task.setDescription(description);
		    task.setPriority(priority);
		    task.setDueDate(date);
            setResult(RESULT_OK);
		    finish();
        }

		Task newTask = new Task(title, description, priority, date);
		Intent saveTaskIntent = new Intent(this, TasksActivity.class);
		saveTaskIntent.putExtra(TasksActivity.EXTRA_TASK, newTask);
		setResult(RESULT_OK, saveTaskIntent);
		finish();

		}catch (EmptyFieldException e){

			Toast toast= Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT);
			toast.show();

		}catch (ParseException e) {

			Toast toast = Toast.makeText(this, "Invalid date format!", Toast.LENGTH_SHORT);
			toast.show();

		}catch (InvalidDateException e){

			Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
			toast.show();

		}
	}

	private void checkDateInput(Date date)throws InvalidDateException {
		Date today = new Date();

		if(date.compareTo(today) < 0){
			throw new InvalidDateException("Invalid date!");
		}
	}


	@OnClick (R.id.edittext_newtask_dueDate)
	public void pickDueDate(){

		final Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);

		DatePickerDialog picker = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						mDueDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
					}
				}, year, month, day);
		picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
		picker.show();

	}


	private void checkInput() throws EmptyFieldException {
		if(mTitleEntry.getText().toString().isEmpty() || mDescriptionEntry.getText().toString().isEmpty() || mDueDate.getText().toString().isEmpty()){
			throw new EmptyFieldException("You have left a empty field!");
		}
	}
}
