package nekotori.furigananeko.Furi;


import com.atilika.kuromoji.ipadic.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NihongoBase {
   private static Set<Character.UnicodeBlock> japaneseKanjiUnicodeBlocks;

    static {
        japaneseKanjiUnicodeBlocks = new HashSet<Character.UnicodeBlock>() {{
            /*add(Character.UnicodeBlock.HIRAGANA);
            add(Character.UnicodeBlock.KATAKANA);*/
            add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
        }};
    }
    /*******convert to hinagana*********/
    public static String toHinagana(String s){
        char[] chs = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c:chs){
            if(c>='ア'&&c<='ン')
                sb.append((char)(c-96));
            else
                sb.append(c);
        }
        return sb.toString();
    }
    /********convert to katakana********/
    public static String toKatakana(String s){
        char[] chs = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c:chs){
            if(c>='あ'&&c<='ん')
                sb.append((char)(c+96));
            else
                sb.append(c);
        }
        return sb.toString();
    }
    public static boolean isKanji(Character ch){
        if (japaneseKanjiUnicodeBlocks.contains(Character.UnicodeBlock.of(ch)))return true;
        else return false;
    }

    public static boolean startWithKanji(String nihongo){
        Character ch=nihongo.charAt(0);
        return isKanji(ch);
    }

    public static List<String> splitWithKanji(String nihongo){
        List<String> ls = new ArrayList<>();
        int i =0;
        for(;i<nihongo.length();i++){
            if(i+1<nihongo.length()&&!isKanji(nihongo.charAt(i))&&isKanji(nihongo.charAt(i+1))) {
                ls.add(nihongo.substring(0, i + 1));
                break;
            }
        }
        if(i<nihongo.length())
        ls.addAll(splitWithKanji(nihongo.substring(i+1)));
        return ls;
    }

   /* public static String originalForm(Token token){
        if(token.getBaseForm().equals(toHinagana(token.getReading()))||
        token.getBaseForm().equals(token.getReading()))return token.getBaseForm();
        else
            return toHinagana(token.getReading());
    }*/
}
