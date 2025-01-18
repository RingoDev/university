

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
		Date();
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
	}

	void Date() {
		Expect(3);
	}

	void Developer() {
		Initials();
		Name();
	}

	void Task() {
		Id();
		if (la.kind == 7 || la.kind == 8 || la.kind == 9) {
			Status();
		}
		Restduration();
		Name();
	}

	void Booking() {
		Id();
		Date();
		Restduration();
		Initials();
		Worktime();
		while (la.kind == 1) {
			Initials();
			Worktime();
		}
		while (la.kind == 3) {
			Date();
			Restduration();
			Initials();
			Worktime();
			while (la.kind == 1) {
				Initials();
				Worktime();
			}
		}
	}

	void Initials() {
		Expect(1);
	}

	void Name() {
		Expect(2);
	}

	void Id() {
		Expect(5);
	}

	void Status() {
		if (la.kind == 7) {
			Get();
		} else if (la.kind == 8) {
			Get();
		} else if (la.kind == 9) {
			Get();
		} else SynErr(17);
	}

	void Restduration() {
		Expect(6);
	}

	void Worktime() {
		Expect(6);
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
