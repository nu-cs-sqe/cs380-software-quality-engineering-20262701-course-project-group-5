package catan.mvc_util;

import java.util.ArrayList;
import java.util.List;

public abstract class Observed {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    protected void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
