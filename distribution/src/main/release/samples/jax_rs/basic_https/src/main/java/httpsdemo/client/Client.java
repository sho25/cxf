begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|httpsdemo
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
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
name|client
operator|.
name|JAXRSClientFactory
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
name|client
operator|.
name|WebClient
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
name|HttpEntity
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
name|HttpGet
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
name|conn
operator|.
name|ssl
operator|.
name|SSLConnectionSocketFactory
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
name|HttpClients
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
name|message
operator|.
name|BasicHeader
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
name|ssl
operator|.
name|SSLContexts
import|;
end_import

begin_import
import|import
name|httpsdemo
operator|.
name|common
operator|.
name|Customer
import|;
end_import

begin_import
import|import
name|httpsdemo
operator|.
name|common
operator|.
name|CustomerService
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_CONFIG_FILE
init|=
literal|"ClientConfig.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BASE_SERVICE_URL
init|=
literal|"https://localhost:9000/customerservice/customers"
decl_stmt|;
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
name|String
name|keyStoreLoc
init|=
literal|"src/main/config/clientKeystore.jks"
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|keyStore
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|keyStoreLoc
argument_list|)
argument_list|,
literal|"cspass"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|SSLContext
name|sslcontext
init|=
name|SSLContexts
operator|.
name|custom
argument_list|()
operator|.
name|loadTrustMaterial
argument_list|(
name|keyStore
argument_list|,
literal|null
argument_list|)
operator|.
name|loadKeyMaterial
argument_list|(
name|keyStore
argument_list|,
literal|"ckpass"
operator|.
name|toCharArray
argument_list|()
argument_list|)
operator|.
name|useProtocol
argument_list|(
literal|"TLSv1.2"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
comment|/*          * Send HTTP GET request to query customer info using portable HttpClient          * object from Apache HttpComponents          */
name|SSLConnectionSocketFactory
name|sf
init|=
operator|new
name|SSLConnectionSocketFactory
argument_list|(
name|sslcontext
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sending HTTPS GET request to query customer info"
argument_list|)
expr_stmt|;
name|CloseableHttpClient
name|httpclient
init|=
name|HttpClients
operator|.
name|custom
argument_list|()
operator|.
name|setSSLSocketFactory
argument_list|(
name|sf
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpGet
name|httpget
init|=
operator|new
name|HttpGet
argument_list|(
name|BASE_SERVICE_URL
operator|+
literal|"/123"
argument_list|)
decl_stmt|;
name|BasicHeader
name|bh
init|=
operator|new
name|BasicHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/xml"
argument_list|)
decl_stmt|;
name|httpget
operator|.
name|addHeader
argument_list|(
name|bh
argument_list|)
expr_stmt|;
name|CloseableHttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|httpget
argument_list|)
decl_stmt|;
name|HttpEntity
name|entity
init|=
name|response
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|entity
operator|.
name|writeTo
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
name|response
operator|.
name|close
argument_list|()
expr_stmt|;
name|httpclient
operator|.
name|close
argument_list|()
expr_stmt|;
comment|/*          *  Send HTTP PUT request to update customer info, using CXF WebClient method          *  Note: if need to use basic authentication, use the WebClient.create(baseAddress,          *  username,password,configFile) variant, where configFile can be null if you're          *  not using certificates.          */
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n\nSending HTTPS PUT to update customer name"
argument_list|)
expr_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|BASE_SERVICE_URL
argument_list|,
name|CLIENT_CONFIG_FILE
argument_list|)
decl_stmt|;
name|Customer
name|customer
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|customer
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setName
argument_list|(
literal|"Mary"
argument_list|)
expr_stmt|;
name|Response
name|resp
init|=
name|wc
operator|.
name|put
argument_list|(
name|customer
argument_list|)
decl_stmt|;
comment|/*          *  Send HTTP POST request to add customer, using JAXRSClientProxy          *  Note: if need to use basic authentication, use the JAXRSClientFactory.create(baseAddress,          *  username,password,configFile) variant, where configFile can be null if you're          *  not using certificates.          */
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n\nSending HTTPS POST request to add customer"
argument_list|)
expr_stmt|;
name|CustomerService
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|BASE_SERVICE_URL
argument_list|,
name|CustomerService
operator|.
name|class
argument_list|,
name|CLIENT_CONFIG_FILE
argument_list|)
decl_stmt|;
name|customer
operator|=
operator|new
name|Customer
argument_list|()
expr_stmt|;
name|customer
operator|.
name|setName
argument_list|(
literal|"Jack"
argument_list|)
expr_stmt|;
name|resp
operator|=
name|wc
operator|.
name|post
argument_list|(
name|customer
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

