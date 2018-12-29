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
name|InputStream
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
name|Assert
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
name|assertFalse
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|TikaContentExtractorTest
block|{
specifier|private
name|TikaContentExtractor
name|extractor
decl_stmt|;
specifier|private
name|SearchConditionParser
argument_list|<
name|SearchBean
argument_list|>
name|parser
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
name|TikaContentExtractor
argument_list|(
operator|new
name|PDFParser
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
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|sc
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"Author==Bertrand*"
argument_list|)
decl_stmt|;
specifier|final
name|SearchBean
name|bean
init|=
name|extractor
operator|.
name|extractMetadataToSearchBean
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
name|bean
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sc
operator|.
name|isMet
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractedTextContentDoesNotMatchSearchCriteria
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|sc
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"Author==Barry*"
argument_list|)
decl_stmt|;
specifier|final
name|SearchBean
name|bean
init|=
name|extractor
operator|.
name|extractMetadataToSearchBean
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
name|bean
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|isMet
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractionFromTextFileUsingPdfParserFails
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"Document should be null, it is not a PDF"
argument_list|,
name|extractor
operator|.
name|extract
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/testTXT.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractionFromRtfFileUsingPdfParserWithoutMediaTypeValidationFails
parameter_list|()
block|{
specifier|final
name|TikaContentExtractor
name|another
init|=
operator|new
name|TikaContentExtractor
argument_list|(
operator|new
name|PDFParser
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Document should be null, it is not a PDF"
argument_list|,
name|another
operator|.
name|extract
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/testRTF.rtf"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractionFromEncryptedPdfFails
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"Document should be null, it is encrypted"
argument_list|,
name|extractor
operator|.
name|extract
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/testPDF.Encrypted.pdf"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractionFromNullInputStreamFails
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"Document should be null, it is encrypted"
argument_list|,
name|extractor
operator|.
name|extract
argument_list|(
operator|(
name|InputStream
operator|)
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

