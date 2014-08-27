begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|search
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
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
name|json
operator|.
name|Json
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|json
operator|.
name|JsonArray
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|json
operator|.
name|JsonArrayBuilder
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
name|NotFoundException
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
name|PathParam
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
name|WebApplicationException
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
name|container
operator|.
name|AsyncResponse
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
name|container
operator|.
name|Suspended
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
operator|.
name|Status
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
name|StreamingOutput
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
name|UriInfo
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
name|helpers
operator|.
name|IOUtils
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
name|document
operator|.
name|DocumentStoredFieldVisitor
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
name|index
operator|.
name|Term
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
name|MatchAllDocsQuery
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
name|search
operator|.
name|TermQuery
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
name|TopDocs
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
name|Catalog
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
name|Storage
name|storage
decl_stmt|;
specifier|private
specifier|final
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
argument_list|)
decl_stmt|;
specifier|public
name|Catalog
parameter_list|(
specifier|final
name|Storage
name|storage
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|storage
operator|=
name|storage
expr_stmt|;
name|initIndex
argument_list|()
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
specifier|public
name|void
name|addBook
parameter_list|(
annotation|@
name|Suspended
specifier|final
name|AsyncResponse
name|response
parameter_list|,
annotation|@
name|Context
specifier|final
name|UriInfo
name|uri
parameter_list|,
specifier|final
name|MultipartBody
name|body
parameter_list|)
block|{
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
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
try|try
block|{
if|if
condition|(
name|exists
argument_list|(
name|source
argument_list|)
condition|)
block|{
name|response
operator|.
name|resume
argument_list|(
name|Response
operator|.
name|status
argument_list|(
name|Status
operator|.
name|CONFLICT
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|byte
index|[]
name|content
init|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|handler
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|storeAndIndex
argument_list|(
name|metadata
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|ex
parameter_list|)
block|{
name|response
operator|.
name|resume
argument_list|(
name|Response
operator|.
name|serverError
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|response
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|response
operator|.
name|resume
argument_list|(
name|Response
operator|.
name|created
argument_list|(
name|uri
operator|.
name|getRequestUriBuilder
argument_list|()
operator|.
name|path
argument_list|(
name|source
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|response
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|response
operator|.
name|resume
argument_list|(
name|Response
operator|.
name|status
argument_list|(
name|Status
operator|.
name|BAD_REQUEST
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
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
name|JsonArray
name|getBooks
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
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
specifier|final
name|IndexSearcher
name|searcher
init|=
operator|new
name|IndexSearcher
argument_list|(
name|reader
argument_list|)
decl_stmt|;
specifier|final
name|JsonArrayBuilder
name|builder
init|=
name|Json
operator|.
name|createArrayBuilder
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|Query
name|query
init|=
operator|new
name|MatchAllDocsQuery
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|ScoreDoc
name|scoreDoc
range|:
name|searcher
operator|.
name|search
argument_list|(
name|query
argument_list|,
literal|1000
argument_list|)
operator|.
name|scoreDocs
control|)
block|{
specifier|final
name|DocumentStoredFieldVisitor
name|visitor
init|=
operator|new
name|DocumentStoredFieldVisitor
argument_list|(
name|LuceneDocumentMetadata
operator|.
name|SOURCE_FIELD
argument_list|)
decl_stmt|;
name|reader
operator|.
name|document
argument_list|(
name|scoreDoc
operator|.
name|doc
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|visitor
operator|.
name|getDocument
argument_list|()
operator|.
name|getField
argument_list|(
name|LuceneDocumentMetadata
operator|.
name|SOURCE_FIELD
argument_list|)
operator|.
name|stringValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
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
name|GET
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"/search"
argument_list|)
specifier|public
name|JsonArray
name|findBook
parameter_list|(
annotation|@
name|Context
name|SearchContext
name|searchContext
parameter_list|,
annotation|@
name|Context
specifier|final
name|UriInfo
name|uri
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
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
specifier|final
name|IndexSearcher
name|searcher
init|=
operator|new
name|IndexSearcher
argument_list|(
name|reader
argument_list|)
decl_stmt|;
specifier|final
name|JsonArrayBuilder
name|builder
init|=
name|Json
operator|.
name|createArrayBuilder
argument_list|()
decl_stmt|;
try|try
block|{
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
specifier|final
name|Query
name|query
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
specifier|final
name|TopDocs
name|topDocs
init|=
name|searcher
operator|.
name|search
argument_list|(
name|query
argument_list|,
literal|1000
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|ScoreDoc
name|scoreDoc
range|:
name|topDocs
operator|.
name|scoreDocs
control|)
block|{
specifier|final
name|Document
name|document
init|=
name|reader
operator|.
name|document
argument_list|(
name|scoreDoc
operator|.
name|doc
argument_list|)
decl_stmt|;
specifier|final
name|String
name|source
init|=
name|document
operator|.
name|getField
argument_list|(
name|LuceneDocumentMetadata
operator|.
name|SOURCE_FIELD
argument_list|)
operator|.
name|stringValue
argument_list|()
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|Json
operator|.
name|createObjectBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"source"
argument_list|,
name|source
argument_list|)
operator|.
name|add
argument_list|(
literal|"score"
argument_list|,
name|scoreDoc
operator|.
name|score
argument_list|)
operator|.
name|add
argument_list|(
literal|"url"
argument_list|,
name|uri
operator|.
name|getBaseUriBuilder
argument_list|()
operator|.
name|path
argument_list|(
name|Catalog
operator|.
name|class
argument_list|)
operator|.
name|path
argument_list|(
name|source
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
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
name|GET
annotation|@
name|Path
argument_list|(
literal|"/{source}"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_OCTET_STREAM
argument_list|)
specifier|public
name|StreamingOutput
name|getBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"source"
argument_list|)
specifier|final
name|String
name|source
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|StreamingOutput
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|InputStream
name|in
init|=
literal|null
decl_stmt|;
try|try
block|{
name|in
operator|=
name|storage
operator|.
name|getDocument
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|FileNotFoundException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|NotFoundException
argument_list|(
literal|"Document does not exist: "
operator|+
name|source
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|/* do nothing */
block|}
block|}
block|}
block|}
block|}
return|;
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
name|storage
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
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
name|void
name|initIndex
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
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
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
argument_list|,
name|analyzer
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
specifier|private
name|boolean
name|exists
parameter_list|(
specifier|final
name|String
name|source
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
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
specifier|final
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
return|return
name|searcher
operator|.
name|search
argument_list|(
operator|new
name|TermQuery
argument_list|(
operator|new
name|Term
argument_list|(
name|LuceneDocumentMetadata
operator|.
name|SOURCE_FIELD
argument_list|,
name|source
argument_list|)
argument_list|)
argument_list|,
literal|1
argument_list|)
operator|.
name|totalHits
operator|>
literal|0
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
specifier|private
name|void
name|storeAndIndex
parameter_list|(
specifier|final
name|LuceneDocumentMetadata
name|metadata
parameter_list|,
specifier|final
name|byte
index|[]
name|content
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedInputStream
name|in
init|=
literal|null
decl_stmt|;
try|try
block|{
name|in
operator|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|content
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Document
name|document
init|=
name|extractor
operator|.
name|extract
argument_list|(
name|in
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
name|storage
operator|.
name|addDocument
argument_list|(
name|metadata
operator|.
name|getSource
argument_list|()
argument_list|,
name|content
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
finally|finally
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|/* do nothing */
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

