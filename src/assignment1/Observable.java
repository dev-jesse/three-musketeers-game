package assignment1;

public abstract class Observable {

	public abstract void register(Observer o);
	public abstract void unregister(Observer o);
	public abstract void notifyObserver();

}
