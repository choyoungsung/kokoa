package testLucene;

import java.nio.file.Paths;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class SimpleLuceneWriter {
	static Logger log = Logger.getLogger("");
	static String indexDir = "lucene-index";
	
	public static void main(String[] args) throws Exception{
		testWrite();
	}
	
	public static void testWrite() throws Exception{
		IndexWriter writer = newIndexWriter("lucene-index");
		
		Document doc = makeFakeDoc();
		indexDoc(writer, doc);
		writer.close();
	}
	
	
	public static Document makeFakeDoc(){
		Document doc = new Document();
		IndexableField field = new StringField("name", "value", Field.Store.YES);
		doc.add(field);
		
		IndexableField field2 = new LongPoint("date", 20160621);
		doc.add(field2);
		
		IndexableField field3 = new TextField("text", "this is text", Field.Store.YES);
		doc.add(field3);
		return doc;
	}
	
	public static void indexDoc(IndexWriter writer, Document doc) throws Exception{
		if(writer.getConfig().getOpenMode() == OpenMode.CREATE){
			writer.addDocument(doc);
		}else{
//			writer.updateDocValues(term, updates);
		}
	}
	
	public static IndexWriter newIndexWriter(String indexDir) throws Exception{
		Directory dir = FSDirectory.open(Paths.get(indexDir));
		Analyzer analzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analzer);
		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(dir, iwc);
		
		return writer;
	}
	
}
