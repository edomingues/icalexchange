package edomingues.icalexchange;

import java.util.List;

public interface IExchangeClient {

	public List<Appointment> getAppointments(String key) throws Exception;

}
