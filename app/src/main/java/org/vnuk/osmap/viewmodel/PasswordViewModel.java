package org.vnuk.osmap.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.vnuk.osmap.model.Password;

public class PasswordViewModel extends AndroidViewModel {
    private MutableLiveData<String> mldPassword;

    public PasswordViewModel(@NonNull Application application) {
        super(application);
        mldPassword = new MutableLiveData<>();
    }

    public void submitPassword(String passValue) {
        Password password = new Password(passValue);
        if (password.isPasswordValid())
            mldPassword.setValue(passValue);
        else
            mldPassword.setValue("");
    }

    public MutableLiveData<String> getMldPassword() {
        return mldPassword;
    }
}
