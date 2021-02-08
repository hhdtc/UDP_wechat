
import java.security.Key;
 
import javax.crypto.Cipher;
/**
 * A class handles encryption and decryption
 */
public class EncrypDES {
	

	private static String strDefaultKey = "saauoiyxcjd@#$%^&";
 

	private Cipher encryptCipher = null;
 

	private Cipher decryptCipher = null;
 
	/**
	 * default encryptor
	 * @throws Exception any exception
	 */
	public EncrypDES() throws Exception {
		this(strDefaultKey);
	}
 
	/**
	 * 
	 * @param KeyString specific key
	 * @throws Exception any exception
	 */
 
	public EncrypDES(String KeyString) throws Exception {
 
		// Security.addProvider(new com.sun.crypto.provider.SunJCE());
		Key key = getKey(KeyString.getBytes());
		encryptCipher = Cipher.getInstance("DES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);
		decryptCipher = Cipher.getInstance("DES");
		decryptCipher.init(Cipher.DECRYPT_MODE, key);
	}
 
	/**
	 * Byte to HEX string
	 * 
	 * 
	 * @param arraybytes byte which need to be transfer
	 * @return result string
	 * @throws Exception  any exception during this process
	 */
	public static String byteArrtoHexStr(byte[] arraybytes) throws Exception {
		int iLen = arraybytes.length;
		
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arraybytes[i];
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}
 
	/**
	 * HEx String to byte
	 * @param strIn String need to be handled
	 * @return bytes after transforming
	 * @throws Exception any exception
	 */
	public static byte[] hexStrtoByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}
 
	/**
	 * 
	 * bytes encryption
	 * @param arrB bytes need to be encrypted
	 * @return bytes after encrypted
	 * @throws Exception any exception
	 */
	public byte[] encrypt(byte[] arrB) throws Exception {
		return encryptCipher.doFinal(arrB);
	}
 
	/**
	 * String encryption
	 * @param strIn String need to be encrypted
	 * @return String after encryption
	 * @throws Exception any exception
	 */
	public String encrypt(String strIn) throws Exception {
		return byteArrtoHexStr(encrypt(strIn.getBytes()));
	}
 
	/**
	 * byte decryption
	 * @param arrB byte need to be decrypt
	 * @return bytes after decryption
	 * @throws Exception any exception
	 */
	public byte[] decrypt(byte[] arrB) throws Exception {
		return decryptCipher.doFinal(arrB);
	}
 
	/**
	 * string decryption
	 * @param strIn string need to be decryped
	 * @return string after decryption
	 * @throws Exception any exception
	 */
	public String decrypt(String strIn) throws Exception {
		return new String(decrypt(hexStrtoByteArr(strIn)));
	}
 
	/**
	 * generate key
	 * @param tmparr any byte
	 * @return key
	 * @throws Exception any exception
	 */
	private Key getKey(byte[] tmparr) throws Exception {
		byte[] bytearr = new byte[8];
		for (int i = 0; i < tmparr.length && i < bytearr.length; i++) {
			bytearr[i] = tmparr[i];
		}
		Key key = new javax.crypto.spec.SecretKeySpec(bytearr, "DES");
		return key;
	}
}