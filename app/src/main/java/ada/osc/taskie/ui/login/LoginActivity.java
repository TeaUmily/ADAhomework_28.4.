package ada.osc.taskie.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.model.RegistrationToken;
import ada.osc.taskie.presentation.LoginPresenter;
import ada.osc.taskie.ui.register.RegisterActivity;
import ada.osc.taskie.ui.tasks.mainActivity.TasksActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    @BindView(R.id.user_email)
    EditText mUserEmail;
    @BindView(R.id.user_password)
    EditText mUserPwd;

    LoginContract.Presenter mPresenter;
    private String REGISTER_ACTION = "register action";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter(App.getApiInteractor(), App.getPreferences());
        mPresenter.setView(this);

        ButterKnife.bind(this);

        getSupportActionBar().hide();
    }


    @OnClick(R.id.button_login)
    void onLoginButtonClick(){
        loginUser();
    }

    @OnClick(R.id.registration_button)
    void onRegisterButtonClick() {

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", true).commit();
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void loginUser() {
        RegistrationToken login = new RegistrationToken("", mUserEmail.getText().toString(), mUserPwd.getText().toString());
        mPresenter.loginUser(login);
    }

    @Override
    public void onUserLoggedIn() {
        startActivity(new Intent(this, TasksActivity.class));
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(this, "Network error, please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserInvalidError() {
        Toast.makeText(this, "User is not valid", Toast.LENGTH_SHORT).show();
    }
}