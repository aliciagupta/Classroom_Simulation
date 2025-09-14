package classroom;
import java.util.Random;

public class Main{
	public static void main(String[] args) {
		Classroom classroom = new Classroom();
		classroom.enterClassroom("students1.in");
		classroom.createSeats("seating1.in");
		classroom.seatStudents();
		
		System.out.println("Initial classroom:");
		classroom.printClassroom();
		classroom.insertMusicalChairs();
		classroom.playMusicalChairs();

		System.out.println("After musical chairs:");
		classroom.printClassroom();
	}
}