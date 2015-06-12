package compilador;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Compilador {
public static Lexer lexer;

    public static void main(String[] args) throws FileNotFoundException, IOException {
       /*Lexer lexer = new Lexer(args[0]); 
       Token t;
       t = lexer.scan();
       while (lexer.getEOF() != -1){
           if (t != null){
           System.out.println('<'+t.getValor()+','+t.getTag()+'>');
           }
           t = lexer.scan();
       }
       lexer.printTabSimbolos();
       */
        
        Parser p = new Parser("teste3");
        
    }
  
}


