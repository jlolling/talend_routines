
public class PlayWithHttpRequest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("http.keepAlive", "false");
		String user = "admin";
		String password = "admin";
		String url = "http://on-0337-jll.local:18080/manager/status";
		try {
			request(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("########################################################################");
		user = "dummy";
		password = "dummy";
		url = "http://on-0337-jll.local:18080/manager/status?dummy=dummy";
		try {
			request(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void request(String url, final String user, final String password) throws Exception {
		java.net.URL testUrl = new java.net.URL(url);

//		java.net.Authenticator
//				.setDefault(new java.net.Authenticator() {
//					
//					@Override
//					protected java.net.PasswordAuthentication getPasswordAuthentication() {
//
//						final String decryptedPassword_tHttpRequest_1 = password;
//						System.out.println("User=" + user);
//						return new java.net.PasswordAuthentication(
//								user,
//								decryptedPassword_tHttpRequest_1
//										.toCharArray());
//					}
//				});

		java.net.HttpURLConnection httpUrlConn = (java.net.HttpURLConnection) testUrl
				.openConnection();
		httpUrlConn.setRequestMethod("GET");
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
//		urlConn_tHttpRequest_1.setRequestProperty("no-cache", "true");
		boolean connected_tHttpRequest_1 = false;
		int responseCode_tHttpRequest_1 = 0;
		String responseMessage_tHttpRequest_1 = null;
		try {
			httpUrlConn.connect();
			connected_tHttpRequest_1 = true;
			responseCode_tHttpRequest_1 = httpUrlConn
					.getResponseCode();
			responseMessage_tHttpRequest_1 = httpUrlConn
					.getResponseMessage();

			byte[] buffer_tHttpRequest_1 = new byte[1024];
			int bos_buffer_tHttpRequest_1 = 0;
			StringBuilder sb_tHttpRequest_1 = new StringBuilder();

			if (java.net.HttpURLConnection.HTTP_OK == responseCode_tHttpRequest_1) {
				java.io.InputStream bis_tHttpRequest_1 = new java.io.BufferedInputStream(
						httpUrlConn.getInputStream());
				while ((bos_buffer_tHttpRequest_1 = bis_tHttpRequest_1
						.read(buffer_tHttpRequest_1)) != -1) {
					sb_tHttpRequest_1.append(new String(
							buffer_tHttpRequest_1, 0,
							bos_buffer_tHttpRequest_1));
				}
				bis_tHttpRequest_1.close();
			} else {

				System.err.println(responseCode_tHttpRequest_1
						+ " " + responseMessage_tHttpRequest_1);

			}

			System.out.println(sb_tHttpRequest_1.toString());
			httpUrlConn.disconnect();
		} catch (Exception e) {

			System.err.println(e.getMessage());

		}

	}

}
