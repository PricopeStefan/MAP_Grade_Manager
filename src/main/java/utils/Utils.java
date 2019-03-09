package utils;

import domain.Nota;
import domain.Profesor;
import domain.Student;
import domain.Tema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Utils {
    private static Integer currentWeek;

    public static void writeNotaToStudentFile(Nota n, String feedback) {
        String fileName = n.getStudent().getNume() + ".txt";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
            bw.write("Tema: " + n.getTema().getID() + "\n");
            bw.write("Nota: " + n.getValoare() + "\n");
            bw.write("Predata in saptamana: " + n.getPredataPe() + "\n");
            bw.write("Deadline: " + n.getTema().getDeadLine() + "\n");
            bw.write("Feedback: " + feedback + "\n\n");
            bw.close();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void readWeekFromFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));
            String currentWeekString = br.readLine();
            String[] data = currentWeekString.split("=");
            currentWeek = Integer.parseInt(data[1]);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public static int getCurrentWeek() {
        return currentWeek;
    }

    public static double computeMedie(Student s, Iterable<Nota> noteS, Iterable<Tema> temeS) {
        ObservableList<Nota> note = FXCollections.observableList(StreamSupport.stream(noteS.spliterator(), false).collect(Collectors.toList()));
        Predicate<Nota> nuEsteAStudentului = n -> !n.getStudent().equals(s);

        note.removeIf(nuEsteAStudentului);

        double medie = 0;
        int ponderiNote = 0;

        for(int i = 0; i < note.size(); i++) {
            medie += note.get(i).getValoare() * (note.get(i).getTema().getDeadLine() - note.get(i).getTema().getDataPrimire());
            ponderiNote += note.get(i).getTema().getDeadLine() - note.get(i).getTema().getDataPrimire();
        }

        ObservableList<Tema> teme = FXCollections.observableList(StreamSupport.stream(temeS.spliterator(), false).collect(Collectors.toList()));
        Predicate<Tema> aPredatTema = t -> {
            for(int i = 0; i < note.size(); i++)
                if (note.get(i).getTema().equals(t))
                    return true;
            return false;
        };

        teme.removeIf(aPredatTema);
        for(int i = 0; i < teme.size(); i++) {
            if(teme.get(i).getDeadLine() < currentWeek) { //calculam media doar daca tema s-a terminat
                medie += teme.get(i).getDeadLine() - teme.get(i).getDataPrimire();
                ponderiNote += teme.get(i).getDeadLine() - teme.get(i).getDataPrimire();
            }
        }

        try {
            return round(medie / ponderiNote, 2);
        } catch (Exception ex) {
            return 1;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
