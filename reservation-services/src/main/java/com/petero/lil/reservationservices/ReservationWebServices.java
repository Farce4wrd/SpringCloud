package com.petero.lil.reservationservices;

import java.sql.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationWebServices {
	private final ReservationRepository repository;
	
	
	public ReservationWebServices(ReservationRepository repository) {
		this.repository = repository;
	}
	
	
	@GetMapping("/{id}")
	public Reservation getReservation(@PathVariable("id") long id) {
		return this.repository.findById(id).get();
	}
	
	@GetMapping
	public Iterable<Reservation> getReservation(@RequestParam(name="date", required=false) Date date){
		if(null !=date) {
			return this.repository.findAllBydate(date);
		}
		return this.repository.findAll();
	}

}
