package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest
{
	private final DataBasePrepareService dataBasePrepareService = new DataBasePrepareService();

	@Spy
	private final TicketDAO ticketDAO;

	public TicketDAOTest()
	{
		ticketDAO = new TicketDAO();
	}

	@BeforeEach
	public void setUpPerTest()
	{
		this.dataBasePrepareService.clearDataBaseEntries();
	}

	@Test
	public void saveTicket_shouldATicketInDB_withTicket()
	{
		Ticket ticket = new Ticket();
		Ticket ticketEnter = new Ticket();
		String vehicleRegNumber = "ABCDEF";
		ticketEnter.setVehicleRegNumber(vehicleRegNumber);
		ticketEnter.setPrice(1.5);
		ticketEnter.setInTime(new Date());
		ticketEnter.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
		ticketDAO.saveTicket(ticketEnter);
		Ticket ticketExit = ticketDAO.getTicket(vehicleRegNumber);
		verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
		assertEquals(ticketEnter.getParkingSpot(), ticketExit.getParkingSpot());
		assertEquals(ticketEnter.getPrice(), ticketExit.getPrice());
		assertEquals(ticketEnter.getVehicleRegNumber(), ticketExit.getVehicleRegNumber());

		assertNotNull(ticketEnter.getInTime());
	}
}
