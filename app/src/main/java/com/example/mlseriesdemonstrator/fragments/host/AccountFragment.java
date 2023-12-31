package com.example.mlseriesdemonstrator.fragments.host;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.ChangePasswordActivity;
import com.example.mlseriesdemonstrator.activities.SplashScreenActivity;
import com.example.mlseriesdemonstrator.activities.host.HostHistoryActivity;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

  Context context;
  TextView fullNameTxt;
  TextView roleTxt;
  TextView eventCount;
  Button historyBtn;
  Button resetPasswordBtn;
  Button logoutBtn;
  User user;
  TextView nameAcronymTxt;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(
          LayoutInflater inflater,
          ViewGroup container,
          Bundle savedInstanceState
  ) {

    View view = inflater.inflate(R.layout.fragment_account2, container, false);

    context = getActivity();
    user = Utility.getUser();

    // Make elements interactive in the screen
    fullNameTxt = view.findViewById(R.id.HOST_FULL_NAME);
    roleTxt = view.findViewById(R.id.ROLE);
    eventCount = view.findViewById(R.id.EVENT_COUNT);
    historyBtn = view.findViewById(R.id.HOST_HISTORY);
    resetPasswordBtn = view.findViewById(R.id.HOST_RESET_PASSWORD);
    logoutBtn = view.findViewById(R.id.LOGOUT);
    nameAcronymTxt = view.findViewById(R.id.NAME_ACR);

    // Set the details of the host
    setTexts();

    historyBtn.setOnClickListener(
            v -> startActivity(new Intent(context, HostHistoryActivity.class))
    );

    resetPasswordBtn.setOnClickListener(
            v -> startActivity(new Intent(context, ChangePasswordActivity.class))
    );

    logoutBtn.setOnClickListener(v -> {

      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

      firebaseAuth.signOut();

      startActivity(
              new Intent(
                      getActivity(),
                      SplashScreenActivity.class
              )
      );

      requireActivity().finish();
    });

    return view;
  }

  private void setTexts() {

    String fullName = user.getFirstName() + " " + user.getLastName();
    String nameAcr = String.valueOf(user.getFirstName().charAt(0)) + user.getLastName().charAt(0);

    fullNameTxt.setText(fullName);
    nameAcronymTxt.setText(nameAcr);

    EventManager.getEventsByHostId(user.getInstitutionalID(), context, events -> {
      if (!events.isEmpty()) {
        String eventCountStr = String.valueOf(events.size());

        eventCount.setText(eventCountStr);
      }
    });
  }
}