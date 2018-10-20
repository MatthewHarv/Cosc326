/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */
import java.awt.*;
import javax.swing.*;
import java.util.*;

public class tree extends JFrame {
 public static int axis;



 public static double order = 0;
 public static double factor = 0;
 public static ArrayList < Integer > targetCoordinatesHori = new ArrayList < Integer > ();
 public static ArrayList < Integer > targetCoordinatesVert = new ArrayList < Integer > ();
 public static ArrayList < Integer > printarrayCoordinates = new ArrayList < Integer > ();
 public static int length;
 public static boolean offscreen=false;


 public tree() {
  super("Trees");
  setSize(axis, axis);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setResizable(false);
  setLocationRelativeTo(null);

 }

 void drawLines(Graphics g) {
  Graphics2D canvas = (Graphics2D) g;
  for (int i = 0; i < printarrayCoordinates.size(); i = i + 4) {
   canvas.drawLine(printarrayCoordinates.get(i), printarrayCoordinates.get(i + 1), printarrayCoordinates.get(i + 2), printarrayCoordinates.get(i + 3));

  }

 }

 public void paint(Graphics g) {
  super.paint(g);
  drawLines(g);

 }

 public static void main(String[] args) {

   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
double width = screenSize.getWidth();
double height = screenSize.getHeight();
if(width<height){
     double axisCalc = width*0.8;
     axis= (int) axisCalc;
} else{
      double axisCalc = height*0.8;
     axis= (int) axisCalc;
}
Scanner sc = new Scanner(System.in);
while(true){
  System.out.print("Enter order>0 : ");
order = sc.nextInt();
if(order>0){
    break;
}
}

while(true){
  
  System.out.print("Enter factor >0 but <=1: ");
  factor = sc.nextDouble();
  if(factor>0&&factor<=1){
      break;
  }
}

     length=axis/3;


  calculateTree(order, factor);




  SwingUtilities.invokeLater(new Runnable() {
   public void run() {
    new tree().setVisible(true);
   }
  });
 }

 public static void coordinateCalculator(int x, int y, boolean horiz) {
  if (horiz) {
   int yvalue = y;
   int plusxvalue = x + length / 2;
   int minusxvalue = x - length / 2;
   printarrayCoordinates.addAll(Arrays.asList(plusxvalue, yvalue, minusxvalue, yvalue));
   targetCoordinatesHori.addAll(Arrays.asList(plusxvalue, yvalue, minusxvalue, yvalue));

  } else {
   int xvalue = x;
   int plusyvalue = y + length / 2;
   int minusyvalue = y - length / 2;
   printarrayCoordinates.addAll(Arrays.asList(xvalue, plusyvalue, x, minusyvalue));
   targetCoordinatesVert.addAll(Arrays.asList(xvalue, plusyvalue, x, minusyvalue));


  }
 }





 public static void calculateTree(double order, double factor) {
  if (order > 0) {
   printarrayCoordinates.add(axis / 2 - (length / 2));
   printarrayCoordinates.add(axis / 2);
   printarrayCoordinates.add(axis / 2 + (length / 2));
   printarrayCoordinates.add(axis / 2);

  }
  if (order > 1) {
   targetCoordinatesHori.add(axis / 2 - (length / 2));
   targetCoordinatesHori.add(axis / 2);
   targetCoordinatesHori.add(axis / 2 + (length / 2));
   targetCoordinatesHori.add(axis / 2);


   for (int o = 2; o < order + 1; o++) {
       if(offscreen){
           break;
       }
    if (o % 2 != 0) {

     double factorcalc = length * factor;
     length = (int) factorcalc;
     if (length == 0) {
      break;
     }
     for (int j = 0; j < targetCoordinatesVert.size(); j += 2) {
      //   System.out.println("here is "+targetCoordinatesVert.get(j));

      coordinateCalculator(targetCoordinatesVert.get(j), targetCoordinatesVert.get(j + 1), true);
      if(targetCoordinatesVert.get(j)<0||targetCoordinatesVert.get(j)>axis){
         offscreen=true;
      }

     }
     targetCoordinatesVert = new ArrayList<Integer>();
    } else {
     double factorcalc = length * factor;
     length = (int) factorcalc;
     if (length == 0) {
      break;
     }
     for (int j = 0; j < targetCoordinatesHori.size(); j += 2) {
      coordinateCalculator(targetCoordinatesHori.get(j), targetCoordinatesHori.get(j + 1), false);
     }
     targetCoordinatesHori = new ArrayList<Integer>();
    }
   }
  }
  System.out.println("Finish");
 }
}