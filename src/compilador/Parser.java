/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import static compilador.Compilador.lexer;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Thais
 */
public class Parser {

    private Token tok;
    private Lexer lexer;
    private final String arquivo;
    private Tag tag;
    
    public Parser(String arquivo) throws IOException  {
        this.arquivo = arquivo;
        lexer = new Lexer( this.arquivo );
        execute();
    }
    
    public void execute () throws IOException{
        tok = getToken();
        S();
    }
    
     public Token getToken () throws FileNotFoundException, IOException{
        tok =  lexer.scan();
        if (lexer.getEOF() != -1 && tok != null){
            System.out.println('<'+tok.getValor()+','+tok.getTag()+'>');
            return tok;            
        }else{
            return null;
        }
        
    }
        
    
    void advance() throws IOException{
        tok = getToken();
    }
    
    void eat (int t) throws IOException{
        if (tok.getTag() == t) advance();
        else error("ERRO - EAT");
    }
    
    void error(String str){
        System.out.println(str);
       
    }
   
    
    void S() throws IOException{
        switch (tok.getTag()){//token getTag retornar int
            case Tag.DCL: 
                eat(Tag.ID); eat(Tag.STRT);
                stmtList(); eat(Tag.END);
                break;
            
            default: error("S()");break;
            
        }
    }

    private void stmtList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
