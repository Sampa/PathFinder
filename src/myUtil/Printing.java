package myUtil;

import javax.swing.*;

import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

/**
 * Created by Sampa on 2013-12-23.
 */
public class Printing{

    public static void print(String s){
        System.out.print(s);
    }
    public static void print(int i){
        System.out.print(""+i);
    }
    public static void println(int i){
        System.out.print(""+i);
    }
    public static void println(String s){
        System.out.println(s);
    }
    public static void println(boolean b){
        System.out.println("" + b);
    }
    public static void print(boolean b) {
        System.out.println("" + b);
    }
 }
