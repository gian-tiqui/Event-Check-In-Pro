package com.example.mlseriesdemonstrator.activities.host;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.LoadingActivity2;
import com.example.mlseriesdemonstrator.activities.student.ActivateStudentAccountActivity;

public class ActivateEmployeeAccountActivity extends AppCompatActivity {

  Context context;
  EditText employeeIdTxt;
  TextView notEmployeeTxt;
  Button checkEmployeeBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activate_employee);

    context = ActivateEmployeeAccountActivity.this;

    employeeIdTxt = findViewById(R.id.EMPLOYEE_ID);
    notEmployeeTxt = findViewById(R.id.NOT_EMPLOYEE);
    checkEmployeeBtn = findViewById(R.id.CHECK_EMPLOYEE);

    notEmployeeTxt.setOnClickListener(v -> {
      startActivity(new Intent(context, ActivateStudentAccountActivity.class));
      finish();
    });

    checkEmployeeBtn.setOnClickListener(v -> {
      String employeeIDStr = employeeIdTxt.getText().toString();

      Intent intent = new Intent(context, LoadingActivity2.class);

      if (employeeIDStr.isEmpty()) {
        employeeIdTxt.setError("Please enter your employee ID");
        return;
      }

      intent.putExtra("employee_id", employeeIDStr);
      intent.putExtra("mode", "host");

      startActivity(intent);
      finish();
    });
  }
}