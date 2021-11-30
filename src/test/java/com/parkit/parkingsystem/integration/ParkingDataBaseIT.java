package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final String VehicleRegNumber = "ABC123";

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static ParkingSpotDAO parkingSpotDAO;

    @Mock
    private static TicketDAO ticketDAO;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() {
        ticketDAO = mock(TicketDAO.class);
        parkingSpotDAO = mock(ParkingSpotDAO.class);
        dataBasePrepareService = new DataBasePrepareService();
    }

    @AfterAll
    public static void tearDown() {
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(VehicleRegNumber);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Mockito.when(parkingSpotDAO.getNextAvailableSlot(Mockito.any())).thenReturn(1);
        parkingService.processIncomingVehicle();

        Mockito.verify(ticketDAO).saveTicket(Mockito.any(Ticket.class));
        Mockito.verify(parkingSpotDAO).updateParking(Mockito.any(ParkingSpot.class));

    }

    @Test
    public void testParkingLotExit() {
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        dataBasePrepareService.clearDataBaseEntries();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(VehicleRegNumber);
        ticket.setPrice(0);
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setOutTime(null);
        ticketDAO.saveTicket(ticket);
        Mockito.when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        Mockito.when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

        ParkingService parkingServiceOut = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingServiceOut.processExitingVehicle();
        Assertions.assertEquals(Math.round(1.5 * 0.95 * 100.0) / 100.0, ticket.getPrice());
    }
}
