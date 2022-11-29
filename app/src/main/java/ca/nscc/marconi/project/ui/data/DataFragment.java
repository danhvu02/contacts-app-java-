package ca.nscc.marconi.project.ui.data;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import ca.nscc.marconi.project.R;
import ca.nscc.marconi.project.User;
import ca.nscc.marconi.project.UserAdapter;
import ca.nscc.marconi.project.databinding.FragmentDataBinding;

public class DataFragment extends Fragment {

    private DataViewModel mViewModel;
    private FragmentDataBinding binding;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    public static DataFragment newInstance() {
        return new DataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.recycleView;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirestoreRecyclerOptions<User>
                options = new FirestoreRecyclerOptions.Builder<User>().setQuery(db.collection("User"), User.class).build();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new UserAdapter(options, getContext());
        recyclerView.setAdapter(userAdapter);
        return root;

    }

    @Override
    public void onStart() {
        super.onStart();
        userAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        userAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        userAdapter.notifyDataSetChanged();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        // TODO: Use the ViewModel
    }


}
