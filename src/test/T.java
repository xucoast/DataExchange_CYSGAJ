package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * test
 * Created by smc
 * date on 2016/3/15.
 * Email:sunmch@163.com
 */
public class T {
    private static final String LISTENER_FILE_DIR ="/files/ftp/";
    private static final String LISTENER_FILE_DIR2 ="/files/";
    public static void main(String[] args) {
        File file = new File(LISTENER_FILE_DIR2+"DSC04002.JPG");

        try {
            FileInputStream inputStream = new FileInputStream(file);
            File outFile = new File(LISTENER_FILE_DIR+"好的_2017-12-21.JPG");

            FileOutputStream outputStream = new FileOutputStream(outFile);
            int a = 0 ;
            while((a = inputStream.read()) != -1){
                outputStream.write(a);
                System.out.println("======sasdasd==========");
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
