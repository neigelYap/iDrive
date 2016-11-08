package util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Hex;
import org.owasp.esapi.crypto.CipherSpec;
import org.owasp.esapi.crypto.CipherText;
import org.owasp.esapi.crypto.CryptoHelper;
import org.owasp.esapi.crypto.PlainText;
import org.owasp.esapi.errors.ConfigurationException;
import org.owasp.esapi.errors.EncryptionException;

public class EncryptESAPI {
	public CipherText encrypt(SecretKey key, PlainText plain) {
	    String encryptAlgorithm = ESAPI.securityConfiguration().getEncryptionAlgorithm();
	    int encryptionKeyLength = ESAPI.securityConfiguration().getEncryptionKeyLength();
	    String randomAlgorithm = ESAPI.securityConfiguration().getRandomAlgorithm();
	 
	    SecureRandom random = SecureRandom.getInstance(randomAlgorithm);
	    SecretKey secretKey = CryptoHelper.generateSecretKey(encryptAlgorithm, encryptionKeyLength);
	    byte[] raw = secretKey.getEncoded();
	    SecretKeySpec secretKeySpec = new SecretKeySpec(raw, encryptAlgorithm );
	 
	    byte[] plaintext = plain.asBytes();
	    boolean overwritePlaintext = ESAPI.securityConfiguration().overwritePlainText();
	    assert key != null : "(Master) encryption key may not be null";
	 
	    boolean success = false;	// Used in 'finally' clause.
	    String xform = null;
		int keySize = key.getEncoded().length * 8;	// Convert to # bits
	 
		try {
		  xform = ESAPI.securityConfiguration().getCipherTransformation();
	      String[] parts = xform.split("/");
	      assert parts.length == 3 : "Malformed cipher transformation: " + xform;
	      String cipherMode = parts[1];
	 
	      if ( ! CryptoHelper.isAllowedCipherMode(cipherMode) )
	            throw new EncryptionException(...);
	 
		  Cipher encrypter = Cipher.getInstance(xform);
		  String cipherAlg = encrypter.getAlgorithm();
		  int keyLen = ESAPI.securityConfiguration().getEncryptionKeyLength();
	 
		  if ( keySize != keyLen ) {
			logger.warning(Logger.SECURITY_FAILURE, "Encryption key length mismatch. ESAPI.EncryptionKeyLength is " +
				 keyLen + " bits, but length of actual encryption key is " + keySize +
				" bits.  Did you remember to regenerate your master key (if that is what you are using)???");
		  }
	      if ( keySize &lt; keyLen ) {
			logger.warning(Logger.SECURITY_FAILURE, "Actual key size of " + keySize + " bits SMALLER THAN specified " +
				 "encryption key length (ESAPI.EncryptionKeyLength) of " + keyLen + " bits with cipher algorithm " + cipherAlg);
		  }
		  if ( keySize &lt; 80 ) {		// Most cryptographers today consider 80-bits to be the minimally safe key size.
			logger.warning(Logger.SECURITY_FAILURE, "Potentially unsecure encryption. Key size of " + keySize + "bits " +
			       "not sufficiently long for " + cipherAlg + ". Should use appropriate algorithm with key size " +
			       "of *at least* 80 bits except when required by legacy apps.");
		  }
	 
		  String skeyAlg = key.getAlgorithm();
		  if ( !( cipherAlg.startsWith( skeyAlg + "/" ) || cipherAlg.equals( skeyAlg ) ) ) {
			logger.warning(Logger.SECURITY_FAILURE, "Encryption mismatch between cipher algorithm (" +
				 cipherAlg + ") and SecretKey algorithm (" + skeyAlg + "). Cipher will use algorithm " + cipherAlg);
		  }
	 
		   byte[] ivBytes = null;
		   CipherSpec cipherSpec = new CipherSpec(encrypter, keySize);	// Could pass the ACTUAL (intended) key size
	 
		   if ( cipherSpec.requiresIV() ) {
			 String ivType = ESAPI.securityConfiguration().getIVType();
			 IvParameterSpec ivSpec = null;
			 if ( ivType.equalsIgnoreCase("random") ) {
				 ivBytes = ESAPI.randomizer().getRandomBytes(encrypter.getBlockSize());
			 } else if ( ivType.equalsIgnoreCase("fixed") ) {
				 String fixedIVAsHex = ESAPI.securityConfiguration().getFixedIV();
				 ivBytes = Hex.decode(fixedIVAsHex);
			 } else {
				 throw new ConfigurationException("Property Encryptor.ChooseIVMethod must be set to 'random' or 'fixed'");
			 }
			 ivSpec = new IvParameterSpec(ivBytes);
			 cipherSpec.setIV(ivBytes);
			 encrypter.init(Cipher.ENCRYPT_MODE, key, ivSpec);
			} else {
			  encrypter.init(Cipher.ENCRYPT_MODE, key);
			}
	 
			raw = encrypter.doFinal(plaintext);
	        CipherText ciphertext = new CipherText(cipherSpec, raw);
			success = true;
			return ciphertext;
		} catch (InvalidKeyException ike) {
		} catch (ConfigurationException cex) {
		} catch (InvalidAlgorithmParameterException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (BadPaddingException e) {
		} catch (NoSuchAlgorithmException e) {
		} catch (NoSuchPaddingException e) {
		} finally {
	       if ( success &amp;&amp; overwritePlaintext ) {
			 plain.overwrite();
			}
		}
	}
}
