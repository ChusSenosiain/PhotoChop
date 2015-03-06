package es.molestudio.photochop.controller.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseFacebookUtils;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppTextView;
import es.molestudio.photochop.View.PagerSlidingTabStrip;
import es.molestudio.photochop.controller.fragment.SignInFragment;
import es.molestudio.photochop.controller.fragment.SignUpFragment;
import es.molestudio.photochop.model.User;

public class LogInActivity extends ActionBarActivity implements
    SignUpFragment.FinishSignUpListener {

    public static final int RQ_LOGGIN = 2564;

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
        mActionBar.setTitle(getString(R.string.app_name));
        // enable ActionBar to have a custom layout
        mActionBar.setDisplayHomeAsUpEnabled(true);


        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mPagerAdapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);
        mViewPager.setOffscreenPageLimit(0);
        mTabs.setViewPager(mViewPager);

        mViewPager.setCurrentItem(0, true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }


    ////////////////////////////////////////////////////////////////////
    // INTERFACE IMPLEMENTATION
    ////////////////////////////////////////////////////////////////////
    // SignUpFragment.FinishSignUpListener
    @Override
    public void onFinishSingUp(User user) {
        // Go to the sign in page
        mViewPager.setCurrentItem(0, true);
        SignInFragment fragment = (SignInFragment) mPagerAdapter.getItem(0);
        fragment.setUserData(user);

    }


    ////////////////////////////////////////////////////////////////////
    // fragment pager adapter
    ////////////////////////////////////////////////////////////////////
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




}
