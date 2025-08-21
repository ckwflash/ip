/**
 * Application entry point
 */

public class Luna {
    public static void main(String[] args) {
        Task[] tasks = new Task[100];
        int count = 0;

        String intro =
            "____________________________________________________________\n"
                + " Hello, nice to meet you! I'm Luna\n"
                + " What can I do for you?\n"
                + "____________________________________________________________\n";
        String goodbye =
            "____________________________________________________________\n"
                + " Goodbye! Hope to see you again\n"
                + "____________________________________________________________\n";
        System.out.println(intro);

        String input = "";
        while (true) {
            input = System.console().readLine();
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                for (int i = 0; i < count; i++) {
                    System.out.println((i + 1) + ". " + tasks[i].taskView());
                }
                System.out.println();
            } else if (input.startsWith("mark")) {
                markCommand(input, tasks, count, true);
                System.out.println();
            } else if (input.startsWith("unmark")) {
                markCommand(input, tasks, count, false);
                System.out.println();
            } else {
                tasks[count] = new Task(input);
                count++;
                System.out.println("added: " + input);
                System.out.println();
            }
        }

        System.out.println(goodbye);
    }

    /**
     * Checks if the marking is valid and does so
     */
    private static void markCommand(String input, Task[] tasks, int count, boolean markDone) {
        String[] parts = input.split(" ");
        if (parts.length > 1) {
            int index = Integer.parseInt(parts[1]) - 1;
            if (index >= 0 && index < count) {
                tasks[index].markDone(markDone);
                if (markDone) {
                    System.out.println("Nice! This task has been marked as done:");
                    System.out.println("  " + tasks[index].taskView());
                } else {
                    System.out.println("OK, This task has been marked as not done yet:");
                    System.out.println("  " + tasks[index].taskView());
                }
            }
        }
    }
}
