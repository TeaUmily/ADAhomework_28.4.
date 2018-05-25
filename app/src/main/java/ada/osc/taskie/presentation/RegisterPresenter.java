package ada.osc.taskie.presentation;

import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.RegistrationToken;
import ada.osc.taskie.ui.register.RegisterContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenter implements RegisterContract.Presenter {

    private final ApiInteractor interactor;

    private RegisterContract.View registerView;

    public RegisterPresenter(ApiInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(RegisterContract.View registerView) {
        this.registerView = registerView;
    }

    @Override
    public void registerUser(RegistrationToken token) {
        if (token != null && !token.email.isEmpty()
                && !token.password.isEmpty()
                && !token.userName.isEmpty()) {
            createNewUser(token);
        } else {
            registerView.showUserInvalidError();
        }
    }

    private void createNewUser(RegistrationToken token) {
        interactor.registerUser(token, getRegisterCallback());
    }

    private Callback<RegistrationToken> getRegisterCallback() {
        return new Callback<RegistrationToken>() {
            @Override
            public void onResponse(Call<RegistrationToken> call, Response<RegistrationToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    registerView.onUserRegistered();
                }
            }

            @Override
            public void onFailure(Call<RegistrationToken> call, Throwable t) {
                registerView.showNetworkError();
            }
        };
    }

}
