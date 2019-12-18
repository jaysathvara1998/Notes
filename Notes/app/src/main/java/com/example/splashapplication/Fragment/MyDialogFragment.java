package com.example.splashapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.splashapplication.Activities.AddTaskActivity;
import com.example.splashapplication.Adapter.DBAdapter;
import com.example.splashapplication.R;

public class MyDialogFragment extends DialogFragment {
    EditText etCategory;
    DBAdapter sqLiteHelper;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {

        if (getArguments() != null) {
            if (getArguments().getBoolean("notAlertDialog")) {
                return super.onCreateDialog(savedInstanceState);
            }
        }

        final Dialog myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.add_category);
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);
        etCategory= (EditText) myDialog.findViewById(R.id.etCategory);
        Button login = (Button) myDialog.findViewById(R.id.btnAddCategory);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stCategory = etCategory.getText().toString();
                if (!stCategory.isEmpty()){
                    sqLiteHelper.addCategory(stCategory);
                    Intent i = new Intent(getActivity(), AddTaskActivity.class);
                    startActivity(i);
                    myDialog.dismiss();
                }
            }
        });
        myDialog.show();
        myDialog.setCanceledOnTouchOutside(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert Dialog");
        builder.setMessage("Alert Dialog inside DialogFragment");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_category, container, false);
        sqLiteHelper = new DBAdapter(getActivity());
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                return true;
            }
        });
        toolbar.setTitle("Add Category");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final EditText editText = view.findViewById(R.id.etCategory);

        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("category")))
            editText.setText(getArguments().getString("category"));

        Button btnDone = view.findViewById(R.id.btnAddCategory);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogListener dialogListener = (DialogListener) getActivity();
                dialogListener.onFinishEditDialog(editText.getText().toString());
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("API123", "onCreate");

        boolean setFullScreen = false;
        if (getArguments() != null) {
            setFullScreen = getArguments().getBoolean("fullScreen");
        }

        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface DialogListener {
        void onFinishEditDialog(String inputText);
    }
}
