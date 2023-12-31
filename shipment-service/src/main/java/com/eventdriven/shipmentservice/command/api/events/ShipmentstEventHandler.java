package com.eventdriven.shipmentservice.command.api.events;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.eventdriven.commonservice.events.OrderShippedEvent;
import com.eventdriven.commonservice.events.ShipmentCancelledEvent;
import com.eventdriven.shipmentservice.command.api.data.Shipment;
import com.eventdriven.shipmentservice.command.api.data.ShipmentRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ShipmentstEventHandler {

	private ShipmentRepository shipmentRepository;

	public ShipmentstEventHandler(ShipmentRepository shipmentRepository) {
		this.shipmentRepository = shipmentRepository;
	}

	@EventHandler
	public void on(OrderShippedEvent event) {
		log.info("OrderShippedEvent : {}", event.getShipmentId());
		Shipment shipment = new Shipment();
		BeanUtils.copyProperties(event, shipment);
		shipmentRepository.save(shipment);
		log.info("Shipment Saved for Order Id : {}", event.getOrderId());
	}

	@EventHandler
	public void on(ShipmentCancelledEvent event) {
		log.info("ShipmentCancelledEvent : {}", event.getShipmentId());
		Shipment shipment = shipmentRepository.findById(event.getShipmentId()).get();
		shipment.setShipmentStatus(event.getShipmentStatus());
		shipmentRepository.save(shipment);
	}
}
