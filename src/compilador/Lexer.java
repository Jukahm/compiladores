package compilador;
import java.io.*;

public class Lexer {
    
    public static int linha = 1;    //Contador de linhas
    private char ch = ' ';          //Caractere lido do arquivo
    private FileReader arquivo;
    
    //Construtor
    public Lexer(String fileName) throws FileNotFoundException{
        try{
            arquivo = new FileReader (fileName);
        }
        catch(FileNotFoundException e){
            System.out.println("Arquivo não encontrado");
            throw e;
        }
        TabelaSimbolos.words.put(Word.declare.getLexema(), Word.declare);
        TabelaSimbolos.words.put(Word.start.getLexema(), Word.start);
        TabelaSimbolos.words.put(Word.end.getLexema(), Word.end);
        TabelaSimbolos.words.put(Word.tipoInt.getLexema(), Word.tipoInt);
        TabelaSimbolos.words.put(Word.tipoString.getLexema(), Word.tipoString);
        TabelaSimbolos.words.put(Word.condIf.getLexema(), Word.condIf);
        TabelaSimbolos.words.put(Word.then.getLexema(), Word.then);
        TabelaSimbolos.words.put(Word.condElse.getLexema(), Word.condElse);
        TabelaSimbolos.words.put(Word.loopDo.getLexema(), Word.loopDo);
        TabelaSimbolos.words.put(Word.loopWhile.getLexema(), Word.loopWhile);
        TabelaSimbolos.words.put(Word.read.getLexema(), Word.read);
        TabelaSimbolos.words.put(Word.write.getLexema(), Word.write);
        TabelaSimbolos.words.put(Word.or.getLexema(), Word.or);
        TabelaSimbolos.words.put(Word.and.getLexema(), Word.and);
    }  
    
    private void readch() throws IOException{
        ch = (char) arquivo.read();
    }
    
    private boolean readch(char c) throws IOException{
        readch();
        if (ch != c)
            return false;
        ch = ' ';
        return true;
    }
    
    public Token scan() throws IOException{
        //Desconsidera delimitadores na entrada
        for (; ;readch()){
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') 
                continue;
            else if (ch == '\n')
                linha++;
            else 
                break;       
        }
        //Identificadores
        if (Character.isLetter(ch)){
            StringBuffer sb = new StringBuffer();
            do{
                sb.append(ch);
                readch();
            }while(Character.isLetterOrDigit(ch));
            String s = sb.toString();
            Word w = (Word)TabelaSimbolos.words.get(s);
            if(w != null)
                return w; //palavra já existe na HashTable
            w = new Word (s, Tag.ID);
            TabelaSimbolos.words.put(s, w);
            return w;
        }
        
        //Virgula,PontoVirgula,DoisPontos
        if (ch == ','){
            Word w = new Word(",", Tag.VG);
            readch();
            return w;    
        } else if (ch == ';'){
            Word w = new Word(";", Tag.PVG);
            readch();
            return w;
        } else if (ch == ':'){
            Word w = new Word(";", Tag.DPTS);
            readch();
            return w;
        }
        //Caracteres não especificados
        Token t = new Token(ch);
        ch = ' ';
        return t;
    }
}
