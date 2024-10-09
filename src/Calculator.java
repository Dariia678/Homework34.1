import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {
        private JTextField display;
        private StringBuilder currentInput;

        public Calculator() {
            currentInput = new StringBuilder();

            // ������������ ����
            setTitle("������������� �����������");
            setSize(400, 600);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // ���� ��� �����
            display = new JTextField();
            display.setEditable(false);
            display.setFont(new Font("Arial", Font.PLAIN, 30));
            display.setHorizontalAlignment(JTextField.RIGHT);
            add(display, BorderLayout.NORTH);

            // ������ ������
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(5, 4, 10, 10));

            // ������
            String[] buttonLabels = {
                    "7", "8", "9", "/",
                    "4", "5", "6", "*",
                    "1", "2", "3", "-",
                    "0", "C", "?", "+",
                    "="
            };

            for (String label : buttonLabels) {
                JButton button = new JButton(label);
                button.setFont(new Font("Arial", Font.PLAIN, 30));
                button.addActionListener(this);
                buttonPanel.add(button);
            }

            add(buttonPanel, BorderLayout.CENTER);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            switch (command) {
                case "C": // ��������
                    clearDisplay();
                    break;
                case "?": // �������� ������� ������
                    backspace();
                    break;
                case "=": // ��������� ���������
                    calculate();
                    break;
                default: // ������ ������
                    appendToDisplay(command);
                    break;
            }
        }

        private void appendToDisplay(String value) {
            currentInput.append(value);
            display.setText(currentInput.toString());
        }

        private void clearDisplay() {
            currentInput.setLength(0); // �������� ������ ��������
            display.setText("");
        }

        private void backspace() {
            if (currentInput.length() > 0) {
                currentInput.setLength(currentInput.length() - 1); // �������� ������� ������
                display.setText(currentInput.toString());
            }
        }

        private void calculate() {
            try {
                // �������� ����������
                String result = String.valueOf(eval(currentInput.toString()));
                display.setText(result);
                currentInput.setLength(0);
                currentInput.append(result); // �������� ��������� ��� ��������� ���������
            } catch (Exception e) {
                display.setText("�������");
                currentInput.setLength(0);
            }
        }

        // ����� ��� ���������� ������������ ������
        private double eval(String expression) {
            // ������ ���������� ������ ����� �������������
            String[] tokens = expression.split(" ");
            double result = 0;
            double num1 = Double.parseDouble(tokens[0]);
            String operator = "";

            for (int i = 1; i < tokens.length; i++) {
                if (tokens[i].matches("[+\\-*/]")) {
                    operator = tokens[i];
                } else {
                    double num2 = Double.parseDouble(tokens[i]);
                    switch (operator) {
                        case "+":
                            num1 += num2;
                            break;
                        case "-":
                            num1 -= num2;
                            break;
                        case "*":
                            num1 *= num2;
                            break;
                        case "/":

                            if (num2 == 0) {
                                throw new ArithmeticException("ĳ����� �� ����");
                            }
                            num1 /= num2;
                            break;
                    }
                }
            }
            return num1;
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                Calculator calculator = new Calculator();
                calculator.setVisible(true);
            });
        }
    }
