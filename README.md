icalexchange
============

This is a REST API that returns an iCalendar file with the events retrieved from a Microsoft Exchange Server. This can be used, for instance, to sync appointments from an Exchange Calendar to a Google Calendar.

Usage
-----

Edit the `Configuration.java` file and set the URI of the Exchange server, the username, domain, and the encrypted password of your Exchange account. To generate an encrypted password look at the `EncryptorTest.java`. The encryption algorythm has as input your current Exchange password and a random key (this key will be used each time you invoke the REST API). The output is the encrypted password that you must put in the `Configuration.java` file.

You must also configure your local time zone to be used with the all-day events (For a list of possible time zones see [here](http://tutorials.jenkov.com/java-date-time/java-util-timezone.html)). If you do not want to set your local time zone, you can disable all-day events by setting the `SUPPORT_ALL_DAY_EVENTS` field to `false`. The all day events will continue to be retrieved from the Exchange server, but they are export as normal events in the iCalendar file. 

In this configuration file you may also configure the number of months, before and after today, to retrieve from the Exchange server.

Before compiling, the EWSJavaAPI library must be installer in your local maven repository. Run:
```
mvn install:install-file -Dfile=lib/EWSJavaAPI_1.2.jar -DgroupId=EWSJavaAPI -DartifactId=EWSJavaAPI -Dversion=1.2 -Dpackaging=jar
```
Then compile the project:
```
mvn clean install
```
Now deploy the generate war file `icalexchange-war-1.0-SNAPSHOT.war`, from the target directory, to your favorite web server.
After deployed invoke the REST API as in the following example, replacing `mykey` with the key previously used to encrypt your password.
```
http://icalexchange.example.org/ical?key=mykey
```

License
-------

This project is released under the terms of the [MIT license](http://en.wikipedia.org/wiki/MIT_License).

Source Code
-----------

The source code of this project is available at https://github.com/edomingues/icalexchange.

