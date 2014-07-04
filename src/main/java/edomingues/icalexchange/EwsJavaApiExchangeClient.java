package edomingues.icalexchange;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import microsoft.exchange.webservices.data.AppointmentSchema;
import microsoft.exchange.webservices.data.BodyType;
import microsoft.exchange.webservices.data.CalendarFolder;
import microsoft.exchange.webservices.data.CalendarView;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.FindItemsResults;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;
import edomingues.icalexchange.encryption.Encryptor;

public class EwsJavaApiExchangeClient implements IExchangeClient {
	
	public List<Appointment> getAppointments(String key) throws Exception {
		
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);

        ExchangeCredentials credentials = new WebCredentials(Configuration.USERNAME, new Encryptor().decrypt(Configuration.PASSWORD_ENCRYPTED, key), Configuration.DOMAIN);
        service.setCredentials(credentials);

        service.setUrl(new URI(Configuration.SERVER_URI));

        CalendarFolder folder = CalendarFolder.bind(service, WellKnownFolderName.Calendar);
		
		Calendar first = Calendar.getInstance(TimeZone.getTimeZone("Etc/UTC"));
		Calendar last = (Calendar) first.clone();
		first.add(Calendar.MONTH, -Configuration.NUMBER_OF_PAST_MONTHS_TO_GET);
		last.add(Calendar.MONTH, Configuration.NUMBER_OF_FUTURE_MONTHS_TO_GET);
        
        return findAppointments(service, folder, first.getTime(), last.getTime());
	}
	
	private List<Appointment> findAppointments(ExchangeService service, CalendarFolder folder, Date startDate, Date endDate) throws Exception
    {
        List<Appointment> appointments = new LinkedList<Appointment>();
        FindItemsResults<microsoft.exchange.webservices.data.Appointment> findResults = folder.findAppointments(new CalendarView(startDate, endDate));
        PropertySet properties = new PropertySet(AppointmentSchema.Subject, 
												AppointmentSchema.Body, 
												AppointmentSchema.Location, 
												AppointmentSchema.Start, 
												AppointmentSchema.End, 
												AppointmentSchema.IsAllDayEvent);
        properties.setRequestedBodyType(BodyType.Text);
        service.loadPropertiesForItems(findResults, properties);

        for (microsoft.exchange.webservices.data.Appointment appt : findResults.getItems())
        {        	
        	Appointment appointment = createAppointment(appt);
        	
        	appointments.add(appointment);    
        }
        
        return appointments;
    }

	private Appointment createAppointment(microsoft.exchange.webservices.data.Appointment appt)	throws Exception {
		Appointment appointment = new Appointment();
		appointment.subject = appt.getSubject();
		appointment.start = fixUtc(appt.getStart());
		appointment.end = fixUtc(appt.getEnd());
		appointment.isAllDayEvent = appt.getIsAllDayEvent();
		appointment.description = MessageBody.getStringFromMessageBody(appt.getBody());
		appointment.location = appt.getLocation();
		
		return appointment;
	}
	
	/**
	 * EWS wrongly returns a date from the server, which are in UTC, using the local time zone. 
	 * For instance, if the server has 8:00 UTC and the local time zone is UTC+1 it returns 8:00 UTC+1.
	 */
	private Date fixUtc(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Calendar result = Calendar.getInstance(TimeZone.getTimeZone("Etc/UTC"));
		result.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
		result.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));
		return result.getTime();
	}

}
