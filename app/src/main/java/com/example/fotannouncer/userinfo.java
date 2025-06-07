package com.example.fotannouncer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class userinfo extends AppCompatActivity {
    private ImageButton back;
    private Button logout;
    private Button editinfo;
    Dialog dialog;
    Dialog edit;
    Button cancel,ok;
    Button canceledit,okedit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userinfo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back = findViewById(R.id.backbutton2); // Correctly initialize the login button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userinfo.this, News.class);
                startActivity(intent);
            }
        });
        logout = findViewById(R.id.signout); // Correctly initialize the login button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.show();
            }
        });
        dialog=new Dialog(userinfo.this);
        dialog.setContentView(R.layout.dialogboxout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialogboxbackground));
        dialog.setCancelable(false);

        cancel=dialog.findViewById(R.id.cancel);
        ok=dialog.findViewById(R.id.ok);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userinfo.this, MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        edit=new Dialog(userinfo.this);
        edit.setContentView(R.layout.editdialog);

        WindowManager.LayoutParams layoutParams = edit.getWindow().getAttributes();
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL; // Align to top and center horizontally
        layoutParams.y = 150; // Set the Y offset from the top (adjust this value as needed)

        edit.getWindow().setAttributes(layoutParams);
        edit.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        edit.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialogboxbackground));
        edit.setCancelable(false);

        canceledit=edit.findViewById(R.id.canceledigt);
        okedit=edit.findViewById(R.id.okedit);

        canceledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.dismiss();
            }
        });
//        okedit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(userinfo.this, userinfo.class);
//                startActivity(intent);
//                dialog.dismiss();
//            }
//        });
        editinfo = findViewById(R.id.editinfo); // Correctly initialize the login button
        editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.show();
            }
        });
    }
}
