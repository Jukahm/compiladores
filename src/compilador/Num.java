package compilador;

public class Num extends Token {
    
    public final String val;
    
    public Num(String valor){
        super(valor,Tag.NUM);
        this.val = valor;
    }

    
}
