package ca.nscc.marconi.project.ui.credit;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import ca.nscc.marconi.project.R;
import ca.nscc.marconi.project.databinding.FragmentCreditBinding;

public class CreditFragment extends Fragment {

    private TextView name, courseNumber, currentDate;
    private ConstraintLayout cLayout;

    private FragmentCreditBinding binding;
    public static CreditFragment newInstance() {
        return new CreditFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCreditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        name = binding.nameTextView;
        courseNumber = binding.courseTextView;
        currentDate = binding.currentDateTextView;
        name.setText("Daniel Vu");
        courseNumber.setText("MOBI3002");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
            LocalDate Date = LocalDate.now();
            currentDate.setText(dtf.format(Date));
        }

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}