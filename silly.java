import java.util.Arrays;
import java.util.Scanner;


//Jacob Steinebronn
//silly.java
public class silly {
	//this stores the number of characters in the alphabet, to avoid hard-coding
	//'magic numbers'
	static final int alpha_size=26;
	public static void main(String[] args) {
		Scanner s=new Scanner(System.in);
		//parse and loop over the number of cases
		int nq=s.nextInt();
		while(nq-->0) {
			
			//Parse the length of the string. This will be used a lot as iterator stops.
			int len=s.nextInt();
			
			//parse the string and put it into a char array,
			//so we don't have to call charAt() so many times
			char[] str=s.next().toCharArray();
			
			//If the string is of even length, there's no way to partition into equal freq[],
			//so just print 0 and continue to the next case
			if(len%2==0) {
				System.out.println(0);
				continue;
			}
			
			//ans will store the number of correct possible answers
			int ans=0;
			//this array will store the relative frequencies of letters on the left half to the 
			//right half of the string. 
			int[] freq=new int[alpha_size];
			
			//treat the first half of the string, INCLUDING the middle, as a positive freq
			//That is, let every letter on the first half increment the freq
			for(int i=0;i<=len/2;i++)freq[str[i]-'a']++;
			//treat the back half of the string, EXCLUDING the middle, as a negative freq
			//Doing this negates letters that appear on both sides of the string. 
			for(int i=len/2+1;i<len;i++)freq[str[i]-'a']--;			
			
			//An ideal string will have a freq[] with mostly zeros and a single positive or negative one.
			//This shows that the string is mostly balanced, and you just need to count the number of ways
			//to remove this one to completely balance the string.
			
			//the target character we will be looking to remove. 
			char tar=' ';
			boolean firstHalfBad=false;
			for(int i=0;i<alpha_size;i++) {
				if(freq[i]<0||freq[i]>1) {
					//if any freq is negative or greater than 1, no way deleting a character is gonna work
					firstHalfBad=true;
					break;
				}
				if(freq[i]==1) {
					//this means there is exactly 1 more of this letter than any other
					tar=(char)('a'+i);
				}
			}
			
			//If we did indeed find a valid target character, each deletion of that character on 
			//the first half of the string will result in a silly string
			if(!firstHalfBad) {
				for(int i=0;i<=len/2;i++)if(str[i]==tar)ans++;
			}
			
			//after considering deleting a character from the first half of the
			//string, now we consider delting from the second half. This means 
			//the middle character is now a negative, not a positive.
			freq[str[len/2]-'a']-=2;
			
			
			//Now, we loop through the back half of the string. An ideal string now has a freq[] with 
			//all zeros and a single -1. This means that deleting a negative character will balance
			//the string halves and make them silly.
			tar=' ';
			boolean secondHalfBad=false;
			for(int i=0;i<alpha_size;i++) {
				if(freq[i]>0||freq[i]<-1) {
					//if any freq is positive or less than -1, no way deleting a character is gonna work
					secondHalfBad=true;
					break;
				}
				if(freq[i]==-1) {
					//this means there is exactly 1 more of this letter than any other
					tar=(char)('a'+i);
				}
			}
			
			//If we've successfully identified a target character, loop through the back half of the string
			//and count the number of times it shows up. This will be added to the answer before, to save casework.
			if(!secondHalfBad) {
				for(int i=len/2+1;i<len;i++)if(str[i]==tar)ans++;
			}
			
			//We did it! Victory lap!
			System.out.println(ans);
		}
		s.close();
	}
}
