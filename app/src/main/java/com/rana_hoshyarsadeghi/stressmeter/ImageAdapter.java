package com.rana_hoshyarsadeghi.stressmeter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;
    private int currentPage = 0; // Tracks which page is currently shown

    // I tried working on a more dynamic/automated way for the value of the pictures
    // but I unfortunately didn't have enough time to implement it and I didn't want to get
    // a negative value which I have neutralized but I wanted to keep some credibility for the graph as well!
    private static final int[][] PAGES = {
            {
                    R.drawable.psm_peaceful_person,
                    R.drawable.psm_yoga4,
                    R.drawable.psm_neutral_person2,
                    R.drawable.psm_neutral_child,
                    R.drawable.psm_anxious,
                    R.drawable.psm_stressed_person,
                    R.drawable.psm_stressed_person4,
                    R.drawable.psm_stressed_person6,
                    R.drawable.psm_headache2,
                    R.drawable.psm_barbed_wire2,
                    R.drawable.psm_to_do_list,
                    R.drawable.psm_work4,
                    R.drawable.psm_alarm_clock,
                    R.drawable.psm_running3,
                    R.drawable.psm_beach3,
                    R.drawable.psm_mountains11
            },
            {
                    R.drawable.psm_cat,
                    R.drawable.psm_dog_sleeping,
                    R.drawable.psm_puppy,
                    R.drawable.psm_puppy3,
                    R.drawable.psm_reading_in_bed2,
                    R.drawable.psm_clutter,
                    R.drawable.psm_sticky_notes2,
                    R.drawable.psm_gambling4,
                    R.drawable.psm_lonely,
                    R.drawable.psm_lonely2,
                    R.drawable.psm_alarm_clock2,
                    R.drawable.psm_wine3,
                    R.drawable.psm_hiking3,
                    R.drawable.psm_kettle,
                    R.drawable.psm_blue_drop,
                    R.drawable.psm_bar
            },
            {
                    R.drawable.psm_bird3,
                    R.drawable.psm_lake3,
                    R.drawable.psm_work4,
                    R.drawable.psm_to_do_list3,
                    R.drawable.psm_stressed_person7,
                    R.drawable.psm_stressed_person8,
                    R.drawable.psm_stressed_person12,
                    R.drawable.psm_headache,
                    R.drawable.psm_exam4,
                    R.drawable.psm_alarm_clock,
                    R.drawable.psm_neutral_person2,
                    R.drawable.psm_mountains11,
                    R.drawable.psm_beach3,
                    R.drawable.psm_peaceful_person,
                    R.drawable.psm_yoga4,
                    R.drawable.psm_stressed_cat
            }
    };
    public static final java.util.Map<Integer, Integer> STRESS_SCORES = new java.util.HashMap<>();
    static {
        STRESS_SCORES.put(R.drawable.psm_peaceful_person, 1);
        STRESS_SCORES.put(R.drawable.psm_yoga4, 2);
        STRESS_SCORES.put(R.drawable.psm_mountains11, 2);
        STRESS_SCORES.put(R.drawable.psm_beach3, 3);
        STRESS_SCORES.put(R.drawable.psm_lake3, 3);
        STRESS_SCORES.put(R.drawable.psm_lawn_chairs3, 3);
        STRESS_SCORES.put(R.drawable.psm_baby_sleeping, 2);
        STRESS_SCORES.put(R.drawable.psm_dog_sleeping, 2);
        STRESS_SCORES.put(R.drawable.psm_cat, 3);
        STRESS_SCORES.put(R.drawable.psm_bird3, 3);
        STRESS_SCORES.put(R.drawable.psm_blue_drop, 2);
        STRESS_SCORES.put(R.drawable.psm_neutral_person2, 5);
        STRESS_SCORES.put(R.drawable.psm_neutral_child, 4);
        STRESS_SCORES.put(R.drawable.psm_reading_in_bed2, 4);
        STRESS_SCORES.put(R.drawable.psm_running3, 6);
        STRESS_SCORES.put(R.drawable.psm_running4, 6);
        STRESS_SCORES.put(R.drawable.psm_kettle, 5);
        STRESS_SCORES.put(R.drawable.psm_bar, 7);
        STRESS_SCORES.put(R.drawable.psm_wine3, 7);
        STRESS_SCORES.put(R.drawable.psm_puppy, 4);
        STRESS_SCORES.put(R.drawable.psm_puppy3, 4);
        STRESS_SCORES.put(R.drawable.fish_normal017, 4);
        STRESS_SCORES.put(R.drawable.psm_talking_on_phone2, 7);
        STRESS_SCORES.put(R.drawable.psm_alarm_clock, 8);
        STRESS_SCORES.put(R.drawable.psm_alarm_clock2, 8);
        STRESS_SCORES.put(R.drawable.psm_to_do_list, 9);
        STRESS_SCORES.put(R.drawable.psm_to_do_list3, 9);
        STRESS_SCORES.put(R.drawable.psm_sticky_notes2, 9);
        STRESS_SCORES.put(R.drawable.psm_lonely, 10);
        STRESS_SCORES.put(R.drawable.psm_lonely2, 10);
        STRESS_SCORES.put(R.drawable.psm_gambling4, 10);
        STRESS_SCORES.put(R.drawable.psm_hiking3, 6);
        STRESS_SCORES.put(R.drawable.psm_work4, 11);
        STRESS_SCORES.put(R.drawable.psm_clutter, 10);
        STRESS_SCORES.put(R.drawable.psm_clutter, 10);
        STRESS_SCORES.put(R.drawable.psm_headache, 12);
        STRESS_SCORES.put(R.drawable.psm_headache2, 12);
        STRESS_SCORES.put(R.drawable.psm_exam4, 13);
        STRESS_SCORES.put(R.drawable.psm_angry_face, 13);
        STRESS_SCORES.put(R.drawable.psm_anxious, 12);
        STRESS_SCORES.put(R.drawable.psm_stressed_person, 14);
        STRESS_SCORES.put(R.drawable.psm_stressed_person3, 15);
        STRESS_SCORES.put(R.drawable.psm_stressed_person4, 13);
        STRESS_SCORES.put(R.drawable.psm_stressed_person6, 15);
        STRESS_SCORES.put(R.drawable.psm_stressed_person7, 15);
        STRESS_SCORES.put(R.drawable.psm_stressed_person8, 16);
        STRESS_SCORES.put(R.drawable.psm_stressed_person12, 16);
        STRESS_SCORES.put(R.drawable.psm_stressed_cat, 13);
        STRESS_SCORES.put(R.drawable.psm_barbed_wire2, 16);
    }


    private final List<Integer> shuffledImages = new ArrayList<>();
    private boolean shuffled = false;

    public ImageAdapter(Context ctx) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        shuffleCurrentPage();
    }

    public void nextPage() {
        currentPage = (currentPage + 1) % PAGES.length;
        shuffleCurrentPage();
    }

    private void shuffleCurrentPage() {
        shuffledImages.clear();
        for (int resId : PAGES[currentPage]) {
            shuffledImages.add(resId);
        }
        Collections.shuffle(shuffledImages);
        shuffled = true;
    }

    @Override
    public int getCount() {
        return shuffledImages.size();
    }

    @Override
    public Object getItem(int position) {
        return shuffledImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        if (cell == null) {
            cell = inflater.inflate(R.layout.item_stress_image, parent, false);
        }

        ImageView img = cell.findViewById(R.id.img_cell);
        img.setImageResource(shuffledImages.get(position));

        return cell;
    }
}


