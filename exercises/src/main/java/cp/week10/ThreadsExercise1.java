package cp.week10;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise1 {
	/*
	 * - Create a Counter class storing an integer (a field called i), with an
	 * increment and decrement method.
	 * - Make Counter thread-safe (see Chapter 2 in the book)
	 * - Does it make a different to declare i private or public?
	 */

	private static class Counter {
		private Integer i = 0;

		public void increment() {
			synchronized (this) {
				this.i++;
			}
		}

		public void decrement() {
			synchronized (this) {
				this.i--;
			}
		}

		@Override
		public String toString() {
			return this.i.toString();
		}
	}

	public static void main(String[] args) {
		var counter = new Counter();

		int amount = 1_000_000;
		Thread t1 = new Thread(() -> {
			int i = 0;
			while (i++ < amount) {
				counter.increment();
				counter.decrement();
			}
		});

		Thread t2 = new Thread(() -> {
			int i = 0;
			while (i++ < amount) {
				counter.decrement();
				counter.increment();
			}
		});

		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
			System.out.println(counter);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
