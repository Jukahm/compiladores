package compilador;

import java.io.*;

public class Lexer {

    public static int linha = 1;    //Contador de linhas
    private int EOF = 0;      //Controle final de arquivo
    private char ch = ' ';          //Caractere lido do arquivo
    private FileReader arquivo;

    //Construtor
    public Lexer(String fileName) throws FileNotFoundException {
        try {
            arquivo = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }
        TabelaSimbolos.words.put(Word.declare.getLexema(), Word.declare);
        TabelaSimbolos.words.put(Word.start.getLexema(), Word.start);
        TabelaSimbolos.words.put(Word.end.getLexema(), Word.end);
        TabelaSimbolos.words.put(Word.tipoInt.getLexema(), Word.tipoInt);
        TabelaSimbolos.words.put(Word.tipoString.getLexema(), Word.tipoString);
        TabelaSimbolos.words.put(Word.condIf.getLexema(), Word.condIf);
        TabelaSimbolos.words.put(Word.then.getLexema(), Word.then);
        TabelaSimbolos.words.put(Word.condElse.getLexema(), Word.condElse);
        TabelaSimbolos.words.put(Word.loopDo.getLexema(), Word.loopDo);
        TabelaSimbolos.words.put(Word.loopWhile.getLexema(), Word.loopWhile);
        TabelaSimbolos.words.put(Word.read.getLexema(), Word.read);
        TabelaSimbolos.words.put(Word.write.getLexema(), Word.write);
        TabelaSimbolos.words.put(Word.or.getLexema(), Word.or);
        TabelaSimbolos.words.put(Word.and.getLexema(), Word.and);
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
                        do{
                            readch();
                            if (ch == '\n'){
                                linha++;
                            }else if (ch == '*'){
                                if (readch('/')){
                                    return null;
                                }                                
                            }else
                               continue;
                        }while (ch != '*');
                        
                        return null;

                    default:
                        return new Token("/", Tag.DIV);

                }

            case '“':
                readch();
                return new Token("“", Tag.AASP);

            case '”':
                readch();
                return new Token("”", Tag.FASP);

        }

        //Números
        if (Character.isDigit(ch)) {
            int valor = 0;
            try {
                do {
                    valor = 10 * valor + Character.digit(ch, 10);
                    readch();
                } while (Character.isDigit(ch));
            } catch (Exception e) {
                System.out.println("Erro! Valor de inteiro maior que o maximo permitido");
            }
            return new Num(Integer.toString(valor));
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
            if (ch != ',' && ch != ';' && ch != ':' && ch != '(' && ch != ')' && ch != '=' && ch != '<' && ch != '>' && ch != ' ' && ch != '+' && ch != '*' && ch != '/' && ch != '-' && ch != '\t' && ch != '\r' && ch != '\b' && ch != '\n' && EOF != -1) {
                System.out.println("ERRO! Unexpected character \"" + ch + "\" na linha " + linha);
                readch();
            }
            String s = sb.toString();
            Word w;
            w = (Word) TabelaSimbolos.words.get(s);
            if (w != null) {
                return w; //palavra já existe na HashTable
            }
            w = new Word(s, Tag.ID);
            TabelaSimbolos.words.put(s, w);
            return w;
        }

        //Caracteres não especificados
        Token t = new Token("" + ch, ch);
        if (ch != (char) -1) {
            System.out.println("ERRO - Caractere não especificado na linha " + linha);
        }
        ch = ' ';
        return t;
    }

    public int getEOF() {
        return EOF;
    }
}
