package events;

import domain.Profesor;

public class ProfesorEvent extends Event<Profesor>{
    public ProfesorEvent(Profesor oldData,Profesor newData,EventType type){
        super(oldData,newData,type);
    }
}