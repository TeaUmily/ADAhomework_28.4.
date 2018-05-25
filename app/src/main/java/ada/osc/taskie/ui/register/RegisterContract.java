package ada.osc.taskie.ui.register;

import ada.osc.taskie.model.RegistrationToken;

public interface RegisterContract {

    interface View{

        void showUserInvalidError();

        void onUserRegistered();

        void showNetworkError();
    }

    interface Presenter{

        void setView(RegisterContract.View registerView);

        void registerUser(RegistrationToken token);

    }
}
