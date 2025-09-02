package hanna.calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CalculatorController {
    @FXML
    private Button btnPercent;
    @FXML
    private Button btnNegate;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSubtract;
    @FXML
    private Button btnMultiply;
    @FXML
    private Button btnDivide;
    @FXML
    private TextField output;

    private Double firstNumber;
    private String binaryOperator;
    private boolean lastButtonWasDigit = true;

    @FXML
    private void initialize() {
        btnPercent.setText(CalculatorConstants.OPERATOR_PERCENT);
        btnNegate.setText(CalculatorConstants.OPERATOR_NEGATE);

        btnAdd.setText(CalculatorConstants.OPERATOR_ADD);
        btnSubtract.setText(CalculatorConstants.OPERATOR_SUBTRACT);
        btnMultiply.setText(CalculatorConstants.OPERATOR_MULTIPLY);
        btnDivide.setText(CalculatorConstants.OPERATOR_DIVIDE);

        output.setText(CalculatorConstants.OUTPUT_EMPTY_VALUE);
    }

    @FXML
    private void handleNumberButtonClick(ActionEvent actionEvent) {
        String digit = ((Button) actionEvent.getSource()).getText();

        if (!lastButtonWasDigit || isOutputEmpty()) {
            setOutputString(digit);
        } else {
            setOutputString(getOutputString() + digit);
        }

        lastButtonWasDigit = true;
    }

    @FXML
    private void handleDotButtonClick(ActionEvent actionEvent) {
        if (lastButtonWasDigit) {
            if (!getOutputString().contains(".")) {
                setOutputString(getOutputString() + ".");
            }
        } else {
            setOutputString("0.");
            lastButtonWasDigit = true;
        }
    }

    @FXML
    private void handleClearButtonClick(ActionEvent actionEvent) {
        firstNumber = null;
        binaryOperator = null;
        lastButtonWasDigit = true;
        setOutputString(CalculatorConstants.OUTPUT_EMPTY_VALUE);
    }

    @FXML
    private void handleUnaryButtonClick(ActionEvent actionEvent) {
        if (isOutputEmpty()) {
            return;
        }

        String unaryOperator = ((Button) actionEvent.getSource()).getText();

        switch (unaryOperator) {
            case CalculatorConstants.OPERATOR_PERCENT -> setNumber(getNumber() / 100);
            case CalculatorConstants.OPERATOR_NEGATE -> setNumber(getNumber() * -1);
            default -> throw new IllegalArgumentException("Unknown operator " + unaryOperator);
        }
    }

    @FXML
    private void handleBinaryButtonClick(ActionEvent actionEvent) {
        if (lastButtonWasDigit) {
            if (firstNumber != null && binaryOperator != null) {
                setNumber(calculate());
            }

            firstNumber = getNumber();
        }

        binaryOperator = ((Button) actionEvent.getSource()).getText();
        lastButtonWasDigit = false;
    }

    @FXML
    private void handleEqualButtonClick(ActionEvent actionEvent) {
        if (lastButtonWasDigit && firstNumber != null && binaryOperator != null) {
            setNumber(calculate());

            firstNumber = getNumber();
            binaryOperator = null;
        }

        lastButtonWasDigit = false;
    }

    private String getOutputString() {
        return output.getText();
    }

    private void setOutputString(String outputNumber) {
        output.setText(outputNumber);
    }

    private double getNumber() {
        return Double.parseDouble(getOutputString());
    }

    private void setNumber(double number) {
        String text;
        double abs = Math.abs(number);

        if (abs >= 1_000_000_000 || (abs > 0 && abs < 0.000001)) {
            DecimalFormat df = new DecimalFormat("0.########E0", DecimalFormatSymbols.getInstance(Locale.US));
            text = df.format(number);
        } else {
            DecimalFormat df = new DecimalFormat("0.########", DecimalFormatSymbols.getInstance(Locale.US));
            text = df.format(number);
        }

        setOutputString(text);
    }

    private boolean isOutputEmpty() {
        return getOutputString().equals(CalculatorConstants.OUTPUT_EMPTY_VALUE);
    }

    private Double calculate() {
        Double secondNumber = getNumber();

        return switch (binaryOperator) {
            case CalculatorConstants.OPERATOR_ADD -> firstNumber + secondNumber;
            case CalculatorConstants.OPERATOR_SUBTRACT -> firstNumber - secondNumber;
            case CalculatorConstants.OPERATOR_MULTIPLY -> firstNumber * secondNumber;
            case CalculatorConstants.OPERATOR_DIVIDE -> {
                if (secondNumber == 0) {
                    yield Double.NaN;
                }
                yield firstNumber / secondNumber;
            }
            default -> throw new IllegalArgumentException("Unknown operator " + binaryOperator);
        };
    }
}