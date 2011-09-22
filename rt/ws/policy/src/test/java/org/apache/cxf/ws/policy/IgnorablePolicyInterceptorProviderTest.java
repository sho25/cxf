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
name|Collections
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
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|message
operator|.
name|MessageImpl
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
name|PrimitiveAssertion
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|IgnorablePolicyInterceptorProviderTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|ONEWAY_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://tempuri.org/policy"
argument_list|,
literal|"OneWay"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|DUPLEX_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://tempuri.org/policy"
argument_list|,
literal|"Duplex"
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testProvider
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
literal|"/org/apache/cxf/ws/policy/ignorable-policy.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|PolicyInterceptorProviderRegistry
name|pipreg
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
name|pipreg
argument_list|)
expr_stmt|;
name|PolicyInterceptorProvider
name|pip
init|=
name|pipreg
operator|.
name|get
argument_list|(
name|ONEWAY_QNAME
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pip
argument_list|)
expr_stmt|;
name|PolicyInterceptorProvider
name|pip2
init|=
name|pipreg
operator|.
name|get
argument_list|(
name|DUPLEX_QNAME
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pip
argument_list|,
name|pip2
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
annotation|@
name|Test
specifier|public
name|void
name|testInterceptorAssertion
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
literal|"/org/apache/cxf/ws/policy/ignorable-policy.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|PolicyInterceptorProviderRegistry
name|pipreg
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
name|pipreg
argument_list|)
expr_stmt|;
name|PolicyInterceptorProvider
name|pip
init|=
name|pipreg
operator|.
name|get
argument_list|(
name|ONEWAY_QNAME
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pip
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|Message
argument_list|>
argument_list|>
name|list
decl_stmt|;
name|list
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|pip
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertion
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|list
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|pip
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertion
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|list
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|pip
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertion
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|list
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|pip
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAssertion
argument_list|(
name|list
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
name|verifyAssertion
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|Message
argument_list|>
argument_list|>
name|list
parameter_list|)
block|{
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|AssertionInfoMap
name|aim
init|=
name|createTestAssertions
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|,
name|aim
argument_list|)
expr_stmt|;
try|try
block|{
name|aim
operator|.
name|check
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"not yet asserted"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyException
name|e
parameter_list|)
block|{
comment|// ok
block|}
for|for
control|(
name|Interceptor
argument_list|<
name|Message
argument_list|>
name|p
range|:
name|list
control|)
block|{
name|p
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|aim
operator|.
name|check
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTwoBuses
parameter_list|()
block|{
name|ClassPathXmlApplicationContext
name|context
init|=
literal|null
decl_stmt|;
name|Bus
name|cxf1
init|=
literal|null
decl_stmt|;
name|Bus
name|cxf2
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"/org/apache/cxf/ws/policy/ignorable-policy2.xml"
argument_list|)
expr_stmt|;
name|cxf1
operator|=
operator|(
name|Bus
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"cxf1"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cxf1
argument_list|)
expr_stmt|;
name|cxf2
operator|=
operator|(
name|Bus
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"cxf2"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cxf2
argument_list|)
expr_stmt|;
name|PolicyInterceptorProviderRegistry
name|pipreg1
init|=
name|cxf1
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
name|pipreg1
argument_list|)
expr_stmt|;
name|PolicyInterceptorProviderRegistry
name|pipreg2
init|=
name|cxf2
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
name|pipreg2
argument_list|)
expr_stmt|;
name|PolicyInterceptorProvider
name|pip1
init|=
name|pipreg1
operator|.
name|get
argument_list|(
name|ONEWAY_QNAME
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pip1
argument_list|)
expr_stmt|;
name|PolicyInterceptorProvider
name|pip2
init|=
name|pipreg2
operator|.
name|get
argument_list|(
name|ONEWAY_QNAME
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pip1
argument_list|,
name|pip2
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
literal|null
operator|!=
name|cxf1
condition|)
block|{
name|cxf1
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
name|AssertionInfoMap
name|createTestAssertions
parameter_list|()
block|{
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|PolicyAssertion
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Assertion
name|a
init|=
operator|new
name|PrimitiveAssertion
argument_list|(
name|ONEWAY_QNAME
argument_list|)
decl_stmt|;
name|Assertion
name|b
init|=
operator|new
name|PrimitiveAssertion
argument_list|(
name|DUPLEX_QNAME
argument_list|)
decl_stmt|;
name|AssertionInfo
name|ai
init|=
operator|new
name|AssertionInfo
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|AssertionInfo
name|bi
init|=
operator|new
name|AssertionInfo
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|aim
operator|.
name|put
argument_list|(
name|ONEWAY_QNAME
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|ai
argument_list|)
argument_list|)
expr_stmt|;
name|aim
operator|.
name|put
argument_list|(
name|DUPLEX_QNAME
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|bi
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|aim
return|;
block|}
block|}
end_class

end_unit

