/*  Matthew Harvey 6083573


 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */

import java.util.*;

public class threes {
  
    
    public static int xcounter=0;
    public static int zcounter=0;
    public static void main(String[]args){
        
        
        
        System.out.println("test");
         int xa = 0x4276a000;
         System.out.println(xa);
      System.out.println(String.format("%x", xa));

        System.exit(0);
        
int count=0;
double x=2;
        while(count<70){ 
            ArrayList<Integer> resultx=new ArrayList<Integer>(); 
            int solution=0;
            x++;
          for(double z=x-1;z>1;z--){
              double y=Math.sqrt((1+z*z*z*z)-(x*x));
              if(y%1==0){
            if(checkFactors(x,y,z)){
                count++;
                int intx = (int) x;
                int inty = (int) y;
                int intz = (int) z;
                   if(count>73){
                    break;
                }     
                   resultx.add(intx);
                   resultx.add(inty);
                   resultx.add(intz);
               // System.out.println((count)+" "+intx +" "+ inty +" "+intz);

            }
          }
        }
          printer(resultx,true);
        }
        System.out.println("");
        count=0;
        double z=2;
        while(count<70){
            ArrayList<Integer> resulty=new ArrayList<Integer>(); 
            z++;
            for(x=z+1;x<(Math.sqrt(z*z*z*z+1));x++){
                double y=Math.sqrt((1+z*z*z*z)-(x*x));
                if(y%1==0){
                 if(checkFactors(x,y,z)){
                count++;
                int intx = (int) x;
                int inty = (int) y;
                int intz = (int) z;
                   if(count>73){
                    break;
                }     
                System.out.println((count)+" "+intx +" "+ inty +" "+intz);

            }
            }
            }
            printer(resulty,false);
        }
    }
    
    public static void printer(ArrayList<Integer> p,boolean isX){
        
        if(isX && p.size()>0){
        int[] arrayOrder=new int[p.size()/3];
        int count=0;
         for(int i=0;i<p.size();i=i+3){;
            arrayOrder[count]=(p.get(i+2));
        count++;     
        }
         Arrays.sort(arrayOrder);
         
                                                     
         for(int i=0;i<arrayOrder.length;i++){
              for(int j=2;j<p.size();j+=3){
                 
                  if(arrayOrder[i]==p.get(j)){
                      
                      if(xcounter<70){
                      System.out.println(xcounter+1+" " + p.get(j-2)+" "+p.get(j-1)+" "+p.get(j));
                      xcounter++;
                  }
                  }

        }
         }
        
    }
        if(!isX&&p.size()>0){
            int[] arrayOrder=new int[p.size()/3];
        int count=0;
         for(int i=0;i<p.size();i=i+3){;
            arrayOrder[count]=(p.get(i));
        count++;     
        }
         Arrays.sort(arrayOrder);
         for(int i=0;i<arrayOrder.length;i++){
             System.out.println(arrayOrder[i]);
         }
                                                      
         for(int i=0;i<arrayOrder.length;i++){
              for(int j=0;j<p.size();j+=3){
                  if(arrayOrder[i]==p.get(j)){  
                      if(zcounter<70){
                      System.out.println(zcounter+1+" " + p.get(j)+" "+p.get(j+1)+" "+p.get(j+2));
                      zcounter++;
                  }
                  }

        }
         }
        }
    }

 
 public static boolean checkFactors(double x,double y,double z){
     if(!(z<x&&x<y)){
         return false;
     }
     if((x*x+y*y)!=(1+z*z*z*z)){
         return false;
     }
     Set factorlist=new HashSet();
     for(int i=2;i<x+1;i++){
         if(x%i==0){
             factorlist.add(i);
         }
     }
     for(int j=2;j<y+1;j++){
         if(y%j==0){
             if(factorlist.contains(j)){
                 return false;
             }
             factorlist.add(j);
         }
     }
     for(int k=2;k<z+1;k++){
         if(z%k==0){
            if(factorlist.contains(k)){
                 return false;
             }
         }
     }
     
     
   
     
return true;
 }
}

