package ada.osc.taskie.presentation;

import android.content.SharedPreferences;

import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.LoginResponse;
import ada.osc.taskie.model.RegistrationToken;
import ada.osc.taskie.ui.login.LoginContract;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements LoginContract.Presenter {

    private final ApiInteractor mApiInteractor;
    private final SharedPreferences mPreferences;

    private LoginContract.View mLoginView;

    public LoginPresenter(ApiInteractor mApiInteractor, SharedPreferences mPreferences) {
        this.mApiInteractor = mApiInteractor;
        this.mPreferences = mPreferences;
    }


    @Override
    public void setView(LoginContract.View loginView) {
        this.mLoginView = loginView;
    }

    @Override
    public void loginUser(RegistrationToken login) {
        if (!login.email.isEmpty()
                && !login.password.isEmpty()) {
            mApiInteractor.loginUser(login, getLoginCallback());
        } else {
            mLoginView.showUserInvalidError();
        }

    }

    private Callback<LoginResponse> getLoginCallback() {
        return new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null ) {
                    mPreferences.edit().putString(SharedPrefsUtil.TOKEN, response.body().mToken).apply();

                    mLoginView.onUserLoggedIn();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mLoginView.showNetworkError();
            }
        };
    }
}
