package com.nttdata.bbva.customer.documents;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpenAccount {
	private String id;
	private String customerId;
	private Customer customer;
	private String productId;
	private Product product;
	private BigDecimal amountAvailable; // Monto disponible
	private BigDecimal creditLine; // Monto de la linea de crédito (Tarjeta de crédito)
	private List<Operation> operations;
}
