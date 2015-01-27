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
name|fault
package|;
end_package

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
name|text
operator|.
name|DateFormat
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
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|XMLGregorianCalendar
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
name|soap
operator|.
name|SOAPFault
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
name|Bus
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|systest
operator|.
name|ws
operator|.
name|common
operator|.
name|SecurityTestUtil
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
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|WSSConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|util
operator|.
name|WSSecurityUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|util
operator|.
name|XmlSchemaDateFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|example
operator|.
name|contract
operator|.
name|doubleit
operator|.
name|DoubleItFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|example
operator|.
name|contract
operator|.
name|doubleit
operator|.
name|DoubleItPortType
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

begin_comment
comment|/**  * Some tests for modified requests  */
end_comment

begin_class
specifier|public
class|class
name|ModifiedRequestTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|ModifiedRequestServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|STAX_PORT
init|=
name|allocatePort
argument_list|(
name|ModifiedRequestServer
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAMESPACE
init|=
literal|"http://www.example.org/contract/DoubleIt"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_QNAME
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItService"
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
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|ModifiedRequestServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
name|SecurityTestUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
name|stopAllServers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testModifiedSignedTimestamp
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|ModifiedRequestTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|ModifiedRequestTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItFault.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItAsymmetricPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Client
name|cxfClient
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|ModifiedTimestampInterceptor
name|modifyInterceptor
init|=
operator|new
name|ModifiedTimestampInterceptor
argument_list|()
decl_stmt|;
name|cxfClient
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|modifyInterceptor
argument_list|)
expr_stmt|;
name|makeInvocation
argument_list|(
name|port
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Streaming invocation
name|port
operator|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|STAX_PORT
argument_list|)
expr_stmt|;
name|cxfClient
operator|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|modifyInterceptor
operator|=
operator|new
name|ModifiedTimestampInterceptor
argument_list|()
expr_stmt|;
name|cxfClient
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|modifyInterceptor
argument_list|)
expr_stmt|;
name|makeInvocation
argument_list|(
name|port
argument_list|,
literal|true
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testModifiedSignature
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|ModifiedRequestTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|ModifiedRequestTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItFault.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItAsymmetricPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Client
name|cxfClient
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|ModifiedSignatureInterceptor
name|modifyInterceptor
init|=
operator|new
name|ModifiedSignatureInterceptor
argument_list|()
decl_stmt|;
name|cxfClient
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|modifyInterceptor
argument_list|)
expr_stmt|;
name|makeInvocation
argument_list|(
name|port
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Streaming invocation
name|port
operator|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|STAX_PORT
argument_list|)
expr_stmt|;
name|cxfClient
operator|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|modifyInterceptor
operator|=
operator|new
name|ModifiedSignatureInterceptor
argument_list|()
expr_stmt|;
name|cxfClient
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|modifyInterceptor
argument_list|)
expr_stmt|;
name|makeInvocation
argument_list|(
name|port
argument_list|,
literal|true
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testUntrustedSignature
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|ModifiedRequestTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client-untrusted.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|ModifiedRequestTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItFault.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItAsymmetricPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|makeInvocation
argument_list|(
name|port
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Streaming invocation
name|port
operator|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|STAX_PORT
argument_list|)
expr_stmt|;
name|makeInvocation
argument_list|(
name|port
argument_list|,
literal|true
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testModifiedEncryptedKey
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|ModifiedRequestTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|ModifiedRequestTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItFault.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItAsymmetricPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Client
name|cxfClient
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|ModifiedEncryptedKeyInterceptor
name|modifyInterceptor
init|=
operator|new
name|ModifiedEncryptedKeyInterceptor
argument_list|()
decl_stmt|;
name|cxfClient
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|modifyInterceptor
argument_list|)
expr_stmt|;
name|makeInvocation
argument_list|(
name|port
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Streaming invocation
name|port
operator|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|STAX_PORT
argument_list|)
expr_stmt|;
name|cxfClient
operator|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|modifyInterceptor
operator|=
operator|new
name|ModifiedEncryptedKeyInterceptor
argument_list|()
expr_stmt|;
name|cxfClient
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|modifyInterceptor
argument_list|)
expr_stmt|;
name|makeInvocation
argument_list|(
name|port
argument_list|,
literal|true
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testModifiedEncryptedSOAPBody
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|ModifiedRequestTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|ModifiedRequestTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItFault.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItAsymmetricPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Client
name|cxfClient
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|ModifiedEncryptedSOAPBody
name|modifyInterceptor
init|=
operator|new
name|ModifiedEncryptedSOAPBody
argument_list|()
decl_stmt|;
name|cxfClient
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|modifyInterceptor
argument_list|)
expr_stmt|;
name|makeInvocation
argument_list|(
name|port
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Streaming invocation
name|port
operator|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|STAX_PORT
argument_list|)
expr_stmt|;
name|cxfClient
operator|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|modifyInterceptor
operator|=
operator|new
name|ModifiedEncryptedSOAPBody
argument_list|()
expr_stmt|;
name|cxfClient
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|modifyInterceptor
argument_list|)
expr_stmt|;
name|makeInvocation
argument_list|(
name|port
argument_list|,
literal|true
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|makeInvocation
parameter_list|(
name|DoubleItPortType
name|port
parameter_list|,
name|boolean
name|streaming
parameter_list|)
throws|throws
name|DoubleItFault
block|{
try|try
block|{
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on a modified request"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|SOAPFault
name|fault
init|=
name|ex
operator|.
name|getFault
argument_list|()
decl_stmt|;
if|if
condition|(
name|streaming
condition|)
block|{
name|assertTrue
argument_list|(
literal|"soap:Sender"
operator|.
name|equals
argument_list|(
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
operator|||
literal|"soap:Receiver"
operator|.
name|equals
argument_list|(
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|fault
operator|.
name|getFaultString
argument_list|()
operator|.
name|contains
argument_list|(
name|WSSecurityException
operator|.
name|UNIFIED_SECURITY_ERR
argument_list|)
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|subcodeIterator
init|=
name|fault
operator|.
name|getFaultSubcodes
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|subcodeIterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
name|fault
operator|.
name|getFaultCode
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"Sender"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fault
operator|.
name|getFaultString
argument_list|()
argument_list|,
name|WSSecurityException
operator|.
name|UNIFIED_SECURITY_ERR
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|subcodeIterator
init|=
name|fault
operator|.
name|getFaultSubcodes
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|subcodeIterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|subcode
init|=
name|subcodeIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|WSSecurityException
operator|.
name|SECURITY_ERROR
argument_list|,
name|subcode
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|subcodeIterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|ModifiedTimestampInterceptor
extends|extends
name|AbstractModifyRequestInterceptor
block|{
annotation|@
name|Override
specifier|public
name|void
name|modifySecurityHeader
parameter_list|(
name|Element
name|securityHeader
parameter_list|)
block|{
if|if
condition|(
name|securityHeader
operator|!=
literal|null
condition|)
block|{
comment|// Find the Timestamp + change it.
name|Element
name|timestampElement
init|=
name|WSSecurityUtil
operator|.
name|findElement
argument_list|(
name|securityHeader
argument_list|,
literal|"Timestamp"
argument_list|,
name|WSConstants
operator|.
name|WSU_NS
argument_list|)
decl_stmt|;
name|Element
name|createdValue
init|=
name|WSSecurityUtil
operator|.
name|findElement
argument_list|(
name|timestampElement
argument_list|,
literal|"Created"
argument_list|,
name|WSConstants
operator|.
name|WSU_NS
argument_list|)
decl_stmt|;
name|DateFormat
name|zulu
init|=
operator|new
name|XmlSchemaDateFormat
argument_list|()
decl_stmt|;
name|XMLGregorianCalendar
name|createdCalendar
init|=
name|WSSConfig
operator|.
name|datatypeFactory
operator|.
name|newXMLGregorianCalendar
argument_list|(
name|createdValue
operator|.
name|getTextContent
argument_list|()
argument_list|)
decl_stmt|;
comment|// Add 5 seconds
name|Duration
name|duration
init|=
name|WSSConfig
operator|.
name|datatypeFactory
operator|.
name|newDuration
argument_list|(
literal|5000L
argument_list|)
decl_stmt|;
name|createdCalendar
operator|.
name|add
argument_list|(
name|duration
argument_list|)
expr_stmt|;
name|Date
name|createdDate
init|=
name|createdCalendar
operator|.
name|toGregorianCalendar
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|createdValue
operator|.
name|setTextContent
argument_list|(
name|zulu
operator|.
name|format
argument_list|(
name|createdDate
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|modifySOAPBody
parameter_list|(
name|Element
name|soapBody
parameter_list|)
block|{
comment|//
block|}
block|}
specifier|private
specifier|static
class|class
name|ModifiedSignatureInterceptor
extends|extends
name|AbstractModifyRequestInterceptor
block|{
annotation|@
name|Override
specifier|public
name|void
name|modifySecurityHeader
parameter_list|(
name|Element
name|securityHeader
parameter_list|)
block|{
if|if
condition|(
name|securityHeader
operator|!=
literal|null
condition|)
block|{
name|Element
name|signatureElement
init|=
name|WSSecurityUtil
operator|.
name|findElement
argument_list|(
name|securityHeader
argument_list|,
literal|"Signature"
argument_list|,
name|WSConstants
operator|.
name|SIG_NS
argument_list|)
decl_stmt|;
name|Node
name|firstChild
init|=
name|signatureElement
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
operator|(
name|firstChild
operator|instanceof
name|Element
operator|)
operator|&&
name|firstChild
operator|!=
literal|null
condition|)
block|{
name|firstChild
operator|=
name|signatureElement
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
operator|(
operator|(
name|Element
operator|)
name|firstChild
operator|)
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Id"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|modifySOAPBody
parameter_list|(
name|Element
name|soapBody
parameter_list|)
block|{
comment|//
block|}
block|}
specifier|private
specifier|static
class|class
name|ModifiedEncryptedKeyInterceptor
extends|extends
name|AbstractModifyRequestInterceptor
block|{
annotation|@
name|Override
specifier|public
name|void
name|modifySecurityHeader
parameter_list|(
name|Element
name|securityHeader
parameter_list|)
block|{
if|if
condition|(
name|securityHeader
operator|!=
literal|null
condition|)
block|{
name|Element
name|encryptedKey
init|=
name|WSSecurityUtil
operator|.
name|findElement
argument_list|(
name|securityHeader
argument_list|,
literal|"EncryptedKey"
argument_list|,
name|WSConstants
operator|.
name|ENC_NS
argument_list|)
decl_stmt|;
name|Element
name|cipherValue
init|=
name|WSSecurityUtil
operator|.
name|findElement
argument_list|(
name|encryptedKey
argument_list|,
literal|"CipherValue"
argument_list|,
name|WSConstants
operator|.
name|ENC_NS
argument_list|)
decl_stmt|;
name|String
name|cipherText
init|=
name|cipherValue
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|StringBuilder
name|stringBuilder
init|=
operator|new
name|StringBuilder
argument_list|(
name|cipherText
argument_list|)
decl_stmt|;
name|int
name|index
init|=
name|stringBuilder
operator|.
name|length
argument_list|()
operator|/
literal|2
decl_stmt|;
name|char
name|ch
init|=
name|stringBuilder
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|ch
operator|!=
literal|'A'
condition|)
block|{
name|ch
operator|=
literal|'A'
expr_stmt|;
block|}
else|else
block|{
name|ch
operator|=
literal|'B'
expr_stmt|;
block|}
name|stringBuilder
operator|.
name|setCharAt
argument_list|(
name|index
argument_list|,
name|ch
argument_list|)
expr_stmt|;
name|cipherValue
operator|.
name|setTextContent
argument_list|(
name|stringBuilder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|modifySOAPBody
parameter_list|(
name|Element
name|soapBody
parameter_list|)
block|{
comment|//
block|}
block|}
specifier|private
specifier|static
class|class
name|ModifiedEncryptedSOAPBody
extends|extends
name|AbstractModifyRequestInterceptor
block|{
annotation|@
name|Override
specifier|public
name|void
name|modifySecurityHeader
parameter_list|(
name|Element
name|securityHeader
parameter_list|)
block|{
comment|//
block|}
specifier|public
name|void
name|modifySOAPBody
parameter_list|(
name|Element
name|soapBody
parameter_list|)
block|{
if|if
condition|(
name|soapBody
operator|!=
literal|null
condition|)
block|{
name|Element
name|cipherValue
init|=
name|WSSecurityUtil
operator|.
name|findElement
argument_list|(
name|soapBody
argument_list|,
literal|"CipherValue"
argument_list|,
name|WSConstants
operator|.
name|ENC_NS
argument_list|)
decl_stmt|;
name|String
name|cipherText
init|=
name|cipherValue
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|StringBuilder
name|stringBuilder
init|=
operator|new
name|StringBuilder
argument_list|(
name|cipherText
argument_list|)
decl_stmt|;
name|int
name|index
init|=
name|stringBuilder
operator|.
name|length
argument_list|()
operator|/
literal|2
decl_stmt|;
name|char
name|ch
init|=
name|stringBuilder
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|ch
operator|!=
literal|'A'
condition|)
block|{
name|ch
operator|=
literal|'A'
expr_stmt|;
block|}
else|else
block|{
name|ch
operator|=
literal|'B'
expr_stmt|;
block|}
name|stringBuilder
operator|.
name|setCharAt
argument_list|(
name|index
argument_list|,
name|ch
argument_list|)
expr_stmt|;
name|cipherValue
operator|.
name|setTextContent
argument_list|(
name|stringBuilder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

