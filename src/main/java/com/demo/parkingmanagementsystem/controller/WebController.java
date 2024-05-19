package com.demo.parkingmanagementsystem.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.demo.parkingmanagementsystem.model.Booking;
import com.demo.parkingmanagementsystem.model.BookingSuccess;
import com.demo.parkingmanagementsystem.model.Parking;
import com.demo.parkingmanagementsystem.model.User;
import com.demo.parkingmanagementsystem.repo.BookingRepository;
import com.demo.parkingmanagementsystem.service.EmailService;
import com.demo.parkingmanagementsystem.service.ParkingService;
import com.demo.parkingmanagementsystem.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebController {

	private UserService userService;
	private ParkingService parkingService;
	private BookingRepository bookingRepo;
	private EmailService emailService;

	public WebController(UserService userService, ParkingService parkingService, BookingRepository bookingRepo,
			EmailService emailService) {
		this.userService = userService;
		this.parkingService = parkingService;
		this.bookingRepo = bookingRepo;
		this.emailService = emailService;
	}

	
	/** 
	 * @param request
	 * @param session
	 * @return String
	 */
	@GetMapping(value = "/login")
	public String login(HttpServletRequest request, HttpSession session) {
		session.setAttribute("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		return "login";
	}

	
	/** 
	 * @param request
	 * @param key
	 * @return String
	 */
	private String getErrorMessage(HttpServletRequest request, String key) {
		Exception exception = (Exception) request.getSession().getAttribute(key);
		if (exception instanceof BadCredentialsException) {
			return "Invalid username or password!";
		} else if (exception instanceof LockedException) {
			return exception.getMessage();
		} else {
			return "Invalid username or password!";
		}
	}

	
	/** 
	 * @param authentication
	 * @param model
	 * @return String
	 * @throws NullPointerException
	 */
	@GetMapping(value = "/home")
	public String home(Authentication authentication, Model model) throws NullPointerException {
		if (authentication.getAuthorities().toString().equals("[ADMIN]")
				|| authentication.getAuthorities().toString().equals("[USER]")) {
			Booking booking = new Booking();
			booking.setParkingcity(userService.findUserByPhone(Long.parseLong(authentication.getName())).getCity());
			model.addAttribute("booking", booking);
			return "home";

		}
		return "login";
	}

	
	/** 
	 * @param authentication
	 * @param model
	 * @return String
	 * @throws NullPointerException
	 */
	@GetMapping(value = "/admin")
	public String admin(Authentication authentication, Model model) throws NullPointerException {
		if (authentication.getAuthorities().toString().equals("[ADMIN]")) {
			return "admin";
		}
		return "login";
	}

	
	/** 
	 * @param authentication
	 * @param model
	 * @return String
	 * @throws NullPointerException
	 */
	@GetMapping(value = "/addparking")
	public String addParking(Authentication authentication, Model model) throws NullPointerException {
		if (authentication.getAuthorities().toString().equals("[ADMIN]")) {
			model.addAttribute("parking", new Parking());
			return "addparking";
		}
		return "login";
	}

	
	/** 
	 * @param parking
	 * @param result
	 * @param model
	 * @return String
	 */
	@PostMapping(value = "/addparking")
	public String saveParking(@ModelAttribute("parking") Parking parking, BindingResult result, Model model) {
		log.info("Saving parking details : " + parking.getParkingname());
		try {
			if (parking.getParkingownerphone() == 0 || Long.toString(parking.getParkingownerphone()).length() < 10) {
				result.rejectValue("parkingownerphone", null, "Phone number is not valid");
			}
			if (parking.getParkingmanagerphone() == 0
					|| Long.toString(parking.getParkingmanagerphone()).length() < 10) {
				result.rejectValue("parkingmanagerphone", null, "Phone number is not valid");
			}
			if (result.hasErrors()) {
				model.addAttribute("parking", parking);
				return "admin";
			}
			Parking savedParking = parkingService.saveParkingDetails(parking);
			if (savedParking != null) {
				log.info("Parking details saved successfully. Id : " + parking.getParkingid() + ", Name : "
						+ parking.getParkingname());
				return "adminsuccess";
			}
		} catch (Exception e) {
			log.error("Exception while saving parking details : " + e.getMessage());
		}
		return "error";
	}

	
	/** 
	 * @param model
	 * @return String
	 */
	@GetMapping(value = "/register")
	public String newUser(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	
	/** 
	 * @param user
	 * @param result
	 * @param model
	 * @return String
	 */
	@PostMapping(value = "/register")
	public String registerUser(@ModelAttribute("user") User user, BindingResult result, Model model) {
		log.info("User registration started for : " + user.getFirstname() + " " + user.getLastname());
		try {
			if (user.getPhone() == 0 || Long.toString(user.getPhone()).length() < 10) {
				result.rejectValue("phone", null, "Phone number is not valid");
			}
			if (result.hasErrors()) {
				model.addAttribute("user", user);
				return "register";
			}
			User existingUser = userService.findUserByPhone(user.getPhone());
			if (existingUser != null) {
				log.info("An account already exists with phone number : " + existingUser.getPhone());
				result.rejectValue("phone", null, "There is already an account registered with the same phone number");
			}
			if (result.hasErrors()) {
				model.addAttribute("user", user);
				return "register";
			}
			User savedUser = userService.saveUser(user);
			if (savedUser != null) {
				log.info("User saved successfully : " + savedUser.getFirstname() + " " + savedUser.getLastname());
				return "success";
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return "error";
	}

	
	/** 
	 * @param booking
	 * @param result
	 * @param model
	 * @param authentication
	 * @return String
	 */
	@PostMapping(value = "/booking")
	public String parkingBooking(@ModelAttribute("booking") Booking booking, BindingResult result, Model model,
			Authentication authentication) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		User user = userService.findUserByPhone(Long.parseLong(authentication.getName()));

		booking.setUserphone(Long.parseLong(authentication.getName()));
		booking.setBookingdate(formatter.format(new Date()).toString());
		booking.setVehicleregistrationnumber(booking.getVehicleregistrationnumber().toUpperCase());

		log.info("--------User Booking Details--------");
		log.info("booking id : " + booking.getBookingid());
		log.info("phone: : " + booking.getUserphone());
		log.info("parking id : " + booking.getParkingid());
		log.info("parking city : " + booking.getParkingcity());
		log.info("vehicle type : " + booking.getVehicletype());
		log.info("reg number : " + booking.getVehicleregistrationnumber());
		log.info("booking date : " + booking.getBookingdate());
		log.info("start time : " + booking.getParkingstartdatetime());
		log.info("end time : " + booking.getParkingenddatetime());
		log.info("total hrs : " + booking.getTotalparkinghours());
		log.info("bill amount : " + booking.getBillamount());
		try {
			if (booking.getParkingid() == 0) {
				result.rejectValue("parkingid", null, "Parking is not valid");
			}
			if (result.hasErrors()) {
				model.addAttribute("booking", booking);
				return "home";
			}
			Booking savedBooking = bookingRepo.save(booking);

			log.info("--------Saved Booking Details--------");
			log.info("booking id : " + savedBooking.getBookingid());
			log.info("phone: : " + savedBooking.getUserphone());
			log.info("parking id : " + savedBooking.getParkingid());
			log.info("parking city : " + savedBooking.getParkingcity());
			log.info("vehicle type : " + savedBooking.getVehicletype());
			log.info("reg number : " + savedBooking.getVehicleregistrationnumber());
			log.info("booking date : " + savedBooking.getBookingdate());
			log.info("start time : " + savedBooking.getParkingstartdatetime());
			log.info("end time : " + savedBooking.getParkingenddatetime());
			log.info("total hrs : " + savedBooking.getTotalparkinghours());
			log.info("bill amount : " + savedBooking.getBillamount());

			Parking parkingDetails = getParkingDetails(savedBooking.getParkingid());
			BookingSuccess bookingSuccess = new BookingSuccess();

			bookingSuccess.setBookingid(savedBooking.getBookingid());
			bookingSuccess.setUserphone(savedBooking.getUserphone());
			bookingSuccess.setParkingname(parkingDetails.getParkingname());
			bookingSuccess
					.setParkingaddress(parkingDetails.getParkingaddress() + ", " + parkingDetails.getParkingcity());
			bookingSuccess.setVehicletype(savedBooking.getVehicletype());
			bookingSuccess.setVehicleregistrationnumber(savedBooking.getVehicleregistrationnumber());
			bookingSuccess.setBookingdate(savedBooking.getBookingdate());
			bookingSuccess.setParkingstartdatetime(savedBooking.getParkingstartdatetime());
			bookingSuccess.setParkingenddatetime(savedBooking.getParkingenddatetime());
			bookingSuccess.setTotalparkinghours(savedBooking.getTotalparkinghours());
			bookingSuccess.setBillamount(savedBooking.getBillamount());

			model.addAttribute("bookingSuccess", bookingSuccess);
			sendEmail(user.getEmail(), user.getFirstname(), bookingSuccess);
			return "bookingsuccess";

		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return "error";
	}

	
	/** 
	 * @param city
	 * @return List<Parking>
	 */
	@GetMapping(value = "/parkinglist")
	@ResponseBody
	public List<Parking> getParkingList(@RequestParam("city") String city) {
		return parkingService.getParkingList(city);
	}

	
	/** 
	 * @param id
	 * @return Parking
	 */
	@GetMapping(value = "/parkingdetails")
	@ResponseBody
	public Parking getParkingDetails(@RequestParam("id") long id) {
		return parkingService.getParkingDetails(id);
	}

	
	/** 
	 * @param id
	 * @param @RequestParam("vehicletype"
	 * @return List<Booking>
	 */
	@GetMapping(value = "/bookings")
	@ResponseBody
	public List<Booking> getBookings(@RequestParam("id") long id, @RequestParam("vehicletype") String vehicletype,
			@RequestParam("date") String date) {
		log.info("Id : " + id + ", Vehicle Type : " + vehicletype + ", Date : " + date);
		List<Booking> bookings = bookingRepo.getBookings(id, vehicletype, date + "%");
		log.info("Total bookings : " + bookings.size());
		return bookings;
	}

	
	/** 
	 * @param id
	 * @param @RequestParam("vehicletype"
	 * @return int
	 */
	@GetMapping(value = "/bookedslots")
	@ResponseBody
	public int getBookedSlots(@RequestParam("id") long id, @RequestParam("vehicletype") String vehicletype,
			@RequestParam("parkingstartdate") String parkingstartdate,
			@RequestParam("parkingenddate") String parkingenddate) {

		int bookedSlots = bookingRepo.getBookedSlots(id, vehicletype, parkingstartdate, parkingenddate);
		log.info("Total booked slots for parking id : " + id + " Vehicle Type : " + vehicletype
				+ " Parking Start Time : " + parkingenddate + " Parking End Time " + parkingenddate + " = "
				+ bookedSlots);
		return bookedSlots;
	}

	
	/** 
	 * @param authentication
	 * @param model
	 * @return String
	 */
	@GetMapping(value = "/mybookings")
	public String mybookings(Authentication authentication, Model model) {
		List<Object[]> bookingList = bookingRepo.getBookingByPhone(Long.parseLong(authentication.getName()));
		model.addAttribute("bookingList", bookingList);
		return "mybookings";
	}

	
	/** 
	 * @param toEmail
	 * @param userName
	 * @param bookingSuccess
	 */
	public void sendEmail(String toEmail, String userName, BookingSuccess bookingSuccess) {
		String subject = "BookMyParking Booking Id : "+bookingSuccess.getBookingid();
		String text = "<html><body><p>Dear "+userName+",</p><b>Congratulations Booking Successful !!</b>"
			+"<p>Here are your booking details:</p>"
			+"Booking Id : <span>"+bookingSuccess.getBookingid()+"</span><br>"
			+"Booking Date : <span>"+bookingSuccess.getBookingdate()+"</span><br>"
			+"Phone : <span>"+bookingSuccess.getUserphone()+"</span><br>"
			+"Parking Name : <span>"+bookingSuccess.getParkingname()+"</span><br>"
			+"Parking Address : <span>"+bookingSuccess.getParkingaddress()+"</span><br>"
			+"Vehicle Type : <span>"+bookingSuccess.getVehicletype()+"</span><br>"
			+"Vehicle Number : <span>"+bookingSuccess.getVehicleregistrationnumber()+"</span><br>"
			+"Parking Start Time : <span>"+bookingSuccess.getParkingstartdatetime()+"</span><br>"
			+"Parking End Time : <span>"+bookingSuccess.getParkingenddatetime()+"</span><br>"
			+"Total Parking Hours : <span>"+bookingSuccess.getTotalparkinghours()+"</span><br>"
			+"Bill Amount : <span>"+bookingSuccess.getBillamount()+"</span><br><br>Regards,<br>PMS"
			+"</body></html>";
		try {
			emailService.sendSimpleMessage(toEmail, subject, text);
			log.info("Email success for booking id : "+bookingSuccess.getBookingid());
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("Email error for booking id : "+bookingSuccess.getBookingid());
		}
	}

	@ExceptionHandler
	public String handleException(NullPointerException ex) {
		log.info("NullPointerException handling, rediricting to login page");
		return "login";
	}

}
