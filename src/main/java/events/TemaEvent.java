package events;

import domain.Tema;

public class TemaEvent extends Event<Tema>{
//    private Nota oldData, newData;
//    private EventType type;

    public TemaEvent(Tema oldData, Tema newData, EventType type) {
//        this.oldData = oldData;
//        this.newData = newData;
//        this.type = type;
        super(oldData, newData, type);
    }

//    public Nota getOldData() {
//        return oldData;
//    }
//
//    public void setOldData(Nota oldData) {
//        this.oldData = oldData;
//    }
//
//    public Nota getNewData() {
//        return newData;
//    }
//
//    public void setNewData(Nota newData) {
//        this.newData = newData;
//    }
//
//    public EventType getType() {
//        return type;
//    }
//
//    public void setType(EventType type) {
//        this.type = type;
//    }
}
