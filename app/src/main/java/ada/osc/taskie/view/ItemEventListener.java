package ada.osc.taskie.view;

import ada.osc.taskie.model.Task;

public interface ItemEventListener {
   void onTaskSwipeRight(Task task);
   void onTaskSwipeLeft(Task task);
   void onClick(Task task);
   void onToggleClick(Task task);
   void onPriorityColorClick(Task task);
}
