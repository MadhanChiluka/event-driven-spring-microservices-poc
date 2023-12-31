package com.eventdriven.commonservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_table")
@NoArgsConstructor
public class User {
	@Id
	private String userId;
	private String firstName;
	private String lastName;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "card_details_id")
	private CardDetails cardDetails;
}