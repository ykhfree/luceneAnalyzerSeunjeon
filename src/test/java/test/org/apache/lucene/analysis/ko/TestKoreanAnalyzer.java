package test.org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzerSeunjeon;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

public class TestKoreanAnalyzer extends TestCase {

	@Test
	@Ignore
	public void testKoreanAnalyzer() throws Exception {
		
		//String input = "이 규정에 특별한 규정이 있는 것을 제외하고는 법ㆍ영ㆍ규칙 또는 문화체육관광부 감사규정이 에서 및 정하는 바에 따른다.";
		String input = "철수는 충남 대천에 살고 있는데, 은퇴하고 시설 관리 비즈니스를 하기를 원한다. 시설 관리 관련 사업자들을 만나보니 관련 사업을 하려면 대체로 5 억은 필요하고, 이차보전 비율은 2% 이내가 좋다는 의견을 듣고 정부에서 운영하는 지자체 협약 지원정보를 검색한다.";

		KoreanAnalyzerSeunjeon a = new KoreanAnalyzerSeunjeon(false, true, true, true);
		
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
