import java.util.*;
import java.io.*;

public class paper {
  public static void main(final String[] args) {
    final Scanner in = new Scanner(System.in);
    int n = in.nextInt();

    while (n-- > 0) {
      final char[] str = in.next().toCharArray();

      final HashMap<Character, Integer> mp = new HashMap<>();

      for (final char c : str) {
        mp.put(c, mp.getOrDefault(c, 0) + 1);
      }

      for (final char c : str) {
        if (mp.get(c) == 1) {
          mp.put(c, -1);
        }
      }

      String ans = "";
      for (final char c : str) {
        if (mp.get(c) > 1) {
          ans += c;
          mp.put(c, -1);
        }
      }

      if (ans.length() == 0) {
        ans = "NONE!";
      }

      System.out.println(ans);
    }

    in.close();
  }
}
