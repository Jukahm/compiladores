package compilador;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Compilador {
public static Lexer lexer;

    public static void main(String[] args) {
   
        try{
            
        Parser p = new Parser("teste6");
        
        }
        catch (Exception e){
            System.out.println ("Erro compilador");
        }
    }
  
}


