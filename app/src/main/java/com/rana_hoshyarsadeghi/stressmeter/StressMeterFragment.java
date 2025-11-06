package com.rana_hoshyarsadeghi.stressmeter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StressMeterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stress_meter, container, false);
        GridView grid = root.findViewById(R.id.grid_stress);
        View btnMore = root.findViewById(R.id.btn_more_images);
        ImageAdapter adapter = new ImageAdapter(requireContext());
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                int resId = (item instanceof Integer) ? (Integer) item : 0;

                Intent intent = new Intent(requireContext(), ImageResponseActivity.class);
                intent.putExtra("resId", resId);       // which image to show large
                intent.putExtra("position", position); // which grid index (for scoring later)
                startActivity(intent);
            }
        });
        btnMore.setOnClickListener(v -> {
            adapter.nextPage();
            adapter.notifyDataSetChanged();
            //android.widget.Toast.makeText(requireContext(), //I wasn't sure if we need to have this option or not and It was covering the button also!
                //    "Page changed!", android.widget.Toast.LENGTH_SHORT).show();
        });
      //  android.widget.Toast.makeText(requireContext(),
             //       "More images clicked", android.widget.Toast.LENGTH_SHORT).show();

        return root;
    }
}
