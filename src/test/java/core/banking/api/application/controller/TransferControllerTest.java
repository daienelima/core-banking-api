package core.banking.api.application.controller;

import core.banking.api.application.usecase.TransferUseCase;
import core.banking.api.entrypoint.controller.TransferController;
import core.banking.api.entrypoint.dto.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

    @Mock
    private TransferUseCase useCase;

    @InjectMocks
    private TransferController controller;

    private TransferRequest request;

    @BeforeEach
    void setup() {
        request = new TransferRequest(1L, 2L, new BigDecimal("100.00"));
    }

    @Test
    void shouldTransferSuccessfully() {

        ResponseEntity<Void> response = controller.transfer(request);

        assertEquals(200, response.getStatusCode().value());

        verify(useCase, times(1)).execute(
                request.fromAccountId(),
                request.toAccountId(),
                request.amount()
        );
    }

}