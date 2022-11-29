package ca.nscc.marconi.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.nscc.marconi.project.R;

public class UserDetailActivity extends AppCompatActivity {

    TextView firstnameText, lastnameText, emailText, addressText, phoneText;
    EditText noteEditText;
    ImageButton saveBtn, deleteBtn, editBtn, closeBtn;
    String firstname, lastname, email, address, phone, note, docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        firstnameText = findViewById(R.id.user_firstname_text);
        lastnameText = findViewById(R.id.user_lastname_text);
        emailText = findViewById(R.id.user_email_text);
        addressText = findViewById(R.id.user_address_text);
        phoneText = findViewById(R.id.user_phone_text);
        noteEditText = findViewById(R.id.user_note_edit_text);
        saveBtn = findViewById(R.id.save_user_btn);
        deleteBtn = findViewById(R.id.delete_user_btn);
        editBtn = findViewById(R.id.edit_user_btn);
        closeBtn = findViewById(R.id.close_btn);

        //retrieve data
        firstname = getIntent().getStringExtra("firstname");
        lastname = getIntent().getStringExtra("lastname");
        email = getIntent().getStringExtra("email");
        address = getIntent().getStringExtra("address");
        phone = getIntent().getStringExtra("phone");
        note = getIntent().getStringExtra("note");
        docId = getIntent().getStringExtra("docId");

        firstnameText.setText("First name: " + firstname);
        lastnameText.setText("Last name: " + lastname);
        emailText.setText("Email: " + email);
        addressText.setText("Address: " + address);
        phoneText.setText("Phone Number: " + phone);
        noteEditText.setText("Note: " + note);

        saveBtn.setOnClickListener((v)-> saveNote());
        deleteBtn.setOnClickListener((v)->deleteUserFromFirebase());
        editBtn.setOnClickListener((v)->editUserInformation());
        closeBtn.setOnClickListener((v)->closeDetailActivity());

    }

    void saveNote() {
        String note = noteEditText.getText().toString().substring(noteEditText.getText().toString().indexOf(":") + 2);
        if (note == null || note.isEmpty() ){
            noteEditText.setError("Note is required");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("User").document(docId);
        ref.update("note", note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is updated
                    Toast.makeText(UserDetailActivity.this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserDetailActivity.this, "Failed while updating note", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void deleteUserFromFirebase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this client?").setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference ref = db.collection("User").document(docId);
                        ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //note is deleted
                                    Toast.makeText(UserDetailActivity.this, "Client deleted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(UserDetailActivity.this, "Fail while deleting client information", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog =builder.create();
        alertDialog.show();

    }

    void editUserInformation() {
        Intent intent = new Intent(UserDetailActivity.this, EditActivity.class);
        intent.putExtra("firstname", firstname);
        intent.putExtra("lastname", lastname);
        intent.putExtra("email", email);
        intent.putExtra("address", address);
        intent.putExtra("phone", phone);
        intent.putExtra("note", note);
        intent.putExtra("docId", docId);
        startActivity(intent);
    }

    void closeDetailActivity() {
        finish();
    }
}