module ru.gb.netfilewarehouse {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.codec;
    requires io.netty.buffer;
    requires java.sql;

    opens ru.gb.client to javafx.fxml;
    exports ru.gb.client;
    exports ru.gb.cloudmessages;
    opens ru.gb.cloudmessages to javafx.fxml;
}