import java.io.InputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets;

println("\nTEST1")
byte[] messageData = compress("Hubert Dumpfrey")
println(guessDecompress(messageData))

println("\nTEST2")
byte[] msg2 = compress(
    (1..1025).collect{i -> "hello $i"}.join(",")
)
println(guessDecompress(msg2))

println("\nTEST3")
println(guessDecompress("Not actually compressed".getBytes()))

println("\nTEST4")
println(guessDecompress("PK throwing you off ha ha".getBytes()))


private String guessDecompress(byte[] bytes) {
    final byte expect0=0x1f, expect1=0x8b;
    (bytes.length>=2 && bytes[0]==expect0 && bytes[1]==expect1)
        ?decompressString(bytes)
        :new String(bytes, StandardCharsets.UTF_8);
}

private byte[] compress(String stringData) {
    ByteArrayOutputStream byteOut=new ByteArrayOutputStream()
    GZIPOutputStream zipOut = new GZIPOutputStream(byteOut)
    byte[] toZipBytes = stringData.getBytes()
    zipOut.write(toZipBytes, 0, toZipBytes.length)
    zipOut.flush()
    zipOut.close()
    byteOut.toByteArray()
}

private String decompressString(byte[] input) {
    ByteArrayInputStream byteIn=new ByteArrayInputStream(input)
    GZIPInputStream zipIn=new GZIPInputStream(byteIn)
    new String(readAll(zipIn), "UTF-8")
}

private byte[] readAll(InputStream instr) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream()
    byte[] buffer = new byte[1024]
    while (true) {
        int readLen=instr.read(buffer, 0, buffer.length)
        if (readLen==-1) break;
        baos.write(buffer, 0, readLen)
    }
    instr.close()
    baos.toByteArray()
}