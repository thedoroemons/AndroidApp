package jp.co.spajam.androidapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import jp.co.spajam.androidapp.broadcastreceiver.OnRotateBroadcastReceiver;
import jp.co.spajam.androidapp.fragment.DefaultFragment;
import jp.co.spajam.androidapp.fragment.HumanModeFragment;
import jp.co.spajam.androidapp.fragment.PetModeFragment;
import jp.co.spajam.androidapp.service.SensorService;


public class MainActivity extends ActionBarActivity implements DefaultFragment.OnClickListener,HumanModeFragment.OnClickListener {

    private DefaultFragment defaultFragment;
    private HumanModeFragment humanModeFragment;
    private PetModeFragment petModeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultFragment = new DefaultFragment();
        humanModeFragment = new HumanModeFragment();
        petModeFragment = new PetModeFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.base_layout, defaultFragment, "defaultFragment");
        transaction.commit();

    }

    @Override
    public void onClickMode(@IdRes int id) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (null != transaction) {
            switch (id) {
                case R.id.humanbtn:
                    transaction.replace(R.id.base_layout, humanModeFragment);
                    break;
                case R.id.petbtn:
                    transaction.replace(R.id.base_layout, petModeFragment);
                    break;
                case R.id.settingbtn:
                    startActivity(new Intent(this, SettingActivity.class));
                    break;
                default:
                    transaction.replace(R.id.base_layout, defaultFragment);
                    break;

            }
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Disable Back key
        if(0 != getFragmentManager().getBackStackEntryCount()) {
            getFragmentManager().popBackStack();
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
