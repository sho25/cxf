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
name|jaxws
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
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
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
name|ws
operator|.
name|Endpoint
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|hello_world_soap_http
operator|.
name|any
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|any
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|any_types
operator|.
name|GreeterImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|any_types
operator|.
name|SayHi
operator|.
name|Port
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
specifier|final
class|class
name|AnyClientServerTest
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
name|MyServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|AnyClientServerTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/any"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
class|class
name|MyServer
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
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
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
name|MyServer
name|s
init|=
operator|new
name|MyServer
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
name|LOG
operator|.
name|info
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
name|MyServer
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
name|testAny
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
literal|"/wsdl/any.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|ss
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|Greeter
name|port
init|=
name|ss
operator|.
name|getSoapPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Port
argument_list|>
name|any
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Port
name|anyPort
init|=
operator|new
name|Port
argument_list|()
decl_stmt|;
name|Port
name|anyPort1
init|=
operator|new
name|Port
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|String
argument_list|>
name|ele1
init|=
operator|new
name|JAXBElement
argument_list|<>
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/other"
argument_list|,
literal|"port"
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"hello"
argument_list|)
decl_stmt|;
name|anyPort
operator|.
name|setAny
argument_list|(
name|ele1
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|String
argument_list|>
name|ele2
init|=
operator|new
name|JAXBElement
argument_list|<>
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/other"
argument_list|,
literal|"port"
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"Bon"
argument_list|)
decl_stmt|;
name|anyPort1
operator|.
name|setAny
argument_list|(
name|ele2
argument_list|)
expr_stmt|;
name|any
operator|.
name|add
argument_list|(
name|anyPort
argument_list|)
expr_stmt|;
name|any
operator|.
name|add
argument_list|(
name|anyPort1
argument_list|)
expr_stmt|;
name|String
name|rep
init|=
name|port
operator|.
name|sayHi
argument_list|(
name|any
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|rep
argument_list|,
literal|"helloBon"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testList
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
literal|"/wsdl/any.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|ss
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|Greeter
name|port
init|=
name|ss
operator|.
name|getSoapPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|any_types
operator|.
name|SayHi1
operator|.
name|Port
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|any_types
operator|.
name|SayHi1
operator|.
name|Port
argument_list|>
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|any_types
operator|.
name|SayHi1
operator|.
name|Port
name|port1
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|any_types
operator|.
name|SayHi1
operator|.
name|Port
argument_list|()
decl_stmt|;
name|port1
operator|.
name|setRequestType
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|any_types
operator|.
name|SayHi1
operator|.
name|Port
name|port2
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|any_types
operator|.
name|SayHi1
operator|.
name|Port
argument_list|()
decl_stmt|;
name|port2
operator|.
name|setRequestType
argument_list|(
literal|"Bon"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|port1
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|port2
argument_list|)
expr_stmt|;
name|String
name|rep
init|=
name|port
operator|.
name|sayHi1
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|rep
argument_list|,
literal|"helloBon"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

