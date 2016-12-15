package testLucene;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class SimpleLuceneSearcher {
	static Logger log = Logger.getLogger("Searcher");
//	Analyzer analyzer = null;
	Directory dir = null;
	IndexReader reader  = null;
	IndexSearcher searcher = null;

	public static void main(String[] args) throws Exception{
		searchTest();
	}
	
	public static void searchTest() throws Exception{
		SimpleLuceneSearcher s = new SimpleLuceneSearcher();
		s.init(SimpleLuceneWriter.indexDir);
		
		Query query = s.makeQuery("value", "name");
		
		TopFieldDocs docs = s.search(query, 10);
		
		s.printResult(docs);
		
		s.close();
	}
	
	public void init(String indexDirName) throws Exception{
//		this.analyzer = new StandardAnalyzer();
		
		this.dir = FSDirectory.open(Paths.get(indexDirName));
		
		reader  = DirectoryReader.open(FSDirectory.open(Paths.get(indexDirName))); //IndexReader.open(this.dir);
		searcher = new IndexSearcher(reader);
		
		log.info("reader.numDocs() = " + reader.numDocs());
	}
	
	public void close() throws Exception{
		if(this.reader!=null)
			reader.close();
	}
	
	public TopFieldDocs search(Query query, int listSize) throws Exception{
		Sort sort = new Sort();
		boolean doDocScores = true;
		boolean doMaxScore = true;
		TopFieldDocs docs = searcher.search(query, listSize, sort, doDocScores, doMaxScore);
		return docs;
	}
	
	public Query makeQuery(String queryString, String defaultField) throws Exception{
		StandardQueryParser qp = new StandardQueryParser();
		Query query = qp.parse(queryString, defaultField);
		return query;
	}
	
	public void printResult(TopFieldDocs docs) throws Exception{
		if(docs==null){
			System.err.println("not found");
			return;
		}
		
		int totalHits = docs.totalHits;
		ScoreDoc[] hits = docs.scoreDocs;
		List<Document> docList = convertScoreDoc2Document(hits);
		System.err.println(totalHits);
		for(Document doc : docList){
			System.err.println(doc);
		}
	}
	
	
	public List<Document> convertScoreDoc2Document(ScoreDoc[] hits) throws Exception{
		if(hits==null){
			return null;
		}
		
		List<Document> res = new ArrayList<Document>();
		for(int i=0;i<hits.length;i++){
			Document d = convertScoreDoc2Document(hits[i]);
			res.add(d);
		}
		return res;
	}
	
	public Document convertScoreDoc2Document(ScoreDoc hit) throws Exception{
		int docid = hit.doc;
		Document d = searcher.doc(docid);
		return d;
	}
	
	
	//////////
//	public List<String> terms(String field) throws Exception{
////		p.println("reader.numDocs() " + reader.numDocs());
//		TermEnum terms = reader.terms(new Term(field));
//		
//		List<String> list = new ArrayList<String>();
//		while(terms.next()){
//			list.add(terms.term().text());
//		}
//		return list;
//	}
//	
//	// 
//	public static Map<String, String> parseBooleanQueryStr(String queryStr, String recordSeps, String fieldSeps) throws Exception{
//		if(queryStr==null)
//			return null;
//		String[] arr = queryStr.split(recordSeps);
//		Map<String, String> res = new HashMap<String, String>();
//		for(String item : arr){
//			String[] pair = item.split(fieldSeps);
//			String query = pair[0];
//			String field = pair[1];
//			res.put(query, field);
//		}
//		return res;
//	}
//	
//	// query:fieldName,query:fieldName 형태로 구성된다. 
//	public static Map<String, String> parseBooleanQueryStr(String queryStr, String defaultFieldName) throws Exception{
//		if(queryStr==null)
//			return null;
//		String[] arr = queryStr.split(",");
//		if(arr==null)
//			return null;
//		Map<String, String> res = new HashMap<String, String>();
//		for(String item : arr){
//			try{
//				String[] pair = item.split(":");
//				if(pair==null || pair.length<=1){
//					if(defaultFieldName!=null){
//						String query = pair[0];
//						String field = defaultFieldName;
//						res.put(query, field);
//					}
//				}else{
//					String query = pair[0];
//					String field = pair[1];
//					res.put(query, field);
//				}
//			}catch(Exception e){
//				log.error(e, e);
//			}
//		}
//		return res;
//	}
//	
//	public ScoreDoc[] search(Map<String, String> queryAndFieldNames, int minNumberShouldMatch, boolean bShoudOrMust, Sort sor, int hitsPerPage) throws Exception{
//		BooleanQuery queryCollection = new BooleanQuery();
//		queryCollection.setMinimumNumberShouldMatch(minNumberShouldMatch); // should + 
//		
//		BooleanClause.Occur occur = null ; 
//		if(bShoudOrMust)
//			occur = BooleanClause.Occur.SHOULD;
//		else
//			occur = BooleanClause.Occur.MUST;
//				
//		Set<String> querys = queryAndFieldNames.keySet();
//		for(String query : querys){
//			String field = queryAndFieldNames.get(query);
//			String[] queryTxtAndBoost = query.split("\\^");
//			if(queryTxtAndBoost!=null && queryTxtAndBoost.length==2){ // query에 ^ boost를 사용한 경우; => 어두운^2:mood => 어두운^2 => 어두운 _ 2
//				String queryTxt = queryTxtAndBoost[0];
//				float boost = Float.parseFloat(queryTxtAndBoost[1]);
//				Query t = new TermQuery(new Term(field, queryTxt));
//				t.setBoost(boost);
//				//System.err.println("BOOSTED QUERY IS " + queryTxt + " " + boost + " " + field);
//				queryCollection.add(t, occur);
//			}else{
//				queryCollection.add(new TermQuery(new Term(field, query)), occur);
//			}
//		}
//		return search(queryCollection, sor, hitsPerPage);
//	}
//	
//	
//	public ScoreDoc[] search(Query q, Sort sor, int hitsPerPage) throws Exception{
//		if(sor==null){
//			sor = new Sort();
//		}
//		
//		TopFieldCollector collector = TopFieldCollector.create(sor, hitsPerPage, false, true, true, false);
//		searcher.search(q, collector);
//		TopDocs tops = collector.topDocs();
//		return tops.scoreDocs;
//	}
//	
//	
//	
//	public ScoreDoc[] search(String query, String defaultField, Sort sor, int hitsPerPage) throws Exception{
//		Query q = new QueryParser(Version.LUCENE_35, defaultField, this.analyzer).parse(query);
//
//		if(sor==null){
//			sor = new Sort();
//		}
//		
//		TopFieldDocs selected = searcher.search(q, hitsPerPage, sor);
//	
//		ScoreDoc[] hits = selected.scoreDocs;
//		return hits;
//	}
//	
//	
//	public Document convertScoreDoc2Document(ScoreDoc hit) throws Exception{
//		int docid = hit.doc;
//		Document d = searcher.doc(docid);
//		return d;
//	}
//	
//	//////////
//	public List<Document> convertScoreDocList2DocumentList(ScoreDoc[] hits) throws Exception{
//		List<Document> list = new ArrayList<Document>();
//		
//		for(int i=0;i<hits.length;++i) {
//			int docId = hits[i].doc;
//			Document d = searcher.doc(docId);
//			list.add(d);
//		}
//		return list;
//	}
//	
//	
//	
//	//////////
//	public List<List<String>> convertScoreDoc(ScoreDoc[] hits, String[] resultFields) throws Exception{
//		List<List<String>> mat = new ArrayList<List<String>>();
//		for(int i=0;i<hits.length;++i) {
//			int docId = hits[i].doc;
//			Document d = searcher.doc(docId);
//			
//			List<String> line = new ArrayList<String>();
//			for(String resultField : resultFields){
//				Fieldable f= d.getFieldable(resultField);
//				line.add(f.stringValue());
//			}
//			mat.add(line);
//		}
//		return mat;
//	}
//	
//	
//	public List<String> convertScoreDoc(ScoreDoc[] hits, String resultField) throws Exception{
//		List<String> list = new ArrayList<String>();
//		for(int i=0;i<hits.length;++i) {
//			int docId = hits[i].doc;
//			Document d = searcher.doc(docId);
//			
//			Fieldable f= d.getFieldable(resultField);
//			
//			list.add(f.stringValue());
//		}
//		return list;
//	}
//	
//	
//	public List<Pair<String, Float>> convertScoreDocWithScore(ScoreDoc[] hits, String resultField) throws Exception{
//		List<Pair<String, Float>> list = new ArrayList<Pair<String, Float>>();
//		for(int i=0;i<hits.length;++i) {
//			int docId = hits[i].doc;
//			float score = hits[i].score;
//			Document d = searcher.doc(docId);
//			
//			Fieldable f= d.getFieldable(resultField);
//			Pair<String, Float> pair = new Pair<String, Float>();
//			pair.first = f.stringValue();
//			pair.second = score;
//			list.add(pair);
//		}
//		return list;
//	}
//	
//	public List<List<String>> convertScoreDoc2Multi(ScoreDoc[] hits, String resultField) throws Exception{
//		List<List<String>>  list = new ArrayList<List<String>> ();
//		for(int i=0;i<hits.length;++i) {
//			int docId = hits[i].doc;
//			Document d = searcher.doc(docId);
//			
//			List<String> line = new ArrayList<String>();
//			Fieldable[] arr = d.getFieldables(resultField);
//			for(Fieldable f : arr){
//				line.add(f.stringValue());
//			}
//			list.add(line);
//		}
//		return list;
//	}
//	
//	
//	
////////////
//	public static List<String> getSingleValue(Document doc, String[] fields){
//		List<String> list = new ArrayList<String>();
//		
//		for(int i = 0; i<fields.length; i++){
//			Fieldable f= doc.getFieldable(fields[i]);
//			if(f==null)
//				list.add(null);
//			else
//				list.add(f.stringValue());
//		}
//		return list;
//	}
//	
//	public static String getSingleValue(Document doc, String field){
//		Fieldable f= doc.getFieldable(field);
//		return  f.stringValue();
//	}
//	
//	public static List<List<String>> getMultiValue(Document doc, String[] fields){
//		List<List<String>> table = new ArrayList<List<String>>();
//		
//		for(int i = 0; i<fields.length; i++){
//			List<String> line = getMultiValue(doc, fields[i]);
//			table.add(line);
//		}
//		return table;
//	}
//	
//	public static List<String> getMultiValue(Document doc, String field){
//		Fieldable[] list = doc.getFieldables(field);
//		List<String> res = new ArrayList<String>();
//		for(Fieldable f : list){
//			res.add(f.stringValue());
//		}
//		return res;
//	}
//	
//	public static Map<String, String> getAllValue(Document doc){
//		Map<String, String> res = new HashMap<String, String>();
//		List<Fieldable> list = doc.getFields();
//		for(Fieldable field : list){
//			res.put(field.name(), field.stringValue());
//		}
//		return res;
//	}
//	
//	//tagList:(tvn and 이뉴스 and 김성주)
//	public static QueryParser getQueryParser(String defaultField){
//		Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_35);
//		QueryParser qp4UserMeta = new QueryParser(Version.LUCENE_35, defaultField, analyzer);
//		return qp4UserMeta;
//	}
}
