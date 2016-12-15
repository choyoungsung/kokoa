package lucene6_1.analyzer.sample;

import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.AttributeReflector;

public final class MyPartOfSpeechAttributeImpl extends AttributeImpl implements MyPartOfSpeechAttribute {

private PartOfSpeech pos = PartOfSpeech.Unknown;

public void setPartOfSpeech(PartOfSpeech pos) {
this.pos = pos;
}

public PartOfSpeech getPartOfSpeech() {
return pos;
}

@Override
public void clear() {
pos = PartOfSpeech.Unknown;
}

@Override
public void copyTo(AttributeImpl target) {
((MyPartOfSpeechAttribute) target).setPartOfSpeech(pos);
}

@Override //????????? 이 함수에 대한 내용이 없다. 
public void reflectWith(AttributeReflector arg0) {
	// TODO Auto-generated method stub ?????????
	
}
}
