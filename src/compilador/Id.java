package compilador;

/**
 * @author Thais
 */
public class Id {
    String identificador;
    int tipo;
    
    
    public Id(Token tk){
        this.identificador = tk.getValor();
    }
    
    public String getValue(){
        return this.identificador;
    }
    
    public int getTipo(){
        return this.tipo;
    }
}
