package com.nttdata.bbva.customer.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customerTypes")
public class CustomerType {
	@Id
	private String id;
	@NotEmpty(message = "El campo name es requerido.")
	private String name;
	@NotEmpty(message = "El campo shortName es requerido.")
	private String shortName;
}
