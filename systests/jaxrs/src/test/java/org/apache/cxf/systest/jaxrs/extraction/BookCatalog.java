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
name|systest
operator|.
name|jaxrs
operator|.
name|extraction
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|DELETE
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|multipart
operator|.
name|Attachment
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
name|multipart
operator|.
name|MultipartBody
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
name|SearchContext
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
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|tika
operator|.
name|LuceneDocumentMetadata
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
name|tika
operator|.
name|TikaLuceneContentExtractor
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
name|RAMDirectory
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
name|util
operator|.
name|Version
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

begin_class
annotation|@
name|Path
argument_list|(
literal|"/catalog"
argument_list|)
specifier|public
class|class
name|BookCatalog
block|{
specifier|private
specifier|final
name|TikaLuceneContentExtractor
name|extractor
init|=
operator|new
name|TikaLuceneContentExtractor
argument_list|(
operator|new
name|PDFParser
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Directory
name|directory
init|=
operator|new
name|RAMDirectory
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Analyzer
name|analyzer
init|=
operator|new
name|StandardAnalyzer
argument_list|(
name|Version
operator|.
name|LUCENE_4_9
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|LuceneQueryVisitor
argument_list|<
name|SearchBean
argument_list|>
name|visitor
init|=
name|createVisitor
argument_list|()
decl_stmt|;
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
specifier|public
name|Response
name|addBook
parameter_list|(
specifier|final
name|MultipartBody
name|body
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
specifier|final
name|Attachment
name|attachment
range|:
name|body
operator|.
name|getAllAttachments
argument_list|()
control|)
block|{
specifier|final
name|DataHandler
name|handler
init|=
name|attachment
operator|.
name|getDataHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|source
init|=
name|handler
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|LuceneDocumentMetadata
name|metadata
init|=
operator|new
name|LuceneDocumentMetadata
argument_list|()
operator|.
name|withSource
argument_list|(
name|source
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
name|handler
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|metadata
argument_list|)
decl_stmt|;
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
specifier|final
name|IndexWriter
name|writer
init|=
name|getIndexWriter
argument_list|()
decl_stmt|;
try|try
block|{
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
block|}
finally|finally
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|Collection
argument_list|<
name|ScoreDoc
argument_list|>
name|findBook
parameter_list|(
annotation|@
name|Context
name|SearchContext
name|searchContext
parameter_list|)
throws|throws
name|IOException
block|{
name|IndexReader
name|reader
init|=
name|DirectoryReader
operator|.
name|open
argument_list|(
name|directory
argument_list|)
decl_stmt|;
name|IndexSearcher
name|searcher
init|=
operator|new
name|IndexSearcher
argument_list|(
name|reader
argument_list|)
decl_stmt|;
try|try
block|{
name|visitor
operator|.
name|visit
argument_list|(
name|searchContext
operator|.
name|getCondition
argument_list|(
name|SearchBean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|searcher
operator|.
name|search
argument_list|(
name|visitor
operator|.
name|getQuery
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|1000
argument_list|)
operator|.
name|scoreDocs
argument_list|)
return|;
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|DELETE
specifier|public
name|Response
name|delete
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|IndexWriter
name|writer
init|=
name|getIndexWriter
argument_list|()
decl_stmt|;
try|try
block|{
name|writer
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|writer
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
specifier|private
name|IndexWriter
name|getIndexWriter
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|IndexWriter
argument_list|(
name|directory
argument_list|,
operator|new
name|IndexWriterConfig
argument_list|(
name|Version
operator|.
name|LUCENE_4_9
argument_list|,
name|analyzer
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|LuceneQueryVisitor
argument_list|<
name|SearchBean
argument_list|>
name|createVisitor
parameter_list|()
block|{
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
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|fieldTypes
operator|.
name|put
argument_list|(
literal|"modified"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
expr_stmt|;
name|LuceneQueryVisitor
argument_list|<
name|SearchBean
argument_list|>
name|visitor
init|=
operator|new
name|LuceneQueryVisitor
argument_list|<
name|SearchBean
argument_list|>
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
return|return
name|visitor
return|;
block|}
block|}
end_class

end_unit

