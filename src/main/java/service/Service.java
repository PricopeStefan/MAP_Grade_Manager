package service;


import domain.Nota;
import domain.Profesor;
import domain.Student;
import domain.Tema;
import repository.ConcreteCrudRepository;
import utils.Utils;
import validators.ValidationException;
import utils.Pair;
import events.*;
import events.Observable;
import events.Observer;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Service implements Observable<Event>{
    private ConcreteCrudRepository<Integer, Student> repoStudenti;
    private ConcreteCrudRepository<Integer, Tema> repoTeme;
    private ConcreteCrudRepository<Integer, Profesor> repoProfesori;
    private ConcreteCrudRepository<Pair<Integer,Integer>, Nota> repoNote;

    private ArrayList<Observer<Event>> observers;
    /**
     * Constructorul explicit al clasei service
     * @param repoStudenti Repositoryul de studenti
     * @param repoTeme Repositoryul de teme
     * @param repoNote Repositoryul de note
     */
    public Service(ConcreteCrudRepository<Integer, Student> repoStudenti, ConcreteCrudRepository<Integer, Tema> repoTeme, ConcreteCrudRepository<Integer, Profesor> repoProfesori, ConcreteCrudRepository<Pair<Integer, Integer>, Nota> repoNote) {
        this.repoStudenti = repoStudenti;
        this.repoTeme = repoTeme;
        this.repoProfesori = repoProfesori;
        this.repoNote = repoNote;

        this.observers = new ArrayList<>();

        //validateNote();
    }

    /**
     * Verifica daca exista note in repositoryul de note de la studenti/teme inexistente
     */
    private void validateNote() {
        Iterable<Nota> all = repoNote.findAll();

        all.forEach((n) -> {
            Integer idStudent = n.getStudent().getID();
            Integer idTema = n.getTema().getID();

            if(repoStudenti.findOne(idStudent) == null || repoTeme.findOne(idTema) == null)
                repoNote.delete(new Pair<>(idStudent, idTema));
        });

    }

    /**
     * Metoda de save a unui student
     * @param s - studentul ce se va incerca sa se adauge
     * @return null daca a fost adaugat, elementul dat daca exista deja/nu este valid
     */
    public Student save(Student s) {
        Student deReturnat = repoStudenti.save(s);

        if(deReturnat == null) {
            notifyObservers(new StudentEvent(null,s, EventType.ADD));
        }
        else
            throw new ValidationException("Student existent deja!");

        return deReturnat;
    }

    /**
     * Metoda de save a unei teme
     * @param t - tema ce se va incerca sa se adauge
     * @return null daca a fost adaugata in repo tema, elementul dat in caz contrar
     */
    public Tema save(Tema t) {
        Tema deReturnat = repoTeme.save(t);

        if(deReturnat == null) {
            notifyObservers(new TemaEvent(null,t, EventType.ADD));
        }
        else
            throw new ValidationException("Tema existenta deja!");

        return deReturnat;
    }

    public Nota saveNota(Integer studentID, Integer temaID, Integer valoare, Integer predataPe, Profesor profesor, String feedback) {
        //se presupune ca sunt deja validate
        Student s = repoStudenti.findOne(studentID);
        Tema t = repoTeme.findOne(temaID);

        if(predataPe > t.getDeadLine() + 2) {
            return new Nota(s, t, -1, -1, profesor);
        }

        if(predataPe > t.getDeadLine()) {
            valoare = valoare - 3 * (predataPe - t.getDeadLine());
            System.out.println("A predat tema cu intarziere");
        }

        Nota n = new Nota(s, t, valoare, predataPe, profesor);
        System.out.println(n);
        Nota deReturnat = repoNote.save(n);

        if(deReturnat == null) {
            System.out.println("Se presupune ca notific " + observers.size() + " observers");
            Utils.writeNotaToStudentFile(n, feedback);
            notifyObservers(new NotaEvent(null,n, EventType.ADD));
        }
        else
            throw new ValidationException("Nota existenta deja!");

        return deReturnat;
    }

    public Profesor save(Profesor s) {
        Profesor deReturnat = repoProfesori.save(s);

        if(deReturnat == null) {
            notifyObservers(new ProfesorEvent(null,s, EventType.ADD));
        }
        else
            throw new ValidationException("Profesor existent deja!");

        return deReturnat;
    }

    /**
     * Metoda de stergere a unui student din repo
     * @param id - ID-ul studentului ce se va sterge
     * @return null daca elementul nu exista in repo, elementul cu id-ul dat in caz contrar
     */
    public Student deleteStudent(Integer id) {
        Student deSters = repoStudenti.findOne(id);
        Student deReturnat = repoStudenti.delete(id);

        if(deReturnat != null)
            notifyObservers(new StudentEvent(deSters, null, EventType.DELETE));
        else
            throw new ValidationException("Student inexistent!");

        return deReturnat;
    }

    /**
     * Metoda de stergere a unei teme din repo
     * @param id - ID-ul temei ce se va sterge
     * @return null daca elementul nu exista in repo, elementul cu id-ul dat in caz contrar
     */
    public Tema deleteTema(Integer id) {
        Tema deSters = repoTeme.findOne(id);
        Tema deReturnat = repoTeme.delete(id);

        if(deReturnat != null)
            notifyObservers(new TemaEvent(deSters, null, EventType.DELETE));
        else
            throw new ValidationException("Tema inexistenta!");

        return deReturnat;
    }

    public Profesor deleteProfesor(Integer id) {
        Profesor deSters = repoProfesori.findOne(id);
        Profesor deReturnat = repoProfesori.delete(id);

        if(deReturnat != null)
            notifyObservers(new ProfesorEvent(deSters, null, EventType.DELETE));
        else
            throw new ValidationException("Profesor inexistent!");

        return deReturnat;
    }

    public Nota deleteNota(Pair<Integer, Integer> id) {
        Nota deSters = repoNote.findOne(id);
        Nota deReturnat = repoNote.delete(id);

        if(deReturnat != null)
            notifyObservers(new NotaEvent(deSters, null, EventType.DELETE));
        else
            throw new ValidationException("Profesor inexistent!");

        return deReturnat;
    }
    /**
     * Metoda de modificare/update a unui student
     * @param nSt - noul student cu noi parametrii
     * @return null daca nu exista in repo, elementul modificat in caz contrar
     */
    public Student updateStudent(Student nSt) {
        Student old = repoStudenti.findOne(nSt.getID());
        Student deReturnat = repoStudenti.update(nSt);

        if(deReturnat != null) {
            notifyObservers(new StudentEvent(old, nSt, EventType.UPDATE));
        }
        else {
            throw new ValidationException("Elementul nu exista!");
        }

        return deReturnat;
    }

    /**
     * Metoda de modificare / update a unei teme
     * @param id - ID-ul temei ce se va modifica
     * @param newDeadline - Noul deadline al temei cu id-ul dat
     * @return null daca tema nu exista in repo, noul deadline in caz contrar
     */
    public Integer updateTema(Integer id, Integer newDeadline) {
        Tema t = repoTeme.findOne(id);
        if(t != null) {
            if(newDeadline != null && Utils.getCurrentWeek() < newDeadline && t.getDeadLine() < newDeadline) {
                t.setDeadLine(newDeadline);
                repoTeme.update(t);
                notifyObservers(new TemaEvent(t, t, EventType.UPDATE));
                return t.getDeadLine();
            }
        }
        return null;
    }

    public void updateNota(Pair<Integer, Integer> id, Integer newNota) {
        Nota n = repoNote.findOne(id);
        if(n != null) {
            if(newNota != null && newNota < 11 && newNota > 0) {
                n.setValoare(newNota);
                repoNote.update(n);
                notifyObservers(new NotaEvent(n, n, EventType.UPDATE));
            }
        }
    }
    public Iterable<Student> findAllStudents() {
        return repoStudenti.findAll();
    }

    public Tema findOneTema(Integer id) {
        return repoTeme.findOne(id);
    }

    public Iterable<Tema> findAllTeme() {
        return repoTeme.findAll();
    }

    public Iterable<Nota> findAllNote() {
        return repoNote.findAll();
    }

    public Iterable<Profesor> findAllProfesori() { return repoProfesori.findAll(); }

    /**
     * Functie pt size
     * @return size-ul repositoryului de studenti
     */
    public Integer sizeStudents() {
        return repoStudenti.size();
    }

    /**
     * Functie pt size
     * @return size-ul repositoryului de teme
     */
    public Integer sizeTeme() {
        return repoTeme.size();
    }


    public void addObserver(Observer<Event> obs) {
        observers.add(obs);
    }
    public void removeObserver(Observer<Event> event) {
        for(int i = 0; i < observers.size(); i++)
            if (observers.get(i) == null)
                observers.remove(i);
    }
    public void notifyObservers(Event E) {
        removeObserver(null);
        for(Observer<Event> obs:observers) {
            obs.update(E);
        }
    }
}
