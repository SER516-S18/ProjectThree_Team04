package team04.project3.util;

public interface Subject {

    void register(Observer observer);
    void unregister(Observer observer);
    void notifyObserver();
}
