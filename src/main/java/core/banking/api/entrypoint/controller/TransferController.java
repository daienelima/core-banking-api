package core.banking.api.entrypoint.controller;

import core.banking.api.application.usecase.TransferUseCase;
import core.banking.api.entrypoint.dto.TransferRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController implements TransferApi {

    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);
    private final TransferUseCase useCase;

    public TransferController(TransferUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequest request) {
        logger.info("Iniciando transferência de [{}] da conta [{}] para conta [{}]",
                request.amount(), request.fromAccountId(), request.toAccountId());

        useCase.execute(request.fromAccountId(), request.toAccountId(), request.amount());

        logger.info("Transferência concluída com sucesso. De: [{}], Para: [{}], Valor: [{}]",
                request.fromAccountId(), request.toAccountId(), request.amount());

        return ResponseEntity.ok().build();
    }
}

