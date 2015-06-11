/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public void execute() throws IOException {
        tok = getToken();
        S();
    }

    public Token getToken() throws FileNotFoundException, IOException {
        tok = lexer.scan();
        if (lexer.getEOF() != -1 && tok != null) {
            System.out.println('<' + tok.getValor() + ',' + tok.getTag() + '>');
            return tok;
        } else {
            System.out.println("erro lexer");
            return null;
        }

    }

    void advance() throws IOException {
        tok = getToken();
    }

    void eat(int t) throws IOException {
        if (tok.getTag() == t) {
            System.out.println("eating: " + tok.getValor());
            advance();
        } else {
            error("ERRO - EAT ");
            System.exit(0);
        }
    }

    void error(String str) {
        System.out.print("ERROR!!!    ");
        System.out.println(str);

    }

    void S() throws IOException {
        switch (tok.getTag()) {//token getTag retornar int
            case Tag.DCL:
                eat(Tag.DCL);
                declList();
                eat(Tag.STRT);
                stmtList();
                eat(Tag.END);
                break;

            default:
                error("Default S()");
                break;

        }
    }

    private void declList() {
        try {
            decl();
            eat(Tag.PVG);
            while (tok.getTag() == Tag.ID) {
                declList();
               
            }
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + " -----> Declaração incorreta!");
        }
    }

    private void decl() throws IOException {
        //decl ::= ident-list “:” type
        try {
            identList();
            eat(Tag.DPTS);
            type();
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + " -----> Declaração incorreta!");
        }
    }

    private void identList() throws IOException {
        //ident-list ::= identifier {"," identifier}
        eat(Tag.ID);
        while (tok.getTag() == Tag.VG) {
            eat(Tag.VG);
            eat(Tag.ID);
        }
    }

    private void type() throws IOException {
        //type ::= int | string
        if (tok.getTag() == Tag.INT) {
            eat(Tag.INT);
        } else if (tok.getTag() == Tag.STRG) {
            eat(Tag.STRG);
        } else {
            error("Linha " + lexer.getLinha()
                    + " -----> Tipo de variavel indeterminado!");
        }
    }

    private void stmtList() throws IOException {
        stmt();
        eat(Tag.PVG);
        while (tok.getTag() != Tag.END) {
            declList();
            eat(Tag.PVG);
        }
    }

    private void stmt() throws IOException {
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
                error("Linha " + lexer.getLinha()
                        + "\n -----> Statment incorreto!");
        }

    }

    private void assignStmt() throws IOException {
        eat(Tag.ID);
        eat(Tag.ATRB);
        simpleExpr();
    }

    private void simpleExpr() {
        //tirar recursividade
    }

    private void ifStmt() {
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
        try {
            eat(Tag.READ);
            eat(Tag.AP);
            identifier();
            eat(Tag.FP);
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + "\n -----> READ incorreto!");
        }
    }

    private void writeStmt() {
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

    private void condition() {
        espression();
    }

    private void espression() {
        simpleExpr();
        if (isRelop()) {
            expressionE();
        }

    }

    private void expressionE() {
        try {
            relop();
            simpleExpr();
        } catch (Exception e) {
            error("Linha " + lexer.getLinha()
                    + "\n -----> Expressão incorreta!");
        }
    }

    private void stmtSufix() throws IOException {
        eat(Tag.WHL);
        condition();
    }

    private void identifier() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
