package edomingues.icalexchange.encryption;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.junit.Test;

public class EncryptorTest {

	@Test
	public void testEncryptionDescryption() throws GeneralSecurityException, IOException {
		String password = "123";
		String key = "key";
		String cypher = new Encryptor().encrypt(password, key);
		String resultPassword = new Encryptor().decrypt(cypher, key);
		assertEquals(password, resultPassword);
	}

}
