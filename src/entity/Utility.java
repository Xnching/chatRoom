package entity;

import java.util.Scanner;

public class Utility {
    public static Scanner scanner = new Scanner(System.in);
    /*
     * 从控制台读取长度字符串
     * @return
     */
    public static String readString(){
        String content = scanner.nextLine();//读取首行
        return content;
    }
}
