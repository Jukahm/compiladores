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
            System.out.println("erro lexer");
            return null;
        }
        
    }
        
    
    void advance() throws IOException{
        tok = getToken();
    }
    
    void eat (int t) throws IOException{
        if (tok.getTag() == t){
            System.out.println("eating: "+tok.getValor());
            advance();
        }
        else error("ERRO - EAT");
    }    
    void error(String str){
        System.out.println(str);
       
    }
   
    
    void S() throws IOException{
        switch (tok.getTag()){//token getTag retornar int
            case Tag.DCL: eat(Tag.DCL);
                declList(); eat(Tag.STRT);
                stmtList();
                break;
            
            default: error("Default S()");break;
            
        }
    }
  private void declList() throws IOException{
      decl(); eat(Tag.PVG);
      while(tok.getTag()==Tag.ID){
          declList();
      }
  }
  
  
   private void decl() throws IOException{
       //decl ::= ident-list “:” type
       identList(); eat(Tag.DPTS); type();
   }
    private void identList () throws IOException{
       //ident-list ::= identifier {"," identifier}
       eat(Tag.ID);
       while (tok.getTag()==Tag.VG){
           eat(Tag.VG); eat (Tag.ID);
       }
   }
   private void type() throws IOException{
       //type ::= int | string
       if (tok.getTag()==Tag.INT){
           eat(Tag.INT);
       }else if(tok.getTag()==Tag.STRG){
           eat(Tag.STRG);
       }
       else{
           error("Linha "+lexer.getLinha()+
                   " -----> Tipo de variavel indeterminado!");
       }
   }
   
   
    private void stmtList() throws IOException {
       stmt(); eat(Tag.PVG);
    }
    private void stmt(){
        
    }
}
