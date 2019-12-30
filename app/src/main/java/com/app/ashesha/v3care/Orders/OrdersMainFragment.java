package com.app.ashesha.v3care.Orders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ashesha.v3care.R;

import java.util.ArrayList;
import java.util.List;

public class OrdersMainFragment extends Fragment {

    View rootView;
    TabLayout tablayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_orders_main, container, false);


        tablayout = rootView.findViewById(R.id.tabLayout);
        viewPager = rootView.findViewById(R.id.viewPager);
        tablayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        if (viewPager != null) {
            setUpViewPager(viewPager);

        }
        tablayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        return rootView;

    }

    private void setUpViewPager(ViewPager viewPager) {
        OrdersAdapter ordersAdapter = new OrdersAdapter(getFragmentManager());
        ordersAdapter.addFragment(new CompletedOrderFragment(), "Completed Order");
        ordersAdapter.addFragment(new PendingOrdersFragment(), "Pending Order");
        viewPager.setAdapter(ordersAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

    }

    public class OrdersAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> titiles = new ArrayList<>();


        int TabCount;

        public OrdersAdapter(FragmentManager fragmentManager) {

            super(fragmentManager);

        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titiles.add(title);

        }

        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);

        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titiles.get(position);
        }


    }
}
