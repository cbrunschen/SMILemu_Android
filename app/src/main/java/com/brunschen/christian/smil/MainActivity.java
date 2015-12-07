package com.brunschen.christian.smil;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.brunschen.christian.graphic.android.AndroidFont;
import com.brunschen.christian.smil.sound.SoundGenerator;

public class MainActivity extends ActionBarActivity {
  private SMIL smil;

  private ControlPanelFragment controlPanelFragment;
  private TypewriterFragment typewriterFragment;
  private TapeReaderFragment tapeReaderFragement;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final ActionBar actionBar = getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    setContentView(R.layout.activity_main);

    final AndroidFont f = new AndroidFont(Typeface.MONOSPACE, 10);
    final ControlPanel cp = new ControlPanel(f, f);

    smil = new SMIL();
    smil.setControlPanel(cp);
    
    TapeReader tapeReader = new TapeReader(smil.asyncIoClock(), SMIL.ticksPerSecond, f);
    tapeReader.setTape(SMIL.tape("A1"));
    smil.setTapeReader(tapeReader);
    smil.setSoundGenerator(new SoundGenerator() {
      @Override public void stopDestination(boolean finishPlaying, boolean retainData) { }
      @Override public void startDestination() { }
      @Override public void pushBufferToDestination() { }
      @Override public boolean canGenerateSound() { return false; }
    });
    smil.setTypewriter(new ListenableTypewriter());

    controlPanelFragment = new ControlPanelFragment();
    tapeReaderFragement = new TapeReaderFragment();
    typewriterFragment = new TypewriterFragment();

    smil.init();
    
    // Create a tab listener that is called when the user changes tabs.
    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
      public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        switch (tab.getPosition()) {
        case 0:
          getSupportFragmentManager().beginTransaction().replace(R.id.container, controlPanelFragment).commit();
          break;
        case 1:
          getSupportFragmentManager().beginTransaction().replace(R.id.container, tapeReaderFragement).commit();
          break;
        case 2:
          getSupportFragmentManager().beginTransaction().replace(R.id.container, typewriterFragment).commit();
          break;
        }
      }

      public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) { }
      public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) { }
    };
    
    actionBar.addTab(actionBar.newTab().setText("Control Panel").setTabListener(tabListener));
    actionBar.addTab(actionBar.newTab().setText("Tape Reader").setTabListener(tabListener));
    actionBar.addTab(actionBar.newTab().setText("Typewriter").setTabListener(tabListener));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public SMIL getSmil() {
    return smil;
  }

  public class FragmentsAdapter extends FragmentStatePagerAdapter {
    public FragmentsAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int index) {
      switch (index) {
      case 0:
        return controlPanelFragment;
      case 1:
        return tapeReaderFragement;
      case 2:
        return typewriterFragment;
      default:  
        return null;
      }
    }

    @Override
    public CharSequence getPageTitle(int index) {
      switch (index) {
      case 0:
        return "Control Panel";
      case 1:
        return "Tape Reader";
      case 2:
        return "Typewriter";
      default:  
        return null;
      }
    }

    @Override
    public int getCount() {
      return 3;
    }
  }
}
