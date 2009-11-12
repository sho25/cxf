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
name|net
operator|.
name|HttpURLConnection
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|dom
operator|.
name|DOMSource
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
name|Dispatch
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
name|Service
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
name|helpers
operator|.
name|XMLUtils
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
name|hello_world_xml_http
operator|.
name|wrapped
operator|.
name|XMLService
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

begin_class
specifier|public
class|class
name|ProviderXMLClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_xml_http/wrapped"
argument_list|,
literal|"XMLService"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_xml_http/wrapped"
argument_list|,
literal|"XMLProviderPort"
argument_list|)
decl_stmt|;
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
name|XMLServer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyPost
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:9022/XMLService/XMLProviderPort"
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|connection
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|setRequestMethod
argument_list|(
literal|"POST"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setDoInput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|int
name|i
init|=
name|connection
operator|.
name|getResponseCode
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|connection
operator|.
name|getContentType
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"xml"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDOMSourcePAYLOAD
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world_xml_wrapped.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|XMLService
name|service
init|=
operator|new
name|XMLService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/messages/XML_GreetMeDocLiteralReq.xml"
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|DOMSource
name|reqMsg
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reqMsg
argument_list|)
expr_stmt|;
name|Dispatch
argument_list|<
name|DOMSource
argument_list|>
name|disp
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|portName
argument_list|,
name|DOMSource
operator|.
name|class
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
decl_stmt|;
name|DOMSource
name|result
init|=
name|disp
operator|.
name|invoke
argument_list|(
name|reqMsg
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|Node
name|respDoc
init|=
name|result
operator|.
name|getNode
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"greetMeResponse"
argument_list|,
name|respDoc
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestXMLBindingProviderMessage"
argument_list|,
name|respDoc
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/messages/XML_GreetMeDocLiteralReq_invalid.xml"
argument_list|)
expr_stmt|;
name|doc
operator|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|reqMsg
operator|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|reqMsg
argument_list|)
expr_stmt|;
name|disp
operator|=
name|service
operator|.
name|createDispatch
argument_list|(
name|portName
argument_list|,
name|DOMSource
operator|.
name|class
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
expr_stmt|;
try|try
block|{
name|result
operator|=
name|disp
operator|.
name|invoke
argument_list|(
name|reqMsg
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should have a schema validation exception of some sort"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//expected - different validators are throwing different error messages though
block|}
block|}
block|}
end_class

end_unit

