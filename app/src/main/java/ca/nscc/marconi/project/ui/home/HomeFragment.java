package ca.nscc.marconi.project.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.nscc.marconi.project.databinding.FragmentHomeBinding;
import ca.nscc.marconi.project.User;

public class HomeFragment extends Fragment {

    private EditText fnameEditText, lnameEditText, emailEditText, addressEditText, phoneEditText, noteEditText;
    private Button submitButton;
    private ConstraintLayout cLayout;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        cLayout = binding.constraintLayout;
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) view.getContext()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

        fnameEditText = binding.fnameEditText; //fnameEditText = (EditText) findViewById(R.id.fnameEditText);
        lnameEditText = binding.lnameEditText;
        emailEditText = binding.emailEditText;
        addressEditText = binding.addressEditText;
        phoneEditText = binding.phoneEditText;
        noteEditText = binding.noteEditText;
        submitButton = binding.submitButton;

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //retrieve the values from the editText fields
                String fname = fnameEditText.getText().toString();
                String lname = lnameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String note = noteEditText.getText().toString();

                InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);

                if (validateData(fname, lname, email, address, phone, note) ) {
                    User user = new User(fname, lname, email, address, phone, note);
                    saveUserToFirebase(user);
                    clearForm((ViewGroup) binding.constraintLayout);

                }

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    boolean validateData(String fname, String lname, String email, String address, String phone, String note){
        //validate the data that are input by user
        if(fname.isEmpty() || fname.equals("")){
            fnameEditText.setError("First name is required");
            return false;
        }

        if(lname.isEmpty() || lname.equals("")){
            lnameEditText.setError("Last name is required");
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

        if(note.isEmpty() || note.equals("")){
            noteEditText.setError("Note is required");
            return false;
        }
        return true;
    }

    void saveUserToFirebase(User user){
        // getting instance from Firebase Firestore.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // creating a collection reference for Firebase Firetore database.
        CollectionReference dbUser= db.collection("User");

        // below method is use to add data to Firebase Firestore.
        dbUser.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(getActivity(), "Data Received", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(getActivity(), "Fail to add user \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }
}