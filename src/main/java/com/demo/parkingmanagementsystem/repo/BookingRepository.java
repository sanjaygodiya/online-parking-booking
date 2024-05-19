package com.demo.parkingmanagementsystem.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.parkingmanagementsystem.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	@Query(value = "SELECT * FROM BOOKING WHERE PARKINGID = ?1 AND VEHICLETYPE = ?2"
			+ " AND (PARKINGENDDATETIME LIKE ?3 OR PARKINGSTARTDATETIME LIKE ?3"
			+ " OR BOOKINGDATE LIKE ?3)", nativeQuery = true)
	public List<Booking> getBookings(long id, String vehicletype, String date);

	@Query(value = "SELECT COUNT(*) FROM BOOKING WHERE PARKINGID = ?1 AND VEHICLETYPE = ?2 AND "
			+ "TO_TIMESTAMP(?3,\'yyyy-MM-dd\"T\"HH24:mi\') < TO_TIMESTAMP(PARKINGENDDATETIME,\'yyyy-MM-dd\"T\"HH24:mi\') AND "
			+ "TO_TIMESTAMP(PARKINGSTARTDATETIME,\'yyyy-MM-dd\"T\"HH24:mi\') < TO_TIMESTAMP(?4,\'yyyy-MM-dd\"T\"HH24:mi\')", nativeQuery = true)
	public int getBookedSlots(long id, String vehicletype, String parkingstartdate, String parkingenddate);

	@Query(value = "SELECT B.*, P.PARKINGNAME FROM BOOKING B JOIN PARKINGINFORMATION P ON B.PARKINGID=P.PARKINGID WHERE B.USERPHONE=? ORDER BY B.BOOKINGDATE DESC, B.BOOKINGID DESC FETCH FIRST 15 ROW ONLY", nativeQuery = true)
	public List<Object[]> getBookingByPhone(long phone);
}
