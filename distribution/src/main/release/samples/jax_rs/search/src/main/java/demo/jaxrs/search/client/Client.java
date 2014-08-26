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
name|client
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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpClient
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
name|httpclient
operator|.
name|HttpException
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
name|httpclient
operator|.
name|methods
operator|.
name|DeleteMethod
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
name|httpclient
operator|.
name|methods
operator|.
name|GetMethod
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
name|httpclient
operator|.
name|methods
operator|.
name|PostMethod
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
name|httpclient
operator|.
name|methods
operator|.
name|multipart
operator|.
name|ByteArrayPartSource
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
name|httpclient
operator|.
name|methods
operator|.
name|multipart
operator|.
name|FilePart
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
name|httpclient
operator|.
name|methods
operator|.
name|multipart
operator|.
name|MultipartRequestEntity
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
name|httpclient
operator|.
name|methods
operator|.
name|multipart
operator|.
name|Part
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

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
name|Client
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|url
init|=
literal|"http://localhost:9000/catalog"
decl_stmt|;
specifier|final
name|HttpClient
name|httpClient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|uploadToCatalog
argument_list|(
name|url
argument_list|,
name|httpClient
argument_list|,
literal|"jsr339-jaxrs-2.0-final-spec.pdf"
argument_list|)
expr_stmt|;
name|uploadToCatalog
argument_list|(
name|url
argument_list|,
name|httpClient
argument_list|,
literal|"JavaWebSocketAPI_1.0_Final.pdf"
argument_list|)
expr_stmt|;
name|list
argument_list|(
name|url
argument_list|,
name|httpClient
argument_list|)
expr_stmt|;
name|search
argument_list|(
name|url
argument_list|,
name|httpClient
argument_list|,
literal|"ct==java"
argument_list|)
expr_stmt|;
name|search
argument_list|(
name|url
argument_list|,
name|httpClient
argument_list|,
literal|"ct==Java"
argument_list|)
expr_stmt|;
name|search
argument_list|(
name|url
argument_list|,
name|httpClient
argument_list|,
literal|"ct==websockets"
argument_list|)
expr_stmt|;
name|search
argument_list|(
name|url
argument_list|,
name|httpClient
argument_list|,
literal|"ct==WebSockets"
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|url
argument_list|,
name|httpClient
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|list
parameter_list|(
specifier|final
name|String
name|url
parameter_list|,
specifier|final
name|HttpClient
name|httpClient
parameter_list|)
throws|throws
name|IOException
throws|,
name|HttpException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent HTTP GET request to query all books in catalog"
argument_list|)
expr_stmt|;
specifier|final
name|GetMethod
name|get
init|=
operator|new
name|GetMethod
argument_list|(
name|url
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|status
init|=
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|get
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|==
literal|200
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|get
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|search
parameter_list|(
specifier|final
name|String
name|url
parameter_list|,
specifier|final
name|HttpClient
name|httpClient
parameter_list|,
specifier|final
name|String
name|expression
parameter_list|)
throws|throws
name|IOException
throws|,
name|HttpException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent HTTP GET request to search the books in catalog: "
operator|+
name|expression
argument_list|)
expr_stmt|;
specifier|final
name|GetMethod
name|get
init|=
operator|new
name|GetMethod
argument_list|(
name|url
operator|+
literal|"/search"
argument_list|)
decl_stmt|;
name|get
operator|.
name|setQueryString
argument_list|(
literal|"$filter="
operator|+
name|expression
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|status
init|=
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|get
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|==
literal|200
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|get
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|uploadToCatalog
parameter_list|(
specifier|final
name|String
name|url
parameter_list|,
specifier|final
name|HttpClient
name|httpClient
parameter_list|,
specifier|final
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
throws|,
name|HttpException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent HTTP POST request to upload the file into catalog: "
operator|+
name|filename
argument_list|)
expr_stmt|;
specifier|final
name|PostMethod
name|post
init|=
operator|new
name|PostMethod
argument_list|(
name|url
argument_list|)
decl_stmt|;
specifier|final
name|Part
index|[]
name|parts
init|=
block|{
operator|new
name|FilePart
argument_list|(
name|filename
argument_list|,
operator|new
name|ByteArrayPartSource
argument_list|(
name|filename
argument_list|,
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|Client
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/"
operator|+
name|filename
argument_list|)
argument_list|)
argument_list|)
argument_list|)
block|}
decl_stmt|;
name|post
operator|.
name|setRequestEntity
argument_list|(
operator|new
name|MultipartRequestEntity
argument_list|(
name|parts
argument_list|,
name|post
operator|.
name|getParams
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|status
init|=
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|post
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|==
literal|201
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|post
operator|.
name|getResponseHeader
argument_list|(
literal|"Location"
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|status
operator|==
literal|409
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Document already exists: "
operator|+
name|filename
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|post
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|delete
parameter_list|(
specifier|final
name|String
name|url
parameter_list|,
specifier|final
name|HttpClient
name|httpClient
parameter_list|)
throws|throws
name|IOException
throws|,
name|HttpException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent HTTP DELETE request to remove all books from catalog"
argument_list|)
expr_stmt|;
specifier|final
name|DeleteMethod
name|delete
init|=
operator|new
name|DeleteMethod
argument_list|(
name|url
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|status
init|=
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|delete
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|==
literal|200
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|delete
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|delete
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

