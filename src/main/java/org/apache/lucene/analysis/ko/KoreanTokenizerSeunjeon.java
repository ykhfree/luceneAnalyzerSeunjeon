package org.apache.lucene.analysis.ko;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.util.AttributeFactory;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.Eojeol;
import org.bitbucket.eunjeon.seunjeon.LNode;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Lucene Korean Tokenizer using seunjeon 1.1.0
 * 
 * @author ykhfree <ykhfree@icloud.com>
 *
 */
public class KoreanTokenizerSeunjeon extends Tokenizer {

	private CharTermAttribute charTermAttribute;
	private PositionIncrementAttribute posIncrAtt;
	private PositionLengthAttribute posLenAtt;
	private OffsetAttribute offsetAttribute;
	private TypeAttribute typeAttribute;
	private Queue<LuceneToken> tokensQueue;
	private TokenBuilder tokenBuilder;

    /**
     * Constructor
     * @param options
     */
	KoreanTokenizerSeunjeon(TokenizerOptions options) {
		super(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);

		initAttributes();
		TokenBuilder.setMaxUnkLength(options.getMaxUnkLength());
		if (options.getUserDictPath() != null) {
			TokenBuilder.setUserDict(options.getUserDictPath());

		} else {
			TokenBuilder.setUserDict(Arrays.asList(options.getUserWords()).iterator());
		}
		tokenBuilder = new TokenBuilder(
				options.getDeCompound(),
				options.getDeInflect(),
				options.getIndexEojeol(),
				options.getPosTagging(),
				TokenBuilder.convertPos(options.getIndexPoses()));
	}

	/**
	 * Add attributes
	 *
	 */
	private void initAttributes() {
		charTermAttribute = addAttribute(CharTermAttribute.class);
		posIncrAtt = addAttribute(PositionIncrementAttribute.class);
		posLenAtt = addAttribute(PositionLengthAttribute.class);
		offsetAttribute = addAttribute(OffsetAttribute.class);
		typeAttribute = addAttribute(TypeAttribute.class);
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		tokensQueue = new LinkedList(tokenBuilder.tokenize(getDocument()));
	}

	/**
	 * 토큰 가져오기
	 * @return
	 * @throws IOException
	 */
	@Override
	public final boolean incrementToken() throws IOException {
		if (tokensQueue.isEmpty()) {
			return false;
		} else {
			LuceneToken pos = tokensQueue.poll();
			posIncrAtt.setPositionIncrement(pos.positionIncr());
			posLenAtt.setPositionLength(pos.positionLength());
			offsetAttribute.setOffset(
					correctOffset(pos.startOffset()),
					correctOffset(pos.endOffset()));
			String term = pos.charTerm();
			charTermAttribute.copyBuffer(term.toCharArray(), 0, term.length());
			typeAttribute.setType(pos.poses());

			return true;
		}
	}

	/**
	 * 검색어 가져오기
	 * @return
	 * @throws IOException
	 */
    private String getDocument() throws IOException {
		StringWriter sw = new StringWriter();
		char[] buffer = new char[1024 * 4];
		int n;
		while (-1 != (n = input.read(buffer))) {
			sw.write(buffer, 0, n);
		}
		return sw.toString().toLowerCase();
	}

}
