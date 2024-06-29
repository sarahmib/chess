package ui;

import java.util.Scanner;

public class Repl {
    private ChessClient client = null;
    Scanner scanner = new Scanner(System.in);

    public Repl(String serverUrl) {
        try {
            client = new ChessClient(serverUrl, scanner);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    public void run() {
        System.out.println("Welcome to chess. Sign in to start.");
        System.out.print(client.help());

        String result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                String msg = e.toString();
                System.out.print(msg);
            }
        }
        System.exit(0);
    }

}