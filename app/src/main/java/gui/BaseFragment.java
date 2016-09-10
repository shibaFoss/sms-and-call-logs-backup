package gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import core.App;

public abstract class BaseFragment extends Fragment {

    protected View layoutView;
    private BaseActivity baseActivity;


    /**
     * The method gets called by the {@link BaseFragment} for getting the
     * fragment layout resource Id.
     *
     * @return the fragment layout resource id.
     */
    protected abstract int getLayoutResId();


    /**
     * The method gets called by the {@link BaseFragment} after completion of the
     * fragment layout loading.
     *
     * @param layoutView         the loaded fragment layout view.
     * @param savedInstanceState the savedInstanceState {@link Bundle} of the fragment.
     */
    protected abstract void onAfterLayoutLoad(View layoutView, Bundle savedInstanceState);


    @Override
    public View onCreateView(LayoutInflater layoutInflater,
                             ViewGroup layoutContainer, Bundle savedInstanceState) {
        this.baseActivity = (BaseActivity) getActivity();
        return layoutInflater.inflate(getLayoutResId(), layoutContainer, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.layoutView = view;
        this.onAfterLayoutLoad(view, savedInstanceState);
    }


    public BaseActivity getBaseActivity() {
        return this.baseActivity;
    }


    public App getApp() {
        return this.getBaseActivity().getApp();
    }

}
