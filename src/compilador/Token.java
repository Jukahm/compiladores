package compilador;

public class Token {
    
    public final int tag;   
    
    //Construtor
    public Token(int t){
        tag = t;
    }
    
    public String toString(){
        return "" + tag;
    }
}
