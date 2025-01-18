package todolist;
import java.time.LocalDate;
public class Todo {
	
	private final String description;
	private Status stat;
	private final LocalDate date;
	private final int id;
	
	//Constructor
	Todo(String description, LocalDate date, int id){
		
		this.description = description;
		this.date = date;
		this.id = id;
		this.stat = Status.Open;
		
	}

	public void setDone() {//set status to Done
		this.stat = Status.Done;
	}
	
	Status getStatus() {//get status
		return this.stat;
	}
	
	LocalDate getDate() {//get Date
		return this.date;
	}
	
	public String toString() {
		return id + ": "+date.toString()+" - "+description+"-----"+ stat.toString();
	}
	
	int getID() {
		return this.id;
	}
}
