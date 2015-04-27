package compilador;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Compilador {

    public static void main(String[] args) throws FileNotFoundException, IOException {
       Lexer lexer = new Lexer("teste1"); 
       int EOF = 65535;
       Token t;
       t = lexer.scan();
       while (t.getTag() != EOF){
           System.out.println('<'+t.getValor()+','+t.getTag()+'>');
           t = lexer.scan();
       }
       
        
    }
    
}


