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
                //tok  = new Word ("end", Tag.END);              
                return null;
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
        }
    }

    private void decl() throws IOException {
        //decl ::= ident-list “:” type
        switch (tok.getTag()) {
            case Tag.ID:
                identList();
                eat(Tag.DPTS);
                type();
            default:
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador.R");
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
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador.");
        }
    }

    private void stmtList() throws IOException {
        //::= stmt ";" { stmt ";"}
        switch (tok.getTag()) {
            case (Tag.ID):
            case (Tag.IF):
            case (Tag.DO):
            case (Tag.READ):
            case (Tag.WRT): stmt(); eat(Tag.PVG); 
                while((tok.getTag() == Tag.ID) || (tok.getTag() == Tag.IF) || (tok.getTag() == Tag.DO) || (tok.getTag() == Tag.READ) || (tok.getTag() == Tag.WRT)){
                    stmtList();
                }break;
            default: 
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador, if,do,read,write ou read.");

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
        }

    }

    private void assignStmt() throws IOException {
        //::= identifier "=" simple_expr
        switch(tok.getTag()) {
            case(Tag.ID):   eat(Tag.ID); eat(Tag.ATRB);simpleExpr();break;
            default: 
                System.out.println("Erro Sintático. Linha: " + Lexer.linha + ". Esperado identificador.");
        } 
    }

    private void simpleExpr() {
        // simple-expr ::= term simple-exprS
        try {
            term();
            simpleExprS();
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + "\n -----> Expressão incorreta!");
        }

    }

    private void simpleExprS() throws IOException {
        //simple-exprS ::= addop term simple-exprS | λ
        if (isAddop()) {
            addop();
            term();
            simpleExprS();
        }//else = lambda

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
                break;

        }
    }

    private void ifStmt() {
        //if condition then stmt-list end | if condition then stmt-list else stmt-list end
        try {
            eat(Tag.IF);
            condition();
            eat(Tag.THEN);
            stmtList();
            if (tok.getTag() == Tag.ELSE) {
                eat(Tag.ELSE);
                stmtList();
            }
            eat(Tag.END);
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + "\n -----> Clausula IF incorreto!");
        }
    }

    private void doStmt() {
        //::= do stmt-list stmt-suffix
        try {
            eat(Tag.DO);
            stmtList();
            stmtSufix();
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + "\n-----> Clausula DO incorreta!");
        }
    }

    private void readStmt() {
        //::= read "(" identifier ")"
        try {
            eat(Tag.READ);
            eat(Tag.AP);
            eat(Tag.ID);
            eat(Tag.FP);
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + "\n -----> READ incorreto!");
        }
    }

    private void writeStmt() {
        //::= write "(" writable ")"
        try {
            eat(Tag.WRT);
            eat(Tag.AP);
            writable();
            eat(Tag.FP);
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + "\n -----> WRITE incorreto!");
        }
    }

    private void condition() throws IOException {
        expression();
    }

    private void expression() throws IOException {
        // simple-expr | simple-expr relop simple-expr
        simpleExpr();
        if (isRelop()) {
            relop();
            simpleExpr();
        }
    }

    private void stmtSufix() throws IOException {
        //::= while condition
        eat(Tag.WHL);
        eat(Tag.AP);
        condition();
        eat(Tag.FP);
    }

    private void writable() {
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
                break;
        }

    }

    private boolean isRelop() {
        switch (tok.getTag()) {
            case (Tag.EQ):
            case (Tag.GT):
            case (Tag.GE):
            case (Tag.LT):
            case (Tag.LE):
            case (Tag.NE):
                return true;
            default:
                return false;
        }
    }

    private boolean isAddop() {
        switch (tok.getTag()) {
            case (Tag.SUM):
            case (Tag.MIN):
            case (Tag.OR):
                return true;
            default:
                return false;

        }
    }

    private void term() {
        //term ::= factor-a termT
        try {
            factorA();
            termT();
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + "\n -----> Termo incorreto!");
        }
    }

    private void factorA() {
        //::= factor | ! factor | "-" factor
        try {
            if (tok.getTag() == Tag.NEG) {
                eat(Tag.NEG);
            } else if (tok.getTag() == Tag.MIN) {
                eat(Tag.MIN);
            }
            factor();
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + "\n -----> Fator incorreto!");
        }

    }

    private void termT() throws IOException {
        //termT ::== mulop factor-a termT | λ
        if (isMulop()) {
            mulop();
            factorA();
            termT();
        } else {

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
        }
    }

    private boolean isMulop() {
        switch (tok.getTag()) {
            case (Tag.MUL):
            case (Tag.DIV):
            case (Tag.AND):
                return true;
            default:
                return false;
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
            default:
                constant();
                break;
        }
    }

    private void constant() {
        //::= integer_const | literal
        if (tok.getTag() == Tag.LIT) {
            literal();
        } else {
            integerConst();
        }
    }

    private void literal() {
        try {
            eat(Tag.LIT);

        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + "\n -----> Literal incorreto!");
        }
    }

    private void integerConst() {
        //::= digit {digit}
        try {
            eat(Tag.NUM);
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + "\n -----> Inteiro incorreto!");
        }
    }
}
