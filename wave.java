import java.util.*;
import java.io.*;

public class wave {
  public static void main(final String[] args) {
    final Scanner in = new Scanner(System.in);
    int n = in.nextInt();

    while (n-- > 0) {
      final int a = in.nextInt(), b = in.nextInt();

      String ans = "one";

      if (a < b) {
        ans = "two";
      }

      System.out.println(ans);
    }

    in.close();
  }
}
