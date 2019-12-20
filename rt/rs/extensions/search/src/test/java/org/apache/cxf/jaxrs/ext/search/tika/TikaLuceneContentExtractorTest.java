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
name|tika
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|fiql
operator|.
name|FiqlParser
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
name|lucene
operator|.
name|LuceneQueryVisitor
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
name|IndexReader
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
name|apache
operator|.
name|tika
operator|.
name|parser
operator|.
name|pdf
operator|.
name|PDFParser
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
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|TikaLuceneContentExtractorTest
block|{
specifier|private
name|TikaLuceneContentExtractor
name|extractor
decl_stmt|;
specifier|private
name|Directory
name|directory
decl_stmt|;
specifier|private
name|IndexWriter
name|writer
decl_stmt|;
specifier|private
name|SearchConditionParser
argument_list|<
name|SearchBean
argument_list|>
name|parser
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
specifier|final
name|Analyzer
name|analyzer
init|=
operator|new
name|StandardAnalyzer
argument_list|()
decl_stmt|;
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
name|writer
operator|=
operator|new
name|IndexWriter
argument_list|(
name|directory
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|writer
operator|.
name|commit
argument_list|()
expr_stmt|;
name|parser
operator|=
operator|new
name|FiqlParser
argument_list|<>
argument_list|(
name|SearchBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|extractor
operator|=
operator|new
name|TikaLuceneContentExtractor
argument_list|(
operator|new
name|PDFParser
argument_list|()
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
name|writer
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
annotation|@
name|Test
specifier|public
name|void
name|testExtractedTextContentMatchesSearchCriteria
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Document
name|document
init|=
name|extractor
operator|.
name|extract
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/testPDF.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Document should not be null"
argument_list|,
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"ct==tika"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"ct==incubation"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"ct==toolsuite"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// meta-data
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"Author==Bertrand*"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractedTextContentMatchesTypesAndDateSearchCriteria
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
init|=
operator|new
name|LuceneDocumentMetadata
argument_list|(
literal|"contents"
argument_list|)
operator|.
name|withField
argument_list|(
literal|"modified"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Document
name|document
init|=
name|extractor
operator|.
name|extract
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/testPDF.pdf"
argument_list|)
argument_list|,
name|documentMetadata
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Document should not be null"
argument_list|,
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|commit
argument_list|()
expr_stmt|;
comment|// testPDF.pdf 'modified' is set to '2007-09-14T09:02:31Z'
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"modified=gt=2007-09-14T09:02:31Z"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"modified=le=2007-09-15T09:02:31-0500"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"modified=ge=2007-09-15"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"modified==2007-09-15"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"modified==2007-09-16"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"modified=gt=2007-09-16"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"modified=lt=2007-09-15"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"modified=gt=2007-09-16T09:02:31"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"modified=lt=2007-09-01T09:02:31"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractedTextContentMatchesTypesAndIntegerSearchCriteria
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
init|=
operator|new
name|LuceneDocumentMetadata
argument_list|(
literal|"contents"
argument_list|)
operator|.
name|withField
argument_list|(
literal|"xmpTPg:NPages"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Document
name|document
init|=
name|extractor
operator|.
name|extract
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/testPDF.pdf"
argument_list|)
argument_list|,
name|documentMetadata
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Document should not be null"
argument_list|,
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=gt=0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages==1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=ge=1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=gt=1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=lt=1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractedTextContentMatchesTypesAndByteSearchCriteria
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
init|=
operator|new
name|LuceneDocumentMetadata
argument_list|(
literal|"contents"
argument_list|)
operator|.
name|withField
argument_list|(
literal|"xmpTPg:NPages"
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Document
name|document
init|=
name|extractor
operator|.
name|extract
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/testPDF.pdf"
argument_list|)
argument_list|,
name|documentMetadata
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Document should not be null"
argument_list|,
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=gt=0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages==1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=ge=1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=gt=1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=lt=1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractedTextContentMatchesTypesAndLongSearchCriteria
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
init|=
operator|new
name|LuceneDocumentMetadata
argument_list|(
literal|"contents"
argument_list|)
operator|.
name|withField
argument_list|(
literal|"xmpTPg:NPages"
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Document
name|document
init|=
name|extractor
operator|.
name|extract
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/testPDF.pdf"
argument_list|)
argument_list|,
name|documentMetadata
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Document should not be null"
argument_list|,
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=gt=0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages==1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=ge=1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=gt=1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=lt=1"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractedTextContentMatchesTypesAndDoubleSearchCriteria
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
init|=
operator|new
name|LuceneDocumentMetadata
argument_list|(
literal|"contents"
argument_list|)
operator|.
name|withField
argument_list|(
literal|"xmpTPg:NPages"
argument_list|,
name|Double
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Document
name|document
init|=
name|extractor
operator|.
name|extract
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/testPDF.pdf"
argument_list|)
argument_list|,
name|documentMetadata
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Document should not be null"
argument_list|,
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=gt=0.0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages==1.0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=ge=1.0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=gt=1.0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=lt=1.0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractedTextContentMatchesTypesAndFloatSearchCriteria
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
init|=
operator|new
name|LuceneDocumentMetadata
argument_list|(
literal|"contents"
argument_list|)
operator|.
name|withField
argument_list|(
literal|"xmpTPg:NPages"
argument_list|,
name|Float
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Document
name|document
init|=
name|extractor
operator|.
name|extract
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/testPDF.pdf"
argument_list|)
argument_list|,
name|documentMetadata
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Document should not be null"
argument_list|,
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=gt=0.0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages==1.0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=ge=1.0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=gt=1.0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"xmpTPg:NPages=lt=1.0"
argument_list|,
name|documentMetadata
operator|.
name|getFieldTypes
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContentSourceMatchesSearchCriteria
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
init|=
operator|new
name|LuceneDocumentMetadata
argument_list|()
operator|.
name|withSource
argument_list|(
literal|"testPDF.pdf"
argument_list|)
decl_stmt|;
specifier|final
name|Document
name|document
init|=
name|extractor
operator|.
name|extract
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/testPDF.pdf"
argument_list|)
argument_list|,
name|documentMetadata
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Document should not be null"
argument_list|,
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|writer
operator|.
name|commit
argument_list|()
expr_stmt|;
comment|// Should work by exact match only
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getHits
argument_list|(
literal|"source==testPDF.pdf"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getHits
argument_list|(
literal|"source==testPDF"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ScoreDoc
index|[]
name|getHits
parameter_list|(
specifier|final
name|String
name|expression
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getHits
argument_list|(
name|expression
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|ScoreDoc
index|[]
name|getHits
parameter_list|(
specifier|final
name|String
name|expression
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|fieldTypes
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|IndexReader
name|reader
init|=
name|DirectoryReader
operator|.
name|open
argument_list|(
name|directory
argument_list|)
init|)
block|{
name|IndexSearcher
name|searcher
init|=
operator|new
name|IndexSearcher
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|LuceneQueryVisitor
argument_list|<
name|SearchBean
argument_list|>
name|visitor
init|=
operator|new
name|LuceneQueryVisitor
argument_list|<>
argument_list|(
literal|"ct"
argument_list|,
literal|"contents"
argument_list|)
decl_stmt|;
name|visitor
operator|.
name|setPrimitiveFieldTypeMap
argument_list|(
name|fieldTypes
argument_list|)
expr_stmt|;
name|visitor
operator|.
name|visit
argument_list|(
name|parser
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
argument_list|)
expr_stmt|;
name|ScoreDoc
index|[]
name|hits
init|=
name|searcher
operator|.
name|search
argument_list|(
name|visitor
operator|.
name|getQuery
argument_list|()
argument_list|,
literal|1000
argument_list|)
operator|.
name|scoreDocs
decl_stmt|;
name|assertNotNull
argument_list|(
name|hits
argument_list|)
expr_stmt|;
return|return
name|hits
return|;
block|}
block|}
block|}
end_class

end_unit

