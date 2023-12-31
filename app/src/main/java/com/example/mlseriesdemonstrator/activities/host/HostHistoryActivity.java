package com.example.mlseriesdemonstrator.activities.host;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.EventAdapter;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;

public class HostHistoryActivity extends AppCompatActivity {

  Context context;
  RecyclerView hostedEvents;
  Button backBtn;
  User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_host_history);

    context = HostHistoryActivity.this;
    hostedEvents = findViewById(R.id.HOSTED_EVENTS);
    backBtn = findViewById(R.id.BACK_BTN);
    user = Utility.getUser();

    EventManager.getEventsByHostId(user.getInstitutionalID(), context, events -> {
      if (!events.isEmpty()) {
        hostedEvents.setLayoutManager(new LinearLayoutManager(context));
        hostedEvents.setAdapter(new EventAdapter(context, events));
      } else {
        Utility.showToast(context, "You have no hosted events.");
      }
    });

    backBtn.setOnClickListener(v -> finish());
  }
}