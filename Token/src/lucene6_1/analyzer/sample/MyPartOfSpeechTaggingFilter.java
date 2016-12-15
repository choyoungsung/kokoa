package lucene6_1.analyzer.sample;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import lucene6_1.analyzer.sample.MyPartOfSpeechAttribute.PartOfSpeech;

public class MyPartOfSpeechTaggingFilter extends TokenFilter {
    MyPartOfSpeechAttribute posAtt = addAttribute(MyPartOfSpeechAttribute.class);
    CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    
    protected MyPartOfSpeechTaggingFilter(TokenStream input) {
      super(input);
    }
    
    public boolean incrementToken() throws IOException {
      if (!input.incrementToken()) {return false;}
      posAtt.setPartOfSpeech(determinePOS(termAtt.buffer(), 0, termAtt.length()));
      return true;
    }
    
    // determine the part of speech for the given term
    protected PartOfSpeech determinePOS(char[] term, int offset, int length) {
      // naive implementation that tags every uppercased word as noun
      if (length > 0 && Character.isUpperCase(term[0])) {
        return PartOfSpeech.Noun;
      }
      return PartOfSpeech.Unknown;
    }
  }

