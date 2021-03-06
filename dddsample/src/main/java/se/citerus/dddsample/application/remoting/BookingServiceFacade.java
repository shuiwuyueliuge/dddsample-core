package se.citerus.dddsample.application.remoting;

import se.citerus.dddsample.application.remoting.dto.CargoRoutingDTO;
import se.citerus.dddsample.application.remoting.dto.ItineraryCandidateDTO;
import se.citerus.dddsample.application.remoting.dto.LocationDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This facade shields the domain layer - model, services, repositories -
 * from concerns about such things as the user interface and remoting.
 * It is an application service.
 */
public interface BookingServiceFacade extends Remote {

  String registerNewCargo(String origin, String destination) throws RemoteException;

  CargoRoutingDTO loadCargoForRouting(String trackingId) throws RemoteException;

  void assignCargoToRoute(String trackingId, ItineraryCandidateDTO itinerary) throws RemoteException;

  List<ItineraryCandidateDTO> requestPossibleRoutesForCargo(String trackingId) throws RemoteException;

  List<LocationDTO> listShippingLocations() throws RemoteException;

  List<CargoRoutingDTO> listAllCargos() throws RemoteException;

}
