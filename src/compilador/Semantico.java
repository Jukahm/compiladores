/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;

/**
 *
 * @author mariana
 */
public abstract class Semantico {

    public static void setTipo(Word tok, int tipo) {

        tok.setTipo(tipo);
    }

    public static void setTipos(ArrayList<Word> tokens, int tipo) {
        for (Word w : tokens) {
            setTipo(w, tipo);
        }
    }

    public static int comparaTipos(int tipo1,int tipo2){
    /*
    tipo = 0 -> Erro
    tipo = 4 -> indiferente
    */   
        int tipo;
        if(tipo1 == tipo2){
            tipo = tipo1;
        }else if(tipo1 == 4){
            tipo = tipo2;
        }else if(tipo2 == 4){
            tipo = tipo1;
        }else tipo = 0;
        
        return tipo;
    }
}
