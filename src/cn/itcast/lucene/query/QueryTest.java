package cn.itcast.lucene.query;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.NumberTools;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.junit.Test;

import cn.itcast.lucene.IndexDao;
import cn.itcast.lucene.QueryResult;
import cn.itcast.lucene.utils.File2DocumentUtils;

public class QueryTest {

	IndexDao indexDao = new IndexDao();

	public void queryAndPrintResult(Query query) {
		System.out.println("query:" + query);
		QueryResult qr = indexDao.search(query, 0, 100);
		System.out.println("result:" + qr.getRecordCount());
		for (Document doc : qr.getRecordList()) {
			File2DocumentUtils.printDocumentInfo(doc);
		}
	}

	@Test
	public void testTermQuery() {
		Term term = new Term("name", "File");
		Query query = new TermQuery(term);

		queryAndPrintResult(query);
	}

	@Test
	public void testRangeQuery() {
		Term lowerTerm = new Term("size", NumberTools.longToString(50));
		Term upperTerm = new Term("size", NumberTools.longToString(1000));
		Query query = new RangeQuery(lowerTerm, upperTerm, false);

		queryAndPrintResult(query);
	}

	// public static void main(String[] args) {
	// System.out.println(Long.MAX_VALUE);
	// System.out.println(NumberTools.longToString(1000));
	// System.out.println(NumberTools.stringToLong("000000000000rs"));
	//
	// System.out.println(DateTools.dateToString(new Date(), Resolution.DAY));
	// System.out.println(DateTools.dateToString(new Date(), Resolution.MINUTE));
	// System.out.println(DateTools.dateToString(new Date(), Resolution.SECOND));
	// }

	@Test
	public void testWildcardQuery() {
		Term term = new Term("name", "Fil?");
		Query query = new WildcardQuery(term);

		queryAndPrintResult(query);
	}

	@Test
	public void testPhraseQuery() {
		PhraseQuery phraseQuery = new PhraseQuery();

		phraseQuery.add(new Term("content", "Fi"));
		phraseQuery.add(new Term("content", "File"));
		phraseQuery.setSlop(2);

		queryAndPrintResult(phraseQuery);
	}

	@Test
	public void testBooleanQuery() {
		PhraseQuery query1 = new PhraseQuery();
		query1.add(new Term("content", "Fi"));
		query1.add(new Term("content", "File"));
		query1.setSlop(2);

		Term lowerTerm = new Term("size", NumberTools.longToString(500));
		Term upperTerm = new Term("size", NumberTools.longToString(1000));
		Query query2 = new RangeQuery(lowerTerm, upperTerm, true);

		BooleanQuery boolQuery = new BooleanQuery();
		boolQuery.add(query1, Occur.MUST);
		boolQuery.add(query2, Occur.SHOULD);

		queryAndPrintResult(boolQuery);
	}

	@Test
	public void testQueryString() {
//		String queryString = "-content:\"北京ʿ \"~2 AND -size:[000000000000dw TO 000000000000rs]";
//		String queryString = "-content:\"北京ʿ \"~2 OR -size:[000000000000dw TO 000000000000rs]";
		String queryString = "-content:\"北京ʿ \"~2 NOT -size:[000000000000dw TO 000000000000rs]";

		QueryResult qr = indexDao.search(queryString, 0, 10);
		System.out.println("result:" + qr.getRecordCount());
		for (Document doc : qr.getRecordList()) {
			File2DocumentUtils.printDocumentInfo(doc);
		}
	}

}
