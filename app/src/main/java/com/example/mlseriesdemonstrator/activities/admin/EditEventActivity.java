package com.example.mlseriesdemonstrator.activities.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.dialogs.CourseDepartmentDialog;
import com.example.mlseriesdemonstrator.geofence.MapsActivity;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.model.Location;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.Objects;

public class EditEventActivity extends AppCompatActivity implements CourseDepartmentDialog.CourseDepartmentListener {

  private static final String TAG = "EditEventActivity";
  TextView hostedByTxt;
  EditText eventTitleTxt;
  EditText eventDateTxt;
  EditText eventTimeTxt;
  EditText locationTxt;
  EditText departmentAndCourseTxt;
  EditText eventStatusTxt;
  private int hour;
  private int minute;
  Event event;
  Context context;
  Button editEventBtn;
  Button deleteEventBtn;
  private Location selectedLocation;
  EditText eventModeTxt;
  private String selectedDepartment;
  private String selectedCourse;
  private String selectedStatus;
  String hostedByStr;
  private String selectedMode;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_event);

    eventTitleTxt = findViewById(R.id.EVENT_TITLE);
    eventDateTxt = findViewById(R.id.EVENT_DATE);
    eventTimeTxt = findViewById(R.id.EVENT_TIME);
    eventModeTxt = findViewById(R.id.EVENT_MODE);
    locationTxt = findViewById(R.id.EVENT_LOCATION);
    departmentAndCourseTxt = findViewById(R.id.DEPARTMENT_AND_COURSE);
    eventStatusTxt = findViewById(R.id.STATUS);
    hostedByTxt = findViewById(R.id.HOSTED_BY);
    editEventBtn = findViewById(R.id.EDIT_EVENT);
    deleteEventBtn = findViewById(R.id.DELETE_EVENT);
    context = EditEventActivity.this;

    String eventId = getIntent().getStringExtra("event_id");

    EventManager.getEventByEventId(eventId, context, events -> {
      if (events != null)
        event = events.get(0);

      setTexts();
    });

    eventDateTxt.setOnClickListener(v -> selectDate());

    eventTimeTxt.setOnClickListener(v -> selectTime());

    departmentAndCourseTxt.setOnClickListener(v -> openDeptAndCourseDialog());

    eventStatusTxt.setOnClickListener(v -> selectStatus());

    locationTxt.setOnClickListener(v -> {
      Intent intent = new Intent(context, MapsActivity.class);
      mapsActivityLauncher.launch(intent);
    });

    editEventBtn.setOnClickListener(v -> {

      AlertDialog.Builder builder = new AlertDialog.Builder(context);

      builder.setTitle(Html.fromHtml("<font color='#8ab4f8'>Edit this event?</font>"));
      builder.setMessage(Html.fromHtml("<font color='#8ab4f8'>Are you sure you want to edit this event?</font>"));

      builder.setPositiveButton("Yes", (dialog1, which) -> {
        editEventDetails();
        finish();
        dialog1.dismiss();
      });

      builder.setNegativeButton("No", (dialog12, which) -> dialog12.dismiss());

      AlertDialog dialog = builder.create();

      Objects.requireNonNull(
                      dialog.getWindow())
              .setBackgroundDrawable(
                      new ColorDrawable(Color.parseColor("#162c46")
                      )
              );

      dialog.show();
    });

    deleteEventBtn.setOnClickListener(v -> {
      AlertDialog.Builder builder = new AlertDialog.Builder(context);

      builder.setTitle(Html.fromHtml("<font color='#8ab4f8'>Delete this event?</font>"));
      builder.setMessage(Html.fromHtml("<font color='#8ab4f8'>Are you sure you want to Delete this event?</font>"));

      builder.setPositiveButton("Yes", (dialog, which) -> {
        deleteEvent();
        finish();
      });

      builder.setNegativeButton("No", ((dialog, which) -> dialog.dismiss()));

      AlertDialog dialog = builder.create();

      Objects.requireNonNull(
                      dialog.getWindow())
              .setBackgroundDrawable(
                      new ColorDrawable(Color.parseColor("#162c46")
                      )
              );

      dialog.show();
    });

  }

  private void deleteEvent() {

    CollectionReference reference = FirebaseFirestore.getInstance().collection("events");

    reference
            .document(event.getEventId())
            .delete()
            .addOnSuccessListener(unused -> {
              CollectionReference reference1 = FirebaseFirestore.getInstance().collection("event_id");

              reference1.document(event.getEventId()).delete();

              Utility.showToast(context, "Event deleted");
            })
            .addOnFailureListener(e -> Objects.requireNonNull(e.getLocalizedMessage()));
  }

  private void editEventDetails() {

    CollectionReference reference = FirebaseFirestore.getInstance().collection("events");

    Location location;

    if (selectedLocation == null)
      location = event.getLocation();
    else
      location = selectedLocation;

    if (selectedDepartment == null)
      selectedDepartment = event.getTargetDepartment();

    if (selectedCourse == null)
      selectedCourse = event.getTargetCourse();

    Event editedEvent = new Event(
            eventTitleTxt.getText().toString(),
            eventDateTxt.getText().toString(),
            eventTimeTxt.getText().toString(),
            location,
            event.getHostId(),
            eventStatusTxt.getText().toString(),
            event.getEventId(),
            selectedDepartment,
            selectedCourse
    );

    reference.document(event.getEventId())
            .set(editedEvent)
            .addOnCompleteListener(task -> {
              Utility.showToast(context, "Event edited");
              finish();
            })
            .addOnFailureListener(e -> Log.e(TAG, Objects.requireNonNull(e.getLocalizedMessage())));
  }

  private void selectStatus() {
    String[] choices = {"started", "upcoming", "cancelled", "ended"};

    AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Dialog);
    builder.setTitle("Select Status");
    builder.setItems(choices, (dialog, which) -> {
      selectedStatus = choices[which];
      eventStatusTxt.setText(selectedStatus);
    });

    AlertDialog dialog = builder.create();
    dialog.show();
  }


  private void setTexts() {

    getHostById(user -> {
      if (user != null) {
        hostedByStr = user.getFirstName() + " " + user.getLastName();
        hostedByTxt.setText(hostedByStr);
      }
    });

    final String ONLINE_EVENT = "Online Event";
    final String ONSITE_EVENT = "On-site Event";

    eventTitleTxt.setText(event.getTitle());
    eventDateTxt.setText(event.getDate());
    eventTimeTxt.setText(event.getStartTime());
    String deptAndCourse = event.getTargetDepartment() + " | " + event.getTargetCourse();
    departmentAndCourseTxt.setText(deptAndCourse);
    eventStatusTxt.setText(event.getStatus());
    if ( event.getLocation().getLocationAddress().equals(ONLINE_EVENT) ) {
      eventModeTxt.setText(ONLINE_EVENT);
    } else {
      eventModeTxt.setText(ONSITE_EVENT);
    }
    locationTxt.setText(event.getLocation().getLocationAddress());
  }

  private final ActivityResultLauncher<Intent> mapsActivityLauncher = registerForActivityResult(
          new ActivityResultContracts.StartActivityForResult(),
          result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
              Intent data = result.getData();
              if (data != null) {
                String locationName = data.getStringExtra("location_name");
                double longitude = data.getDoubleExtra("longitude", 0);
                double latitude = data.getDoubleExtra("latitude", 0);
                float locationRadius = data.getFloatExtra("location_geofence_radius", 150);

                LatLng latLng = new LatLng(latitude, longitude);

                // Check if the location data is not null
                if (locationName != null) {
                  // Update the locationTxt with the selected location name
                  locationTxt.setText(locationName);

                  // You can also store the selected location data in selectedLocation
                  selectedLocation = new Location(locationName, latLng, locationRadius);
                }
              }
            }
          }
  );

  private void openDeptAndCourseDialog() {

    CourseDepartmentDialog dialog = new CourseDepartmentDialog();

    dialog.show(getSupportFragmentManager(), "Course and Department Dialog");
  }

  private void selectTime() {

    TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay, minute1) -> {
      hour = hourOfDay;
      minute = minute1;
      String hourStr = (hour < 10) ? "0" + hour : String.valueOf(hour);
      String minuteStr = (minute < 10) ? "0" + minute : String.valueOf(minute);
      String startTimeStr = hourStr + ":" + minuteStr;

      eventTimeTxt.setText(startTimeStr);
    };

    TimePickerDialog timePickerDialog = new TimePickerDialog(
            context,
            android.R.style.Theme_Holo_Dialog,
            onTimeSetListener,
            hour,
            minute,
            true
    );

    Objects.requireNonNull(
            timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)
    );

    timePickerDialog.setTitle("Select Time");
    timePickerDialog.show();
  }

  private void selectDate() {

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog.OnDateSetListener dateSetListener = (view, year1, month1, dayOfMonth) -> {
      String date = year1 + "/" + (month1 + 1) + "/" + dayOfMonth;
      eventDateTxt.setText(date);
    };

    DatePickerDialog dialog = new DatePickerDialog(
            context,
            android.R.style.Theme_Holo_Dialog,
            dateSetListener,
            year,
            month,
            day
    );

    Objects.requireNonNull(
            dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)
    );

    dialog.setTitle("Select date");

    dialog.show();
  }

  @Override
  public void applyTexts(String department, String course) {
    String selectedTxt = department + " - " + course;
    selectedDepartment = department;
    selectedCourse = course;
    departmentAndCourseTxt.setText(selectedTxt);
  }

  private void getHostById(HostCallback callback) {

    CollectionReference reference = FirebaseFirestore.getInstance().collection("user_id");

    reference.get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
              for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String userId = documentSnapshot.getId();

                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .collection("user_details")
                        .whereEqualTo("institutionalID", event.getHostId())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots1 -> {
                          if (!queryDocumentSnapshots1.isEmpty()) {
                            User user = queryDocumentSnapshots1.getDocuments().get(0).toObject(User.class);
                            callback.onHostRetrieved(user);
                          }
                        })
                        .addOnFailureListener(e -> {
                          Log.e(TAG, "Error fetching user details: " + e.getMessage());
                          callback.onHostRetrieved(null);
                        });
              }
            });
  }

  public interface HostCallback {
    void onHostRetrieved(User user);
  }

  private void selectMode() {
    String[] modes = { "Online", "On-site" };

    AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Dialog);
    builder.setTitle("Select mode (Online or On-site)");
    builder.setItems(modes, (dialog, which) -> {
      selectedMode = modes[which];
      eventModeTxt.setText(selectedMode);

      if ( selectedMode.equals("Online") ) {
        final String ONLINE_EVENT = "Online Event";
        LatLng latLng = new LatLng(0, 0);
        locationTxt.setText(ONLINE_EVENT);

        selectedLocation = new Location(ONLINE_EVENT, latLng, 0);
      } else {
        locationTxt.setText("");
      }
    });

    AlertDialog dialog = builder.create();
    dialog.show();
  }

}