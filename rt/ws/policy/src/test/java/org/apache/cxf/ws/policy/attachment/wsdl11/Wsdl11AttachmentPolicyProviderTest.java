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
name|ws
operator|.
name|policy
operator|.
name|attachment
operator|.
name|wsdl11
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|WSDLException
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
name|binding
operator|.
name|BindingFactoryManager
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
name|configuration
operator|.
name|ConfiguredBeanLocator
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
name|CastUtils
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
name|service
operator|.
name|model
operator|.
name|BindingMessageInfo
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|DestinationFactoryManager
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
name|ws
operator|.
name|policy
operator|.
name|AssertionBuilderRegistry
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
name|ws
operator|.
name|policy
operator|.
name|AssertionBuilderRegistryImpl
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
name|ws
operator|.
name|policy
operator|.
name|PolicyBuilder
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
name|ws
operator|.
name|policy
operator|.
name|PolicyBuilderImpl
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
name|ws
operator|.
name|policy
operator|.
name|PolicyEngine
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
name|ws
operator|.
name|policy
operator|.
name|PolicyException
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
name|ws
operator|.
name|policy
operator|.
name|PolicyRegistryImpl
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
name|ws
operator|.
name|policy
operator|.
name|builder
operator|.
name|primitive
operator|.
name|PrimitiveAssertionBuilder
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
name|wsdl
operator|.
name|WSDLManager
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
name|wsdl11
operator|.
name|WSDLManagerImpl
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
name|wsdl11
operator|.
name|WSDLServiceBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|PolicyComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|util
operator|.
name|PolicyComparator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|Assert
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|Wsdl11AttachmentPolicyProviderTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|NAMESPACE_URI
init|=
literal|"http://apache.org/cxf/calculator"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|OPERATION_NAME
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE_URI
argument_list|,
literal|"add"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|ServiceInfo
index|[]
name|services
decl_stmt|;
specifier|private
specifier|static
name|EndpointInfo
index|[]
name|endpoints
decl_stmt|;
specifier|private
name|Wsdl11AttachmentPolicyProvider
name|app
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|IMocksControl
name|control
init|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|oneTimeSetUp
parameter_list|()
throws|throws
name|Exception
block|{
name|IMocksControl
name|control
init|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|WSDLManager
name|manager
init|=
operator|new
name|WSDLManagerImpl
argument_list|()
decl_stmt|;
name|WSDLServiceBuilder
name|builder
init|=
operator|new
name|WSDLServiceBuilder
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|DestinationFactoryManager
name|dfm
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|dfm
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|BindingFactoryManager
name|bfm
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bfm
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bfm
operator|.
name|getBindingFactory
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|int
name|n
init|=
literal|19
decl_stmt|;
name|services
operator|=
operator|new
name|ServiceInfo
index|[
name|n
index|]
expr_stmt|;
name|endpoints
operator|=
operator|new
name|EndpointInfo
index|[
name|n
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|String
name|resourceName
init|=
literal|"/attachment/wsdl11/test"
operator|+
name|i
operator|+
literal|".wsdl"
decl_stmt|;
name|URL
name|url
init|=
name|Wsdl11AttachmentPolicyProviderTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
try|try
block|{
name|services
index|[
name|i
index|]
operator|=
name|builder
operator|.
name|buildServices
argument_list|(
name|manager
operator|.
name|getDefinition
argument_list|(
name|url
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Failed to build service from resource "
operator|+
name|resourceName
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|services
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|endpoints
index|[
name|i
index|]
operator|=
name|services
index|[
name|i
index|]
operator|.
name|getEndpoints
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoints
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|oneTimeTearDown
parameter_list|()
block|{
name|endpoints
operator|=
literal|null
expr_stmt|;
name|services
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|bus
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|AssertionBuilderRegistry
name|abr
init|=
operator|new
name|AssertionBuilderRegistryImpl
argument_list|()
decl_stmt|;
name|abr
operator|.
name|setIgnoreUnknownAssertions
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|PrimitiveAssertionBuilder
name|ab
init|=
operator|new
name|PrimitiveAssertionBuilder
argument_list|()
decl_stmt|;
name|abr
operator|.
name|registerBuilder
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/test/assertions"
argument_list|,
literal|"A"
argument_list|)
argument_list|,
name|ab
argument_list|)
expr_stmt|;
name|abr
operator|.
name|registerBuilder
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/test/assertions"
argument_list|,
literal|"B"
argument_list|)
argument_list|,
name|ab
argument_list|)
expr_stmt|;
name|abr
operator|.
name|registerBuilder
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/test/assertions"
argument_list|,
literal|"C"
argument_list|)
argument_list|,
name|ab
argument_list|)
expr_stmt|;
name|PolicyBuilderImpl
name|pb
init|=
operator|new
name|PolicyBuilderImpl
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|pb
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|pb
operator|.
name|setAssertionBuilderRegistry
argument_list|(
name|abr
argument_list|)
expr_stmt|;
name|app
operator|=
operator|new
name|Wsdl11AttachmentPolicyProvider
argument_list|()
expr_stmt|;
name|app
operator|.
name|setBuilder
argument_list|(
name|pb
argument_list|)
expr_stmt|;
name|app
operator|.
name|setRegistry
argument_list|(
operator|new
name|PolicyRegistryImpl
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testElementPolicies
parameter_list|()
throws|throws
name|WSDLException
block|{
name|Policy
name|p
decl_stmt|;
comment|// no extensions
name|p
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|services
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|==
literal|null
operator|||
name|p
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// extensions not of type Policy or PolicyReference
name|p
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|services
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|==
literal|null
operator|||
name|p
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// one extension of type Policy, without assertion builder
try|try
block|{
name|p
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|services
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PolicyException not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
comment|// one extension of type Policy
name|p
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|services
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|p
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|p
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// two extensions of type Policy
name|p
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|services
index|[
literal|4
index|]
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|p
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|p
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|app
operator|.
name|getElementPolicy
argument_list|(
name|ei
argument_list|)
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEffectiveServicePolicies
parameter_list|()
throws|throws
name|WSDLException
block|{
name|Policy
name|p
decl_stmt|;
name|Policy
name|ep
decl_stmt|;
comment|// no extensions
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|services
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ep
operator|==
literal|null
operator|||
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|services
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|==
literal|null
operator|||
name|p
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// extensions not of type Policy or PolicyReference
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|services
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ep
operator|==
literal|null
operator|||
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// one extension of type Policy, without assertion builder
try|try
block|{
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|services
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PolicyException not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
comment|// one extension of type Policy
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|services
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|p
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|services
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PolicyComparator
operator|.
name|compare
argument_list|(
name|p
argument_list|,
name|ep
argument_list|)
argument_list|)
expr_stmt|;
comment|// two extensions of type Policy
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|services
index|[
literal|4
index|]
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|p
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|services
index|[
literal|4
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PolicyComparator
operator|.
name|compare
argument_list|(
name|p
argument_list|,
name|ep
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEffectiveEndpointPolicies
parameter_list|()
block|{
name|Policy
name|ep
decl_stmt|;
name|Policy
name|p
decl_stmt|;
comment|// port has no extensions
comment|// porttype has no extensions
comment|// binding has no extensions
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|endpoints
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ep
operator|==
literal|null
operator|||
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// port has one extension of type Policy
comment|// binding has no extensions
comment|// porttype has no extensions
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|endpoints
index|[
literal|5
index|]
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|p
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|endpoints
index|[
literal|5
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PolicyComparator
operator|.
name|compare
argument_list|(
name|p
argument_list|,
name|ep
argument_list|)
argument_list|)
expr_stmt|;
comment|// port has no extensions
comment|// binding has one extension of type Policy
comment|// porttype has no extensions
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|endpoints
index|[
literal|6
index|]
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|p
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|endpoints
index|[
literal|6
index|]
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PolicyComparator
operator|.
name|compare
argument_list|(
name|p
argument_list|,
name|ep
argument_list|)
argument_list|)
expr_stmt|;
comment|// port has no extensions
comment|// binding has no extensions
comment|// porttype has one extension of type Policy
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|endpoints
index|[
literal|7
index|]
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|p
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|endpoints
index|[
literal|7
index|]
operator|.
name|getInterface
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PolicyComparator
operator|.
name|compare
argument_list|(
name|p
argument_list|,
name|ep
argument_list|)
argument_list|)
expr_stmt|;
comment|// port has one extension of type Policy
comment|// porttype has one extension of type Policy
comment|// binding has one extension of type Policy
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|endpoints
index|[
literal|8
index|]
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|3
argument_list|)
expr_stmt|;
comment|// port has no extensions
comment|// binding has no extensions
comment|// porttype has no extension elements but one extension attribute of type PolicyURIs
comment|// consisting of two references (one local, one external)
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|endpoints
index|[
literal|18
index|]
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEffectiveBindingOperationPolicies
parameter_list|()
block|{
name|Policy
name|ep
decl_stmt|;
comment|// operation has no extensions
comment|// binding operation has no extensions
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|getBindingOperationInfo
argument_list|(
name|endpoints
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ep
operator|==
literal|null
operator|||
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// operation has no extensions
comment|// binding operation has one extension of type Policy
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|getBindingOperationInfo
argument_list|(
name|endpoints
index|[
literal|9
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// operation has one extension of type Policy
comment|// binding operation has no extensions
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|getBindingOperationInfo
argument_list|(
name|endpoints
index|[
literal|10
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// operation has one extension of type Policy
comment|// binding operation one extension of type Policy
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|getBindingOperationInfo
argument_list|(
name|endpoints
index|[
literal|11
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEffectiveMessagePolicies
parameter_list|()
block|{
name|Policy
name|ep
decl_stmt|;
comment|// binding operation message has no extensions
comment|// operation message has no extensions
comment|// message has no extensions
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|getBindingMessageInfo
argument_list|(
name|endpoints
index|[
literal|0
index|]
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ep
operator|==
literal|null
operator|||
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// binding operation message has one extension of type Policy
comment|// operation message has no extensions
comment|// message has no extensions
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|getBindingMessageInfo
argument_list|(
name|endpoints
index|[
literal|12
index|]
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// binding operation message has no extensions
comment|// operation message has one extension of type Policy
comment|// message has no extensions
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|getBindingMessageInfo
argument_list|(
name|endpoints
index|[
literal|13
index|]
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// binding operation message has no extensions
comment|// operation message has no extensions
comment|// message has one extension of type Policy
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|getBindingMessageInfo
argument_list|(
name|endpoints
index|[
literal|14
index|]
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// binding operation message has one extension of type Policy
comment|// operation message has one extension of type Policy
comment|// message has one extension of type Policy
name|ep
operator|=
name|app
operator|.
name|getEffectivePolicy
argument_list|(
name|getBindingMessageInfo
argument_list|(
name|endpoints
index|[
literal|15
index|]
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ep
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveLocal
parameter_list|()
block|{
name|Policy
name|ep
decl_stmt|;
comment|// service has one extension of type PolicyReference, reference can be resolved locally
name|ep
operator|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|services
index|[
literal|16
index|]
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|ep
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// port has one extension of type PolicyReference, reference cannot be resolved locally
try|try
block|{
name|app
operator|.
name|getElementPolicy
argument_list|(
name|endpoints
index|[
literal|16
index|]
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PolicyException not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveExternal
parameter_list|()
block|{
comment|// service has one extension of type PolicyReference, reference is external
name|Policy
name|p
init|=
name|app
operator|.
name|getElementPolicy
argument_list|(
name|services
index|[
literal|17
index|]
argument_list|)
decl_stmt|;
name|verifyAssertionsOnly
argument_list|(
name|p
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// port has one extension of type PolicyReference, reference cannot be resolved because
comment|// referenced document does not contain policy with the required if
try|try
block|{
name|app
operator|.
name|getElementPolicy
argument_list|(
name|endpoints
index|[
literal|17
index|]
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PolicyException not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
comment|// binding has one extension of type PolicyReference, reference cannot be resolved because
comment|// referenced document cannot be found
try|try
block|{
name|app
operator|.
name|getElementPolicy
argument_list|(
name|endpoints
index|[
literal|17
index|]
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PolicyException not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|private
name|void
name|verifyAssertionsOnly
parameter_list|(
name|Policy
name|p
parameter_list|,
name|int
name|expectedAssertions
parameter_list|)
block|{
name|List
argument_list|<
name|PolicyComponent
argument_list|>
name|pcs
decl_stmt|;
name|pcs
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|p
operator|.
name|getAssertions
argument_list|()
argument_list|,
name|PolicyComponent
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedAssertions
argument_list|,
name|pcs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedAssertions
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|Constants
operator|.
name|TYPE_ASSERTION
argument_list|,
name|pcs
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|BindingOperationInfo
name|getBindingOperationInfo
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
block|{
return|return
name|ei
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperation
argument_list|(
name|OPERATION_NAME
argument_list|)
return|;
block|}
specifier|private
name|BindingMessageInfo
name|getBindingMessageInfo
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|boolean
name|in
parameter_list|)
block|{
return|return
name|in
condition|?
name|ei
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperation
argument_list|(
name|OPERATION_NAME
argument_list|)
operator|.
name|getInput
argument_list|()
else|:
name|ei
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperation
argument_list|(
name|OPERATION_NAME
argument_list|)
operator|.
name|getOutput
argument_list|()
return|;
block|}
block|}
end_class

end_unit

