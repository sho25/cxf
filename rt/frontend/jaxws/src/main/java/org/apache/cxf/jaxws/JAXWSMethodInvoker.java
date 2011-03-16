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
name|Provider
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
name|handler
operator|.
name|MessageContext
operator|.
name|Scope
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
name|Fault
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
name|context
operator|.
name|WebServiceContextImpl
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
name|context
operator|.
name|WrappedMessageContext
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
name|SingletonFactory
import|;
end_import

begin_class
specifier|public
class|class
name|JAXWSMethodInvoker
extends|extends
name|AbstractJAXWSMethodInvoker
block|{
specifier|public
name|JAXWSMethodInvoker
parameter_list|(
specifier|final
name|Object
name|bean
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|SingletonFactory
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXWSMethodInvoker
parameter_list|(
name|Factory
name|factory
parameter_list|)
block|{
name|super
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|serviceObject
parameter_list|,
name|Method
name|m
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|)
block|{
comment|// set up the webservice request context
name|WrappedMessageContext
name|ctx
init|=
operator|new
name|WrappedMessageContext
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|Scope
operator|.
name|APPLICATION
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|handlerScopedStuff
init|=
name|removeHandlerProperties
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|WebServiceContextImpl
operator|.
name|setMessageContext
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|res
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|(
name|params
operator|==
literal|null
operator|||
name|params
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
name|m
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|equals
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
condition|)
block|{
name|params
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|res
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
operator|)
name|super
operator|.
name|invoke
argument_list|(
name|exchange
argument_list|,
name|serviceObject
argument_list|,
name|m
argument_list|,
name|params
argument_list|)
argument_list|)
expr_stmt|;
comment|//update the webservice response context
name|updateWebServiceContext
argument_list|(
name|exchange
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Fault
name|f
parameter_list|)
block|{
comment|//get chance to copy over customer's header
name|updateHeader
argument_list|(
name|exchange
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
throw|throw
name|f
throw|;
block|}
finally|finally
block|{
name|addHandlerProperties
argument_list|(
name|ctx
argument_list|,
name|handlerScopedStuff
argument_list|)
expr_stmt|;
comment|//clear the WebServiceContextImpl's ThreadLocal variable
name|WebServiceContextImpl
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
block|}
end_class

end_unit

