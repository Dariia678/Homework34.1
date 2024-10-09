import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {
        private JTextField display;
        private StringBuilder currentInput;

        public Calculator() {
            currentInput = new StringBuilder();

            // Налаштування вікна
            setTitle("Інтерактивний калькулятор");
            setSize(400, 600);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // Поле для вводу
            display = new JTextField();
            display.setEditable(false);
            display.setFont(new Font("Arial", Font.PLAIN, 30));
            display.setHorizontalAlignment(JTextField.RIGHT);
            add(display, BorderLayout.NORTH);

            // Панель кнопок
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(5, 4, 10, 10));

            // Кнопки
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
                case "C": // Очистити
                    clearDisplay();
                    break;
                case "?": // Видалити останній символ
                    backspace();
                    break;
                case "=": // Обчислити результат
                    calculate();
                    break;
                default: // Додати символ
                    appendToDisplay(command);
                    break;
            }
        }

        private void appendToDisplay(String value) {
            currentInput.append(value);
            display.setText(currentInput.toString());
        }

        private void clearDisplay() {
            currentInput.setLength(0); // Очистити текуще введення
            display.setText("");
        }

        private void backspace() {
            if (currentInput.length() > 0) {
                currentInput.setLength(currentInput.length() - 1); // Видалити останній символ
                display.setText(currentInput.toString());
            }
        }

        private void calculate() {
            try {
                // Виконати обчислення
                String result = String.valueOf(eval(currentInput.toString()));
                display.setText(result);
                currentInput.setLength(0);
                currentInput.append(result); // Зберегти результат для подальших обчислень
            } catch (Exception e) {
                display.setText("Помилка");
                currentInput.setLength(0);
            }
        }

        // Метод для обчислення математичних виразів
        private double eval(String expression) {
            // Просте обчислення виразів через інтерпретацію
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
                                throw new ArithmeticException("Ділення на нуль");
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
