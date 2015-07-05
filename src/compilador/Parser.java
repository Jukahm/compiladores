package compilador;

import static compilador.Compilador.lexer;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    void S() throws IOException {
        //::= [ declare decl-list] start stmt-list end
        switch (tok.getTag()) {
            case Tag.DCL:
                eat(Tag.DCL);
                declList();
                eat(Tag.STRT);
                stmtList();
                eat(Tag.END);
                break;
            case Tag.STRT:
                eat(Tag.STRT);
                stmtList();
                eat(Tag.END);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Caracter esperado: Declare ou Start.");
                System.exit(0);
        }
    }

    private void declList() throws IOException {
        //::= decl ";" { decl ";"}
        switch (tok.getTag()) {
            case Tag.ID:
                decl();
                eat(Tag.PVG);
                while (tok.getTag() == Tag.ID) {
                    declList();
                }
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador.");
                System.exit(0);
        }
    }

    private void decl() throws IOException {
        //decl ::= ident-list “:” type
        switch (tok.getTag()) {
            case Tag.ID:
                identList();
                eat(Tag.DPTS);
                type();
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador.");
                System.exit(0);
        }
    }

    private void identList() throws IOException {
        //ident-list ::= identifier {"," identifier}
        switch (tok.getTag()) {
            case Tag.ID:
                eat(Tag.ID);
                while (tok.getTag() == Tag.VG) {
                    eat(Tag.VG);
                    eat(Tag.ID);
                }
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador.");
                System.exit(0);
        }
    }

    private void type() throws IOException {
        //type ::= int | string
        switch (tok.getTag()) {
            case Tag.INT:
                eat(Tag.INT);
                break;
            case Tag.STRG:
                eat(Tag.STRG);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado tipo.");
                System.exit(0);
        }
    }

    private void stmtList() throws IOException {
        //::= stmt ";" { stmt ";"}
        switch (tok.getTag()) {
            case (Tag.ID):
            case (Tag.IF):
            case (Tag.DO):
            case (Tag.READ):
            case (Tag.WRT):
                stmt();
                eat(Tag.PVG);
                while ((tok.getTag() == Tag.ID) || (tok.getTag() == Tag.IF) || (tok.getTag() == Tag.DO) || (tok.getTag() == Tag.READ) || (tok.getTag() == Tag.WRT)) {
                    stmtList();
                }
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador, if,do,read,write ou read.");
                System.exit(0);
        }
    }

    private void stmt() throws IOException {
        //::= assign-stmt | if-stmt | do-stmt | read-stmt | write-stmt

        switch (tok.getTag()) {
            case (Tag.ID):
                assignStmt();
                break;
            case (Tag.IF):
                ifStmt();
                break;
            case (Tag.DO):
                doStmt();
                break;
            case (Tag.READ):
                readStmt();
                break;
            case (Tag.WRT):
                writeStmt();
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador, if,do,read,write ou read.");
                System.exit(0);
        }

    }

    private void assignStmt() throws IOException {
        //::= identifier "=" simple_expr
        switch (tok.getTag()) {
            case (Tag.ID):
                eat(Tag.ID);
                eat(Tag.ATRB);
                simpleExpr();
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador.");
                System.exit(0);
        }
    }

    private void simpleExpr() throws IOException {
        // simple-expr ::= term simple-exprS
        switch (tok.getTag()) {
            case (Tag.NUM):
            case (Tag.ID):
            case (Tag.AP):
            case (Tag.NEG):
            case (Tag.MIN):
            case (Tag.LIT):
                term();
                simpleExprS();
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ".");
                System.exit(0);
        }
    }

    private void simpleExprS() throws IOException {
        //simple-exprS ::= addop term simple-exprS | λ
        switch (tok.getTag()) {
            case (Tag.SUM):
            case (Tag.OR):
            case (Tag.MIN):
                addop();
                term();
                simpleExprS();
                break;
            default:
                break;
        }
    }

    private void addop() throws IOException {
        switch (tok.getTag()) {
            case (Tag.SUM):
                eat(Tag.SUM);
                break;
            case (Tag.MIN):
                eat(Tag.MIN);
                break;
            case (Tag.OR):
                eat(Tag.OR);
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado caracteres: + ou - ou OR");
                System.exit(0);

        }
    }

    private void ifStmt() throws IOException {
        //if condition then stmt-list end | if condition then stmt-list else stmt-list end
        switch (tok.getTag()) {
            case Tag.IF:
                eat(Tag.IF);
                condition();
                eat(Tag.THEN);
                stmtList();
                ifStmt2();
                break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'if'");
                System.exit(0);
        }
    }

    private void ifStmt2() throws IOException {
        switch (tok.getTag()) {
            case Tag.ELSE:
                eat(Tag.ELSE);
                stmtList();
                eat(Tag.END);
                break;
            case Tag.END: eat(Tag.END);break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'if'");
                System.exit(0);
        }
    }

    private void doStmt() throws IOException {
        //::= do stmt-list stmt-suffix
        switch(tok.getTag()) {
            case Tag.DO:
            eat(Tag.DO);
            stmtList();
            stmtSufix();break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'do'");
                System.exit(0);
        }
    }

    private void readStmt() throws IOException {
        //::= read "(" identifier ")"
        switch(tok.getTag()){
            case Tag.READ:
            eat(Tag.READ);
            eat(Tag.AP);
            eat(Tag.ID);
            eat(Tag.FP);break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'read'");
                System.exit(0);
        } 
    }

    private void writeStmt() throws IOException {
        //::= write "(" writable ")"
        switch(tok.getTag()){
            case Tag.WRT:
            eat(Tag.WRT);
            eat(Tag.AP);
            writable();
            eat(Tag.FP);break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'write'");
                System.exit(0);
        } 
    }

    private void condition() throws IOException {
        expression();
    }

    private void expression() throws IOException {
        // simple-expr | simple-expr relop simple-expr
        switch(tok.getTag()){
            case (Tag.NUM):
            case (Tag.ID):
            case (Tag.AP):
            case (Tag.NEG):
            case (Tag.MIN): simpleExpr();expression2();break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'read'");
                System.exit(0);
        }
    }
    private void expression2() throws IOException {
        switch(tok.getTag()){
            case (Tag.EQ):
            case (Tag.LT):
            case (Tag.LE):
            case (Tag.GT):
            case (Tag.GE):
            case (Tag.NE):relop();simpleExpr();break;
            default:
                break;
        }
    }
    
    private void stmtSufix() throws IOException {
        //::= while condition
        switch(tok.getTag()){
            case Tag.WHL: eat(Tag.WHL);condition();break;
            default: 
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'while'");
                System.exit(0);
        }
    }

    private void writable() throws IOException {
        simpleExpr();
    }

    private void relop() throws IOException {
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
    }

    private void term() throws IOException {
        //term ::= factor-a termT
        switch(tok.getTag()){
           case (Tag.NUM):
            case (Tag.ID):
            case (Tag.AP):
            case (Tag.NEG):
            case (Tag.MIN): 
            case (Tag.LIT):factorA();termT();break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'int','id','(','-' ou '!'");
                System.exit(0); 
        }
    }

    private void factorA() throws IOException {
        //::= factor | ! factor | "-" factor
        switch(tok.getTag()){
            case Tag.ID:    
            case Tag.NUM:
            case Tag.LIT: 
            case Tag.AP: factor();break;
            case Tag.NEG:   eat(Tag.NEG);factor();break;
            case Tag.MIN:   eat(Tag.MIN);factor();break;
            default:  
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: 'id','!' ou '-'");
                System.exit(0);    
        }
    }
        
    private void termT() throws IOException {
        //termT ::== mulop factor-a termT | λ
        switch(tok.getTag()){
            case Tag.MUL:
            case Tag.DIV:
            case Tag.AND: mulop();factorA();termT();break;
            default: break;      
        }
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

    private void factor() throws IOException {
        //::= identifier | constant | "(" expression ")"
        switch (tok.getTag()) {
            case (Tag.ID):
                eat(Tag.ID);
                break;
            case (Tag.AP):
                eat(Tag.AP);
                expression();
                eat(Tag.FP);
                break;
            case (Tag.NUM): eat(Tag.NUM);break;
            case (Tag.LIT): eat(Tag.LIT);break;
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado: id, num, literal ou '('.");
                System.exit(0);
        }
    }
}
