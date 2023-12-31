package com.example.mlseriesdemonstrator.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mlseriesdemonstrator.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CourseDepartmentDialog extends AppCompatDialogFragment {

  private static final String TAG = "CourseDepartmentDialog";
  Spinner departmentSpinner;
  Spinner courseSpinner;
  private HashMap<String, ArrayList<String>> departments;
  private CourseDepartmentListener listener;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Dialog);
    LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
    View view = layoutInflater.inflate(R.layout.layout_course_and_dept_dialog, null);

    builder.setView(view)
            .setTitle("Select Department and Course")
            .setNegativeButton("Cancel", (dialog, which) -> {

            })
            .setPositiveButton("Confirm", (dialog, which) -> {

            });

    departmentSpinner = view.findViewById(R.id.DEPARTMENTS);
    courseSpinner = view.findViewById(R.id.COURSES);

    setCoursesPerDept();
    setSpinners();

    return builder.create();
  }

  private void setCoursesPerDept() {

    departments = new HashMap<>();

    final ArrayList<String> ALL = new ArrayList<>();

    ALL.add("ALL");

    final ArrayList<String> CITCS = new ArrayList<>();

    CITCS.add("BSCS");
    CITCS.add("BSIT");
    CITCS.add("ACT");
    CITCS.add("ALL");

    final ArrayList<String> CBA = new ArrayList<>();

    CBA.add("BSBA");
    CBA.add("ALL");

    final ArrayList<String> CAS = new ArrayList<>();

    CAS.add("BACOMM");
    CAS.add("BSPSYCH");
    CAS.add("ALL");

    final ArrayList<String> graduateStudies = new ArrayList<>();

    graduateStudies.add("MBA");
    graduateStudies.add("MIT");
    graduateStudies.add("ALL");

    final ArrayList<String> CCJ = new ArrayList<>();

    CCJ.add("BSCRIM");
    CCJ.add("ALL");

    final ArrayList<String> CTE = new ArrayList<>();

    CTE.add("BEED");
    CTE.add("BSED");
    CTE.add("ALL");

    departments.put("ALL", ALL);
    departments.put("CITCS", CITCS);
    departments.put("CBA", CBA);
    departments.put("CAS", CAS);
    departments.put("Graduate Studies", graduateStudies);
    departments.put("CCJ", CCJ);
    departments.put("CTE", CTE);
  }

  private void setSpinners() {

    String[] departmentsStr = departments.keySet().toArray(new String[0]);

    ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(
            requireActivity(),
            R.layout.spinner_item,
            departmentsStr
    );

    departmentAdapter.setDropDownViewResource(R.layout.spinner_item);

    departmentSpinner.setAdapter(departmentAdapter);

    departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String department = parent.getItemAtPosition(position).toString();

        String[] coursesStr = Objects.requireNonNull(departments.get(department)).toArray(new String[0]);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(
                requireActivity(),
                R.layout.spinner_item,
                coursesStr
        );

        courseAdapter.setDropDownViewResource(R.layout.spinner_item);

        courseSpinner.setAdapter(courseAdapter);

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String course = parent.getItemAtPosition(position).toString();

            if (listener != null) {
              listener.applyTexts(department, course);
            }

          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
        });
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  public interface CourseDepartmentListener {
    void applyTexts(String department, String course);
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);

    try {
      listener = (CourseDepartmentListener) context;
    } catch (ClassCastException e) {
      Log.d(TAG, "onAttach Failed: " + context);
    }
  }
}
