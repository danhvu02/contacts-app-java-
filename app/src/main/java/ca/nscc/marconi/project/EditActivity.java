package ca.nscc.marconi.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditActivity extends AppCompatActivity {

    TextView noteText;
    EditText firstnameEditText, lastnameEditText, emailEditText, addressEditText, phoneEditText;
    ImageButton saveBtn, closeBtn;
    String firstname, lastname, email, address, phone, note, docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        firstnameEditText = findViewById(R.id.user_firstname_edit_text);
        lastnameEditText = findViewById(R.id.user_lastname_edit_text);
        emailEditText = findViewById(R.id.user_email_edit_text);
        addressEditText = findViewById(R.id.user_address_edit_text);
        phoneEditText = findViewById(R.id.user_phone_edit_text);
        noteText = findViewById(R.id.user_note_text);
        saveBtn = findViewById(R.id.save_user_btn);
        closeBtn = findViewById(R.id.close_btn);

        //retrieve data
        firstname = getIntent().getStringExtra("firstname");
        lastname = getIntent().getStringExtra("lastname");
        email = getIntent().getStringExtra("email");
        address = getIntent().getStringExtra("address");
        phone = getIntent().getStringExtra("phone");
        note = getIntent().getStringExtra("note");
        docId = getIntent().getStringExtra("docId");

        firstnameEditText.setText("First name: " + firstname);
        lastnameEditText.setText("Last name: " + lastname);
        emailEditText.setText("Email: " + email);
        addressEditText.setText("Address: " + address);
        phoneEditText.setText("Phone Number: " + phone);
        noteText.setText("Note: " + note);

        saveBtn.setOnClickListener((v)->saveUserInformation());
        closeBtn.setOnClickListener((v)->closeDetailActivity());
    }

    void saveUserInformation() {
        String firstname = firstnameEditText.getText().toString().substring(firstnameEditText.getText().toString().indexOf(":") + 2);
        String lastname = lastnameEditText.getText().toString().substring(lastnameEditText.getText().toString().indexOf(":") + 2);
        String email = emailEditText.getText().toString().substring(emailEditText.getText().toString().indexOf(":") + 2);
        String address = addressEditText.getText().toString().substring(addressEditText.getText().toString().indexOf(":") + 2);
        String phone = phoneEditText.getText().toString().substring(phoneEditText.getText().toString().indexOf(":") + 2);

        if (validateData(firstname, lastname, email, address, phone) ){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ref = db.collection("User").document(docId);
            ref.update("firstName", firstname, "lastName", lastname, "email", email, "address", address, "phone", phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //note is updated
                        Toast.makeText(EditActivity.this, "Information updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditActivity.this, "Failed while updating information", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    void closeDetailActivity() {
        finish();
    }


    boolean validateData(String fname, String lname, String email, String address, String phone){
        //validate the data that are input by user
        if(fname.isEmpty() || fname.equals("")){
            firstnameEditText.setError("First name is required");
            return false;
        }

        if(lname.isEmpty() || lname.equals("")){
            lastnameEditText.setError("Last name is required");
            return false;
        }

        if(email.isEmpty() || email.equals("")){
            emailEditText.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }

        if(address.isEmpty() || address.equals("")){
            addressEditText.setError("Address is required");
            return false;
        }

        if(phone.isEmpty() || phone.equals("")){
            phoneEditText.setError("Phone number is required");
            return false;
        }

        return true;
    }
}