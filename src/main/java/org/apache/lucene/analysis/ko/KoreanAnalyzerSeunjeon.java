package org.apache.lucene.analysis.ko;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;

import java.io.IOException;
import java.io.Reader;

/**
 *
 * KoreanAnalyzerSeunjeon
 *
 * @author ykhfree <ykhfree@icloud.com>
 *
 * 은전한닢 scala 버전(seunjeon)을 사용할 수 있게끔한 루씬 분석기입니다.
 * 본 분석기는 elasticsearch seunjeon plugin을 참고로 개발되었습니다.
 *
 * 기준 Seunjeon 버전 => 1.1.0
 *
 */
public class KoreanAnalyzerSeunjeon extends StopwordAnalyzerBase {

  private TokenizerOptions options;

  /** An unmodifiable set containing some common words that are usually not useful for searching. */
  public static final CharArraySet STOP_WORDS_SET;
  static {
    try {
      STOP_WORDS_SET = loadStopwordSet(false, KoreanAnalyzerSeunjeon.class, "stopwords.txt", "#");
    } catch (IOException ioe) {
      throw new Error("Cannot load stop words", ioe);
    }

  }

  public KoreanAnalyzerSeunjeon(boolean deCompound, boolean deInflect, boolean indexEojeol, boolean postTagging) {
    this(STOP_WORDS_SET);

    this.options = TokenizerOptions.create("search").
            setDeCompound(deCompound).
            setDeInflect(deInflect).
            setIndexEojeol(indexEojeol).
            setIndexPoses(TokenizerOptions.INDEX_POSES).
            setPosTagging(postTagging).
            setMaxUnkLength(TokenizerOptions.MAX_UNK_LENGTH);
  }

  /**
   * Lucene KoreanAnalyzerSeunjeon
   */

  private KoreanAnalyzerSeunjeon(CharArraySet stopWords) {
    super(stopWords);
  }


  @Override
  protected TokenStreamComponents createComponents(String s) {
    final KoreanTokenizerSeunjeon src = new KoreanTokenizerSeunjeon(options);
    TokenStream tok = new LowerCaseFilter(src);
    tok = new StopFilter(tok, stopwords);
    return new TokenStreamComponents(src, tok) {
      @Override
      protected void setReader(final Reader reader) throws IOException {
        super.setReader(reader);
      }
    };
  }

}

