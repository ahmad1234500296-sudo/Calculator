package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField display;
    private String operator = "";
    private double num1 = 0;
    private boolean newNumber = true;

    public Calculator() {
        setTitle("My Calculator");
        setSize(350, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.BLACK);

        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.BLACK);
        display.setForeground(Color.WHITE);
        display.setBorder(
            BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 4, 8, 8));
        panel.setBackground(Color.BLACK);
        panel.setBorder(
            BorderFactory.createEmptyBorder(5, 10, 10, 10)
        );

        String[] buttons = {
            "C", "⌫", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "−",
            "1", "2", "3", "+",
            "√", "0", ".", "="
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusPainted(false);
            button.setForeground(Color.WHITE);
            button.setOpaque(true);

            if ("÷×−+=%√".contains(text)) {
                button.setBackground(new Color(255, 149, 0));
            } else if (text.equals("C") || text.equals("⌫")) {
                button.setBackground(new Color(100, 100, 100));
            } else {
                button.setBackground(new Color(45, 45, 45));
            }

            button.addActionListener(this);
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        try {
            if (command.matches("[0-9]")) {
                if (newNumber
                        || display.getText().equals("0")
                        || display.getText().equals("Invalid input")
                        || display.getText().equals("Cannot divide by zero")) {
                    display.setText(command);
                    newNumber = false;
                } else {
                    display.setText(display.getText() + command);
                }
            }

            else if (command.equals(".")) {
                if (newNumber) {
                    display.setText("0.");
                    newNumber = false;
                } else if (!display.getText().contains(".")) {
                    display.setText(display.getText() + ".");
                }
            }

            else if (command.equals("C")) {
                display.setText("0");
                num1 = 0;
                operator = "";
                newNumber = true;
            }

            else if (command.equals("⌫")) {
                if (!newNumber
                        && display.getText().length() > 1) {
                    display.setText(
                        display.getText().substring(
                            0, display.getText().length() - 1
                        )
                    );
                } else {
                    display.setText("0");
                    newNumber = true;
                }
            }

            else if (command.equals("√")) {
                double value = Double.parseDouble(display.getText());

                if (value < 0) {
                    display.setText("Invalid input");
                } else {
                    display.setText(format(Math.sqrt(value)));
                }

                operator = "";
                newNumber = true;
            }

            else if (command.equals("%")) {
                double value = Double.parseDouble(display.getText());
                display.setText(format(value / 100));

                operator = "";
                newNumber = true;
            }

            else if (command.equals("=")) {
                if (!operator.isEmpty()) {
                    double num2 =
                        Double.parseDouble(display.getText());

                    double result = calculate(num1, num2, operator);

                    display.setText(format(result));
                    operator = "";
                    newNumber = true;
                }
            }

            else if ("+−×÷".contains(command)) {
                num1 = Double.parseDouble(display.getText());
                operator = command;
                newNumber = true;
            }

        } catch (ArithmeticException ex) {
            display.setText("Cannot divide by zero");
            operator = "";
            newNumber = true;
        } catch (NumberFormatException ex) {
            display.setText("Invalid input");
            operator = "";
            newNumber = true;
        }
    }

    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+":
                return a + b;

            case "−":
                return a - b;

            case "×":
                return a * b;

            case "÷":
                if (b == 0) {
                    throw new ArithmeticException();
                }
                return a / b;

            default:
                return b;
        }
    }

    private String format(double value) {
        if (Double.isFinite(value)
                && value == Math.rint(value)
                && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }

        return String.valueOf(value);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator());
    }
}
