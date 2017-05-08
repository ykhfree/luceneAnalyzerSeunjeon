package test.org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzerSeunjeon;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import junit.framework.TestCase;
import org.junit.Test;

public class TestKoreanAnalyzer extends TestCase {

	@Test
	public void testKoreanAnalyzer() throws Exception {
		
		String input = "이 규정에 특별한 규정이 있는 것을 제외하고는 법ㆍ영ㆍ규칙 또는 문화체육관광부 감사규정이 정하는 바에 따른다.";
		//String input = "삼성전자";

		KoreanAnalyzerSeunjeon a = new KoreanAnalyzerSeunjeon(true, true, true, true);
		
		StringBuilder actual = new StringBuilder();
		
		TokenStream ts = a.tokenStream("bogus", input);
	    CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
	    ts.reset();
	    while (ts.incrementToken()) {
	      actual.append(termAtt.toString());
	      actual.append(" | ");
	    }
	    System.out.println(actual);

	    ts.end();
	    ts.close();
	}
	
	/*public void testConvertUnicode() throws Exception {
		char c = 0x772C;
		System.out.println(c);
		
		int code = '領';
		System.out.println(code);
		
		System.out.println(Character.getType('&'));
	}*/
}
