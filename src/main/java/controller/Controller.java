package controller;

import events.Event;
import events.Observer;
import service.Service;
import utils.ViewAssistant;

public abstract class Controller implements Observer<Event>{
    protected Service service;
    protected ViewAssistant viewAssistant;

    public void setService(Service service) {
        if(service != null) {
            this.service = service;
            service.addObserver(this);
        }
    }

    public void setLogin(ViewAssistant viewAssistant) {
        this.viewAssistant = viewAssistant;
        setupGeneral();
    }

    protected abstract void setupGeneral();
}
