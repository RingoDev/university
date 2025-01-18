

import java.util.*;
import java.util.stream.*;


public class Parser {
	public static final int _EOF = 0;
	public static final int _initials = 1;
	public static final int _string = 2;
	public static final int _date = 3;
	public static final int _time = 4;
	public static final int _id = 5;
	public static final int _duration = 6;
	public static final int _open = 7;
	public static final int _active = 8;
	public static final int _done = 9;
	public static final int maxT = 16;

	static final boolean _T = true;
	static final boolean _x = false;
	static final int minErrDist = 2;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;
	
	public Scanner scanner;
	public Errors errors;

	void println(String string) { 
		System.out.println(string);
	}


    List<String> semErrors = new ArrayList<>();

    Comparator<Task> taskComparator = (o1, o2) -> {
       int number1 = Integer.parseInt(o1.id.substring(0, o1.id.length() - 1));
       int number2 = Integer.parseInt(o2.id.substring(0, o2.id.length() - 1));
       Character char1 = o1.id.charAt(o1.id.length() - 1);
       Character char2 = o2.id.charAt(o2.id.length() - 1);
       if (number1 == number2) return char1.compareTo(char2);
       else return number1 - number2;
    };
    Comparator<Developer> developerComparator = Comparator.comparing(d -> d.initials);
    Set<Task> tasks = new TreeSet<>(taskComparator);
    List<Booking> bookings = new ArrayList<>();
    Set<Developer> devs = new TreeSet<>(developerComparator);
    Task collectorTask;
    final String COLLECTOR_TASK_ID = "999a";
    Developer collectorDeveloper;
    final String COLLECTOR_DEVELOPER_INITIALS = "XXXX";


    void insertDev(Developer dev) {
        if (devs.stream().anyMatch(d -> d.initials.equals(dev.initials))) {
            SemErr("Entwickler/in " + dev.initials + " mehrfach gelistet. Der letzte Eintrag zÃÂÃÂ¤hlt.");
            devs.removeIf(d -> d.initials.equals(dev.initials));
        }
        devs.add(dev);
    }

    void insertTask(Task task) {
        if (getTask(task.id) != null) {
            SemErr("Aufgabe " + task.id + " mehrfach gelistet. Der letzte Eintrag zÃ¤hlt.");
            tasks.removeIf(t -> t.id.equals(task.id));
        }
        tasks.add(task);
    }

    Booking createBooking(String id) {
        Booking booking = new Booking();
        if (getTask(id) == null) {
            SemErr("Aufgabe " + id + " nicht gelistet. Die Buchungen fÃÂ¼r diese Aufgabe werden der Sammelaufgabe\n" +
                    COLLECTOR_TASK_ID + " zugeordnet.");
            if (collectorTask == null) {
                collectorTask = new Task();
                collectorTask.id = COLLECTOR_TASK_ID;
                collectorTask.status = "none";
                collectorTask.duration = 0.0;
                collectorTask.comment = "UNASSIGNED TASK";
            }
            booking.task = collectorTask;
        } else {
            booking.task = getTask(id);
        }
        return booking;
    }

    Developer getDev(String initials) {
        Developer dev = devs.stream().filter(d -> d.initials.equals(initials)).findFirst().orElse(null);
        if (dev == null) {
            SemErr("Entwickler/in mit den Initialen " + initials + " wird in einer Buchung angefÃ¼hrt aber nicht in der Entwicklerliste. Die Buchung wird dem Sammelentwickler mit den Initialen " + COLLECTOR_DEVELOPER_INITIALS + " zugeordnet");
            if (collectorDeveloper == null) {
                collectorDeveloper = new Developer();
                collectorDeveloper.initials = COLLECTOR_DEVELOPER_INITIALS;
                collectorDeveloper.name = "UNASSIGNED DEV";
            }
            return collectorDeveloper;
        }
        return dev;
    }

    Task getTask(String id) {
        return tasks.stream().filter(task -> task.id.equals(id)).findFirst().orElse(null);
    }

    List<Entry> getEntries() {
        return bookings.stream().flatMap(b -> b.entries.stream()).collect(Collectors.toList());
    }

    List<Task> getTasksWithStatus(String status) {
        return tasks.stream().filter(t -> status.equals(t.status)).sorted(taskComparator).collect(Collectors.toList());
    }

