package com.example.nthucs.prototype.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nthucs.prototype.R;
import com.example.nthucs.prototype.Settings.SettingAdapter;
import com.example.nthucs.prototype.TabsBar.TabsController;
import com.example.nthucs.prototype.TabsBar.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 2016/7/16.
 */
public class SettingsActivity extends AppCompatActivity {

    // list view adapter for setting list
    private SettingAdapter settingAdapter;

    // settings' title
    private static final String myProfile = "My Profile";
    private static final String myCurrentExercise = "My Current Exercise";//運動紀錄
    private static final String myWeightLossGoal = "My Weight Loss Goal";
    private static final String displayAsMetricImperial = "Display as metric/imperial";
    private static final String weightChart = "Weight Chart";
    private static final String calorieConsumption = "Calorie Consumption";
    private static final String myFavourites = "My Favourites";
    private static final String myBloodPressure = "My Blood Pressure";
    private static final String drinkWaterDiary = "Drink Water Diary";
    private static final String myTemperatureRecord = "My Temperature Record";
    // , drinkWaterDiary , myTemperatureRecord
    private String[] titleStr = new String[]{myProfile, myCurrentExercise, myWeightLossGoal, displayAsMetricImperial, weightChart, calorieConsumption, myFavourites , myBloodPressure , drinkWaterDiary, myTemperatureRecord};

    // list view for including textView
    private ListView setting_list;

    // string list for every setting item's title
    private List<String> setting_title;

    // element for the bottom of the tab content
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Settings");
        setContentView(R.layout.activity_settings);

        // initialize tabLayout and viewPager
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        initializeTabLayout();

        // call function to active tabs listener
        TabsController tabsController = new TabsController(4, SettingsActivity.this, tabLayout, viewPager);
        tabsController.processTabLayout();

        // initialize setting list and process controllers
        setting_list = (ListView)findViewById(R.id.setting_list);
        processControllers();

        // initialize and set adapter, pass title with string
        setting_title = new ArrayList<>(Arrays.asList(titleStr));
        settingAdapter = new SettingAdapter(this, R.layout.single_setting, setting_title);
        setting_list.setAdapter(settingAdapter);

        selectTab(4);
    }

    // Initialize tab layout
    private void initializeTabLayout() {
        ViewPagerAdapter pagerAdapter =
                new ViewPagerAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // set custom icon for every tab
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Always select tab 4
        selectTab(4);
    }

    // select specific tab
    private void selectTab(int index) {
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        tab.select();
    }

    // process list click listener
    private void processControllers() {
        // construct settings list click listener
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String title = settingAdapter.getItem(position);
                switch (title) {
                    // go to my profile activity
                    case myProfile:
                        Intent intent_profile = new Intent();
                        intent_profile.setClass(SettingsActivity.this, MyProfileActivity.class);
                        startActivity(intent_profile);
                        finish();
                        break;
                    case myCurrentExercise:
                        Intent intent_current_exercise = new Intent();
                        intent_current_exercise.setClass(SettingsActivity.this, MyCurrentExerciseActivity.class);
                        startActivity(intent_current_exercise);
                        finish();
                        break;
                    case myWeightLossGoal:
                        Intent intent_weight_loss = new Intent();
                        intent_weight_loss.setClass(SettingsActivity.this, MyWeightLossGoalActivity.class);
                        startActivity(intent_weight_loss);
                        finish();
                        break;
                    case displayAsMetricImperial:
                        Intent intent_display = new Intent();
                        intent_display.setClass(SettingsActivity.this, DisplayAsMetricImperialActivity.class);
                        startActivity(intent_display);
                        finish();
                        break;
                    case weightChart:
                        Intent intent_weight_chart = new Intent();
                        intent_weight_chart.setClass(SettingsActivity.this, WeightChartActivity.class);
                        startActivity(intent_weight_chart);
                        finish();
                        break;
                    case calorieConsumption:
                        Intent intent_calorie_consumption = new Intent();
                        intent_calorie_consumption.setClass(SettingsActivity.this, CalorieConsumptionActivity.class);
                        startActivity(intent_calorie_consumption);
                        finish();
                        break;
                    case myFavourites:
                        Intent intent_my_favourites = new Intent();
                        intent_my_favourites.setClass(SettingsActivity.this, MyFavouritesActivity.class);
                        startActivity(intent_my_favourites);
                        finish();
                        break;
                    case myBloodPressure:
                        Intent intent_my_blood_pressure = new Intent();
                        intent_my_blood_pressure.setClass(SettingsActivity.this , MyBloodPressure.class);
                        startActivity(intent_my_blood_pressure);
                        finish();
                        break;
                    case drinkWaterDiary:
                        Intent intent_drink_water_diary = new Intent();
                        intent_drink_water_diary.setClass(SettingsActivity.this , DrinkWaterDiary.class);
                        startActivity(intent_drink_water_diary);
                        finish();
                        break;
                    case myTemperatureRecord:
                        Intent intent_my_temperature_record = new Intent();
                        intent_my_temperature_record.setClass(SettingsActivity.this , MyTemperatureRecord.class);
                        startActivity(intent_my_temperature_record);
                        finish();
                        break;

                }
            }
        };

        // register settings list click listener
        setting_list.setOnItemClickListener(itemListener);
    }
}
