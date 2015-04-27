package compilador;

public class Num extends Token {
    
    public final String valor;
    
    public Num(String valor){
        super(valor,Tag.NUM);
        this.valor = valor;
    }

    
}
