package compilador;

public class Word extends Token {
    
    private String lexema = "";
    
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
    
            
    //Construtor
    public Word (String s, int tag){
        super(tag);
        lexema = s;
    }
    
    public String getLexema(){
        return lexema;
    }
}
