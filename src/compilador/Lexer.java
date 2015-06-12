package compilador;

import java.io.*;
import static java.lang.System.exit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Lexer {

    public static int linha = 1;    //Contador de linhas
    private int EOF = 0;      //Controle final de arquivo
    private char ch = ' ';          //Caractere lido do arquivo
    private FileReader arquivo;
    private HashMap tabelaReservada = new HashMap();

    //Construtor
    public Lexer(String fileName) throws FileNotFoundException {
        try {
            arquivo = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }
        
        tabelaReservada.put(Word.declare.getLexema(), Word.declare);
        tabelaReservada.put(Word.start.getLexema(), Word.start);
        tabelaReservada.put(Word.end.getLexema(), Word.end);
        tabelaReservada.put(Word.tipoInt.getLexema(), Word.tipoInt);
        tabelaReservada.put(Word.tipoString.getLexema(), Word.tipoString);
        tabelaReservada.put(Word.condIf.getLexema(), Word.condIf);
        tabelaReservada.put(Word.then.getLexema(), Word.then);
        tabelaReservada.put(Word.condElse.getLexema(), Word.condElse);
        tabelaReservada.put(Word.loopDo.getLexema(), Word.loopDo);
        tabelaReservada.put(Word.loopWhile.getLexema(), Word.loopWhile);
        tabelaReservada.put(Word.read.getLexema(), Word.read);
        tabelaReservada.put(Word.write.getLexema(), Word.write);
        tabelaReservada.put(Word.or.getLexema(), Word.or);
        tabelaReservada.put(Word.and.getLexema(), Word.and);
    }

    private void readch() throws IOException {
        EOF = arquivo.read();
        ch = (char) EOF;
    }

    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c) {
            return false;
        }
        ch = ' ';
        return true;
    }

    public Token scan() throws IOException {
        //Desconsidera delimitadores na entrada
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') {
                continue;
            } else if (ch == '\n') {
                linha++;
            } else {
                break;
            }
        }

        //Operadores e pontuação.
        switch (ch) {
            case '=':
                if (readch('=')) {
                    return Word.equal;
                } else {
                    return new Token("=", Tag.ATRB);
                }

            case '<':
                if (readch('=')) {
                    return Word.lessEqual;
                } else if (readch('>')) {
                    return Word.notEqual;
                } else {
                    return new Token("<", Tag.LT);
                }

            case '>':
                if (readch('=')) {
                    return Word.greaterEqual;
                } else {
                    return new Token(">", Tag.GT);
                }

            case ',':
                readch();
                return new Token(",", Tag.VG);

            case ';':
                readch();
                return new Token(";", Tag.PVG);

            case ':':
                readch();
                return new Token(":", Tag.DPTS);

            case '(':
                readch();
                return new Token("(", Tag.AP);

            case ')':
                readch();
                return new Token(")", Tag.FP);

            case '+':
                readch();
                return new Token("+", Tag.SUM);

            case '-':
                readch();
                return new Token("-", Tag.MIN);

            case '*':
                readch();
                return new Token("*", Tag.MUL);

            case '/':
                readch();//proximo caractere
                switch (ch) {
                    case ('/')://comentario de uma linha
                        while (!readch('\n'));
                        linha++;
                        return null;
                    case ('*')://comentário de varias linhas
                        do {
                            readch();
                            if (ch == (char) -1) {
                                System.out.println("WARNING! Comentário não encerrado!");
                                return null;
                            }
                            if (ch == '\n') {
                                linha++;
                            } else if (ch == '*') {
                                if (readch('/')) {
                                    return null;
                                }
                            }
                        } while ((EOF != (char) -1));

                    default:
                        return new Token("/", Tag.DIV);

                }
        }
        
                //Literal 
        if (ch == '“' || ch == '"') {
            //return new Token ("\"", Tag.ASP);
            StringBuffer w = new StringBuffer();
            w.append(ch);
            readch();
            do {
                w.append(ch);
                readch();
            } while ((ch != '”') && (ch != '"') && (ch != (char) -1));
            if (ch == (char) -1) {
                System.out.println("ERRO! String literal não finalizada!");
                return null;
            } else {
                w.append(ch);
            }
            readch();
            return new Token(w.toString(), Tag.LIT);
        }

        //Números
        if (Character.isDigit(ch)) {
            StringBuffer valor = new StringBuffer();
                do {
                    valor.append(ch);
                    readch();
                } while (Character.isDigit(ch));
            if (Float.parseFloat(valor.toString()) > Integer.MAX_VALUE) {
                System.out.println("Erro! Valor de inteiro maior que o maximo permitido");
                return new Num(valor.toString());
            }
            if (Character.isLetter(ch)) {
                System.out.println("Erro! Identificador incorreto na linha " + linha);
            }

            return new Num(valor.toString());
        }

        //Identificadores
        if (Character.isLetter(ch)) {
            StringBuffer sb = new StringBuffer();
            int count = 0; //identificadores devem ter menos de 20 caracteres
            do {
                sb.append(ch);
                count++;
                readch();
            } while (Character.isLetterOrDigit(ch) && count < 20);
            /*if (ch != ',' && ch != ';' && ch != ':' && ch != '(' && ch != ')' && ch != '=' && ch != '<' && ch != '>' && ch != ' ' && ch != '+' && ch != '*' && ch != '/' && ch != '-' && ch != '\t' && ch != '\r' && ch != '\b' && ch != '\n' && EOF != -1) {
             System.out.println("ERRO - Caractere não especificado \"" + ch + "\" na linha " + linha);
             return e_identificador(sb);
             //readch();
             }*/
            if(count >= 20){
                System.out.println("ERRO! Identificador excedeu o número permitido de caracteres!");
            }
            String s = sb.toString();
            Word w;
            w = (Word) tabelaReservada.get(s);
            if (w == null) {
                w = (Word) TabelaSimbolos.words.get(s);
                if (w != null) {
                    return w; //palavra já existe na HashTable
                } else {
                    w = new Word(s, Tag.ID);
                    TabelaSimbolos.words.put(s, w);
                    return w;
                }
            } else {
                //w = new Word(s, Tag.ID);
                return w;
            }
            
        }


        //Caracteres não especificados
        if (ch != (char) -1) {
            System.out.println("ERRO - Caractere não especificado na linha " + linha + " ch: \"" + ch + "\".");
            Token t = new Token("" + ch, Tag.NESP);
            readch();
            return t;

        }
        Token t = new Token("" + ch, ch);
        ch = ' ';
        return t;
    }

    //Verifica se identificador já existe em algum tabela 
    //E insere na tabela caso seja necessário
    /*public Word e_identificador(StringBuffer sb) {
        String s = sb.toString();
        Word w;
        w = (Word) tabelaReservada.get(s);
        if (w == null) {
            w = (Word) TabelaSimbolos.words.get(s);
            if (w != null) {
                return w; //palavra já existe na HashTable
            } else {
                w = new Word(s, Tag.ID);
                TabelaSimbolos.words.put(s, w);
                return w;
            }
        } else {
            w = new Word(s, Tag.ID);
            return w;
        }
    }*/

    public int getEOF() {
        return EOF;
    }

    public void printTabReservada() {
        System.out.println("<------ TABELA DE PALAVRAS RESERVADAS ------->");
        Iterator it = tabelaReservada.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey());
            it.remove(); // avoids a ConcurrentModificationException
        }

    }

    public void printTabSimbolos() {
        System.out.println("<------ TABELA DE SÍMBOLOS ------->");
        Iterator it = TabelaSimbolos.words.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey());
            it.remove(); // avoids a ConcurrentModificationException
        }

    }

    public int getLinha(){
        return this.linha;
    }
}
