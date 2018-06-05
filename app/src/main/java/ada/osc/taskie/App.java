package ada.osc.taskie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.interaction.ApiInteractorImpl;
import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import io.realm.Realm;
import retrofit2.Retrofit;

public class App extends Application {

    private static ApiInteractor apiInteractor;

    private static SharedPreferences preferences;
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();

        final Retrofit retrofit = RetrofitUtil.createRetrofit();
        final ApiService apiService = retrofit.create(ApiService.class);

        mContext = this.getBaseContext();

        Realm.init(this);
        apiInteractor = new ApiInteractorImpl(apiService);

        preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public static ApiInteractor getApiInteractor() {
        return apiInteractor;
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    public static boolean isInternetConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
