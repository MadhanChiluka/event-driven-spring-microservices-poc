package com.eventdriven.shipmentservice.command.api.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {

	@Id
	private String shipmentId;
	private String orderId;
	private String ShipmentStatus;
}
