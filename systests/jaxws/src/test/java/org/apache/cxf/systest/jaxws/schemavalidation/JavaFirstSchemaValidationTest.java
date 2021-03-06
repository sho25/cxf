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
operator|.
name|schemavalidation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Collections
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
name|annotations
operator|.
name|SchemaValidation
operator|.
name|SchemaValidationType
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
name|Server
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
name|Feature
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
name|validation
operator|.
name|DefaultSchemaValidationTypeProvider
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
name|validation
operator|.
name|SchemaValidationFeature
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|message
operator|.
name|Message
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
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|JavaFirstSchemaValidationTest
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getNewPortNumber
argument_list|(
name|JavaFirstSchemaValidationTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|PORT2
init|=
name|TestUtil
operator|.
name|getNewPortNumber
argument_list|(
name|JavaFirstSchemaValidationTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|PORT_UNUSED
init|=
name|TestUtil
operator|.
name|getNewPortNumber
argument_list|(
name|JavaFirstSchemaValidationTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|List
argument_list|<
name|Server
argument_list|>
name|serverList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|PersonServiceAnnotated
name|annotatedClient
decl_stmt|;
specifier|private
specifier|static
name|PersonService
name|client
decl_stmt|;
specifier|private
specifier|static
name|PersonServiceRPC
name|rpcClient
decl_stmt|;
specifier|private
specifier|static
name|PersonServiceWithRequestResponseAnns
name|annotatedNonValidatingClient
decl_stmt|;
specifier|private
specifier|static
name|PersonServiceWithRequestResponseAnns
name|disconnectedClient
decl_stmt|;
specifier|private
specifier|static
name|PersonServiceWithRequestResponseAnns
name|noValidationServerClient
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
name|createServer
argument_list|(
name|PORT
argument_list|,
name|PersonService
operator|.
name|class
argument_list|,
operator|new
name|PersonServiceImpl
argument_list|()
argument_list|,
literal|null
argument_list|,
name|createSchemaValidationFeature
argument_list|()
argument_list|)
expr_stmt|;
name|createServer
argument_list|(
name|PORT2
argument_list|,
name|PersonServiceWithRequestResponseAnns
operator|.
name|class
argument_list|,
operator|new
name|PersonServiceWithRequestResponseAnnsImpl
argument_list|()
argument_list|,
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
name|createNoSchemaValidationFeature
argument_list|()
argument_list|)
expr_stmt|;
name|createServer
argument_list|(
name|PORT
argument_list|,
name|PersonServiceAnnotated
operator|.
name|class
argument_list|,
operator|new
name|PersonServiceAnnotatedImpl
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|createServer
argument_list|(
name|PORT
argument_list|,
name|PersonServiceRPC
operator|.
name|class
argument_list|,
operator|new
name|PersonServiceRPCImpl
argument_list|()
argument_list|,
literal|null
argument_list|,
name|createSchemaValidationFeature
argument_list|()
argument_list|)
expr_stmt|;
name|createServer
argument_list|(
name|PORT
argument_list|,
name|PersonServiceWithRequestResponseAnns
operator|.
name|class
argument_list|,
operator|new
name|PersonServiceWithRequestResponseAnnsImpl
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|annotatedClient
operator|=
name|createClient
argument_list|(
name|PORT
argument_list|,
name|PersonServiceAnnotated
operator|.
name|class
argument_list|,
name|SchemaValidationType
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|annotatedNonValidatingClient
operator|=
name|createClient
argument_list|(
name|PORT
argument_list|,
name|PersonServiceWithRequestResponseAnns
operator|.
name|class
argument_list|,
name|SchemaValidationType
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|client
operator|=
name|createClient
argument_list|(
name|PORT
argument_list|,
name|PersonService
operator|.
name|class
argument_list|,
name|SchemaValidationType
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|disconnectedClient
operator|=
name|createClient
argument_list|(
name|PORT_UNUSED
argument_list|,
name|PersonServiceWithRequestResponseAnns
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|rpcClient
operator|=
name|createClient
argument_list|(
name|PORT
argument_list|,
name|PersonServiceRPC
operator|.
name|class
argument_list|,
name|SchemaValidationType
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|noValidationServerClient
operator|=
name|createClient
argument_list|(
name|PORT2
argument_list|,
name|PersonServiceWithRequestResponseAnns
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|SchemaValidationFeature
name|createSchemaValidationFeature
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|SchemaValidationType
argument_list|>
name|operationMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|operationMap
operator|.
name|put
argument_list|(
literal|"saveInheritEndpoint"
argument_list|,
name|SchemaValidationType
operator|.
name|BOTH
argument_list|)
expr_stmt|;
name|operationMap
operator|.
name|put
argument_list|(
literal|"saveNoValidation"
argument_list|,
name|SchemaValidationType
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|operationMap
operator|.
name|put
argument_list|(
literal|"saveValidateIn"
argument_list|,
name|SchemaValidationType
operator|.
name|IN
argument_list|)
expr_stmt|;
name|operationMap
operator|.
name|put
argument_list|(
literal|"saveValidateOut"
argument_list|,
name|SchemaValidationType
operator|.
name|OUT
argument_list|)
expr_stmt|;
name|DefaultSchemaValidationTypeProvider
name|provider
init|=
operator|new
name|DefaultSchemaValidationTypeProvider
argument_list|(
name|operationMap
argument_list|)
decl_stmt|;
return|return
operator|new
name|SchemaValidationFeature
argument_list|(
name|provider
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|SchemaValidationFeature
name|createNoSchemaValidationFeature
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|SchemaValidationType
argument_list|>
name|operationMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|operationMap
operator|.
name|put
argument_list|(
literal|"*"
argument_list|,
name|SchemaValidationType
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|DefaultSchemaValidationTypeProvider
name|provider
init|=
operator|new
name|DefaultSchemaValidationTypeProvider
argument_list|(
name|operationMap
argument_list|)
decl_stmt|;
return|return
operator|new
name|SchemaValidationFeature
argument_list|(
name|provider
argument_list|)
return|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|Server
name|server
range|:
name|serverList
control|)
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
specifier|static
name|String
name|getAddress
parameter_list|(
name|String
name|port
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|sei
parameter_list|)
block|{
return|return
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/"
operator|+
name|sei
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRPCLit
parameter_list|()
throws|throws
name|Exception
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
name|person
operator|.
name|setFirstName
argument_list|(
literal|"Foo"
argument_list|)
expr_stmt|;
name|person
operator|.
name|setLastName
argument_list|(
literal|"Bar"
argument_list|)
expr_stmt|;
comment|//this should work
name|rpcClient
operator|.
name|saveValidateOut
argument_list|(
name|person
argument_list|)
expr_stmt|;
try|try
block|{
name|person
operator|.
name|setFirstName
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|rpcClient
operator|.
name|saveValidateOut
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Marshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"lastName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// so this is the default, we are inheriting from the service level SchemaValidation annotation
comment|// which is set to BOTH
annotation|@
name|Test
specifier|public
name|void
name|testEndpointSchemaValidationAnnotated
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
try|try
block|{
name|annotatedClient
operator|.
name|saveInheritEndpoint
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unmarshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|person
operator|.
name|setFirstName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|annotatedClient
operator|.
name|saveInheritEndpoint
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unmarshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setLastName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|annotatedClient
operator|.
name|saveInheritEndpoint
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSaveValidateInAnnotated
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
try|try
block|{
name|annotatedClient
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unmarshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|person
operator|.
name|setFirstName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|annotatedClient
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unmarshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setLastName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|annotatedClient
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
comment|// no validation at all is required
annotation|@
name|Test
specifier|public
name|void
name|testSaveNoValidationAnnotated
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
name|annotatedClient
operator|.
name|saveNoValidation
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|person
operator|.
name|setFirstName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|annotatedClient
operator|.
name|saveNoValidation
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|person
operator|.
name|setLastName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|annotatedClient
operator|.
name|saveNoValidation
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRequestValidationWithClientValidationDisabled
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
try|try
block|{
name|annotatedNonValidatingClient
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
comment|// has to be server side exception, as all validation is disabled on client
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Marshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setFirstName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
try|try
block|{
name|annotatedNonValidatingClient
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
comment|// has to be server side exception, as all validation is disabled on client
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Marshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setLastName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|annotatedNonValidatingClient
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResponseValidationWithClientValidationDisabled
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
try|try
block|{
name|person
operator|.
name|setFirstName
argument_list|(
literal|"InvalidResponse"
argument_list|)
expr_stmt|;
name|person
operator|.
name|setLastName
argument_list|(
literal|"WhoCares"
argument_list|)
expr_stmt|;
name|annotatedNonValidatingClient
operator|.
name|saveValidateOut
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
comment|// has to be server side exception, as all validation is disabled on client
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Marshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEndpointSchemaValidationProvider
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
try|try
block|{
name|client
operator|.
name|saveInheritEndpoint
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unmarshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|person
operator|.
name|setFirstName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|client
operator|.
name|saveInheritEndpoint
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unmarshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setLastName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|client
operator|.
name|saveInheritEndpoint
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSaveValidateInProvider
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
try|try
block|{
name|client
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unmarshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|person
operator|.
name|setFirstName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|client
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unmarshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setLastName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|client
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSaveNoValidationProvider
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
name|client
operator|.
name|saveNoValidation
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|person
operator|.
name|setFirstName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|client
operator|.
name|saveNoValidation
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|person
operator|.
name|setLastName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|client
operator|.
name|saveNoValidation
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRequestClientValidation
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
try|try
block|{
name|disconnectedClient
operator|.
name|saveValidateOut
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Marshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setFirstName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
try|try
block|{
name|disconnectedClient
operator|.
name|saveValidateOut
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Marshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setLastName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
comment|// this confirms that we passed client validation as we then got the connectivity error
try|try
block|{
name|disconnectedClient
operator|.
name|saveValidateOut
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
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
literal|"Could not send Message"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResponseClientValidation
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
try|try
block|{
name|noValidationServerClient
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unmarshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setFirstName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
try|try
block|{
name|noValidationServerClient
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unmarshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setLastName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|noValidationServerClient
operator|.
name|saveValidateIn
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSaveValidationOutProvider
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
try|try
block|{
name|client
operator|.
name|saveValidateOut
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
comment|// verify its server side outgoing
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Marshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setFirstName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
try|try
block|{
name|client
operator|.
name|saveValidateOut
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|sfe
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|sfe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Marshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|person
operator|.
name|setLastName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// empty string is valid
name|client
operator|.
name|saveValidateOut
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|createClient
parameter_list|(
name|String
name|port
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|serviceClass
parameter_list|,
name|SchemaValidationType
name|type
parameter_list|,
name|Feature
modifier|...
name|features
parameter_list|)
block|{
name|JaxWsProxyFactoryBean
name|clientFactory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|clientFactory
operator|.
name|setServiceClass
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
name|clientFactory
operator|.
name|setAddress
argument_list|(
name|getAddress
argument_list|(
name|port
argument_list|,
name|serviceClass
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|features
operator|!=
literal|null
condition|)
block|{
name|Collections
operator|.
name|addAll
argument_list|(
name|clientFactory
operator|.
name|getFeatures
argument_list|()
argument_list|,
name|features
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
name|newClient
init|=
operator|(
name|T
operator|)
name|clientFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|Client
name|proxy
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|newClient
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|proxy
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
name|HTTPConduit
name|conduit
init|=
operator|(
name|HTTPConduit
operator|)
name|proxy
operator|.
name|getConduit
argument_list|()
decl_stmt|;
comment|// give me longer debug times
name|HTTPClientPolicy
name|clientPolicy
init|=
operator|new
name|HTTPClientPolicy
argument_list|()
decl_stmt|;
name|clientPolicy
operator|.
name|setConnectionTimeout
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
name|clientPolicy
operator|.
name|setReceiveTimeout
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|setClient
argument_list|(
name|clientPolicy
argument_list|)
expr_stmt|;
return|return
name|newClient
return|;
block|}
specifier|public
specifier|static
name|Server
name|createServer
parameter_list|(
name|String
name|port
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|serviceInterface
parameter_list|,
name|Object
name|serviceImpl
parameter_list|,
name|SchemaValidationType
name|type
parameter_list|,
name|Feature
modifier|...
name|features
parameter_list|)
throws|throws
name|IOException
block|{
name|JaxWsServerFactoryBean
name|svrFactory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFactory
operator|.
name|setServiceClass
argument_list|(
name|serviceImpl
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|features
operator|!=
literal|null
condition|)
block|{
name|Collections
operator|.
name|addAll
argument_list|(
name|svrFactory
operator|.
name|getFeatures
argument_list|()
argument_list|,
name|features
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
name|svrFactory
operator|.
name|setAddress
argument_list|(
name|getAddress
argument_list|(
name|port
argument_list|,
name|serviceInterface
argument_list|)
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceBean
argument_list|(
name|serviceImpl
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|svrFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|serverList
operator|.
name|add
argument_list|(
name|server
argument_list|)
expr_stmt|;
return|return
name|server
return|;
block|}
block|}
end_class

end_unit

