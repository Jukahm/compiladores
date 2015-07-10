package compilador;

import static compilador.Compilador.lexer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Thais
 */
public class Parser {

    private Token tok;
    private Lexer lexer;
    private final String arquivo;

    public Parser(String arquivo) throws IOException {
        this.arquivo = arquivo;
        lexer = new Lexer(this.arquivo);
        execute();
    }

    public void execute() {
        try {
            tok = getToken();
            S();
            if (tok != null && lexer.getEOF() != -1) {
                System.out.println("Erro Sintático. Código após encerramento do programa");
            }
        } catch (Exception e) {
            error("execute");
        }
    }

    public Token getToken() throws IOException {
        try {
            if (lexer.getEOF() != -1) {
                tok = lexer.scan();

                System.out.println('<' + tok.getValor() + ',' + tok.getTag() + '>');
                return tok;
            } else {
                lexer.printTabSimbolos();
                System.exit(0);
            }
        } catch (Exception e) {
            error("Catch getToken");
        }
        return null;
    }

    void eat(int t) throws IOException {
        if (tok.getTag() == t) {
            System.out.println("EATING: " + tok.getValor());
            getToken();
        } else {
            System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Caracter esperado: " + t);
            System.exit(0);
        }
    }

    void error(String str) {
        System.out.print("ERROR!!!    ");
        System.out.println(str);
    }

