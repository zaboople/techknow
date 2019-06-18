import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This pulls a RSA 256 public key out of a typical PEM file. Doing that is rather poorly documented.
 */
public class RSA256PublicKeyParser {
    private final static String publicKeyString="""
        -----BEGIN CERTIFICATE-----
        MIIDFTCCAf2gAwIBAgIJMGy1UXpygxTFMA0GCSqGSIb3DQEBCwUAMCgxJjAkBgNV
        BAMTHXNpZW1lbnMtcWEtMDAwMjguZXUuYXV0aDAuY29tMB4XDTE5MDYxMjE2MzU0
        MVoXDTMzMDIxODE2MzU0MVowKDEmMCQGA1UEAxMdc2llbWVucy1xYS0wMDAyOC5l
        dS5hdXRoMC5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCWVnjk
        SRgV9ev7PScQ52fzxulNbC+6NMsooDhLHYCiGtJj62n5g7VlqcbShGic0gDkjSYA
        Z1fqHZzCZU4r/kFpGDu5Qwz6Cjs0A1WP3uECgIArsrrW1DVo0tAjVSt+1A+Khijk
        eRmgd683ovE1/JD4QkWALFNnqrndXSq/i2uBz/v91oeovmej7ze6lBXg4wTqEQHC
        ryMQPiXn/l+lTt5y6H62YVv+wxROLGyn+smkdRPjJcFKQP2lpsufCmUzwKdn8ibt
        zXcm9a7iuAMb2UVWfgmNn6sJGpZiHRBiRJn4qdLyHOinIa/NBS0zS3X1aKfFyecJ
        Fiw5mIJm4BCN76YzAgMBAAGjQjBAMA8GA1UdEwEB/wQFMAMBAf8wHQYDVR0OBBYE
        FG55CCGrMgdFx6I1Nj1kBVeGisIjMA4GA1UdDwEB/wQEAwIChDANBgkqhkiG9w0B
        AQsFAAOCAQEAS71zByvxhZf2M1IbeAqJn0hgFdg0TYi49v3LlriiG0FMfUb8/VDA
        Xwr8tdr6yTByq0FXFf7jvW3mokfxYsOwTpUpHcGFgt1I0+JTv05vs3prP+i+f4c1
        NfYYHWw/uYq/F2Pd1292Oxn24Yq+pjAeloVRAjpbhFQKIDqBmeEY8YhiITVAp8ED
        S1sCm6VEgdhlZ5utRh3bLERaUdxvP61pqhrecLZij/XlZ3J2C/Ub8b4/j7JlAMGQ
        zh8aFuXMdfB7M7Um52h8e3es/pACTMEUKyRi3jXJaKShakVh9NCdupWhyekrxkXj
        qKagamza1LD5VMyIYpIzTndjJAJZGUnTiQ==
        -----END CERTIFICATE-----
    """.stripIndent().trim()

    public static PublicKey getRSAPublicKeyFromPEM(String pem) {
        byte[] keyBytes = getPEMBytes(pem);
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        X509Certificate cer = (X509Certificate) fact.generateCertificate(new ByteArrayInputStream(keyBytes));
        PublicKey pubKey = cer.getPublicKey();
    }

    private static byte[] getPEMBytes(String pem) {
        final String beginCertificate="-----BEGIN CERTIFICATE-----";
        final String endCertificate="-----END CERTIFICATE-----";
        pem=pem.substring(pem.indexOf(beginCertificate)+beginCertificate.length());
        pem=pem.substring(0, pem.indexOf(endCertificate)).replaceAll("\n", "").trim();
        return Base64.getDecoder().decode(pem);
    }

    public static void main(String[] args) {
        println(getRSAPublicKeyFromPEM(publicKeyString))
    }
}
