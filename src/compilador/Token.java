package compilador;

public class Token {
    
    public final int tag;
    public final String valor;
    
    //Construtor
    public Token(String s, int t){
        tag = t;
        valor = s;
    }
    
    public String getValor(){
        return valor;
    }
    
    public int getTag(){
        return tag;
    }
}