    Set<Developer> getParticipants() {
        Set<Developer> set = new TreeSet<>(Comparator.comparing(d -> d.initials));
        set.addAll(bookings.stream().flatMap(b -> b.entries.stream()).flatMap(e -> e.singleEntries.stream()).map(s -> s.dev).filter(d -> !d.initials.equals(COLLECTOR_DEVELOPER_INITIALS)).collect(Collectors.toList()));
        return set;
    }

    Set<Developer> getNonParticipants() {
        Set<Developer> participants = getParticipants();
        return devs.stream().filter(dev -> !participants.contains(dev)).collect(Collectors.toSet());
    }

    double getWorktime(Developer dev) {
        List<SingleEntry> list = bookings.stream().flatMap(b -> b.entries.stream()).flatMap(e -> e.singleEntries.stream())
                .filter(se -> se.dev == dev).collect(Collectors.toList());
        return list.stream().map(e -> e.hours).reduce(0.0, Double::sum);
    }

    double getTaskRestduration(Task task) {
        List<Entry> list = bookings.stream().filter(b -> b.task == task).flatMap(b -> b.entries.stream()).sorted((e1, e2) -> e2.date.compareTo(e1.date)).collect(Collectors.toList());
        return list.stream().findFirst().map(e -> e.rest).orElse(task.duration);
    }

    double getHoursBooked(Task task) {
        return bookings.stream().filter(b -> b.task == task).flatMap(b -> b.entries.stream()).flatMap(e -> e.singleEntries.stream())
                .map(se -> se.hours).reduce(0.0, Double::sum);
    }

    List<Developer> developersOnDate(String date) {
        return getEntries().stream().filter(e -> date.equals(e.date)).flatMap(e -> e.singleEntries.stream()).map(se -> se.dev).collect(Collectors.toList());
    }

    double workhoursOnDate(String date) {
        return getEntries().stream().filter(e -> date.equals(e.date)).flatMap(e -> e.singleEntries.stream()).map(se -> se.hours).reduce(0.0, Double::sum);
    }

    Set<String> getDates() {
        Set<String> set = new TreeSet<>(String::compareTo);
        set.addAll(getEntries().stream().map(e -> e.date).collect(Collectors.toList()));
        return set;
    }


    static class Developer{
        String initials;
        String name;
        double worktime;
        Developer(){
        this.worktime = 0.0;
        }
    }

    static class Task{
        String id;
        String status;
        double duration;
        String comment;
    }

    static class Booking{
        Task task;
        List<Entry> entries;

        Booking(){
        this.entries = new ArrayList<>();
        }
    }

    static class Entry{
        String date;
        double rest;
        List<SingleEntry> singleEntries;
        Entry(){
        this.singleEntries = new ArrayList<>();
        }
    }

    static class SingleEntry{
        Developer dev;
        double hours;
        SingleEntry(Developer dev, double hours){
            this.dev = dev;
            this.hours = hours;
        }
    }



	
/*----------------------------------------------------------
  --- SCANNER
  ----------------------------------------------------------*/



	public Parser(Scanner scanner) {
		this.scanner = scanner;
		errors = new Errors();
	}

