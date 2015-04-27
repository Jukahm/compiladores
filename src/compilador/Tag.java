package compilador;

public class Tag {
    //Palavras reservadas
    public final static int DCL = 1;
    public final static int STRT = 2;
    public final static int END = 3;
    public final static int INT = 4;
    public final static int STRG = 5;
    public final static int IF = 6;
    public final static int THEN = 7;
    public final static int ELSE = 8;
    public final static int DO = 9;
    public final static int WHL = 10;
    public final static int READ = 11;
    public final static int WRT = 12;
    public final static int OR = 13;
    public final static int AND = 14;
    
    //Operadores e Pontuação
    public final static int EQ = 20;
    public final static int GT = 21;
    public final static int GE = 22;
    public final static int LT = 23;
    public final static int LE = 24;
    public final static int NE = 25;
    public final static int SUM = 26;
    public final static int MIN = 27;
    public final static int MUL = 28;
    public final static int DIV = 29;
    public final static int PVG = 30;
    public final static int VG = 31;
    public final static int DPTS = 32;
    public final static int AP = 33;
    public final static int FP = 34;
    public final static int ATRB = 35;
    public final static int AASP = 36;
    public final static int FASP = 37;
    
    //Outros tokens
    public final static int NUM = 40;
    public final static int ID = 41;
}
