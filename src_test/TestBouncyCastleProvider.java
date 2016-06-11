public class TestBouncyCastleProvider {

	public static void main(String[] args) {
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

}
