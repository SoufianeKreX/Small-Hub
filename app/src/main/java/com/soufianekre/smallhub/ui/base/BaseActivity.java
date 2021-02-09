package com.soufianekre.smallhub.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.helper.AppHelper;
import com.soufianekre.smallhub.helper.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;
import com.soufianekre.smallhub.ui.widgets.progress.ProgressDialogFragment;

import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

public abstract class BaseActivity extends AppCompatActivity implements BaseMvpView {

    public boolean isProgressShowing = false;
    private BasePresenter<BaseMvpView> mPresenter;
    private Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new BasePresenter<>();
        mPresenter.onAttach(this);
        if (savedInstanceState == null) {
            mPresenter.onCheckGitHubStatus();
        }
        // Restore State

    }


    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onError(String message) {
        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar("Error");
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toasty.info(this, message, Toast.LENGTH_LONG).show();
        } else {
            Toasty.info(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    public void showError(String message){
        if (message != null) {
            Toasty.error(this, message, Toast.LENGTH_LONG).show();
        } else {
            Toasty.error(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    protected void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void showProgress(int resId) {
        String msg = getString(R.string.in_progress);
        if (resId != 0) {
            msg = getString(resId);
        }
        if (!isProgressShowing && !isFinishing()) {
            ProgressDialogFragment fragment = (ProgressDialogFragment)
                    AppHelper.getFragmentByTag(getSupportFragmentManager(),
                    ProgressDialogFragment.TAG);
            if (fragment == null) {
                isProgressShowing = true;
                fragment = ProgressDialogFragment.newInstance(msg, false);
                fragment.show(getSupportFragmentManager(), ProgressDialogFragment.TAG);
            }
        }

    }

    @Override
    public void hideProgress() {
        ProgressDialogFragment fragment = (ProgressDialogFragment)
                AppHelper.getFragmentByTag(getSupportFragmentManager(),
                ProgressDialogFragment.TAG);
        if (fragment != null) {
            isProgressShowing = false;
            fragment.dismiss();
        }
    }

    @Override
    public boolean checkInternetConnection() {
        return NetworkUtils.checkInternetConnection(getApplicationContext());
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        snackbar.show();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }
}