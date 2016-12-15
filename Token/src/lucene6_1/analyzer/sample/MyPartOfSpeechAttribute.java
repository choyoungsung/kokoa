package lucene6_1.analyzer.sample;

import org.apache.lucene.util.Attribute;

public interface MyPartOfSpeechAttribute extends Attribute {
    public static enum PartOfSpeech {
      Noun, Verb, Adjective, Adverb, Pronoun, Preposition, Conjunction, Article, Unknown
    }
  
    public void setPartOfSpeech(PartOfSpeech pos);
  
    public PartOfSpeech getPartOfSpeech();
  }
