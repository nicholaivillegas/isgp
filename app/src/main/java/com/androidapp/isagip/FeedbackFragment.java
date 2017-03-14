package com.androidapp.isagip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.isagip.adapter.ViewPagerAdapter;

/**
 * Created by Nico on 3/14/2017.
 */

public class FeedbackFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    RequestFragment requestFragment;
    NotRequestFragment notRequestFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_feedback);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_feedback);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        requestFragment = new RequestFragment();
        notRequestFragment = new NotRequestFragment();

        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), viewPager, tabLayout);
        viewPagerAdapter.addFragment(requestFragment, "REQUESTED", null);
        viewPagerAdapter.addFragment(notRequestFragment, "NOT REQUESTED", null);
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;
    }

}
