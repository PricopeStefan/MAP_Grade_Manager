package events;

public class Event<E>{
    private E oldData, newData;
    private EventType type;

    public Event(E oldData, E newData, EventType type) {
        this.oldData = oldData;
        this.newData = newData;
        this.type = type;
    }

    public E getOldData() {
        return oldData;
    }

    public void setOldData(E oldData) {
        this.oldData = oldData;
    }

    public E getNewData() {
        return newData;
    }

    public void setNewData(E newData) {
        this.newData = newData;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}
