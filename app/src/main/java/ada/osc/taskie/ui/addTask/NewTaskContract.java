package ada.osc.taskie.ui.addTask;

import ada.osc.taskie.model.Task;

public interface NewTaskContract  {

    interface View {

        void onTaskCreated();

        void showNetworkError();

        void showTaskError();

        void onGetTaskById(Task task);
    }

    interface Presenter{

        void setView(View newTaskView);

        void getTaskById(String id);

        void createTask(Task task);

        void editTask(Task task);



    }


}
