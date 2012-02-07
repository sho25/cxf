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
name|ws
operator|.
name|addr_fromjava
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|java
operator|.
name|util
operator|.
name|List
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
name|BindingProvider
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
name|SOAPFaultException
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|AbstractSoapInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|ReadHeadersInterceptor
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
name|endpoint
operator|.
name|Client
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
name|feature
operator|.
name|LoggingFeature
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
name|frontend
operator|.
name|ClientProxy
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
name|headers
operator|.
name|Header
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
name|interceptor
operator|.
name|Fault
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
name|phase
operator|.
name|Phase
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
name|systest
operator|.
name|ws
operator|.
name|AbstractWSATestBase
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
name|systest
operator|.
name|ws
operator|.
name|addr_fromjava
operator|.
name|client
operator|.
name|AddNumberImpl
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
name|systest
operator|.
name|ws
operator|.
name|addr_fromjava
operator|.
name|client
operator|.
name|AddNumberImplService
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
name|systest
operator|.
name|ws
operator|.
name|addr_fromjava
operator|.
name|client
operator|.
name|AddNumbersException_Exception
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
name|systest
operator|.
name|ws
operator|.
name|addr_fromjava
operator|.
name|server
operator|.
name|Server
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
name|WSAFromJavaTest
extends|extends
name|AbstractWSATestBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|)
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
name|createBus
argument_list|()
expr_stmt|;
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
name|testAddNumbers
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|input
init|=
name|setupInLogging
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|output
init|=
name|setupOutLogging
argument_list|()
decl_stmt|;
name|AddNumberImpl
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|port
operator|.
name|addNumbers
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|expectedOut
init|=
literal|"http://cxf.apache.org/input"
decl_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedOut
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|String
name|expectedIn
init|=
literal|"http://cxf.apache.org/output"
decl_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedIn
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
name|testAddNumbersFault
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|input
init|=
name|setupInLogging
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|output
init|=
name|setupOutLogging
argument_list|()
decl_stmt|;
name|AddNumberImpl
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
try|try
block|{
name|port
operator|.
name|addNumbers
argument_list|(
operator|-
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AddNumbersException_Exception
name|e
parameter_list|)
block|{
assert|assert
literal|true
assert|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
assert|assert
literal|false
assert|;
block|}
name|assertTrue
argument_list|(
name|output
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"http://cxf.apache.org/input"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|String
name|expectedFault
init|=
literal|"http://server.addr_fromjava.ws.systest.cxf.apache.org/AddNumberImpl/"
operator|+
literal|"addNumbers/Fault/AddNumbersException"
decl_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|toString
argument_list|()
argument_list|,
name|input
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedFault
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
name|testAddNumbers2
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|input
init|=
name|setupInLogging
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|output
init|=
name|setupOutLogging
argument_list|()
decl_stmt|;
name|AddNumberImpl
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|port
operator|.
name|addNumbers2
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|base
init|=
literal|"http://server.addr_fromjava.ws.systest.cxf.apache.org/AddNumberImpl"
decl_stmt|;
name|String
name|expectedOut
init|=
name|base
operator|+
literal|"/addNumbers2"
decl_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedOut
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|String
name|expectedIn
init|=
name|base
operator|+
literal|"/addNumbers2Response"
decl_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedIn
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
name|testAddNumbers3Fault
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|input
init|=
name|setupInLogging
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|output
init|=
name|setupOutLogging
argument_list|()
decl_stmt|;
name|AddNumberImpl
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
try|try
block|{
name|port
operator|.
name|addNumbers3
argument_list|(
operator|-
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AddNumbersException_Exception
name|e
parameter_list|)
block|{
assert|assert
literal|true
assert|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
assert|assert
literal|false
assert|;
block|}
name|assertTrue
argument_list|(
name|output
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"http://cxf.apache.org/input"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"http://cxf.apache.org/fault3"
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
name|testAddNumbersJaxWsContext
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|output
init|=
name|setupOutLogging
argument_list|()
decl_stmt|;
name|AddNumberImpl
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|port
decl_stmt|;
name|java
operator|.
name|util
operator|.
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|bp
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SOAPACTION_URI_PROPERTY
argument_list|,
literal|"cxf"
argument_list|)
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|port
operator|.
name|addNumbers
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an ActionNotSupported exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
comment|//expected
block|}
name|String
name|expectedOut
init|=
literal|"cxf</Action>"
decl_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedOut
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"SOAPAction=[\"cxf\"]"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
name|AddNumberImpl
name|getPort
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
literal|"/wsdl_systest_wsspec/add_numbers-fromjava.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|AddNumberImplService
name|service
init|=
operator|new
name|AddNumberImplService
argument_list|(
name|wsdl
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null "
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|AddNumberImpl
name|port
init|=
name|service
operator|.
name|getAddNumberImplPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
return|return
name|port
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnmatchedActions
parameter_list|()
throws|throws
name|Exception
block|{
name|AddNumberImpl
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|port
decl_stmt|;
name|java
operator|.
name|util
operator|.
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|bp
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SOAPACTION_URI_PROPERTY
argument_list|,
literal|"http://cxf.apache.org/input4"
argument_list|)
expr_stmt|;
try|try
block|{
comment|//CXF-2035
name|port
operator|.
name|addNumbers3
argument_list|(
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unexpected wrapper"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFaultFromNonAddressService
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|LoggingFeature
argument_list|()
operator|.
name|initialize
argument_list|(
name|this
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|AddNumberImpl
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
name|java
operator|.
name|util
operator|.
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/AddNumberImplPort-noaddr"
argument_list|)
expr_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|port
operator|.
name|addNumbers
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
try|try
block|{
name|port
operator|.
name|addNumbers3
argument_list|(
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore, expected
block|}
name|long
name|end
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
name|end
operator|-
name|start
operator|)
operator|<
literal|50000
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|RemoveRelatesToHeaderInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|public
name|RemoveRelatesToHeaderInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|READ
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|ReadHeadersInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|List
argument_list|<
name|Header
argument_list|>
name|headers
init|=
name|message
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|Header
name|h2
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Header
name|h
range|:
name|headers
control|)
block|{
if|if
condition|(
literal|"RelatesTo"
operator|.
name|equals
argument_list|(
name|h
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|h2
operator|=
name|h
expr_stmt|;
block|}
block|}
name|headers
operator|.
name|remove
argument_list|(
name|h2
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoRelatesToHeader
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|LoggingFeature
argument_list|()
operator|.
name|initialize
argument_list|(
name|this
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|AddNumberImpl
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
name|Client
name|c
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|c
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|RemoveRelatesToHeaderInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|port
operator|.
name|addNumbers
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
try|try
block|{
name|port
operator|.
name|addNumbers3
argument_list|(
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore, expected
block|}
name|long
name|end
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
name|end
operator|-
name|start
operator|)
operator|<
literal|50000
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

