package com.brunschen.christian.smil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TypewriterFragment extends Fragment implements ListenableTypewriter.Listener {
  private static final String TAG = TypewriterFragment.class.getSimpleName();
  
  private boolean focusable = true;
  private TypewriterView typewriterView;
  private ListenableTypewriter typewriter;
  
  @Override
  public void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    setHasOptionsMenu(true);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_typewriter, container, false);
    typewriterView = (TypewriterView) view.findViewById(R.id.text_view);
    typewriterView.setTextIsSelectable(focusable);
    typewriterView.setMovementMethod(ScrollingMovementMethod.getInstance());
    typewriterView.setHeadPositionListener(new TypewriterView.HeadPositionListener() {
      @Override public void onHeadPosition(final float x, final float y) {
        typewriterView.postDelayed(new Runnable() {
          @Override public void run() {
            Log.i(TAG, String.format("scrolling to %f, %f", x,  y));
            Layout layout = typewriterView.getLayout();
            float xFrag = x / layout.getWidth();
            float yFrag = y / layout.getHeight();
            float xDelta = Math.max(0, layout.getWidth() - typewriterView.getWidth());
            float yDelta = Math.max(0, layout.getHeight() - typewriterView.getHeight());
            typewriterView.scrollTo(Math.round(xDelta * xFrag), Math.round(yDelta * yFrag));
          }
        }, 16l);
      }
    });

    typewriter = (ListenableTypewriter) ((MainActivity) getActivity()).getSmil().typewriter();
    typewriterView.setText(typewriter.text());
    typewriter.setListener(this);
    
    return view;
  }
  
  @Override
  public void onDestroyView() {
    typewriter.setListener(null);
    super.onDestroyView();    
  }
  
  @Override
  public void onAppend(Typewriter typewriter, final String s) {
    typewriterView.post(new Runnable() {
      @Override public void run() {
        typewriterView.append(s);
      }
    });
  }

  @Override
  public void onClear(Typewriter typewriter) {
    typewriterView.post(new Runnable() {
      @Override public void run() {
        typewriterView.setText("");
      }
    });
  }
}
