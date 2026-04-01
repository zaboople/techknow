import java.io.*;
import java.util.stream.*;
import java.util.function.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.format.ResolverStyle;
import java.security.SecureRandom;



public class Test {

    public static void main(String[] args) throws Exception {
        File f = new File(args[0]);
        if (!f.exists())
            throw new RuntimeException("No file: " + args[0]);
        byte[] array = new byte[10000000];
        try (FileInputStream fis = new FileInputStream(f);) {
            int bread=0;
            int offset=0;
            while (
                bread < array.length &&
                (bread=fis.read(array, 0+offset, array.length-offset))>0
            ) {
                offset+=bread;
            }
            String str = new String(array, 0, offset, "UTF-8");
            for (int i=0; i<str.length(); i++) {
                Integer q=(int)str.codePointAt(i);
                if (q < 0 || q > 127) {
                    System.out.print(i+" :"+q);
                    int presub = i - 20;
                    if (presub < 0)
                        presub=0;
                    int len = 40;
                    if (presub + len > str.length()) {
                        len = str.length() - presub;
                    }
                    System.out.append(" >").append(str.substring(presub, presub+len)).append("<");
                    System.out.println();
                }
            }
        }
    }
}
