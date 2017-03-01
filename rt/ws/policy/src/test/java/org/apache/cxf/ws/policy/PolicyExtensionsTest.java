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
package|;
end_package

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
name|Set
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
name|BusFactory
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
name|interceptor
operator|.
name|Interceptor
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
name|ws
operator|.
name|policy
operator|.
name|attachment
operator|.
name|ServiceModelPolicyProvider
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
name|attachment
operator|.
name|external
operator|.
name|DomainExpressionBuilder
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
name|attachment
operator|.
name|external
operator|.
name|DomainExpressionBuilderRegistry
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
name|attachment
operator|.
name|external
operator|.
name|ExternalAttachmentProvider
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
name|attachment
operator|.
name|wsdl11
operator|.
name|Wsdl11AttachmentPolicyProvider
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
name|Assertion
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
name|AssertionBuilderFactory
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
name|builders
operator|.
name|AssertionBuilder
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
name|Test
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|PolicyExtensionsTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|KNOWN
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/test/policy"
argument_list|,
literal|"known"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|KNOWN_DOMAIN_EXPR_TYPE
init|=
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"EndpointReference"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|UNKNOWN
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/test/policy"
argument_list|,
literal|"unknown"
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testCXF4258
parameter_list|()
block|{
name|Bus
name|bus
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"/org/apache/cxf/ws/policy/disable-policy-bus.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|AssertionBuilderRegistry
name|abr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|AssertionBuilderRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|abr
argument_list|)
expr_stmt|;
name|PolicyEngine
name|e
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertNoPolicyInterceptors
argument_list|(
name|bus
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|assertNoPolicyInterceptors
argument_list|(
name|bus
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|assertNoPolicyInterceptors
argument_list|(
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|assertNoPolicyInterceptors
argument_list|(
name|bus
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|assertNoPolicyInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|ints
parameter_list|)
block|{
for|for
control|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|m
range|:
name|ints
control|)
block|{
name|assertFalse
argument_list|(
literal|"Found "
operator|+
name|m
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|m
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"org.apache.cxf.ws.policy"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtensions
parameter_list|()
block|{
name|Bus
name|bus
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"/org/apache/cxf/ws/policy/policy-bus.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|AssertionBuilderRegistry
name|abr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|AssertionBuilderRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|abr
argument_list|)
expr_stmt|;
name|AssertionBuilder
argument_list|<
name|?
argument_list|>
name|ab
init|=
name|abr
operator|.
name|getBuilder
argument_list|(
name|KNOWN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ab
argument_list|)
expr_stmt|;
name|ab
operator|=
name|abr
operator|.
name|getBuilder
argument_list|(
name|UNKNOWN
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ab
argument_list|)
expr_stmt|;
name|PolicyInterceptorProviderRegistry
name|pipr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyInterceptorProviderRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pipr
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|PolicyInterceptorProvider
argument_list|>
name|pips
init|=
name|pipr
operator|.
name|get
argument_list|(
name|KNOWN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pips
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pips
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|pips
operator|=
name|pipr
operator|.
name|get
argument_list|(
name|UNKNOWN
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|pips
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pips
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|DomainExpressionBuilderRegistry
name|debr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DomainExpressionBuilderRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|debr
argument_list|)
expr_stmt|;
name|DomainExpressionBuilder
name|deb
init|=
name|debr
operator|.
name|get
argument_list|(
name|KNOWN_DOMAIN_EXPR_TYPE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|deb
argument_list|)
expr_stmt|;
name|deb
operator|=
name|debr
operator|.
name|get
argument_list|(
name|UNKNOWN
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|deb
argument_list|)
expr_stmt|;
name|PolicyEngine
name|pe
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pe
argument_list|)
expr_stmt|;
name|PolicyEngineImpl
name|engine
init|=
operator|(
name|PolicyEngineImpl
operator|)
name|pe
decl_stmt|;
name|assertNotNull
argument_list|(
name|engine
operator|.
name|getPolicyProviders
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|engine
operator|.
name|getRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|PolicyProvider
argument_list|>
name|pps
init|=
name|engine
operator|.
name|getPolicyProviders
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|pps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|wsdlProvider
init|=
literal|false
decl_stmt|;
name|boolean
name|externalProvider
init|=
literal|false
decl_stmt|;
name|boolean
name|serviceProvider
init|=
literal|false
decl_stmt|;
for|for
control|(
name|PolicyProvider
name|pp
range|:
name|pps
control|)
block|{
if|if
condition|(
name|pp
operator|instanceof
name|Wsdl11AttachmentPolicyProvider
condition|)
block|{
name|wsdlProvider
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pp
operator|instanceof
name|ExternalAttachmentProvider
condition|)
block|{
name|externalProvider
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pp
operator|instanceof
name|ServiceModelPolicyProvider
condition|)
block|{
name|serviceProvider
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
name|wsdlProvider
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|externalProvider
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|serviceProvider
argument_list|)
expr_stmt|;
name|PolicyBuilder
name|builder
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|TestAssertionBuilder
implements|implements
name|AssertionBuilder
argument_list|<
name|Element
argument_list|>
block|{
name|QName
name|knownElements
index|[]
init|=
block|{
name|KNOWN
block|}
decl_stmt|;
specifier|public
name|TestAssertionBuilder
parameter_list|()
block|{         }
specifier|public
name|Assertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|,
name|AssertionBuilderFactory
name|factory
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QName
index|[]
name|getKnownElements
parameter_list|()
block|{
return|return
name|knownElements
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|TestPolicyInterceptorProvider
extends|extends
name|AbstractPolicyInterceptorProvider
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|4588883556748035959L
decl_stmt|;
specifier|public
name|TestPolicyInterceptorProvider
parameter_list|()
block|{
name|super
argument_list|(
name|KNOWN
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

