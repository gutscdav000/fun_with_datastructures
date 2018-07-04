import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {



    public static void main(String args[]) {

        BufferedWriter buffer = null;


       /* try {
            buffer = new BufferedWriter(new FileWriter("back_wordlist1.txt"));

            for (String passwd: Permutations.permute("06basebrad")) {

                if(passwd.contains("brad") && passwd.contains("06")) {
                   buffer.write(passwd);
                   buffer.newLine();
                }
            }

            buffer.close();

        } catch (IOException e) {
            System.out.println("Error code:\t" + e.toString());
        }

        buffer = null;

        try {
            buffer = new BufferedWriter(new FileWriter("back_wordlist2.txt"));

            for (String passwd: Permutations.permute("06basebrAD")) {

                if(passwd.contains("brAD") && passwd.contains("06")) {
                    buffer.write(passwd);
                    buffer.newLine();
                }
            }

            buffer.close();

        } catch (IOException e) {
            System.out.println("Error code:\t" + e.toString());
        }
*/
        buffer = null;

        char[] specialChars = {'!', '@', '$', '#', '%', '&', '*', '?', };
        Integer fileNum = 3;


        for (int i = 0; i < specialChars.length; i++) {


            try {
                buffer = new BufferedWriter(new FileWriter("back_wordlist" + fileNum.toString() + ".txt"));

                System.out.println("back_wordlist" + fileNum.toString() + ".txt");
                fileNum++;
                String perm = "06basebrAD" + specialChars[i];
                System.out.println(perm);

                for (String passwd : Permutations.permute(perm)) {

                    if (passwd.contains("brAD") && passwd.contains("06")) {
                        buffer.write(passwd);
                        buffer.newLine();
                    }
                }

                buffer.close();

            } catch (IOException e) {
                System.out.println("Error code:\t" + e.toString());
            }
        }
    }

}
