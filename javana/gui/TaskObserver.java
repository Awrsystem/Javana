package br.com.javana.gui;

public interface TaskObserver {
	public void update(String msg);
	public void update(double percentage);
	public void updateCursor();
}
