package com.example.sudoku.records;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sudoku.Inf;
import com.example.sudoku.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RecordsFragment extends Fragment {
    DatabaseReference mDatabase;
    ListView recordsList;
    RadioButton easyRadioButton, mediumRadioButton, hardRadioButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.records_fragment, container, false);
        recordsList = (ListView) view.findViewById(R.id.all_records);
        setRecords(Inf.EASY);
        View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) v;
                switch (rb.getId()) {
                    case R.id.easyRadioButton:
                        setRecords(Inf.EASY);
                        break;
                    case R.id.mediumRadioButton:
                        setRecords(Inf.MEDIUM);
                        break;
                    case R.id.hardRadioButton:
                        setRecords(Inf.HARD);
                        break;
                    default:
                        break;
                }
            }
        };
        easyRadioButton = (RadioButton) view.findViewById(R.id.easyRadioButton);
        easyRadioButton.setOnClickListener(radioButtonClickListener);
        mediumRadioButton = (RadioButton) view.findViewById(R.id.mediumRadioButton);
        mediumRadioButton.setOnClickListener(radioButtonClickListener);
        hardRadioButton = (RadioButton) view.findViewById(R.id.hardRadioButton);
        hardRadioButton.setOnClickListener(radioButtonClickListener);
        return view;
    }

    public void setRecords(String dif) {
        ArrayList<Map<String, String>> records = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("records").child(dif);
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            records.clear();
                            for (DataSnapshot data : snapshot.getChildren()) {
                                Map<String, String> record = new HashMap<>();
                                record.put("name", data.child("userName").getValue().toString());
                                record.put("time", data.child("time").getValue().toString());
                                records.add(record);
                            }
                            Context context = getContext();
                            if (context != null) {
                                Collections.sort(records, new Comparator<Map<String, String>>() {
                                    @Override
                                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                                        String[] t1 = o1.get("time").split(":");
                                        String[] t2 = o2.get("time").split(":");
                                        if (Integer.valueOf(t1[0]) - Integer.valueOf(t2[0]) >= 0) {
                                            if (Integer.valueOf(t1[1]) - Integer.valueOf(t2[1]) >= 0) {
                                                return Integer.valueOf(t1[2]) - Integer.valueOf(t2[2]);
                                            }
                                            return -1;
                                        }
                                        return -1;
                                    }
                                });
                                System.out.println(records);
                                int index = 10;

                                if (records.size() <= index)
                                    index = records.size();
                                Log.d("LOG", "records.size() > 0");
                                SimpleAdapter adapter = new SimpleAdapter(getActivity(), records.subList(0, index),
                                        android.R.layout.simple_list_item_2, new String[]{"name", "time"},
                                        new int[]{android.R.id.text1, android.R.id.text2});
                                recordsList.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
    }
}
