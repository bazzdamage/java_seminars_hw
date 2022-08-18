import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

import static java.lang.Character.isDigit;

public class Sem04_Evaluate_Exp {
    public static void main(String[] args) {

        String exp = "(2^3 * (10 / (5 - 3)))"; // 2^3; 8 * (10 / (2); 8 * 5 = 40;
        //exp = "2+3*(2^4-5)^(6+3*2)-10";

        var ans = infix2Postfix(exp);
        System.out.println(ans);

    }

    public static int infix2Postfix(String exp) {

        var stack = new Stack<String>();
        var postfix = new ArrayList<String>();

        String trimmed = exp.replaceAll("\\s", "");
        char[] tokens = trimmed.toCharArray();
        var operands = tokenizer(tokens);

        for (String op : operands) {
            if (Character.isDigit(op.charAt(0))) {
                postfix.add(op);
            } else if (op.equals("(")) {
                stack.push(op);
            } else if (op.equals(")")) {
                while (!stack.isEmpty() && !Objects.equals(stack.peek(), "(")) {
                    postfix.add(stack.pop());
                }
                stack.pop();
            } else {
                while (!stack.isEmpty() && precedence(op) <= precedence(stack.peek())) {
                    postfix.add(stack.pop());
                }
                stack.push(op);
            }
        }
        while (!stack.isEmpty()) {
            if (Objects.equals(stack.peek(), "(")) {
                System.out.println("Invalid Expression");
                return 0;
            }
            postfix.add(stack.pop());
        }

        return postfixExpResolve(postfix);
    }

    private static int postfixExpResolve(ArrayList<String> exp) {

        var stack = new Stack<String>();

        for (String s : exp) {
            if (Character.isDigit(s.charAt(0))) {
                stack.push(s);
            } else {
                int op2 = Integer.parseInt(stack.pop());
                int op1 = Integer.parseInt(stack.pop());
                stack.push(simpleExpSolver(s, op1, op2));
            }
        }

        return Integer.parseInt(stack.pop());

    }

    private static String simpleExpSolver(String operator, int op1, int op2) {

        return switch (operator) {
            case "+" -> String.valueOf(op1 + op2);
            case "-" -> String.valueOf(op1 - op2);
            case "*" -> String.valueOf(op1 * op2);
            case "/" -> String.valueOf(op1 / op2);
            case "^" -> String.valueOf((int) (Math.pow(op1, op2)));
            default -> "0";
        };

    }

    private static int precedence(String s) {

        return switch (s) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> -1;
        };

    }

    private static ArrayList<String> tokenizer(char[] tokens) {

        var sb = new StringBuilder();
        var operands = new ArrayList<String>();

        for (int i = 0; i < tokens.length; i++) {
            while (i < tokens.length && isDigit(tokens[i])) {
                sb.append(tokens[i++]);
            }
            if (sb.length() > 0) {
                operands.add(sb.toString());
                sb.setLength(0);
            }
            if (i < tokens.length) operands.add(Character.toString(tokens[i]));
        }

        return operands;

    }
}
