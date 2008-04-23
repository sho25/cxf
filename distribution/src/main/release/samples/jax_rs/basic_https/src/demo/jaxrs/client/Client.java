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
name|contrib
operator|.
name|ssl
operator|.
name|AuthSSLProtocolSocketFactory
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
name|FileRequestEntity
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
name|PutMethod
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
name|RequestEntity
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
name|protocol
operator|.
name|Protocol
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
name|File
name|wibble
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|File
name|truststore
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|Protocol
name|authhttps
init|=
operator|new
name|Protocol
argument_list|(
literal|"https"
argument_list|,
operator|new
name|AuthSSLProtocolSocketFactory
argument_list|(
name|wibble
operator|.
name|toURL
argument_list|()
argument_list|,
literal|"password"
argument_list|,
name|truststore
operator|.
name|toURL
argument_list|()
argument_list|,
literal|"password"
argument_list|)
argument_list|,
literal|9000
argument_list|)
decl_stmt|;
name|Protocol
operator|.
name|registerProtocol
argument_list|(
literal|"https"
argument_list|,
name|authhttps
argument_list|)
expr_stmt|;
comment|// Sent HTTP GET request to query customer info
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent HTTPS GET request to query customer info"
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|GetMethod
name|httpget
init|=
operator|new
name|GetMethod
argument_list|(
literal|"https://localhost:9000/customerservice/customers/123"
argument_list|)
decl_stmt|;
name|httpget
operator|.
name|addRequestHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
try|try
block|{
name|httpclient
operator|.
name|executeMethod
argument_list|(
name|httpget
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|httpget
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|httpget
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
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
literal|"Sent HTTPS PUT request to update customer info"
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
literal|"update_customer.txt"
argument_list|)
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|File
name|input
init|=
operator|new
name|File
argument_list|(
name|inputFile
argument_list|)
decl_stmt|;
name|PutMethod
name|put
init|=
operator|new
name|PutMethod
argument_list|(
literal|"https://localhost:9000/customerservice/customers"
argument_list|)
decl_stmt|;
name|RequestEntity
name|entity
init|=
operator|new
name|FileRequestEntity
argument_list|(
name|input
argument_list|,
literal|"text/xml; charset=ISO-8859-1"
argument_list|)
decl_stmt|;
name|put
operator|.
name|setRequestEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|result
init|=
name|httpclient
operator|.
name|executeMethod
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
name|result
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
name|put
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
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
literal|"Sent HTTPS POST request to add customer"
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
literal|"add_customer.txt"
argument_list|)
operator|.
name|getFile
argument_list|()
expr_stmt|;
name|input
operator|=
operator|new
name|File
argument_list|(
name|inputFile
argument_list|)
expr_stmt|;
name|PostMethod
name|post
init|=
operator|new
name|PostMethod
argument_list|(
literal|"https://localhost:9000/customerservice/customers"
argument_list|)
decl_stmt|;
name|post
operator|.
name|addRequestHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|entity
operator|=
operator|new
name|FileRequestEntity
argument_list|(
name|input
argument_list|,
literal|"text/xml; charset=ISO-8859-1"
argument_list|)
expr_stmt|;
name|post
operator|.
name|setRequestEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|result
init|=
name|httpclient
operator|.
name|executeMethod
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
name|result
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
name|post
operator|.
name|getResponseBodyAsString
argument_list|()
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
name|out
operator|.
name|println
argument_list|(
literal|"Client Invoking is succeeded!"
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
block|}
end_class

end_unit

