package events;

import domain.Nota;

public class NotaEvent extends Event<Nota>{
//    private Nota oldData, newData;
//    private EventType type;

    public NotaEvent(Nota oldData, Nota newData, EventType type) {
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
