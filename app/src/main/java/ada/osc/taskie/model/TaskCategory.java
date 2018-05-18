package ada.osc.taskie.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TaskCategory extends RealmObject {

    @PrimaryKey
    private String mCategoryName;

    public TaskCategory(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public TaskCategory() {
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }


}
