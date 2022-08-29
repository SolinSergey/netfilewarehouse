module ru.gb.netfilewarehouse {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.codec;
    requires io.netty.buffer;
    requires java.sql;

    opens ru.gb.netfilewarehouse to javafx.fxml;
    exports ru.gb.netfilewarehouse;
    exports ru.gb.cloudmessages;
    opens ru.gb.cloudmessages to javafx.fxml;
    exports ru.gb.hlam;
    opens ru.gb.hlam to javafx.fxml;
}