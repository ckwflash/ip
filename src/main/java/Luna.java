/**
 * Application entry point
 */

public class Luna {
    public static void main(String[] args) {
        String[] inputs = new String[100];
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
                    System.out.println((i + 1) + ". " + inputs[i]);
                }

            } else {
                inputs[count] = input;
                count++;
                System.out.println("added: " + input);
            }
        }

        System.out.println(goodbye);
    }
}
