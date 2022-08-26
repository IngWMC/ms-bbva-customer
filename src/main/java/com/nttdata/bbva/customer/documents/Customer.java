package com.nttdata.bbva.customer.documents;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "customers")
public class Customer {

	@Id
	private String id;
	@NotEmpty(message = "El campo fullName es requerido.")
	private String fullName;
	@NotEmpty(message = "El campo customerTypeId es requerido.")
	private String customerTypeId;
	private CustomerType customerType;
	@NotEmpty(message = "El campo identificationDocument es requerido.")
	private String identificationDocument;
	@NotEmpty(message = "El campo emailAddress es requerido.")
	@Email(message = "El campo emailAddress tiene un formato no válido.")
	private String emailAddress;

	private List<OpenAccount> openAccounts;
}
