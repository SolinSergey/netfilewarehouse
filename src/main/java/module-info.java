module ru.gb.netfilewarehouse {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.gb.netfilewarehouse to javafx.fxml;
    exports ru.gb.netfilewarehouse;
}