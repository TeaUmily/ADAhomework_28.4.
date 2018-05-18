package ada.osc.taskie;

import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.model.TaskCategory;
import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryRepository {

    private static CategoryRepository sRepository = null;

    private Realm mRealm;


    private CategoryRepository(){
        mRealm = Realm.getDefaultInstance();
    }

    public static synchronized CategoryRepository getInstance(){
        if(sRepository == null){
            sRepository = new CategoryRepository();
        }
        return sRepository;
    }

    public void addNewCategory(String categoryName){
        TaskCategory taskCategory = new TaskCategory(categoryName);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(taskCategory);
        mRealm.commitTransaction();
    }

    public List<String> getAllCategories(){
        RealmResults <TaskCategory> results = mRealm.where(TaskCategory.class).findAll();
        List <TaskCategory> allCategoriesList = mRealm.copyFromRealm(results);
        List <String> allCategoriesStringList = new ArrayList<>();
        for (TaskCategory tC: allCategoriesList
                ) {allCategoriesStringList.add(tC.getCategoryName());
        }
        return  allCategoriesStringList;
    }

}
