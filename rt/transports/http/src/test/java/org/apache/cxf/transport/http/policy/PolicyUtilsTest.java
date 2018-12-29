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
name|transport
operator|.
name|http
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
name|Collections
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
name|message
operator|.
name|Exchange
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
name|policy
operator|.
name|PolicyDataEngine
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
name|policy
operator|.
name|impl
operator|.
name|ClientPolicyCalculator
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
name|policy
operator|.
name|impl
operator|.
name|ServerPolicyCalculator
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
name|HTTPServerPolicy
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
name|AssertionInfo
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
name|AssertionInfoMap
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
name|PolicyAssertion
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
name|PolicyDataEngineImpl
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
name|jaxb
operator|.
name|JaxbAssertion
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
name|Before
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|PolicyUtilsTest
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAssertClientPolicyNoop
parameter_list|()
block|{
name|testAssertPolicyNoop
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAssertServerPolicyNoop
parameter_list|()
block|{
name|testAssertPolicyNoop
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|void
name|testAssertPolicyNoop
parameter_list|(
name|boolean
name|isRequestor
parameter_list|)
block|{
name|PolicyDataEngine
name|pde
init|=
operator|new
name|PolicyDataEngineImpl
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|pde
operator|.
name|assertMessage
argument_list|(
name|message
argument_list|,
literal|null
argument_list|,
operator|new
name|ClientPolicyCalculator
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|as
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|as
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|aim
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
if|if
condition|(
name|isRequestor
condition|)
block|{
name|pde
operator|.
name|assertMessage
argument_list|(
name|message
argument_list|,
literal|null
argument_list|,
operator|new
name|ClientPolicyCalculator
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pde
operator|.
name|assertMessage
argument_list|(
name|message
argument_list|,
literal|null
argument_list|,
operator|new
name|ServerPolicyCalculator
argument_list|()
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
name|Test
specifier|public
name|void
name|testAssertClientPolicyOutbound
parameter_list|()
block|{
name|testAssertClientPolicy
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAssertClientPolicyInbound
parameter_list|()
block|{
name|testAssertClientPolicy
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AssertionInfo
name|getClientPolicyAssertionInfo
parameter_list|(
name|HTTPClientPolicy
name|policy
parameter_list|)
block|{
name|JaxbAssertion
argument_list|<
name|HTTPClientPolicy
argument_list|>
name|assertion
init|=
operator|new
name|JaxbAssertion
argument_list|<>
argument_list|(
operator|new
name|ClientPolicyCalculator
argument_list|()
operator|.
name|getDataClassName
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertion
operator|.
name|setData
argument_list|(
name|policy
argument_list|)
expr_stmt|;
return|return
operator|new
name|AssertionInfo
argument_list|(
name|assertion
argument_list|)
return|;
block|}
name|void
name|testAssertClientPolicy
parameter_list|(
name|boolean
name|outbound
parameter_list|)
block|{
name|Message
name|message
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|HTTPClientPolicy
name|ep
init|=
operator|new
name|HTTPClientPolicy
argument_list|()
decl_stmt|;
name|HTTPClientPolicy
name|cmp
init|=
operator|new
name|HTTPClientPolicy
argument_list|()
decl_stmt|;
name|cmp
operator|.
name|setConnectionTimeout
argument_list|(
literal|60000L
argument_list|)
expr_stmt|;
name|HTTPClientPolicy
name|icmp
init|=
operator|new
name|HTTPClientPolicy
argument_list|()
decl_stmt|;
name|icmp
operator|.
name|setAllowChunking
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|AssertionInfo
name|eai
init|=
name|getClientPolicyAssertionInfo
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|AssertionInfo
name|cmai
init|=
name|getClientPolicyAssertionInfo
argument_list|(
name|cmp
argument_list|)
decl_stmt|;
name|AssertionInfo
name|icmai
init|=
name|getClientPolicyAssertionInfo
argument_list|(
name|icmp
argument_list|)
decl_stmt|;
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
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|eai
argument_list|)
expr_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|cmai
argument_list|)
expr_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|icmai
argument_list|)
expr_stmt|;
name|aim
operator|.
name|put
argument_list|(
operator|new
name|ClientPolicyCalculator
argument_list|()
operator|.
name|getDataClassName
argument_list|()
argument_list|,
name|ais
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|aim
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ex
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ex
operator|.
name|getOutMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|outbound
condition|?
name|message
else|:
literal|null
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|outbound
condition|)
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|ex
operator|.
name|getOutFaultMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
block|}
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|PolicyDataEngine
name|pde
init|=
operator|new
name|PolicyDataEngineImpl
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|pde
operator|.
name|assertMessage
argument_list|(
name|message
argument_list|,
name|ep
argument_list|,
operator|new
name|ClientPolicyCalculator
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|eai
operator|.
name|isAsserted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cmai
operator|.
name|isAsserted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|icmai
operator|.
name|isAsserted
argument_list|()
argument_list|)
expr_stmt|;
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
name|testAssertServerPolicyOutbound
parameter_list|()
block|{
name|testAssertServerPolicy
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAssertServerPolicyInbound
parameter_list|()
block|{
name|testAssertServerPolicy
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AssertionInfo
name|getServerPolicyAssertionInfo
parameter_list|(
name|HTTPServerPolicy
name|policy
parameter_list|)
block|{
name|JaxbAssertion
argument_list|<
name|HTTPServerPolicy
argument_list|>
name|assertion
init|=
operator|new
name|JaxbAssertion
argument_list|<>
argument_list|(
operator|new
name|ServerPolicyCalculator
argument_list|()
operator|.
name|getDataClassName
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertion
operator|.
name|setData
argument_list|(
name|policy
argument_list|)
expr_stmt|;
return|return
operator|new
name|AssertionInfo
argument_list|(
name|assertion
argument_list|)
return|;
block|}
name|void
name|testAssertServerPolicy
parameter_list|(
name|boolean
name|outbound
parameter_list|)
block|{
name|Message
name|message
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|HTTPServerPolicy
name|ep
init|=
operator|new
name|HTTPServerPolicy
argument_list|()
decl_stmt|;
name|HTTPServerPolicy
name|mp
init|=
operator|new
name|HTTPServerPolicy
argument_list|()
decl_stmt|;
name|HTTPServerPolicy
name|cmp
init|=
operator|new
name|HTTPServerPolicy
argument_list|()
decl_stmt|;
name|cmp
operator|.
name|setReceiveTimeout
argument_list|(
literal|60000L
argument_list|)
expr_stmt|;
name|HTTPServerPolicy
name|icmp
init|=
operator|new
name|HTTPServerPolicy
argument_list|()
decl_stmt|;
name|icmp
operator|.
name|setSuppressClientSendErrors
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|AssertionInfo
name|eai
init|=
name|getServerPolicyAssertionInfo
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|AssertionInfo
name|mai
init|=
name|getServerPolicyAssertionInfo
argument_list|(
name|mp
argument_list|)
decl_stmt|;
name|AssertionInfo
name|cmai
init|=
name|getServerPolicyAssertionInfo
argument_list|(
name|cmp
argument_list|)
decl_stmt|;
name|AssertionInfo
name|icmai
init|=
name|getServerPolicyAssertionInfo
argument_list|(
name|icmp
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|eai
argument_list|)
expr_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|mai
argument_list|)
expr_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|cmai
argument_list|)
expr_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|icmai
argument_list|)
expr_stmt|;
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
name|aim
operator|.
name|put
argument_list|(
operator|new
name|ServerPolicyCalculator
argument_list|()
operator|.
name|getDataClassName
argument_list|()
argument_list|,
name|ais
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|aim
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|Exchange
name|ex
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ex
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ex
operator|.
name|getOutMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|outbound
condition|?
name|message
else|:
literal|null
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|outbound
condition|)
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|ex
operator|.
name|getOutFaultMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
block|}
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
operator|new
name|PolicyDataEngineImpl
argument_list|(
literal|null
argument_list|)
operator|.
name|assertMessage
argument_list|(
name|message
argument_list|,
name|ep
argument_list|,
operator|new
name|ServerPolicyCalculator
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|eai
operator|.
name|isAsserted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|mai
operator|.
name|isAsserted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|outbound
condition|?
name|cmai
operator|.
name|isAsserted
argument_list|()
else|:
operator|!
name|cmai
operator|.
name|isAsserted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|outbound
condition|?
name|icmai
operator|.
name|isAsserted
argument_list|()
else|:
operator|!
name|icmai
operator|.
name|isAsserted
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

