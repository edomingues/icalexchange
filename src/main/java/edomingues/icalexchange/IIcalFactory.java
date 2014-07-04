package edomingues.icalexchange;

import java.util.List;


public interface IIcalFactory {
	
	public String createCalendar(List<Appointment> appointments) throws Exception; 
}
