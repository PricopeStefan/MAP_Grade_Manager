package events;
//
import domain.Student;
//
public class StudentEvent extends Event<Student>{
//    private Student oldData, newData;
//    private EventType type;
//
    public StudentEvent(Student oldData, Student newData, EventType type) {
        super(oldData, newData, type);
    }
//
//    public Student getOldData() {
//        return oldData;
//    }
//
//    public void setOldData(Student oldData) {
//        this.oldData = oldData;
//    }
//
//    public Student getNewData() {
//        return newData;
//    }
//
//    public void setNewData(Student newData) {
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
//
}
