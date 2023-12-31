package com.eventdriven.commonservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "card_details")
public class CardDetails {
	@Id
	private String id;
	private String name;
	private String cardNumber;
	private Integer validUntilMonth;
	private Integer validUntilYear;
	private Integer cvv;
}
