begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|lucene
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|SearchBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|SearchCondition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|SearchConditionParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|SearchConditionVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|analysis
operator|.
name|Analyzer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|analysis
operator|.
name|standard
operator|.
name|StandardAnalyzer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|IntPoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|StoredField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|TextField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|DirectoryReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexWriterConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexableField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|IndexSearcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|ScoreDoc
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|store
operator|.
name|Directory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|store
operator|.
name|MMapDirectory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractLuceneQueryVisitorTest
block|{
specifier|private
name|DirectoryReader
name|ireader
decl_stmt|;
specifier|private
name|IndexSearcher
name|isearcher
decl_stmt|;
specifier|private
name|Directory
name|directory
decl_stmt|;
specifier|private
name|Analyzer
name|analyzer
decl_stmt|;
specifier|private
name|Path
name|tempDirectory
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|analyzer
operator|=
operator|new
name|StandardAnalyzer
argument_list|()
expr_stmt|;
name|tempDirectory
operator|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"lucene"
argument_list|)
expr_stmt|;
name|directory
operator|=
operator|new
name|MMapDirectory
argument_list|(
name|tempDirectory
argument_list|)
expr_stmt|;
name|IndexWriterConfig
name|config
init|=
operator|new
name|IndexWriterConfig
argument_list|(
name|analyzer
argument_list|)
decl_stmt|;
name|IndexWriter
name|iwriter
init|=
operator|new
name|IndexWriter
argument_list|(
name|directory
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
operator|new
name|Field
argument_list|(
literal|"contents"
argument_list|,
literal|"name=text"
argument_list|,
name|TextField
operator|.
name|TYPE_STORED
argument_list|)
argument_list|)
expr_stmt|;
name|IntPoint
name|intPoint
init|=
operator|new
name|IntPoint
argument_list|(
literal|"intfield"
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|intPoint
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
operator|new
name|StoredField
argument_list|(
literal|"intfield"
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|iwriter
operator|.
name|addDocument
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|iwriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|ireader
operator|=
name|DirectoryReader
operator|.
name|open
argument_list|(
name|directory
argument_list|)
expr_stmt|;
name|isearcher
operator|=
operator|new
name|IndexSearcher
argument_list|(
name|ireader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|ireader
operator|.
name|close
argument_list|()
expr_stmt|;
name|directory
operator|.
name|close
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|tempDirectory
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|SearchConditionParser
argument_list|<
name|SearchBean
argument_list|>
name|getParser
parameter_list|()
function_decl|;
specifier|protected
name|void
name|doTestTextContentMatch
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
name|doTestTextContentMatch
argument_list|(
name|expression
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doTestTextContentMatchWithAnalyzer
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
name|doTestTextContentMatch
argument_list|(
name|expression
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doTestTextContentMatch
parameter_list|(
name|String
name|expression
parameter_list|,
name|boolean
name|useAnalyzer
parameter_list|)
throws|throws
name|Exception
block|{
name|Query
name|query
init|=
name|createTermQuery
argument_list|(
literal|"contents"
argument_list|,
name|expression
argument_list|,
name|useAnalyzer
argument_list|)
decl_stmt|;
name|doTestTextContentMatchWithQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doTestNoMatch
parameter_list|(
name|Query
name|query
parameter_list|)
throws|throws
name|Exception
block|{
name|ScoreDoc
index|[]
name|hits
init|=
name|isearcher
operator|.
name|search
argument_list|(
name|query
argument_list|,
literal|1000
argument_list|)
operator|.
name|scoreDocs
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|hits
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doTestTextContentMatchWithQuery
parameter_list|(
name|Query
name|query
parameter_list|)
throws|throws
name|Exception
block|{
name|ScoreDoc
index|[]
name|hits
init|=
name|isearcher
operator|.
name|search
argument_list|(
name|query
argument_list|,
literal|1000
argument_list|)
operator|.
name|scoreDocs
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|hits
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// Iterate through the results:
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|hits
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Document
name|hitDoc
init|=
name|isearcher
operator|.
name|doc
argument_list|(
name|hits
index|[
name|i
index|]
operator|.
name|doc
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"name=text"
argument_list|,
name|hitDoc
operator|.
name|get
argument_list|(
literal|"contents"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|doTestIntContentMatch
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
name|Query
name|query
init|=
name|createTermQuery
argument_list|(
literal|"intfield"
argument_list|,
name|expression
argument_list|)
decl_stmt|;
name|doTestIntContentMatchWithQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doTestIntContentMatchWithQuery
parameter_list|(
name|Query
name|query
parameter_list|)
throws|throws
name|Exception
block|{
name|ScoreDoc
index|[]
name|hits
init|=
name|isearcher
operator|.
name|search
argument_list|(
name|query
argument_list|,
literal|1000
argument_list|)
operator|.
name|scoreDocs
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|hits
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// Iterate through the results:
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|hits
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Document
name|hitDoc
init|=
name|isearcher
operator|.
name|doc
argument_list|(
name|hits
index|[
name|i
index|]
operator|.
name|doc
argument_list|)
decl_stmt|;
name|IndexableField
name|field
init|=
name|hitDoc
operator|.
name|getField
argument_list|(
literal|"intfield"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|field
operator|.
name|numericValue
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Query
name|createTermQuery
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createTermQuery
argument_list|(
name|expression
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|protected
name|Query
name|createTermQueryWithAnalyzer
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createTermQuery
argument_list|(
name|expression
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|protected
name|Query
name|createTermQuery
parameter_list|(
name|String
name|expression
parameter_list|,
name|boolean
name|useAnalyzer
parameter_list|)
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|filter
init|=
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|SearchConditionVisitor
argument_list|<
name|SearchBean
argument_list|,
name|Query
argument_list|>
name|lucene
init|=
operator|new
name|LuceneQueryVisitor
argument_list|<>
argument_list|(
name|useAnalyzer
condition|?
name|analyzer
else|:
literal|null
argument_list|)
decl_stmt|;
name|lucene
operator|.
name|visit
argument_list|(
name|filter
argument_list|)
expr_stmt|;
return|return
name|lucene
operator|.
name|getQuery
argument_list|()
return|;
block|}
specifier|protected
name|Query
name|createTermQueryWithFieldClassWithAnalyzer
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createTermQueryWithFieldClass
argument_list|(
name|expression
argument_list|,
name|cls
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|protected
name|Query
name|createTermQueryWithFieldClass
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createTermQueryWithFieldClass
argument_list|(
name|expression
argument_list|,
name|cls
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|protected
name|Query
name|createTermQueryWithFieldClass
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|boolean
name|useAnalyzer
parameter_list|)
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|filter
init|=
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|LuceneQueryVisitor
argument_list|<
name|SearchBean
argument_list|>
name|lucene
init|=
operator|new
name|LuceneQueryVisitor
argument_list|<>
argument_list|(
name|useAnalyzer
condition|?
name|analyzer
else|:
literal|null
argument_list|)
decl_stmt|;
name|lucene
operator|.
name|setPrimitiveFieldTypeMap
argument_list|(
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
operator|>
name|singletonMap
argument_list|(
literal|"intfield"
argument_list|,
name|cls
argument_list|)
argument_list|)
expr_stmt|;
name|lucene
operator|.
name|visit
argument_list|(
name|filter
argument_list|)
expr_stmt|;
return|return
name|lucene
operator|.
name|getQuery
argument_list|()
return|;
block|}
specifier|protected
name|Query
name|createTermQuery
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createTermQuery
argument_list|(
name|fieldName
argument_list|,
name|expression
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|protected
name|Query
name|createTermQuery
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|expression
parameter_list|,
name|boolean
name|useAnalyzer
parameter_list|)
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|filter
init|=
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|LuceneQueryVisitor
argument_list|<
name|SearchBean
argument_list|>
name|lucene
init|=
operator|new
name|LuceneQueryVisitor
argument_list|<>
argument_list|(
literal|"ct"
argument_list|,
name|fieldName
argument_list|,
name|useAnalyzer
condition|?
name|analyzer
else|:
literal|null
argument_list|)
decl_stmt|;
name|lucene
operator|.
name|visit
argument_list|(
name|filter
argument_list|)
expr_stmt|;
return|return
name|lucene
operator|.
name|getQuery
argument_list|()
return|;
block|}
specifier|protected
name|Query
name|createTermQueryWithFieldClass
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|filter
init|=
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|LuceneQueryVisitor
argument_list|<
name|SearchBean
argument_list|>
name|lucene
init|=
operator|new
name|LuceneQueryVisitor
argument_list|<>
argument_list|(
literal|"ct"
argument_list|,
name|fieldName
argument_list|)
decl_stmt|;
name|lucene
operator|.
name|setPrimitiveFieldTypeMap
argument_list|(
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
operator|>
name|singletonMap
argument_list|(
name|fieldName
argument_list|,
name|cls
argument_list|)
argument_list|)
expr_stmt|;
name|lucene
operator|.
name|visit
argument_list|(
name|filter
argument_list|)
expr_stmt|;
return|return
name|lucene
operator|.
name|getQuery
argument_list|()
return|;
block|}
specifier|protected
name|Query
name|createPhraseQuery
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createPhraseQuery
argument_list|(
name|fieldName
argument_list|,
name|expression
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|protected
name|Query
name|createPhraseQueryWithAnalyzer
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createPhraseQuery
argument_list|(
name|fieldName
argument_list|,
name|expression
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|protected
name|Query
name|createPhraseQuery
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|expression
parameter_list|,
name|boolean
name|useAnalyzer
parameter_list|)
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|filter
init|=
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|LuceneQueryVisitor
argument_list|<
name|SearchBean
argument_list|>
name|lucene
init|=
operator|new
name|LuceneQueryVisitor
argument_list|<>
argument_list|(
name|fieldName
argument_list|,
name|useAnalyzer
condition|?
name|analyzer
else|:
literal|null
argument_list|)
decl_stmt|;
name|lucene
operator|.
name|visit
argument_list|(
name|filter
argument_list|)
expr_stmt|;
return|return
name|lucene
operator|.
name|getQuery
argument_list|()
return|;
block|}
block|}
end_class

end_unit

