package core.banking.api.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    private Long id;

    private String name;
    private BigDecimal balance;
}