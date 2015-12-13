package com.brunschen.christian.smil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.brunschen.christian.graphic.Color;
import com.brunschen.christian.graphic.android.GraphicView;

public class ControlPanelFragment extends NonInterceptingFragment {
  
  private ControlPanel controlPanel;
  private GraphicView.GraphicPosition graphicPosition;
  private GraphicView graphicView;

  public ControlPanelFragment() {
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    graphicView = new GraphicView(inflater.getContext());
    controlPanel = ((MainActivity) getActivity()).getSmil().controlPanel();
    controlPanel.graphic().backgroundColor = new Color(0.95f, 0.95f, 0.95f);
    graphicView.setGraphic(controlPanel.graphic());
    graphicView.setLastGraphicPosition(graphicPosition);

    graphicView.setOnTouchListener(new View.OnTouchListener() {
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
    });

    return graphicView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    graphicPosition = graphicView.getGraphicPosition();
    graphicView = null;
    super.onDestroyView();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
}
