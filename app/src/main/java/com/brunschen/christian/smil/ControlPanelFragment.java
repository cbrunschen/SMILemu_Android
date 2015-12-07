package com.brunschen.christian.smil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brunschen.christian.graphic.Color;
import com.brunschen.christian.graphic.android.GraphicView;

public class ControlPanelFragment extends Fragment {
  
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
