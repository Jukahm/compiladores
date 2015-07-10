/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;
import java.util.*;
/**
 *
 * @author mariana
 */
public class TabelaSimbolos {
    public static HashMap<String,Word> words = new HashMap();
    
    public HashMap<String,Word> getTabela(){
        return this.words;
    }
}
