package com.example.nthucs.prototype.TabsBar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.nthucs.prototype.Activity.CalendarActivity;
import com.example.nthucs.prototype.Activity.MainActivity;
import com.example.nthucs.prototype.Activity.MessageActivity;
import com.example.nthucs.prototype.Activity.SettingsActivity;

/**
 * Created by user on 2016/7/18.
 */

public class TabsController {
    int activityIndex;
    Activity activity;
    TabLayout tabLayout;
    ViewPager viewPager;

    // action number
    private static final int SCAN_FOOD = 2;
    private static final int TAKE_PHOTO = 3;

    // activity number
    private static final int MAIN_ACTIVITY = 0;
    private static final int CALENDAR_ACTIVITY = 1;
    private static final int MESSAGE_ACTIVITY = 3;
    private static final int SETTING_ACTIVITY = 4;

    public TabsController(int activityIndex, Activity activity, TabLayout tabLayout, ViewPager viewPager) {
        this.activityIndex = activityIndex;
        this.activity = activity;
        this.tabLayout = tabLayout;
        this.viewPager = viewPager;
    }
    public void processTabLayout() {
        // enable tab selected listener
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        if (tab.getPosition() == 0) {
                            // main activity
                            if (activityIndex != MAIN_ACTIVITY) {
                                Intent result = new Intent();
                                result.putExtra("alreadyCall", 0);
                                result.setClass(activity, MainActivity.class);
                                activity.startActivity(result);
                                activity.finish();
                            }
                        } else if (tab.getPosition() == 1) {
                            // calendar activity
                            if (activityIndex != CALENDAR_ACTIVITY) {
                                Intent intent_calendar = new Intent("com.example.nthucs.prototype.CALENDAR");
                                intent_calendar.setClass(activity, CalendarActivity.class);
                                activity.startActivity(intent_calendar);
                                activity.finish();
                            }
                        } else if (tab.getPosition() == 2) {
                            // start activity for result
                            selectImage();
                        } else if (tab.getPosition() == 3) {
                            // message activity
                            if (activityIndex != MESSAGE_ACTIVITY) {
                                Intent intent_mes = new Intent();
                                intent_mes.setClass(activity, MessageActivity.class);
                                activity.startActivity(intent_mes);
                                activity.finish();
                            }
                        } else if (tab.getPosition() == 4) {
                            // setting activity
                            if (activityIndex != SETTING_ACTIVITY) {
                                Intent intent_settings = new Intent("com.example.nthucs.prototype.SETTINGS");
                                intent_settings.setClass(activity, SettingsActivity.class);
                                activity.startActivity(intent_settings);
                                activity.finish();
                            }
                        }
                        //System.out.println("main select: "+tab.getPosition());
                    }
                }
        );
    }

    // select image with two way
    private void selectImage() {
        final CharSequence[] items = { "Take with Camera", "Choose from Gallery", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                if (items[index].equals("Take with Camera")) {
                    Intent intent_camera = new Intent("com.example.nthucs.prototype.TAKE_PICT");
                    activity.startActivityForResult(intent_camera, SCAN_FOOD);
                } else if (items[index].equals("Choose from Gallery")) {
                    Intent intent_gallery = new Intent("com.example.nthucs.prototype.TAKE_PHOTO");
                    activity.startActivityForResult(intent_gallery, TAKE_PHOTO);
                } else if (items[index].equals("Cancel")) {
                    dialog.dismiss();
                    selectTab(activityIndex);
                }
            }
        });
        builder.show();
    }

    // select specific tab
    private void selectTab(int index) {
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        tab.select();
    }
}