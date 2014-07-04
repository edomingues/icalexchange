package edomingues.icalexchange;

import java.net.SocketException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/")
public class IcalExchangeResource {

	private IIcalFactory icalFactory;
	private IExchangeClient exchangeClient = new EwsJavaApiExchangeClient();
	
	public IcalExchangeResource() throws SocketException {
		icalFactory = new Ical4jFactory();
	}
	
	@GET
    @Path("/ical")
    @Produces("text/calendar; charset=UTF-8")
    public Response ical(@QueryParam("key") String key) throws Exception {	
    	final String fileName = "calendar.ics";
    	
    	List<Appointment> appointments = exchangeClient.getAppointments(key);
    	
    	String iCal = icalFactory.createCalendar(appointments);
    	
    	return Response.ok(iCal).header("Content-Disposition", "attachment; filename=" + fileName).build();
    }
}
