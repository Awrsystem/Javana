package br.com.javana.gui;

public interface TaskSubject {
	public abstract void registerObserver(TaskObserver o);

	public abstract void removeObserver(TaskObserver o);

	public abstract void notifyObservers();

	public abstract void notifyPercentage(double percentage);
	
	public abstract void notifyCursor();
}
