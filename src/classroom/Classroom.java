package classroom;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class Classroom {
    private SNode studentsInLine; 
    private SNode musicalChairs; 
    private Random random;
                                 
    private boolean[][] openSeats; 
    private Student[][] studentsInSeats; 

    public Classroom(SNode inLine, SNode mChairs, boolean[][] seats, Student[][] studentsSitting) {
        studentsInLine = inLine;
        musicalChairs = mChairs;
        openSeats = seats;
        studentsInSeats = studentsSitting;
        random = new Random();
    }

    public Classroom() {
        this(null, null, null, null);
    }

    public void enterClassroom(String filename) {
       try { 
            Scanner sc = new Scanner(new File(filename));
            int numOfStudents = sc.nextInt();

            for (int i = 0; i < numOfStudents; i++) {
        
                String firstName = sc.next();
                String lastName = sc.next();
                int height = sc.nextInt();

                Student info = new Student(firstName, lastName, height);
                SNode obj = new SNode(info, studentsInLine);
                studentsInLine = obj;

            }

            sc.close();
        }
        
        catch (FileNotFoundException e) {
            System.out.println("File not found:" + filename);
            e.printStackTrace();
        }
    }


    public void createSeats(String openSeatsFile) {
        try {
            Scanner sc = new Scanner(new File(openSeatsFile));    
            int row = sc.nextInt();
            int column = sc.nextInt();

            openSeats = new boolean[row][column];

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    openSeats[i][j] = sc.nextBoolean();
                }
            }
            studentsInSeats = new Student[row][column];
            sc.close();
        }
        
        catch(FileNotFoundException e) {
            System.out.println("File not found: " + openSeatsFile);
            e.printStackTrace();
        }
    }

    public void seatStudents() {
        for (int i = 0; i < openSeats.length; i++) {
            for (int j = 0; j < openSeats[i].length; j++) {
               
                if (openSeats[i][j] == true && studentsInLine != null && studentsInSeats[i][j] == null) {
                    studentsInSeats[i][j] = studentsInLine.getStudent();
                    studentsInLine = studentsInLine.getNext();
                }

            }
        }
    }

    public void insertMusicalChairs() {
        for (int i = 0; i < studentsInSeats.length; i++) {
            for (int j = 0; j < studentsInSeats[i].length; j++) {
               
                if (studentsInSeats[i][j] != null) {
                    SNode node = new SNode(studentsInSeats[i][j], null);
                    
                    if (musicalChairs == null) {
                        musicalChairs = node;
                        musicalChairs.setNext(node);
                    }

                    else {
                        node.setNext(musicalChairs.getNext());
                        musicalChairs.setNext(node);
                        musicalChairs = node;
                    }
                    studentsInSeats[i][j] = null;
                }
            }
        }
        studentsInLine = null;
    }

    public void moveStudentFromChairsToLine(int size) {

        int n = random.nextInt(size);
        SNode prev = musicalChairs;
        SNode current = musicalChairs.getNext();
        int counter = 0;

        while (counter != n) {
            prev = current;
            current = current.getNext();
            counter++;
        }
            if (current == musicalChairs.getNext()) {
                musicalChairs.setNext(current.getNext());
            }

            else if (current == musicalChairs) {
                prev.setNext(current.getNext());
                musicalChairs = prev;
            }

            else {
                prev.setNext(current.getNext());
            }
   
        insertByName(current.getStudent());

    }
    

    public void insertByName(Student eliminatedStudent) {
       
        SNode node = new SNode(eliminatedStudent, null);

        if (studentsInLine == null) {
            studentsInLine = node;
            return;
        }

        SNode current = studentsInLine;
        SNode previous = null;
       
        while (current != null && eliminatedStudent.compareNameTo(current.getStudent()) >= 0) {
            previous = current;
            current = current.getNext();
        }
        
        if (previous == null) {
            node.setNext(studentsInLine);
            studentsInLine = node;
            return;
        }

        previous.setNext(node);
        node.setNext(current);
    }

    public void eliminateLosingStudents() {
        int size = 0;
        if (musicalChairs != null) {
            SNode current = musicalChairs;

            do {
                size = size + 1;
                current = current.getNext();
            }

            while (current != musicalChairs);
        }

        while (size > 1) {
            moveStudentFromChairsToLine(size);
            size = size - 1;
        }
    }

    public void seatMusicalChairsWinner() {
        if (musicalChairs != null && musicalChairs.getNext().getNext() == musicalChairs) {
            Student winner = musicalChairs.getStudent();
            musicalChairs = null;

            for (int i = 0;  i < openSeats.length; i++) {
                for (int j = 0; j < openSeats[i].length; j++) {
                    if (openSeats[i][j] == true && studentsInSeats[i][j] == null) {
                        studentsInSeats[i][j] = winner;
                        return;
                    }
                }
            }
        }

        else {
            return;
        }
    }
        

    public void playMusicalChairs() {
        eliminateLosingStudents();
        seatMusicalChairsWinner();
        seatStudents();
    }

    public void printStudentsInLine() {
        System.out.println("Students in Line:");
        if (studentsInLine == null) {
            System.out.println("EMPTY");
        }

        for (SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext()) {
            System.out.print(ptr.getStudent().print());
            if (ptr.getNext() != null) {
                System.out.print(" -> ");
            }
        }
        System.out.println("\n");
    }

    public void printSeatedStudents() {

        System.out.println("Sitting Students:");

        if (studentsInSeats != null) {

            for (int i = 0; i < studentsInSeats.length; i++) {
                for (int j = 0; j < studentsInSeats[i].length; j++) {

                    String stringToPrint = "";
                    if (studentsInSeats[i][j] == null) {

                        if (openSeats[i][j] == false) {
                            stringToPrint = "X";
                        } else {
                            stringToPrint = "EMPTY";
                        }

                    } else {
                        stringToPrint = studentsInSeats[i][j].print();
                    }

                    System.out.print(stringToPrint);

                    for (int o = 0; o < (10 - stringToPrint.length()); o++) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        } else {
            System.out.println("EMPTY");
        }
        System.out.println();
    }

    public void printMusicalChairs() {
        System.out.println("Students in Musical Chairs:");

        if (musicalChairs == null) {
            System.out.println("EMPTY");
            System.out.println();
            return;
        }
        SNode ptr;
        for (ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext()) {
            System.out.print(ptr.getStudent().print() + " -> ");
        }
        if (ptr == musicalChairs) {
            System.out.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        System.out.println();
    }

    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    public SNode getStudentsInLine() {
        return studentsInLine;
    }

    public void setStudentsInLine(SNode l) {
        studentsInLine = l;
    }

    public SNode getMusicalChairs() {
        return musicalChairs;
    }

    public void setMusicalChairs(SNode m) {
        musicalChairs = m;
    }

    public boolean[][] getOpenSeats() {
        return openSeats;
    }

    public void setOpenSeats(boolean[][] a) {
        openSeats = a;
    }

    public Student[][] getStudentsInSeats() {
        return studentsInSeats;
    }

    public void setStudentsInSeats(Student[][] s) {
        studentsInSeats = s;
    }

}