	void SynErr (int n) {
		if (errDist >= minErrDist) errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public void SemErr (String msg) {
		if (errDist >= minErrDist) errors.SemErr(t.line, t.col, msg);
		errDist = 0;
	}
	
	void Get () {
		for (;;) {
			t = la;
			la = scanner.Scan();
			if (la.kind <= maxT) {
				++errDist;
				break;
			}

			la = t;
		}
	}
	
	void Expect (int n) {
		if (la.kind==n) Get(); else { SynErr(n); }
	}
	
	boolean StartOf (int s) {
		return set[s][la.kind];
	}
	
	void ExpectWeak (int n, int follow) {
		if (la.kind == n) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}
	
	boolean WeakSeparator (int n, int syFol, int repFol) {
		int kind = la.kind;
		if (kind == n) { Get(); return true; }
		else if (StartOf(repFol)) return false;
		else {
			SynErr(n);
			while (!(set[syFol][kind] || set[repFol][kind] || set[0][kind])) {
				Get();
				kind = la.kind;
			}
			return StartOf(syFol);
		}
	}
	
	void ScrumTaskboard() {
		Expect(10);
		Expect(11);
		Expect(12);
		String date = Date();
		Expect(4);
		Expect(13);
		while (la.kind == 1) {
			Developer();
		}
		Expect(14);
		while (la.kind == 5) {
			Task();
		}
		Expect(15);
		while (la.kind == 5) {
			Booking();
		}
		semErrors.forEach(e -> println(">>>Fehler: "+e));
		println("");
		
		println("");
		println("Diese " + getParticipants().size() + " Entwickler/innen waren beim Sprint dabei: " + getParticipants().stream().map(d -> d.initials).collect(Collectors.toList()).toString());
		println("Diese " + getNonParticipants().size() + " Entwickler/innen fehlten beim Sprint: " + getNonParticipants().stream().map(d -> d.initials).collect(Collectors.toList()).toString());
		println("");
		
		println("");
		List<Developer> result = getParticipants().stream().sorted((p1, p2) -> (int) (10 * (getWorktime(p2) - getWorktime(p1)))).collect(Collectors.toList());
		println("Die einzelnen Entwickler/innen leisteten folgende Stunden:");
		for (Developer dev: result)
		println("    - " + dev.name + " " + "[" + dev.initials + "] " + getWorktime(dev) + " Stunden");
		
		println("");
		List<Task> overTwo = tasks.stream().filter(t -> t.duration > 2).collect(Collectors.toList());
		println("Diese "+overTwo.size() +" Aufgaben haben eine ErstschÃÂ¤tzung > 2 Personentage: " +overTwo.stream().map(d -> d.id).collect(Collectors.toList()).toString());
		
		List<Task> finishedOverZero = getTasksWithStatus("done").stream().filter(t -> getTaskRestduration(t) > 0).collect(Collectors.toList());
		println("Diese " + finishedOverZero.size() + " Aufgaben haben den Status 'erledigt' mit Restaufwand > 0 Stunden: " + finishedOverZero.stream().map(t -> t.id).collect(Collectors.toList()).toString());
		
		println("");
		double active = 0;
		double done = 0;
		for (Task task : getTasksWithStatus("active")) {
		  active += getHoursBooked(task);
		}
		for (Task task : getTasksWithStatus("done")) {
		done += getHoursBooked(task);
		}
		println("FÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂ¼r diese " + getTasksWithStatus("active").size() + " aktiven Aufgaben wurden insgesamt " + active + " Stunden gebucht:" +
		             getTasksWithStatus("active").stream().map(t -> t.id).collect(Collectors.toList()).toString());
		     println("FÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂ¼r diese " + getTasksWithStatus("done").size() + " erledigten Aufgaben wurden insgesamt " + done + " Stunden gebucht:" +
		             getTasksWithStatus("done").stream().map(t -> t.id).collect(Collectors.toList()).toString());
		 
		println("");
		println("FÃ¼r die einzelnen aktiven Aufgaben wurde folgender Arbeitsaufwand gebucht:");
		getTasksWithStatus("active").forEach(t -> println("  - "+t.comment + " "+ "["+t.id+"]"+ " " + getHoursBooked(t) + " Stunden"));
		println("");
		println("FÃ¼r die einzelnen erledigten Aufgaben wurde folgender Arbeitsaufwand gebucht:");
		getTasksWithStatus("done").forEach(t -> println("  - "+t.comment + " "+ "["+t.id+"]"+ " " + getHoursBooked(t) + " Stunden"));
		
		println("");
		println("An den folgenden " + getDates().size() + " Tagen buchten die gelisteten Entwickler/innen den angefÃ¼hrten Arbeitsaufwand:");
		for (String bookingDate : getDates()) {
		   println("      " + bookingDate + " insgesamt " + workhoursOnDate(bookingDate) + " Stunden: " +
		           developersOnDate(bookingDate).stream().map(d -> d.initials).collect(Collectors.toSet()).toString());
		}
		println("");
		
	}

	String  Date() {
		String  date;
		Expect(3);
		date = t.val;
		return date;
	}

	void Developer() {
		Developer dev = new Developer();
		String initials = Initials();
		dev.initials = initials;
		String name = Name();
		dev.name = name;
		insertDev(dev);
	}

	void Task() {
		Task task = new Task();
		String id = Id();
		task.id = id;
		if (la.kind == 7 || la.kind == 8 || la.kind == 9) {
			String status = Status();
			task.status = status;
		}
		double days = Restduration();
		task.duration = days;
		String comment = Name();
		task.comment = comment;
		insertTask(task);
	}

	void Booking() {
		Entry entry;
		String id = Id();
		Booking booking = createBooking(id);
		String date = Date();
		entry = new Entry();entry.date = date;
		double days = Restduration();
		entry.rest = days;
		String i = Initials();
		Developer dev = getDev(i);
		double hours = Worktime();
		entry.singleEntries.add(new SingleEntry(dev,hours));
		while (la.kind == 1) {
			i = Initials();
			hours = Worktime();
			entry.singleEntries.add(new SingleEntry(getDev(i),hours));
		}
		booking.entries.add(entry);
		while (la.kind == 3) {
			entry = new Entry();
			date = Date();
			entry.date = date;
			days = Restduration();
			entry.rest = days;
			i = Initials();
			dev = getDev(i);
			hours = Worktime();
			entry.singleEntries.add(new SingleEntry(dev,hours));
			while (la.kind == 1) {
				i = Initials();
				dev = getDev(i);
				hours = Worktime();
				entry.singleEntries.add(new SingleEntry(dev,hours));
			}
			booking.entries.add(entry);
		}
		bookings.add(booking);
	}

	String  Initials() {
		String  initials;
		Expect(1);
		initials = t.val; 
		return initials;
	}

	String  Name() {
		String  comment;
		Expect(2);
		comment = t.val.replace("\"","");
		return comment;
	}

	String  Id() {
		String  id;
		Expect(5);
		id = t.val;
		return id;
	}

	String  Status() {
		String  status;
		status = "none";
		if (la.kind == 7) {
			Get();
			status = t.val;
		} else if (la.kind == 8) {
			Get();
			status = t.val;
		} else if (la.kind == 9) {
			Get();
			status = t.val;
		} else SynErr(17);
		return status;
	}

	double  Restduration() {
		double  days;
		Expect(6);
		days = Double.parseDouble(t.val);
		return days;
	}

	double  Worktime() {
		double  hours;
		Expect(6);
		hours = Double.parseDouble(t.val);
		return hours;
	}



	public void Parse() {
		la = new Token();
		la.val = "";		
		Get();
		ScrumTaskboard();
		Expect(0);

	}

	private static final boolean[][] set = {
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x}

	};
} // end Parser


