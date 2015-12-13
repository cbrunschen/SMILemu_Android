package com.brunschen.christian.smil;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.brunschen.christian.graphic.android.AndroidFont;
import com.brunschen.christian.smil.sound.SoundGenerator;

public class MainActivity extends AppCompatActivity {
  private SMIL smil;

  private ControlPanelFragment controlPanelFragment;
  private TypewriterFragment typewriterFragment;
  private TapeReaderFragment tapeReaderFragement;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final AndroidFont f = new AndroidFont(Typeface.MONOSPACE, 10);
    final ControlPanel cp = new ControlPanel(f, f);

    smil = new SMIL();
    smil.setControlPanel(cp);
    
    TapeReader tapeReader = new TapeReader(smil.asyncIoClock(), SMIL.ticksPerSecond, f);
    tapeReader.setTape(SMIL.tape("A1"));
    smil.setTapeReader(tapeReader);
    smil.setSoundGenerator(new SoundGenerator() {
      @Override
      public void stopDestination(boolean finishPlaying, boolean retainData) {
      }

      @Override
      public void startDestination() {
      }

      @Override
      public void pushBufferToDestination() {
      }

      @Override
      public boolean canGenerateSound() {
        return false;
      }
    });
    smil.setTypewriter(new ListenableTypewriter());

    controlPanelFragment = new ControlPanelFragment();
    tapeReaderFragement = new TapeReaderFragment();
    typewriterFragment = new TypewriterFragment();

    smil.init();

    ViewPager pager = (ViewPager) findViewById(R.id.pager);
    pager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(pager);
  }

  @Override
  protected void onResume() {
    super.onResume();
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
          case MotionEvent.ACTION_POINTER_DOWN:
          case MotionEvent.ACTION_MOVE:
            v.getParent().requestDisallowInterceptTouchEvent(true);
            break;
          case MotionEvent.ACTION_UP:
          case MotionEvent.ACTION_CANCEL:
            v.getParent().requestDisallowInterceptTouchEvent(false);
            break;
        }
        return false;
      }
    };
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

  public class FragmentsAdapter extends FragmentPagerAdapter {
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
