/**
 * Application entry point
 */

public class Luna {
    public static void main(String[] args) {
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
        while (!input.equals("bye")) {
            input = System.console().readLine();
            if (!input.equals("bye")) {
                System.out.println("Entered: " + input);
            }
        }

        System.out.println(goodbye);
    }
}
