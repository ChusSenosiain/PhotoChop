package es.molestudio.photochop.controller.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppTextView;
import es.molestudio.photochop.View.PagerSlidingTabStrip;
import es.molestudio.photochop.controller.fragment.SignInFragment;
import es.molestudio.photochop.controller.fragment.SignUpFragment;

public class LogInActivity extends ActionBarActivity implements
    ViewPager.OnPageChangeListener{

        public static final int RQ_LOGGIN = 1004;

        private PagerSlidingTabStrip mTabs;
        private ViewPager mViewPager;
        private MyPagerAdapter mPagerAdapter;
        private ActionBar mActionBar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_log_in);

            // Set up toolbar as actionbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // Set up toolbar
            mActionBar = getSupportActionBar();
            mActionBar.setTitle("");
            // enable ActionBar to have a custom layout
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

            View view = getLayoutInflater().inflate(R.layout.action_bar_title_center, null);
            AppTextView tvTitle = (AppTextView) view.findViewById(R.id.tv_title);
            tvTitle.setText("Your Secret Photo");

            mActionBar.setCustomView(view);

            mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

            mViewPager.setAdapter(mPagerAdapter);

            final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                    .getDisplayMetrics());
            mViewPager.setPageMargin(pageMargin);
            mViewPager.setOffscreenPageLimit(0);


            mTabs.setViewPager(mViewPager);
            mTabs.setOnPageChangeListener(this);


            mViewPager.setCurrentItem(0, true);

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        public class MyPagerAdapter extends FragmentPagerAdapter {


            public MyPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.title_signin);
                    case 1:
                        return getString(R.string.title_singup);
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                // getItem is called to instantiate the fragment for the given page.
                // Return a PlaceholderFragment (defined as a static inner class below).

                Fragment fragment;

                switch (position) {
                    case 0:
                        fragment = SignInFragment.newInstance();
                        break;
                    case 1:
                        fragment = SignUpFragment.newInstance();
                        break;
                    default:
                        fragment = SignInFragment.newInstance();
                }

                return fragment;
            }


        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
