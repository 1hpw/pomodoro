import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class Pomodoro {
	private static final Scanner scanner = new Scanner(System.in);
	private static final int
		short_break_seconds = query_break("short", 3, 5)
		, long_break_seconds = query_break("long", 15, 30)
	;

	// side effects
	private static Timer timer;
	private static int interval;

	public static void main(final String[] args) {
		pomodoro(1);
	}

	private static void pomodoro(final int checkmark) {
		System.out.print("\033cWhat is your task? ");
		scanner.nextLine();
		final String task = scanner.nextLine();
		System.out.print("How many seconds do you want to spend on doing \033[1;4;5m" + task + "\033[0m? ");
		final int time = scanner.nextInt();

		final String message = "\033cDoing \033[1;4;5m" + task + "\033[0m for %d seconds.";
		System.out.print(String.format(message, time));
		countdown(time, message);

		System.out.print("\033c");
		if(checkmark < 4) {
			System.out.print("\033c");
			take_break("short");
			pomodoro(checkmark+1);
		} else {
			System.out.print("\033c");
			take_break("long");
			pomodoro(1);
		}
	}

	private static void take_break(final String type) {
		final int time = type=="short"? short_break_seconds: long_break_seconds;
		final String message = "\033c\033[3mTake a " + type + " break for %d seconds.\033[0m";
		System.out.print(String.format(message, time));
		countdown(time, message);
	}

	private static void countdown(final int seconds, final String message) {
		timer = new Timer();
		interval = seconds;
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(interval == 2) {timer.cancel();}
				--interval;
				System.out.print(String.format(message, interval));
			}
		}, 1000, 1000);
		while(1<interval) {
			// WTF?
			System.out.print("");
		}
		try{
			Thread.sleep(1000);
		} catch(Exception e){}
	}

	private static int query_break(final String type, final int min_minutes, final int max_minutes) {
		System.out.print(
			"\u001Bc"
			+ type.substring(0, 1).toUpperCase()
			+ type.substring(1)
			+ " breaks are usually between "
			+ min_minutes * 60
			+ " & "
			+ max_minutes * 60
			+ " seconds.\nHow many seconds are your "
			+ type
			+ " breaks? "
		);
		return scanner.nextInt();
	}
}
