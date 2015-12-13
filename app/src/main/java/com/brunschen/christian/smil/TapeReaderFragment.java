package com.brunschen.christian.smil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.brunschen.christian.graphic.android.GraphicView;

public class TapeReaderFragment extends NonInterceptingFragment {
  
  private GraphicView graphicView;
  private GraphicView.GraphicPosition graphicPosition;
  private TapeReader tapeReader;

  public TapeReaderFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    graphicView = new GraphicView(inflater.getContext());
    tapeReader = ((MainActivity) getActivity()).getSmil().tapeReader();
    graphicView.setLastGraphicPosition(graphicPosition);
    graphicView.setGraphic(tapeReader.graphic());

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
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.tape_reader, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.load_tape) {
      new TapesFragment().show(getFragmentManager(), "Tapes Dialog");
    } else if (item.getItemId() == R.id.remove_tape) {
      tapeReader.setTape(null);
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
  }

  @Override
  public void onDestroyView() {
    graphicPosition = graphicView.getGraphicPosition();
    super.onDestroyView();
    tapeReader = null;
    graphicView = null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
  
  public class TapesFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

      builder.setItems(SMIL.tapes, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          tapeReader.setTape(SMIL.tape(SMIL.tapes[which]));
        }
      });

      builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
        }
      });
      return builder.create();
    }
  }
}
