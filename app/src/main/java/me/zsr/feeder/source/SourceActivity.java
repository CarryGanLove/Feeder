package me.zsr.feeder.source;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.sharesdk.framework.ShareSDK;
import de.greenrobot.event.EventBus;
import me.zsr.feeder.App;
import me.zsr.feeder.R;
import me.zsr.feeder.base.BaseActivity;
import me.zsr.feeder.other.AddSourceActivity;
import me.zsr.feeder.util.CommonEvent;

/**
 * @description:
 * @author: Match
 * @date: 8/29/15
 */
public class SourceActivity extends BaseActivity implements OnSourceSelectedListener {
    public static final int CODE_RESULT_SUCCESS = 1;
    private static final String SP_KEY_VERSION_CODE = "sp_key_version_code";
    private static final int MSG_DOUBLE_TAP = 0;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AVAnalytics.trackAppOpened(getIntent());
        setContentView(R.layout.activity_source);

//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        if (sp.getInt(SP_KEY_VERSION_CODE, 0) < BuildConfig.VERSION_CODE) {
//            // Show newest version info
//            // TODO: 11/4/15 Add en version info
//            SnackbarUtil.show(this, "升级成功：" + FileUtil.readAssetFie(this, "version_info"));
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putInt(SP_KEY_VERSION_CODE, BuildConfig.VERSION_CODE);
//            editor.apply();
//        }

        initView();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    private void initView() {
        initSystemBar();
        initToolbar();
        initDrawer();
        showItemList(App.SOURCE_ID_ALL);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            // FIXME: 11/26/15 Handle strange offset in DrawerLayout
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) mDrawerLayout.getLayoutParams();
            mlp.topMargin = tintManager.getConfig().getStatusBarHeight();
            mDrawerLayout.setLayoutParams(mlp);
        }
    }

    private void initToolbar() {
         mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Make arrow color white
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_hamberger);
        upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    private void initDrawer() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.drawer_frame, new SourceListFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void setListener() {
        mToolbar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar:
                if (mHandler.hasMessages(MSG_DOUBLE_TAP)) {
                    mHandler.removeMessages(MSG_DOUBLE_TAP);
                    EventBus.getDefault().post(CommonEvent.SOURCE_TOOLBAR_DOUBLE_CLICK);
                } else {
                    mHandler.sendEmptyMessageDelayed(MSG_DOUBLE_TAP, ViewConfiguration.getDoubleTapTimeout());
                }
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_source, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                startActivityForResult(new Intent(this, AddSourceActivity.class), 0);
                overridePendingTransition(0, 0);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (CODE_RESULT_SUCCESS == resultCode) {
            showItemList(data.getLongExtra("sourceId", App.SOURCE_ID_ALL));
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSourceSelected(long sourceId) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        showItemList(sourceId);
    }

    private void showItemList(long sourceId) {
        ItemListFragment fragment = (ItemListFragment) getFragmentManager().findFragmentById(R.id.details_frame);
        if (fragment == null || fragment.getShownSourceId() != sourceId) {
            fragment = ItemListFragment.newInstance(sourceId);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.details_frame, fragment);
            ft.commit();
        }
    }
}
