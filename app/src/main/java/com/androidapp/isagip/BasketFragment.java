package com.androidapp.isagip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BasketFragment extends Fragment {

//    TabLayout tabLayout;
//    ViewPager viewPager;
//    ViewPagerAdapter viewPagerAdapter;
//    BasketSentFragment basketSentFragment;
//    BasketReceivedFragment basketReceivedFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket1, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_feedback);
//        viewPager = (ViewPager) view.findViewById(R.id.viewpager_feedback);
//
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        basketReceivedFragment = new BasketReceivedFragment();
//        basketSentFragment = new BasketSentFragment();
//
//
//        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), viewPager, tabLayout);
//        viewPagerAdapter.addFragment(basketReceivedFragment, "RECEIVED", null);
//        viewPagerAdapter.addFragment(basketSentFragment, "SENT", null);
//        viewPagerAdapter.notifyDataSetChanged();
//        viewPager.setAdapter(viewPagerAdapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
    }
}