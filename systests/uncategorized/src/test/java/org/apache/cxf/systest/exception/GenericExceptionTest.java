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
name|exception
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
name|URL
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
name|SOAPBinding
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
name|XPathUtils
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
name|assertEquals
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
name|assertNotNull
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|GenericExceptionTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|Server
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/test/HelloService"
argument_list|,
literal|"HelloService"
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
name|Server
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
name|testGenericException
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/generic"
decl_stmt|;
name|URL
name|wsdlURL
init|=
operator|new
name|URL
argument_list|(
name|address
operator|+
literal|"?wsdl"
argument_list|)
decl_stmt|;
comment|//check wsdl element
name|InputStream
name|ins
init|=
name|wsdlURL
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|ins
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"xsd"
argument_list|,
literal|"http://www.w3.org/2001/XMLSchema"
argument_list|)
expr_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"wsdl"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/"
argument_list|)
expr_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"tns"
argument_list|,
literal|"http://cxf.apache.org/test/HelloService"
argument_list|)
expr_stmt|;
name|XPathUtils
name|xpu
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|Node
name|nd
init|=
name|xpu
operator|.
name|getValueNode
argument_list|(
literal|"//xsd:complexType[@name='objectWithGenerics']"
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nd
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|xpu
operator|.
name|getValueNode
argument_list|(
literal|"//xsd:element[@name='a']"
argument_list|,
name|nd
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|xpu
operator|.
name|getValueNode
argument_list|(
literal|"//xsd:element[@name='b']"
argument_list|,
name|nd
argument_list|)
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdlURL
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|service
operator|.
name|addPort
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/test/HelloService"
argument_list|,
literal|"HelloPort"
argument_list|)
argument_list|,
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|GenericsEcho
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/test/HelloService"
argument_list|,
literal|"HelloPort"
argument_list|)
argument_list|,
name|GenericsEcho
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|port
operator|.
name|echo
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Exception is expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GenericsException
name|e
parameter_list|)
block|{
name|ObjectWithGenerics
argument_list|<
name|Boolean
argument_list|,
name|Integer
argument_list|>
name|genericObj
init|=
name|e
operator|.
name|getObj
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|genericObj
operator|.
name|getA
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|genericObj
operator|.
name|getB
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

