package nekotori.furigananeko.Furi;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TextFuri extends NihongoBase implements Furigana {
    private List<Token> furigana;
    private String original;
    Tokenizer tknz;
    public TextFuri(String original){
        original=original.replace(" ","");
        this.original=original;
        tknz=new Tokenizer();
         furigana = tknz.tokenize(original);
    }
    @Override
    /*注音全部*/
    public String furiAll() {
        StringBuilder sb = new StringBuilder();
        for (Token token : furigana) {

            sb.append(token.getBaseForm()).
                    append("(").
                    append(NihongoBase.toHinagana(token.getReading())).
                    append(")");
        }
        return  sb.toString().trim();
    }
    @Override
    /*只为其中的汉字注音*/
    public String furiKanji(){
        StringBuilder stringBuilder =new StringBuilder();
     //   List<String> ls = NihongoBase.splitWithKanji(original);
        for(Token tk:furigana){
            if(tk.getSurface().equals(NihongoBase.toHinagana(tk.getReading())))
                stringBuilder.append(tk.getSurface());
            else
                stringBuilder.append(tk.getSurface()).
                        append("(").
                        append(NihongoBase.toHinagana(tk.getReading())).
                        append(")");
        }
        return stringBuilder.toString();
    }
    private String getReadingKanji(String s){
        s=s.trim();
        List<Token> tks= tknz.tokenize(s);
        return tks.get(0).getReading();

    }

}
