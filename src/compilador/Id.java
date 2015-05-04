/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Thais
 */
public class Id {
    String identificador;
    
    
    public Id(Token tk){
        this.identificador = tk.getValor();
    }
    
    public String getValue(){
        return this.identificador;
    }
}
