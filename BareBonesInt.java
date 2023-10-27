import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;

public class BareBonesInt {

    public static void main(String[] args) {

        ArrayDeque<String> program = new ArrayDeque<>(); // store entire program as a list of tokens
        HashMap<String, Integer> variables = new HashMap<String, Integer>(); // dictionary to track variables

        program = programReader(); // converts program to token list

        for (int i = 0; i < program.size();i++) {
            executeCommand(program, variables); // runs through all the commands in the program
        }

        System.out.println("Final variable values = " + variables);
        System.exit(0); // leave program
    }

    private static ArrayDeque<String> programReader() {
        // reads the program from a text document

        ArrayDeque<String> program = new ArrayDeque<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("BareBonesCode2.txt"));
            String line = reader.readLine(); // read into a line string

            while (line != null) {

                ArrayDeque<String> tempProgram = new ArrayDeque<>();
                tempProgram = tokeniser(line); // splits a line into tokens

                int length = tempProgram.size();
                for (int i = 0; i < length; i = i + 1) {
                    program.addLast(tempProgram.getFirst()); // add a line's work of tokens to program list
                    tempProgram.removeFirst();
                }

                line = reader.readLine(); // reads a new line
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace(); // if all fails
        }

        return program;
    }

    private static ArrayDeque<java.lang.String> tokeniser(String line) {
        // turns a line of the text document into toens

        ArrayDeque<String> tokenLine = new ArrayDeque<>();
        String token = "";

        for (int i=0; i< line.length(); i++) {
            if ((line.charAt(i) != ' ') && (line.charAt(i) != ';')) { // add a character to token when no whitespace or
                token = token.concat(String.valueOf(line.charAt(i)));
            }

            if (!token.isEmpty() && ((line.charAt(i) == ' ') || (line.charAt(i) == ';'))) { // if finished token, add to the toke list
                tokenLine.addLast(token);
                token = "";
            }
        }

        return tokenLine;
    }

    private static void executeCommand(ArrayDeque<String> program, HashMap<String, Integer> variables) {
        // run any commands and then remove from program

        switch (program.getFirst()) {

            case "clear" -> {
                // set variable to zero/ initialise variable

                program.removeFirst();

                if (variables.get(program.getFirst()) == null) {
                    variables.put(program.getFirst(), 0);
                } else {
                    variables.remove(program.getFirst());
                    variables.put(program.getFirst(), 0);
                }

                program.removeFirst();

            } case "incr" -> {
                // increment 1
                program.removeFirst();

                int variable = variables.get(program.getFirst());
                variable++;

                variables.remove(program.getFirst());
                variables.put(program.getFirst(), variable);

                program.removeFirst();

            } case "decr" -> {
                // decrement 1

                program.removeFirst();

                int variable = variables.get(program.getFirst());
                variable--;

                variables.remove(program.getFirst());
                variables.put(program.getFirst(), variable);

                program.removeFirst();

            } case "while" -> {
                // while loops

                program.removeFirst();

                Boolean condition = false; // the condition of the while loop
                while (!condition) {

                    condition = whileLoop(program, variables);

                }

                while (!(program.getFirst().equals("end"))) { // end of while loop
                    program.removeFirst();
                }

                program.removeFirst();

            } case "not", "do", "then", "X", "0" -> { // extra words for understanding/user

                program.removeFirst();

            }
            default -> throw new IllegalStateException("Unexpected value: " + program.getFirst()); // can't find command

        }
    }

    private static boolean whileLoop (ArrayDeque<String> program, HashMap<String, Integer> variables) {
        // running a while loop - recursive

        ArrayDeque<String> whileProgram = program.clone();

        String whileVariable = whileProgram.getFirst();
        for (int i = 0; i < 2; i++) {
            whileProgram.removeFirst();
        }

        int whileCondition = Integer.parseInt(whileProgram.getFirst()); //getting while loop
        for (int i = 0; i < 2; i++) {
            whileProgram.removeFirst();
        }

        while (!(whileProgram.getFirst().equals("end")) && (variables.get(whileVariable) != whileCondition)) { // check condition at the end of the while loop
            executeCommand(whileProgram, variables); // calling commands inside the while loop
        }

        return variables.get(whileVariable) == whileCondition; // returns if the while loop condition is met

    }

}
