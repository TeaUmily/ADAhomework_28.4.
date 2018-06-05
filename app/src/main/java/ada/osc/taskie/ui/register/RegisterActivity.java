package ada.osc.taskie.ui.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.model.RegistrationToken;
import ada.osc.taskie.presentation.RegisterPresenter;
import ada.osc.taskie.ui.login.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    @BindView(R.id.user_email)
    EditText mUserEmail;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.user_password)
    EditText mUserPwd;

    private RegisterContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        mPresenter = new RegisterPresenter(App.getApiInteractor());
        mPresenter.setView(this);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (!isFirstRun) {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        getSupportActionBar().hide();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.login_textview)
    void onLoginTextClick(){
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();
        startLoginActivity();
    }

    @OnClick(R.id.registration_button)
    void onRegisterButtonClick() {
        registerUser();
    }

    private void registerUser() {
        RegistrationToken user = new RegistrationToken(mUsername.getText().toString(),
                mUserEmail.getText().toString(),
                mUserPwd.getText().toString());

        mPresenter.registerUser(user);
    }

    private void startLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void showUserInvalidError() {
        Toast.makeText(this, "User is not valid", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserRegistered() {
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(this, "Network error, please try again", Toast.LENGTH_SHORT).show();
    }
}
