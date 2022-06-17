package com.petero.lil.roomreservationservice;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room-reservations")
public class RoomReservationWebService {

	private final RoomClient roomClient;
	private final GuestClient guestClient;
	private final ReservationClient reservationClient;
	
	public RoomReservationWebService(RoomClient roomClient, GuestClient guestClient, ReservationClient reservationClient) {
		this.roomClient = roomClient;
		this.guestClient = guestClient;
		this.reservationClient = reservationClient;
	}
	
	
//	@GetMapping
//	public List<RoomReservation> getRoomReservations(){
//		List<Room> rooms = this.roomClient.getAllRooms();
//		List<RoomReservation> roomReservations = new ArrayList<>();
//		rooms.forEach(room ->{
//			RoomReservation roomReservation = new RoomReservation();
//			roomReservation.setRoomNumber(room.getRoomNumber());
//			roomReservation.setRoomId(room.getId());
//			roomReservation.setRoomName(room.getName());
//			roomReservations.add(roomReservation);
//		});
//		return roomReservations;
//	}
//	
	@GetMapping
	public List<RoomReservation> getCompleteRoomReservations(@RequestParam(name="date") Date date){
		//get room details funny way through roomservice
		List<Room> rooms = this.roomClient.getAllRooms();
		//respecting reservationservices' method to include date, did so using date
		List<Reservation> reservations = this.reservationClient.getAllReservations(date);
	
		Map<Long, RoomReservation> roomReservations = new HashMap<>();
		//attaching reservations to room, dont care if it doesnt fit
		rooms.forEach(room ->{
			RoomReservation roomReservation = new RoomReservation();
			roomReservation.setRoomId(room.getId());
			roomReservation.setRoomName(room.getName());
			roomReservation.setRoomNumber(room.getRoomNumber());
			//places it into the hashmap
			roomReservations.put(room.getId(), roomReservation);
			
		});
		
		//for all reservations received, get their roomid and their guest id and assign (after attaching roomids have been attached in hashmap above)
		reservations.forEach(reservation ->{
			
			RoomReservation roomReservation = roomReservations.get(reservation.getRoomId());
			Guest guest = this.guestClient.getGuest(reservation.getGuestId());
			roomReservation.setFirstName(guest.getFirstName());
			roomReservation.setLastName(guest.getLastName());
			roomReservation.setDate(reservation.getDate());
		});
		
		
		return new ArrayList<>(roomReservations.values());
	}
	
	
}
