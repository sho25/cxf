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
name|provider
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
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|MessageFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|ServiceMode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|Addressing
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|staxutils
operator|.
name|StaxUtils
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
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
name|testutil
operator|.
name|common
operator|.
name|TestUtil
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
name|InputStreamEntity
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
name|junit
operator|.
name|BeforeClass
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|CXF4818Test
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|Server
operator|.
name|class
argument_list|)
operator|+
literal|"/AddressProvider/AddressProvider"
decl_stmt|;
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|CXF4818Provider
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|ADDRESS
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF4818
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|body
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"cxf4818data.txt"
argument_list|)
decl_stmt|;
name|CloseableHttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpPost
name|post
init|=
operator|new
name|HttpPost
argument_list|(
name|ADDRESS
argument_list|)
decl_stmt|;
name|post
operator|.
name|setEntity
argument_list|(
operator|new
name|InputStreamEntity
argument_list|(
name|body
argument_list|,
name|ContentType
operator|.
name|TEXT_XML
argument_list|)
argument_list|)
expr_stmt|;
name|CloseableHttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|post
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
argument_list|)
decl_stmt|;
comment|//System.out.println(StaxUtils.toString(doc));
name|Element
name|root
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|Node
name|child
init|=
name|root
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|boolean
name|foundBody
init|=
literal|false
decl_stmt|;
name|boolean
name|foundHeader
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"Header"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|foundHeader
operator|=
literal|true
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Already found body"
argument_list|,
name|foundBody
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"Body"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|foundBody
operator|=
literal|true
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Did not find header before the body"
argument_list|,
name|foundHeader
argument_list|)
expr_stmt|;
block|}
name|child
operator|=
name|child
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Did not find the soap:Body element"
argument_list|,
name|foundBody
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Did not find the soap:Header element"
argument_list|,
name|foundHeader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebServiceProvider
argument_list|(
name|serviceName
operator|=
literal|"GenericService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/basictest"
argument_list|,
name|portName
operator|=
literal|"GenericServicePosrt"
argument_list|)
annotation|@
name|ServiceMode
argument_list|(
name|value
operator|=
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
operator|.
name|Mode
operator|.
name|MESSAGE
argument_list|)
annotation|@
name|Addressing
specifier|public
specifier|static
class|class
name|CXF4818Provider
implements|implements
name|Provider
argument_list|<
name|SOAPMessage
argument_list|>
block|{
annotation|@
name|Resource
specifier|protected
name|WebServiceContext
name|context
decl_stmt|;
specifier|public
name|SOAPMessage
name|invoke
parameter_list|(
name|SOAPMessage
name|request
parameter_list|)
block|{
try|try
block|{
name|String
name|responseText
init|=
literal|"<SOAP-ENV:Envelope "
operator|+
literal|"xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<SOAP-ENV:Body>"
operator|+
literal|"<ns2:FooResponse xmlns:ns2=\"http://cxf.apache.org/soapheader/inband\">"
operator|+
literal|"<ns2:Return>Foo Response Body</ns2:Return>"
operator|+
literal|"</ns2:FooResponse>"
operator|+
literal|"</SOAP-ENV:Body>"
operator|+
literal|"</SOAP-ENV:Envelope>\n"
decl_stmt|;
comment|// Create a SOAP request message
name|MessageFactory
name|soapmsgfactory
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|SOAPMessage
name|responseMessage
init|=
name|soapmsgfactory
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|StreamSource
name|responseMessageSrc
init|=
literal|null
decl_stmt|;
name|responseMessageSrc
operator|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|responseText
argument_list|)
argument_list|)
expr_stmt|;
name|responseMessage
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|setContent
argument_list|(
name|responseMessageSrc
argument_list|)
expr_stmt|;
name|responseMessage
operator|.
name|saveChanges
argument_list|()
expr_stmt|;
return|return
name|responseMessage
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

