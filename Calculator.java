import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Calculator {
    private JFrame frame;
    private JTextField textField;
    private StringBuilder input = new StringBuilder();

    public Calculator() {
        frame = new JFrame("Calculator");
        textField = new JTextField();

        // Set up frame
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(textField, BorderLayout.NORTH);

        // Create buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4));
        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "C", "0", "=", "+"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.charAt(0) == 'C') {
                input.setLength(0);
                textField.setText("");
            } else if (command.charAt(0) == '=') {
                try {
                    double result = eval(input.toString());
                    textField.setText(String.valueOf(result));
                    input.setLength(0); // Clear input after calculation
                } catch (Exception ex) {
                    textField.setText("Error");
                }
            } else {
                input.append(command);
                textField.setText(input.toString());
            }
        }
    }

    private double eval(String expression) {
        // Simple eval implementation (for educational purposes)
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double result = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return result;
            }

            double parseExpression() {
                double result = parseTerm();
                for (;;) {
                    if (eat('+')) result += parseTerm(); // addition
                    else if (eat('-')) result -= parseTerm(); // subtraction
                    else return result;
                }
            }

            double parseTerm() {
                double result = parseFactor();
                for (;;) {
                    if (eat('*')) result *= parseFactor(); // multiplication
                    else if (eat('/')) {
                        double divisor = parseFactor();
                        if (divisor == 0) throw new RuntimeException("Division by zero");
                        result /= divisor; // division
                    } else return result;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double result;
                int startPos = this.pos;
                if (eat('(')) { // parentheses

result = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    result = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return result;
            }
        }.parse();
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
