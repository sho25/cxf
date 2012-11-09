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
name|jaxws
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
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|Provider
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
name|continuations
operator|.
name|SuspendedInvocationException
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
name|service
operator|.
name|Hello
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
name|ExchangeImpl
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
name|MessageContentsList
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|Service
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
name|invoker
operator|.
name|Factory
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
name|invoker
operator|.
name|MethodDispatcher
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
name|easymock
operator|.
name|EasyMock
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

begin_class
specifier|public
class|class
name|JAXWSMethodInvokerTest
extends|extends
name|Assert
block|{
name|Factory
name|factory
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Factory
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|target
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Hello
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testFactoryBeans
parameter_list|()
throws|throws
name|Throwable
block|{
name|Exchange
name|ex
init|=
name|EasyMock
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
name|reset
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|factory
operator|.
name|create
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|JAXWSMethodInvoker
name|jaxwsMethodInvoker
init|=
operator|new
name|JAXWSMethodInvoker
argument_list|(
name|factory
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|jaxwsMethodInvoker
operator|.
name|getServiceObject
argument_list|(
name|ex
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"the target object and service object should be equal "
argument_list|,
name|object
argument_list|,
name|target
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSuspendedException
parameter_list|()
throws|throws
name|Throwable
block|{
name|Exception
name|originalException
init|=
operator|new
name|RuntimeException
argument_list|()
decl_stmt|;
name|ContinuationService
name|serviceObject
init|=
operator|new
name|ContinuationService
argument_list|(
name|originalException
argument_list|)
decl_stmt|;
name|Method
name|serviceMethod
init|=
name|ContinuationService
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"invoke"
argument_list|,
operator|new
name|Class
index|[]
block|{}
argument_list|)
decl_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|Message
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|JAXWSMethodInvoker
name|jaxwsMethodInvoker
init|=
name|prepareJAXWSMethodInvoker
argument_list|(
name|ex
argument_list|,
name|serviceObject
argument_list|,
name|serviceMethod
argument_list|)
decl_stmt|;
try|try
block|{
name|jaxwsMethodInvoker
operator|.
name|invoke
argument_list|(
name|ex
argument_list|,
operator|new
name|MessageContentsList
argument_list|(
operator|new
name|Object
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Suspended invocation swallowed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SuspendedInvocationException
name|suspendedEx
parameter_list|)
block|{
name|assertSame
argument_list|(
name|suspendedEx
argument_list|,
name|serviceObject
operator|.
name|getSuspendedException
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|originalException
argument_list|,
name|suspendedEx
operator|.
name|getRuntimeException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProviderInterpretNullAsOneway
parameter_list|()
throws|throws
name|Throwable
block|{
name|NullableProviderService
name|serviceObject
init|=
operator|new
name|NullableProviderService
argument_list|()
decl_stmt|;
name|Method
name|serviceMethod
init|=
name|NullableProviderService
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"invoke"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|Source
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|Message
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|inMessage
operator|.
name|setInterceptorChain
argument_list|(
operator|new
name|PhaseInterceptorChain
argument_list|(
operator|new
name|TreeSet
argument_list|<
name|Phase
argument_list|>
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|JAXWSMethodInvoker
name|jaxwsMethodInvoker
init|=
name|prepareJAXWSMethodInvoker
argument_list|(
name|ex
argument_list|,
name|serviceObject
argument_list|,
name|serviceMethod
argument_list|)
decl_stmt|;
comment|// request-response with non-null response
name|ex
operator|.
name|setOneWay
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|MessageContentsList
name|obj
init|=
operator|(
name|MessageContentsList
operator|)
name|jaxwsMethodInvoker
operator|.
name|invoke
argument_list|(
name|ex
argument_list|,
operator|new
name|MessageContentsList
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|StreamSource
argument_list|()
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|obj
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|obj
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ex
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
comment|// oneway with non-null response
name|ex
operator|.
name|setOneWay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|obj
operator|=
operator|(
name|MessageContentsList
operator|)
name|jaxwsMethodInvoker
operator|.
name|invoke
argument_list|(
name|ex
argument_list|,
operator|new
name|MessageContentsList
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|StreamSource
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ex
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
comment|// request-response with null response, interpretNullAsOneway not set so
comment|// default should be true
name|ex
operator|.
name|setOneWay
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|serviceObject
operator|.
name|setNullable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|obj
operator|=
operator|(
name|MessageContentsList
operator|)
name|jaxwsMethodInvoker
operator|.
name|invoke
argument_list|(
name|ex
argument_list|,
operator|new
name|MessageContentsList
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|StreamSource
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ex
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
comment|// request-response with null response, interpretNullAsOneway disabled
name|ex
operator|.
name|setOneWay
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|serviceObject
operator|.
name|setNullable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setContextualProperty
argument_list|(
literal|"jaxws.provider.interpretNullAsOneway"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|obj
operator|=
operator|(
name|MessageContentsList
operator|)
name|jaxwsMethodInvoker
operator|.
name|invoke
argument_list|(
name|ex
argument_list|,
operator|new
name|MessageContentsList
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|StreamSource
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|obj
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|obj
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ex
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
comment|// request-response with null response, interpretNullAsOneway explicitly enabled
name|ex
operator|.
name|setOneWay
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|serviceObject
operator|.
name|setNullable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setContextualProperty
argument_list|(
literal|"jaxws.provider.interpretNullAsOneway"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|obj
operator|=
operator|(
name|MessageContentsList
operator|)
name|jaxwsMethodInvoker
operator|.
name|invoke
argument_list|(
name|ex
argument_list|,
operator|new
name|MessageContentsList
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|StreamSource
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ex
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|JAXWSMethodInvoker
name|prepareJAXWSMethodInvoker
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|Object
name|serviceObject
parameter_list|,
name|Method
name|serviceMethod
parameter_list|)
throws|throws
name|Throwable
block|{
name|EasyMock
operator|.
name|reset
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|factory
operator|.
name|create
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|serviceObject
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|factory
operator|.
name|release
argument_list|(
name|ex
argument_list|,
name|serviceObject
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|boi
init|=
operator|new
name|BindingOperationInfo
argument_list|()
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|Service
name|serviceClass
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
name|serviceClass
operator|.
name|size
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|serviceClass
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
name|serviceClass
argument_list|)
expr_stmt|;
name|MethodDispatcher
name|md
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|MethodDispatcher
operator|.
name|class
argument_list|)
decl_stmt|;
name|serviceClass
operator|.
name|get
argument_list|(
name|MethodDispatcher
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|md
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|md
operator|.
name|getMethod
argument_list|(
name|boi
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|serviceMethod
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
comment|// initialize the contextCache
name|ex
operator|.
name|getInMessage
argument_list|()
operator|.
name|getContextualProperty
argument_list|(
literal|"dummy"
argument_list|)
expr_stmt|;
return|return
operator|new
name|JAXWSMethodInvoker
argument_list|(
name|factory
argument_list|)
return|;
block|}
specifier|public
specifier|static
class|class
name|ContinuationService
block|{
specifier|private
name|RuntimeException
name|ex
decl_stmt|;
specifier|public
name|ContinuationService
parameter_list|(
name|Exception
name|throwable
parameter_list|)
block|{
name|ex
operator|=
operator|new
name|SuspendedInvocationException
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|invoke
parameter_list|()
block|{
throw|throw
name|ex
throw|;
block|}
specifier|public
name|Throwable
name|getSuspendedException
parameter_list|()
block|{
return|return
name|ex
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|NullableProviderService
implements|implements
name|Provider
argument_list|<
name|Source
argument_list|>
block|{
specifier|private
name|boolean
name|nullable
decl_stmt|;
specifier|public
name|void
name|setNullable
parameter_list|(
name|boolean
name|nullable
parameter_list|)
block|{
name|this
operator|.
name|nullable
operator|=
name|nullable
expr_stmt|;
block|}
specifier|public
name|Source
name|invoke
parameter_list|(
name|Source
name|request
parameter_list|)
block|{
return|return
name|nullable
condition|?
literal|null
else|:
name|request
return|;
block|}
block|}
block|}
end_class

end_unit

