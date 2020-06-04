
// This fails to do anything useful, but it's interesting
		TrustManager[] trustAllCerts = new TrustManager[] {
		    new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return new X509Certificate[0];
		        }
		        public void checkClientTrusted(
		            java.security.cert.X509Certificate[] certs, String authType) {
		            }
		        public void checkServerTrusted(
		            java.security.cert.X509Certificate[] certs, String authType) {
		        }
		    }
		};
		SSLContext sc = SSLContext.getInstance("TLSv1");
	    sc.init(null, trustAllCerts, new java.security.SecureRandom());
	    SSLContext.setDefault(sc);


	private static void blah() throws Exception {
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new org.apache.http.conn.ssl.TrustSelfSignedStrategy());
		org.apache.http.conn.ssl.SSLConnectionSocketFactory sslsf =
			new org.apache.http.conn.ssl.SSLConnectionSocketFactory(builder.build());
		RestAssured.config=RestAssured.config().sslConfig(
			new SSLConfig().sslSocketFactory(
				new org.apache.http.conn.ssl.SSLSocketFactory(
					new org.apache.http.conn.ssl.TrustSelfSignedStrategy(),
					new org.apache.http.conn.ssl.X509HostnameVerifier() {
						public void verify(String host, javax.net.ssl.SSLSocket ssl){}
						public boolean verify(String host, javax.net.ssl.SSLSession ssl){return true;}
						public void verify(String host, String[] cns, String[] subjectAlts){}
						public void verify(String host, java.security.cert.X509Certificate cert){}
					}
				)
			)
		);
	}
	private static void moreFail() throws Exception {
		RestAssured.config=new io.restassured.config.RestAssuredConfig().sslConfig(
			new SSLConfig()
				.relaxedHTTPSValidation()
				.allowAllHostnames()
				.keyStore(getKeyStoreFile(), "pass")
				//.x509HostnameVerifier(
				//	new org.apache.http.conn.ssl.X509HostnameVerifier() {
				//		public void verify(String host, javax.net.ssl.SSLSocket ssl){}
				//		public boolean verify(String host, javax.net.ssl.SSLSession ssl){return true;}
				//		public void verify(String host, String[] cns, String[] subjectAlts){}
				//		public void verify(String host, java.security.cert.X509Certificate cert){}
				//	}
				//)
		);

	}


    TrustManagerFactory tmf = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm()
    );
    tmf.init(getDevPortalKeyStore());
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
