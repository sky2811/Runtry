package sky.runtry.fragments;

/**
 * Created by pruthvishpatel on 11/9/17.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sky.runtry.R;


public class SelectActivity extends Fragment {

    public SelectActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_select, container, false);
        return rootView;
    }
}