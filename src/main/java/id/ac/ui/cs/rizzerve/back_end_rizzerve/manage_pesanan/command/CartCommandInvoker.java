package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import org.springframework.stereotype.Component;

@Component
public class CartCommandInvoker {
    public Object executeCommand(CartCommand command) {
        return command.execute();
    }
}