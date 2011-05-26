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
name|aegis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Map
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
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|databinding
operator|.
name|AegisDatabinding
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
name|authservice
operator|.
name|AuthService
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
name|authservice
operator|.
name|Authenticate
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
name|endpoint
operator|.
name|dynamic
operator|.
name|DynamicClientFactory
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
name|ClientProxyFactoryBean
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
name|interceptor
operator|.
name|LoggingInInterceptor
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
name|LoggingOutInterceptor
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|test
operator|.
name|TestUtilities
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

begin_class
specifier|public
class|class
name|AegisClientServerTest
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
name|AegisServer
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
name|AegisClientServerTest
operator|.
name|class
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
name|AegisServer
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
name|testAegisClient
parameter_list|()
throws|throws
name|Exception
block|{
name|AegisDatabinding
name|aegisBinding
init|=
operator|new
name|AegisDatabinding
argument_list|()
decl_stmt|;
name|ClientProxyFactoryBean
name|proxyFactory
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFactory
operator|.
name|setDataBinding
argument_list|(
name|aegisBinding
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|setServiceClass
argument_list|(
name|AuthService
operator|.
name|class
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service"
argument_list|)
expr_stmt|;
name|AuthService
name|service
init|=
operator|(
name|AuthService
operator|)
name|proxyFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|authenticate
argument_list|(
literal|"Joe"
argument_list|,
literal|"Joe"
argument_list|,
literal|"123"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|service
operator|.
name|authenticate
argument_list|(
literal|"Joe1"
argument_list|,
literal|"Joe"
argument_list|,
literal|"fang"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|authenticate
argument_list|(
literal|"Joe"
argument_list|,
literal|null
argument_list|,
literal|"123"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|service
operator|.
name|getRoles
argument_list|(
literal|"Joe"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Joe"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Joe-1"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Joe-2"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|roles
index|[]
init|=
name|service
operator|.
name|getRolesAsArray
argument_list|(
literal|"Joe"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|roles
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Joe"
argument_list|,
name|roles
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Joe-1"
argument_list|,
name|roles
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"get Joe"
argument_list|,
name|service
operator|.
name|getAuthentication
argument_list|(
literal|"Joe"
argument_list|)
argument_list|)
expr_stmt|;
name|Authenticate
name|au
init|=
operator|new
name|Authenticate
argument_list|()
decl_stmt|;
name|au
operator|.
name|setSid
argument_list|(
literal|"ffang"
argument_list|)
expr_stmt|;
name|au
operator|.
name|setUid
argument_list|(
literal|"ffang"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|authenticate
argument_list|(
name|au
argument_list|)
argument_list|)
expr_stmt|;
name|au
operator|.
name|setUid
argument_list|(
literal|"ffang1"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|service
operator|.
name|authenticate
argument_list|(
name|au
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxWsAegisClient
parameter_list|()
throws|throws
name|Exception
block|{
name|AegisDatabinding
name|aegisBinding
init|=
operator|new
name|AegisDatabinding
argument_list|()
decl_stmt|;
name|JaxWsProxyFactoryBean
name|proxyFactory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFactory
operator|.
name|setDataBinding
argument_list|(
name|aegisBinding
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|setServiceClass
argument_list|(
name|AuthService
operator|.
name|class
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/jaxwsAndAegis"
argument_list|)
expr_stmt|;
name|AuthService
name|service
init|=
operator|(
name|AuthService
operator|)
name|proxyFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|authenticate
argument_list|(
literal|"Joe"
argument_list|,
literal|"Joe"
argument_list|,
literal|"123"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|service
operator|.
name|authenticate
argument_list|(
literal|"Joe1"
argument_list|,
literal|"Joe"
argument_list|,
literal|"fang"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|authenticate
argument_list|(
literal|"Joe"
argument_list|,
literal|null
argument_list|,
literal|"123"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|service
operator|.
name|getRoles
argument_list|(
literal|"Joe"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Joe"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Joe-1"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Joe-2"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|roles
index|[]
init|=
name|service
operator|.
name|getRolesAsArray
argument_list|(
literal|"Joe"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|roles
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Joe"
argument_list|,
name|roles
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Joe-1"
argument_list|,
name|roles
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|roles
operator|=
name|service
operator|.
name|getRolesAsArray
argument_list|(
literal|"null"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|roles
argument_list|)
expr_stmt|;
name|roles
operator|=
name|service
operator|.
name|getRolesAsArray
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|roles
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"get Joe"
argument_list|,
name|service
operator|.
name|getAuthentication
argument_list|(
literal|"Joe"
argument_list|)
argument_list|)
expr_stmt|;
name|Authenticate
name|au
init|=
operator|new
name|Authenticate
argument_list|()
decl_stmt|;
name|au
operator|.
name|setSid
argument_list|(
literal|"ffang"
argument_list|)
expr_stmt|;
name|au
operator|.
name|setUid
argument_list|(
literal|"ffang"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|authenticate
argument_list|(
name|au
argument_list|)
argument_list|)
expr_stmt|;
name|au
operator|.
name|setUid
argument_list|(
literal|"ffang1"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|service
operator|.
name|authenticate
argument_list|(
name|au
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSDL
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
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/jaxwsAndAegis?wsdl"
argument_list|)
decl_stmt|;
name|Document
name|dom
init|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|TestUtilities
name|util
init|=
operator|new
name|TestUtilities
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|util
operator|.
name|addDefaultNamespaces
argument_list|()
expr_stmt|;
name|util
operator|.
name|assertInvalid
argument_list|(
literal|"//wsdl:definitions/wsdl:types/xsd:schema/"
operator|+
literal|"xsd:complexType[@name='getRolesAsArrayResponse']/"
operator|+
literal|"xsd:sequence/xsd:element[@maxOccurs]"
argument_list|,
name|dom
argument_list|)
expr_stmt|;
name|util
operator|.
name|assertValid
argument_list|(
literal|"//wsdl:definitions/wsdl:types/xsd:schema/"
operator|+
literal|"xsd:complexType[@name='getRolesAsArrayResponse']/"
operator|+
literal|"xsd:sequence/xsd:element[@nillable='true']"
argument_list|,
name|dom
argument_list|)
expr_stmt|;
name|url
operator|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/serviceWithCustomNS?wsdl"
argument_list|)
expr_stmt|;
name|dom
operator|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
name|util
operator|.
name|assertValid
argument_list|(
literal|"//wsdl:definitions[@targetNamespace='http://foo.bar.com']"
argument_list|,
name|dom
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|AegisDatabinding
name|aegisBinding
init|=
operator|new
name|AegisDatabinding
argument_list|()
decl_stmt|;
name|JaxWsProxyFactoryBean
name|proxyFactory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFactory
operator|.
name|setDataBinding
argument_list|(
name|aegisBinding
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|setServiceClass
argument_list|(
name|SportsService
operator|.
name|class
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|setWsdlLocation
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/jaxwsAndAegisSports?wsdl"
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|SportsService
name|service
init|=
operator|(
name|SportsService
operator|)
name|proxyFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Team
argument_list|>
name|teams
init|=
name|service
operator|.
name|getTeams
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|teams
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Patriots"
argument_list|,
name|teams
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//CXF-1251
name|String
name|s
init|=
name|service
operator|.
name|testForMinOccurs0
argument_list|(
literal|"A"
argument_list|,
literal|null
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Anullb"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testComplexMapResult
parameter_list|()
throws|throws
name|Exception
block|{
name|AegisDatabinding
name|aegisBinding
init|=
operator|new
name|AegisDatabinding
argument_list|()
decl_stmt|;
name|JaxWsProxyFactoryBean
name|proxyFactory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFactory
operator|.
name|setDataBinding
argument_list|(
name|aegisBinding
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|setServiceClass
argument_list|(
name|SportsService
operator|.
name|class
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/jaxwsAndAegisSports"
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|SportsService
name|service
init|=
operator|(
name|SportsService
operator|)
name|proxyFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|result
init|=
name|service
operator|.
name|testComplexMapResult
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|result
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|containsKey
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|result
operator|.
name|toString
argument_list|()
argument_list|,
literal|"{key1={1=3}}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenericCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|AegisDatabinding
name|aegisBinding
init|=
operator|new
name|AegisDatabinding
argument_list|()
decl_stmt|;
name|JaxWsProxyFactoryBean
name|proxyFactory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFactory
operator|.
name|setDataBinding
argument_list|(
name|aegisBinding
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|setServiceClass
argument_list|(
name|SportsService
operator|.
name|class
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/jaxwsAndAegisSports"
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|proxyFactory
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|SportsService
name|service
init|=
operator|(
name|SportsService
operator|)
name|proxyFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"ffang"
argument_list|)
expr_stmt|;
name|String
name|ret
init|=
name|service
operator|.
name|getGeneric
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ret
argument_list|,
literal|"ffang"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDynamicClient
parameter_list|()
throws|throws
name|Exception
block|{
name|DynamicClientFactory
name|dcf
init|=
name|DynamicClientFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|dcf
operator|.
name|createClient
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/jaxwsAndAegisSports?wsdl"
argument_list|)
decl_stmt|;
name|Object
name|r
init|=
name|client
operator|.
name|invoke
argument_list|(
literal|"getAttributeBean"
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
name|Method
name|getAddrPlainString
init|=
name|r
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getAttrPlainString"
argument_list|)
decl_stmt|;
name|String
name|s
init|=
operator|(
name|String
operator|)
name|getAddrPlainString
operator|.
name|invoke
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"attrPlain"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

