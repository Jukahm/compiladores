package compilador;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Compilador {

    public static void main(String[] args) throws FileNotFoundException, IOException {
       Lexer lexer = new Lexer("teste1"); 
       Word t;
       t = (Word)lexer.scan();
       System.out.println('<'+t.getLexema()+','+t+'>');
        
    }
    
}


