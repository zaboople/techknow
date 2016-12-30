import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.sql.*;
import java.util.Base64;

public class CrawlDBMetaData {

  public static void main(String[] args) throws Exception {
    String user=null, pass=null, url=null, driver=null;
    boolean dump=false;
    for (int i=0; i<args.length; i++)
      if (args[i].startsWith("-us"))
        user=args[++i];
      else
      if (args[i].startsWith("-p"))
        pass=args[++i];
      else
      if (args[i].startsWith("-ur"))
        url=args[++i];
      else
      if (args[i].startsWith("-dr"))
        driver=args[++i];
      else
      if (args[i].startsWith("-du"))
        dump=true;
      else {
        fail(true, "Unexpected "+args[i]);
        return;
      }
    if (
        fail(user == null, "Missing user") ||
        fail(pass == null, "Missing password") ||
        fail(url == null, "Missing url") ||
        fail(driver == null, "Missing driver")
      )
      return;
    Class.forName(driver);
    Connection conn = DriverManager.getConnection(url, user, pass);
    run(conn, dump);
  }
  private static boolean fail(boolean condition, String msg) {
    if (condition) {
      System.out.println("ERROR: "+msg);
      System.out.println("Usage: -user <user> -pass <pass> -url <url> -driver <driverclass>");
    }
    return condition;
  }

  private static void run(Connection conn, boolean dumpData) throws Exception {
    DatabaseMetaData dmd = conn.getMetaData();
    ResultSet tables = dmd.getTables(null, null, null, new String[]{"TABLE"});
    while (tables.next())  {
      Object catalog = tables.getObject(1);
      Object schema = tables.getObject(2);
      Object table = tables.getObject(3);
      System.out.println("CATALOG: "+catalog +" SCHEMA: "+schema+" TABLE: "+table);
      dumpTable(conn, schema.toString(), table.toString(), dumpData);
    }
  }
  private static void dumpTable(Connection conn, String schema, String tableName, boolean dumpData) throws Exception     {
    tableName = schema+"."+tableName;
    System.out.println("TABLE: "+tableName);

    System.out.print("COLUMNS:\t");
    ResultSet rs = conn.createStatement().executeQuery("select * from "+tableName);

    ResultSetMetaData rsmd = rs.getMetaData();
    int colCount = rsmd.getColumnCount();
    for (int i=1; i<=colCount; i++)
      System.out.print(rsmd.getColumnName(i)+"\t");

    System.out.println();
    if (dumpData) {
      System.out.print("\t");
      while (rs.next()) {
        System.out.print("\t");
        for (int i=1; i<=colCount; i++)
          System.out.print(rs.getObject(i)+"\t");
      }
      System.out.println();
    }
  }

}