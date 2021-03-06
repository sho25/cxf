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
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|net
operator|.
name|URL
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
name|io
operator|.
name|CachedOutputStream
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
name|resource
operator|.
name|URIResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|CloseableHttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpPost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpPut
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|ContentType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|FileEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|CloseableHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|EntityUtils
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
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Sent HTTP GET request to query all customer info
comment|/*          * URL url = new URL("http://localhost:9000/customers");          * System.out.println("Invoking server through HTTP GET to query all          * customer info"); InputStream in = url.openStream(); StreamSource          * source = new StreamSource(in); printSource(source);          */
comment|// Sent HTTP GET request to query customer info
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent HTTP GET request to query customer info"
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:9000/customerservice/customers/123"
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
comment|// Sent HTTP GET request to query sub resource product info
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent HTTP GET request to query sub resource product info"
argument_list|)
expr_stmt|;
name|url
operator|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:9000/customerservice/orders/223/products/323"
argument_list|)
expr_stmt|;
name|in
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
comment|// Sent HTTP PUT request to update customer info
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent HTTP PUT request to update customer info"
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
operator|new
name|Client
argument_list|()
decl_stmt|;
name|String
name|inputFile
init|=
name|client
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/update_customer.xml"
argument_list|)
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|URIResolver
name|resolver
init|=
operator|new
name|URIResolver
argument_list|(
name|inputFile
argument_list|)
decl_stmt|;
name|File
name|input
init|=
operator|new
name|File
argument_list|(
name|resolver
operator|.
name|getURI
argument_list|()
argument_list|)
decl_stmt|;
name|HttpPut
name|put
init|=
operator|new
name|HttpPut
argument_list|(
literal|"http://localhost:9000/customerservice/customers"
argument_list|)
decl_stmt|;
name|put
operator|.
name|setEntity
argument_list|(
operator|new
name|FileEntity
argument_list|(
name|input
argument_list|,
name|ContentType
operator|.
name|TEXT_XML
argument_list|)
argument_list|)
expr_stmt|;
name|CloseableHttpClient
name|httpClient
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
try|try
block|{
name|CloseableHttpResponse
name|response
init|=
name|httpClient
operator|.
name|execute
argument_list|(
name|put
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Response status code: "
operator|+
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Response body: "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// Release current connection to the connection pool once you are
comment|// done
name|put
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
comment|// Sent HTTP POST request to add customer
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent HTTP POST request to add customer"
argument_list|)
expr_stmt|;
name|inputFile
operator|=
name|client
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/add_customer.xml"
argument_list|)
operator|.
name|getFile
argument_list|()
expr_stmt|;
name|resolver
operator|=
operator|new
name|URIResolver
argument_list|(
name|inputFile
argument_list|)
expr_stmt|;
name|input
operator|=
operator|new
name|File
argument_list|(
name|resolver
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
name|HttpPost
name|post
init|=
operator|new
name|HttpPost
argument_list|(
literal|"http://localhost:9000/customerservice/customers"
argument_list|)
decl_stmt|;
name|post
operator|.
name|addHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|post
operator|.
name|setEntity
argument_list|(
operator|new
name|FileEntity
argument_list|(
name|input
argument_list|,
name|ContentType
operator|.
name|TEXT_XML
argument_list|)
argument_list|)
expr_stmt|;
name|httpClient
operator|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
try|try
block|{
name|CloseableHttpResponse
name|response
init|=
name|httpClient
operator|.
name|execute
argument_list|(
name|post
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Response status code: "
operator|+
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Response body: "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// Release current connection to the connection pool once you are
comment|// done
name|post
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getStringFromInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|Exception
block|{
name|CachedOutputStream
name|bos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bos
operator|.
name|getOut
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

