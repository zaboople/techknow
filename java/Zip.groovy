import java.io.InputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

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
    int P='P', K='K'
    if (bytes[0]!=P || bytes[1]!=K)
        return new String(bytes, "UTF-8")
    decompressString(bytes)
}

private byte[] compress(String stringData) {
    ByteArrayOutputStream byteOut=new ByteArrayOutputStream()
    ZipOutputStream zipOut = new ZipOutputStream(byteOut)
    zipOut.putNextEntry(new ZipEntry("xxx"))
    byte[] toZipBytes = stringData.getBytes()
    zipOut.write(toZipBytes, 0, toZipBytes.length)
    zipOut.flush()
    zipOut.closeEntry()
    zipOut.close()
    byteOut.toByteArray()
}

private String decompressString(byte[] input) {
    ByteArrayInputStream byteIn=new ByteArrayInputStream(input)
    ZipInputStream zipIn=new ZipInputStream(byteIn)
    zipIn.getNextEntry()
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