module hanna.calculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens hanna.calculator to javafx.fxml;
    exports hanna.calculator;
}