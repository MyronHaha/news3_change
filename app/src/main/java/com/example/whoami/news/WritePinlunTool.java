package com.example.whoami.news;



import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class WritePinlunTool extends Fragment {

    private ChangeView changeView;
    TextView editText = null;
    View view = null;
    public WritePinlunTool() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        changeView = (ChangeView)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.writepinglun,container,false);
        initData();
        return view;
    }

    private void initData() {
        editText = (TextView) view.findViewById(R.id.editText);
        editText.clearFocus();
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changeView.change();
            }
        });
    }

    public void loss() {
        editText.clearFocus();
    }

    public interface ChangeView{
        abstract void change();

    }
}

