package com.brunschen.christian.smil;

class ListenableTypewriter extends Typewriter.Default {
  private StringBuilder stringBuilder = new StringBuilder();
  private Listener listener;
  
  @Override public synchronized String text() {
    return stringBuilder.toString();
  }

  @Override public synchronized int length() {
    return stringBuilder.length();
  }

  @Override public synchronized void clear() {
    stringBuilder = new StringBuilder();
    if (listener != null) {
      listener.onClear(this);
    }
  }

  @Override public synchronized void append(String s) {
    stringBuilder.append(s);
    if (listener != null) {
      listener.onAppend(this, s);
    }
  }
  
  public synchronized void setListener(Listener l) {
    listener = l;
  }
  
  public interface Listener {
    void onAppend(Typewriter typewriter, String s);
    void onClear(Typewriter typewriter);
  }
}