class Errors {
	public int count = 0;                                    // number of errors detected
	public java.io.PrintStream errorStream = System.out;     // error messages go to this stream
	public String errMsgFormat = "-- line {0} col {1}: {2}"; // 0=line, 1=column, 2=text
	
	protected void printMsg(int line, int column, String msg) {
		StringBuffer b = new StringBuffer(errMsgFormat);
		int pos = b.indexOf("{0}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, line); }
		pos = b.indexOf("{1}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, column); }
		pos = b.indexOf("{2}");
		if (pos >= 0) b.replace(pos, pos+3, msg);
		errorStream.println(b.toString());
	}
	
	public void SynErr (int line, int col, int n) {
		String s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "initials expected"; break;
			case 2: s = "string expected"; break;
			case 3: s = "date expected"; break;
			case 4: s = "time expected"; break;
			case 5: s = "id expected"; break;
			case 6: s = "duration expected"; break;
			case 7: s = "open expected"; break;
			case 8: s = "active expected"; break;
			case 9: s = "done expected"; break;
			case 10: s = "\"Export\" expected"; break;
			case 11: s = "\"of\" expected"; break;
			case 12: s = "\"Taskboard\" expected"; break;
			case 13: s = "\"Developers\" expected"; break;
			case 14: s = "\"Tasks\" expected"; break;
			case 15: s = "\"Bookings\" expected"; break;
			case 16: s = "??? expected"; break;
			case 17: s = "invalid Status"; break;
			default: s = "error " + n; break;
		}
		printMsg(line, col, s);
		count++;
	}

	public void SemErr (int line, int col, String s) {	
		printMsg(line, col, s);
		count++;
	}
	
	public void SemErr (String s) {
		errorStream.println(s);
		count++;
	}
	
	public void Warning (int line, int col, String s) {	
		printMsg(line, col, s);
	}
	
	public void Warning (String s) {
		errorStream.println(s);
	}
} // Errors


class FatalError extends RuntimeException {
	public static final long serialVersionUID = 1L;
	public FatalError(String s) { super(s); }
}
