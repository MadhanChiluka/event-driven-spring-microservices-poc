package com.eventdriven.userservice.projection;

import java.util.Optional;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.eventdriven.commonservice.model.User;
import com.eventdriven.commonservice.queries.GetUserPaymentDetailsQuery;
import com.eventdriven.userservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserProjection {

	private UserRepository userRepository;

	public UserProjection(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@QueryHandler
	public User getUserPaymentDetails(GetUserPaymentDetailsQuery query) {
		log.info("User Id : {}", query.getUserId());
		Optional<User> user = userRepository.findById(query.getUserId());
		log.info("User details {}", user.isPresent() ? user.get() : null);
		return user.isPresent() ? user.get() : null;
	}
}
