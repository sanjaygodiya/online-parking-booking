package com.demo.parkingmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.parkingmanagementsystem.model.Parking;
import com.demo.parkingmanagementsystem.repo.ParkingRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ParkingServiceImpl implements ParkingService {

	@Autowired
	private ParkingRepository parkingRepo;

	public ParkingServiceImpl(ParkingRepository parkingRepo) {
		this.parkingRepo = parkingRepo;
	}

	
	/** 
	 * @param parkingDto
	 * @return Parking
	 */
	@Override
	@Transactional
	public Parking saveParkingDetails(Parking parkingDto) {

		try {
			return parkingRepo.save(parkingDto);
		} catch (Exception e) {
			log.error("Exeption in Parking service while saving data : " + e.getMessage());
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Parking> getParkingList(String city) {
		try {
			return parkingRepo.getParkingListByCity(city);
		} catch (Exception e) {
			log.error("Exeption in Parking service while retrieving parking list : " + e.getMessage());
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Parking getParkingDetails(long id) {
		try {
			return parkingRepo.findById(id).get();
		} catch (Exception e) {
			log.error("Exeption in Parking service while retrieving parking details : " + e.getMessage());
			return null;
		}
	}

}
