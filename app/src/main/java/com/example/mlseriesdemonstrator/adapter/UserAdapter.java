package com.example.mlseriesdemonstrator.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.admin.UserDetailsActivity;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

  static final String TAG = "UserAdapter";
  List<User> originalUsers;
  List<User> filteredUsers;
  private final Context context;
  public interface UsersCallback {
    void onUsersRetrieved(List<User> users);
  }

  public UserAdapter(Context context, ArrayList<User> originalUsers) {
    this.context = context;
    this.originalUsers = originalUsers;
    this.filteredUsers = new ArrayList<>(originalUsers);
  }

  public void fetchUsers(UsersCallback callback) {

    CollectionReference reference = FirebaseFirestore.getInstance().collection("user_id");

    reference.get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
              List<User> users = new ArrayList<>();

              for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                // Get the user ID from the document
                String userId = documentSnapshot.getId();

                // Now, fetch the user details using the obtained user ID
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .collection("user_details")
                        .get()
                        .addOnSuccessListener(userDetailsSnapshots -> {
                          if (!userDetailsSnapshots.isEmpty()) {
                            User user = userDetailsSnapshots.toObjects(User.class).get(0);
                            users.add(user);
                            if (users.size() == queryDocumentSnapshots.size()) {
                              // All users have been fetched
                              callback.onUsersRetrieved(users);
                            }
                          }
                        })
                        .addOnFailureListener(e -> {
                          Log.e(TAG, "Error fetching user details: " + e.getMessage());
                          callback.onUsersRetrieved(null);
                        });
              }
            })
            .addOnFailureListener(e -> {
              Log.e(TAG, "Error fetching user IDs: " + e.getMessage());
              callback.onUsersRetrieved(null);
            });
  }

  @NonNull
  @Override
  public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new UserViewHolder(
            LayoutInflater.from(context).inflate(R.layout.account_view_holder, parent, false)
    );
  }

  @SuppressLint("NotifyDataSetChanged")
  public void setUsers(List<User> users) {
    this.originalUsers = users;
    notifyDataSetChanged();
  }

  @Override
  public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
    User user = filteredUsers.get(position);
    String fullName = String.format(
            "%s %s %s",
            user.getFirstName(),
            user.getMiddleName().charAt(0),
            user.getLastName()
    );

    holder.institutionalId.setText(user.getInstitutionalID());
    holder.institutionalEmail.setText(user.getInstitutionalEmail());
    holder.fullName.setText(fullName);
    holder.departmentTxt.setText(user.getDepartment());
    holder.courseTxt.setText(user.getCourse());
    holder.roleTxt.setText(user.getRole());

    holder.itemView.setOnLongClickListener(v -> {

      /*
      * Go to a screen that will ask if this user will be deleted or change the password of a user
       */

      String lastName = user.getLastName();
      String firstName = user.getFirstName();
      String middleName = user.getMiddleName();
      String role = user.getRole();
      String institutionalID = user.getInstitutionalID();
      String department = user.getDepartment();
      String course = user.getCourse();
      String UID = user.getUID();
      String password = user.getPassword();
      String passwordHashCode = user.getPasswordHashCode();
      String institutionalEmail = user.getInstitutionalEmail();

      Intent intent = new Intent(context, UserDetailsActivity.class);

      intent.putExtra("last_name", lastName);
      intent.putExtra("first_name", firstName);
      intent.putExtra("middle_name", middleName);
      intent.putExtra("role", role);
      intent.putExtra("institutional_id", institutionalID);
      intent.putExtra("department", department);
      intent.putExtra("course", course);
      intent.putExtra("UID", UID);
      intent.putExtra("password", password);
      intent.putExtra("pass_hash", passwordHashCode);
      intent.putExtra("IE", institutionalEmail);

      ((Activity) context).startActivity(intent);

      return true;
    });
  }

  @Override
  public int getItemCount() {
    return filteredUsers.size();
  }

  public void setData(List<User> users) {
    this.originalUsers = users;
    filterUsers("");
  }

  public void filterUsers(String searchText) {
    filteredUsers.clear();
    for (User user : originalUsers) {
      if (
              user.getLastName().toLowerCase().contains(searchText.toLowerCase())
              || user.getRole().toLowerCase().contains(searchText.toLowerCase())
              || user.getFirstName().toLowerCase().contains(searchText.toLowerCase())
              || user.getInstitutionalEmail().toLowerCase().contains(searchText.toLowerCase())
              || user.getCourse().toLowerCase().contains(searchText.toLowerCase())
              || user.getDepartment().toLowerCase().contains(searchText.toLowerCase())
              || user.getInstitutionalID().toLowerCase().contains(searchText.toLowerCase())
      ) {
        filteredUsers.add(user);
      }
    }

    notifyDataSetChanged();
  }

  public List<User> getOriginalData() {
    return originalUsers;
  }

  public List<User> getFilteredData() {
    return filteredUsers;
  }

}
