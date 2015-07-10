package compilador;

public class Word extends Token {
    
    private String lexema = "";
    private int tipo;
    
    //Palavras-chaves
    
    public static final Word declare = new Word("declare", Tag.DCL);
    public static final Word start = new Word("start", Tag.STRT);
    public static final Word end = new Word("end", Tag.END);
    public static final Word tipoInt = new Word("int", Tag.INT);
    public static final Word tipoString = new Word("string", Tag.STRG);
    public static final Word condIf = new Word("if", Tag.IF);
    public static final Word then = new Word("then", Tag.THEN);
    public static final Word condElse = new Word("else", Tag.ELSE);
    public static final Word loopDo = new Word("do", Tag.DO);
    public static final Word loopWhile = new Word("while", Tag.WHL);
    public static final Word read = new Word("read", Tag.READ);
    public static final Word write = new Word("write", Tag.WRT);
    public static final Word or = new Word("or", Tag.OR);
    public static final Word and = new Word("and", Tag.AND);
    public static final Word equal = new Word("==", Tag.EQ);
    public static final Word greaterThan = new Word(">", Tag.GT);
    public static final Word greaterEqual = new Word(">=", Tag.GE);
    public static final Word lessThan = new Word("<", Tag.LT);
    public static final Word lessEqual = new Word("<=", Tag.LE);
    public static final Word notEqual = new Word("<>", Tag.NE);
    public static final Word sum = new Word("+", Tag.SUM);
    public static final Word minus = new Word("-", Tag.MIN);
    public static final Word mult = new Word("*", Tag.MUL);
    public static final Word division = new Word("/", Tag.DIV);
    public static final Word pontoVirgula = new Word(";", Tag.PVG);
    public static final Word virgula = new Word(",", Tag.VG);
    public static final Word doisPontos = new Word(":", Tag.DPTS);
    public static final Word abreParenteses = new Word("(", Tag.AP);
    public static final Word fechaParenteses = new Word(")", Tag.FP);
    public static final Word atribuicao = new Word("=", Tag.ATRB);
    public static final Word negacao = new Word("!", Tag.NEG);
  // public static final Word aspas = new Word("\"", Tag.ASP);
    
    
    //Identificadores de Tipos de Id
    public static final int tipoStr = 2;
    public static final int tipoInteiro = 1;
    public static final int tipoBooleano = 3;
    //Não definido = 0;
            
    //Construtor
    public Word (String s, int tag){
        super(s,tag);
        lexema = s;
    }
    
    public String getLexema(){
        return lexema;
    }
    
    public int getTipo(){
        return tipo;
    }
    
    public void setTipo(int tipo){
        this.tipo = tipo;
    }    
   
}
