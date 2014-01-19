package myUtil;

import javax.swing.*;

import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

/**
 * Created by Sampa on 2013-12-23.
 */
public class Printing{
    /*Wrapper mehods*/
    public static Boolean[] confirmExit(){
        return confirmExit(false,null);
    }
    public static Boolean[] confirmExit(JFrame frame,Boolean doExitNotReturnValue){
        return confirmExit(doExitNotReturnValue,frame);
    }
    public  static Boolean[] confirmExit(Boolean doExitNotReturnValue){
       return  confirmExit(doExitNotReturnValue,null);
    }
    public static Boolean[] confirmExit(JFrame frame) {
        return confirmExit(false, frame);
    }
    public  static Boolean[] confirmExit(Boolean doExitNotReturnValue, JFrame dialogParentComponent){
        Boolean userSelectedToExit = true;
        Boolean userSelectedToSave = false;
        Boolean[] result = new Boolean[2];
        try{
            int response = JOptionPane.showConfirmDialog(dialogParentComponent, "Du har osparade ändringar,vill du spara dem?", "Välj ett alternativ", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (response == YES_OPTION) {
                userSelectedToSave = true;
            }
            if (response == CANCEL_OPTION) {
                userSelectedToExit = false;
                doExitNotReturnValue = false;
            }
        } catch (Exception error) {
            JOptionPane.showMessageDialog(dialogParentComponent, "Gick åt h-vete" + error.getCause());
            doExitNotReturnValue = false; //ingen exit om inget kunde sparas oavsett hur metoden anropades

        }
        if(doExitNotReturnValue &&  userSelectedToExit)
            System.exit(0);
        result[0] = userSelectedToExit;
        result[1] = userSelectedToSave;
        return result;
    }
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
