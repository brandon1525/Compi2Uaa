package Interfaz;

import RecursosCompi.DeclarationType;

public class Item {
    
    String type;
    int val_i;
    float val_f;
    boolean val_b;
    
    public Item(){}
    
    public Item(String t){
        val_i = 0;
        val_f = 0;
        val_b = false;
        type = t;
    }
    
    public Item(String t, int n){
        type = t;
        val_i = n;
    }
    
    public Item(String t, float n){
        type = t;
        val_f = n;
    }
    
    public Item(String t, boolean n){
        type = t;
        val_b = n;
    }
    
    public String toString(){
        switch(type){
            case "int":
                return "tipo: int - valor: " + val_i;
            case "float":
                return "tipo: float - valor: " + val_f;
            case "bool":
                return "tipo: bool - valor: " + val_b;
            default:
                return "nada";
        }
    }
    
}
