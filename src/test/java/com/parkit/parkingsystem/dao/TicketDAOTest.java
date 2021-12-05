package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest
{
	private final DataBasePrepareService dataBasePrepareService = new DataBasePrepareService();
	private static final String VehicleRegNumber = "ABC123";

	private static DataBaseTestConfig dataBaseTestConfig;

	private static TicketDAO ticketDAO;

	@BeforeAll
	public static void setUp() {
		ticketDAO = new TicketDAO();
		dataBaseTestConfig = new DataBaseTestConfig();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
	}

	@BeforeEach
	public void setUpPerTest() {
		this.dataBasePrepareService.clearDataBaseEntries();
	}

	@Test
	public void saveTicket_InsertTicketInDB() {
		Ticket ticketEnter = new Ticket();
		ticketEnter.setVehicleRegNumber(VehicleRegNumber);
		ticketEnter.setPrice(1.5);
		ticketEnter.setInTime(new Date());
		ticketEnter.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));

		boolean result = ticketDAO.saveTicket(ticketEnter);

		//verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));

		//assertTrue(result);
	}




}