    int S() throws IOException {
        //::= [ declare decl-list] start stmt-list end
        int tipo;
        switch (tok.getTag()) {
            case Tag.DCL:
                eat(Tag.DCL);
                tipo = declList();
                eat(Tag.STRT);
                tipo = Semantico.comparaTipos(tipo, stmtList());
                eat(Tag.END);
                break;
            case Tag.STRT:
                eat(Tag.STRT);
                tipo = stmtList();
                eat(Tag.END);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Caracter esperado: Declare ou Start.");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int declList() throws IOException {
        //::= decl ";" { decl ";"}
        int tipo;
        switch (tok.getTag()) {
            case Tag.ID:
                tipo = decl();
                eat(Tag.PVG);
                while (tok.getTag() == Tag.ID) {
                    tipo = Semantico.comparaTipos(tipo, declList());
                }
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador.");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int decl() throws IOException {
        //decl ::= ident-list “:” type
        int tipo;
        switch (tok.getTag()) {
            case Tag.ID:
                ArrayList<Word> lista = identList();
                eat(Tag.DPTS);
                tipo = type();
                Semantico.setTipos(lista,tipo);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador.");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private ArrayList<Word> identList() throws IOException {
        //ident-list ::= identifier {"," identifier}
        ArrayList lista = new ArrayList<Word>();
        switch (tok.getTag()) {
            case Tag.ID:
                lista.add(tok);
                eat(Tag.ID);
                while (tok.getTag() == Tag.VG) {
                    eat(Tag.VG);
                    lista.add(tok);
                    eat(Tag.ID);
                }
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador.");
                System.exit(0);
            //return lista;   
        }
        return lista;
    }

    private int type() throws IOException {
        //type ::= int | string
        int tipo;
        switch (tok.getTag()) {
            case Tag.INT:
                eat(Tag.INT);
                tipo = Word.tipoInteiro;
                break;
            case Tag.STRG:
                eat(Tag.STRG);
                tipo = Word.tipoStr;
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado tipo.");
                tipo = 0;
                System.exit(0);
            //token = new Token("Erro",Tag.ERRO);
        }

        return tipo;
    }

    private int stmtList() throws IOException {
        //::= stmt ";" { stmt ";"}
        int tipo;
        switch (tok.getTag()) {
            case (Tag.ID):
            case (Tag.IF):
            case (Tag.DO):
            case (Tag.READ):
            case (Tag.WRT):
                tipo = stmt();
                if(tipo == 0){
                    System.out.println("Erro Semantico. Na linha: " + Lexer.linha + ". Verifique os tipos utilizados!");
                }
                eat(Tag.PVG);
                while ((tok.getTag() == Tag.ID) || (tok.getTag() == Tag.IF) || (tok.getTag() == Tag.DO) || (tok.getTag() == Tag.READ) || (tok.getTag() == Tag.WRT)) {
                    tipo = Semantico.comparaTipos(tipo, stmtList());
                }
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador, if,do,read,write ou read.");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int stmt() throws IOException {
        //::= assign-stmt | if-stmt | do-stmt | read-stmt | write-stmt
        int tipo;
        switch (tok.getTag()) {
            case (Tag.ID):
                tipo = assignStmt();
                break;
            case (Tag.IF):
                tipo = ifStmt();
                break;
            case (Tag.DO):
                tipo = doStmt();
                break;
            case (Tag.READ):
                tipo = readStmt();
                break;
            case (Tag.WRT):
                tipo = writeStmt();
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador, if,do,read,write ou read.");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int assignStmt() throws IOException {
        //::= identifier "=" simple_expr
        int tipo;
        switch (tok.getTag()) {
            case (Tag.ID):
                Word atr = (Word) tok;
                tipo = atr.getTipo();
                eat(Tag.ID);
                eat(Tag.ATRB);
                tipo = Semantico.comparaTipos(tipo,simpleExpr());
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador.");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int simpleExpr() throws IOException {
        // simple-expr ::= term simple-exprS
        int tipo;
        switch (tok.getTag()) {
            case (Tag.NUM):
            case (Tag.ID):
            case (Tag.AP):
            case (Tag.NEG):
            case (Tag.MIN):
            case (Tag.LIT):
                tipo = term();
                tipo =  Semantico.comparaTipos(tipo, simpleExprS());
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ".");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int simpleExprS() throws IOException {
        //simple-exprS ::= addop term simple-exprS | λ
        int tipo;
        switch (tok.getTag()) {
            case (Tag.SUM):
            case (Tag.OR):
            case (Tag.MIN):
                tipo = addop();
                tipo = Semantico.comparaTipos(tipo, term());
                //addop();
                //tipo = term();
                tipo = Semantico.comparaTipos(tipo, simpleExprS());                
                break;
            default:
                tipo = 4;
                break;
        }
        return tipo;
    }

    private int addop() throws IOException {
        int tipo;
        switch (tok.getTag()) {
            case (Tag.SUM):
                eat(Tag.SUM);
                tipo = 1;
                break;
            case (Tag.MIN):
                eat(Tag.MIN);
                tipo = 1;
                break;
            case (Tag.OR):
                eat(Tag.OR);
                tipo = 3;
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado caracteres: + ou - ou OR");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int ifStmt() throws IOException {
        //if condition then stmt-list end | if condition then stmt-list else stmt-list end
        int tipo;
        switch (tok.getTag()) {
            case Tag.IF:
                eat(Tag.IF);
                tipo = condition() != 3?0:4;
                eat(Tag.THEN);
                tipo = Semantico.comparaTipos(tipo, stmtList());
                tipo = Semantico.comparaTipos(tipo, ifStmt2());
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'if'");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int ifStmt2() throws IOException {
        int tipo;
        switch (tok.getTag()) {
            case Tag.ELSE:
                eat(Tag.ELSE);
                tipo = stmtList();
                eat(Tag.END);
                break;
            case Tag.END:
                tipo = 4;
                eat(Tag.END);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'if'");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int doStmt() throws IOException {
        //::= do stmt-list stmt-suffix
        int tipo;
        switch (tok.getTag()) {
            case Tag.DO:
                eat(Tag.DO);
                stmtList();
                tipo = ((stmtList() != 0) && (stmtSufix() == 3))?3:0;
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'do'");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int readStmt() throws IOException {
        //::= read "(" identifier ")"
        int tipo;
        switch (tok.getTag()) {
            case Tag.READ:
                eat(Tag.READ);
                eat(Tag.AP);
                Word aux = (Word) tok;
                tipo = aux.getTipo();
                if(tipo == 0){
                    System.out.println("Veriável não existe! Linha: " + lexer.getLinha());
                }
                eat(Tag.ID);
                eat(Tag.FP);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'read'");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int writeStmt() throws IOException {
        //::= write "(" writable ")"
        int tipo;
        switch (tok.getTag()) {
            case Tag.WRT:
                eat(Tag.WRT);
                eat(Tag.AP);
                tipo = writable();
                eat(Tag.FP);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'write'");
                tipo = 0;
                System.exit(0);
        }
        return 0;
    }

    private int condition() throws IOException {
        return expression();
    }

    private int expression() throws IOException {
        // simple-expr | simple-expr relop simple-expr
        int tipo;
        switch (tok.getTag()) {
            case (Tag.NUM):
            case (Tag.ID):
            case (Tag.AP):
            case (Tag.NEG):
            case (Tag.MIN):
                int tipo1 = simpleExpr();
                int tipo2 = expression2();
                tipo = Semantico.comparaTipos(tipo1, tipo2);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'read'");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int expression2() throws IOException {
        int tipo;
        switch (tok.getTag()) {
            case (Tag.EQ):
            case (Tag.LT):
            case (Tag.LE):
            case (Tag.GT):
            case (Tag.GE):
            case (Tag.NE):
                relop();
                tipo = simpleExpr();
                break;
            default:
                tipo = 4;
                break;
        }
        return tipo;
    }

    private int stmtSufix() throws IOException {
        //::= while condition
        int tipo;
        switch (tok.getTag()) {
            case Tag.WHL:
                eat(Tag.WHL);
                if (condition() == 3) {
                    tipo = 3;
                } else {
                    tipo = 0;
                }
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'while'");
                tipo = 0;
                System.exit(0);
        }
        System.out.println("Tipo while = "+tipo);
        return tipo;
    }

    private int writable() throws IOException {
        return simpleExpr();
    }

    private int relop() throws IOException {
        int tipo = 4;
        switch (tok.getTag()) {
            case (Tag.EQ):
                eat(Tag.EQ);
                break;
            case (Tag.GT):
                eat(Tag.GT);
                break;
            case (Tag.GE):
                eat(Tag.GE);
                break;
            case (Tag.LT):
                eat(Tag.LT);
                break;
            case (Tag.LE):
                eat(Tag.LE);
                break;
            case (Tag.NE):
                eat(Tag.NE);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: operadores (comparação)");
                System.exit(0);
        }
        return tipo;
    }

    private int term() throws IOException {
        //term ::= factor-a termT
        int tipo;
        switch (tok.getTag()) {
            case (Tag.NUM):
            case (Tag.ID):
            case (Tag.AP):
            case (Tag.NEG):
            case (Tag.MIN):
            case (Tag.LIT):
                int tipo1 = factorA();
                int tipo2 = termT();
                tipo = Semantico.comparaTipos(tipo1, tipo2);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'int','id','(','-' ou '!'");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int factorA() throws IOException {
        //::= factor | ! factor | "-" factor
        int tipo;
        switch (tok.getTag()) {
            case Tag.ID:
            case Tag.NUM:
            case Tag.LIT:
            case Tag.AP:
                tipo = factor();
                break;
            case Tag.NEG:
                eat(Tag.NEG);
                tipo = factor();
                break;
            case Tag.MIN:
                eat(Tag.MIN);
                tipo = factor();
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'id','!' ou '-'");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }

    private int termT() throws IOException {
        //termT ::== mulop factor-a termT | λ
        int tipo;
        switch (tok.getTag()) {
            case Tag.MUL:
            case Tag.DIV:
            case Tag.AND:
                mulop();
                int tipo1 = factorA();
                int tipo2 = termT();
                tipo = Semantico.comparaTipos(tipo1, tipo2);
                break;
            default:
                tipo = 4;
                break;
        }
        return tipo;
    }

    private void mulop() throws IOException {
        switch (tok.getTag()) {
            case (Tag.MUL):
                eat(Tag.MUL);
                break;
            case (Tag.DIV):
                eat(Tag.DIV);
                break;
            case (Tag.AND):
                eat(Tag.AND);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: operadores.");
                System.exit(0);
        }
    }

    private int factor() throws IOException {
        //::= identifier | constant | "(" expression ")"
        int tipo;
        switch (tok.getTag()) {
            case (Tag.ID):
                Word wValue = (Word) tok;
                tipo = wValue.getTipo();
                eat(Tag.ID);
                break;
            case (Tag.AP):
                eat(Tag.AP);
                tipo = expression();
                eat(Tag.FP);
                break;
            case (Tag.NUM):
                tipo = Word.tipoInteiro;
                eat(Tag.NUM);
                break;
            case (Tag.LIT):
                tipo = Word.tipoStr;
                eat(Tag.LIT);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: id, num, literal ou '('.");
                tipo = 0;
                System.exit(0);
        }
        return tipo;
    }
}
