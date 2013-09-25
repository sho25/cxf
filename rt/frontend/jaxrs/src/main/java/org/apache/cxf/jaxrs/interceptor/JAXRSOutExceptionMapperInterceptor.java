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
name|jaxrs
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|interceptor
operator|.
name|AbstractOutDatabindingInterceptor
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
name|jaxrs
operator|.
name|lifecycle
operator|.
name|ResourceProvider
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
name|jaxrs
operator|.
name|model
operator|.
name|ClassResourceInfo
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
name|jaxrs
operator|.
name|provider
operator|.
name|ServerProviderFactory
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|logging
operator|.
name|FaultListener
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
name|logging
operator|.
name|NoOpFaultListener
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
name|phase
operator|.
name|PhaseManager
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSOutExceptionMapperInterceptor
extends|extends
name|AbstractOutDatabindingInterceptor
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JAXRSOutExceptionMapperInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|JAXRSOutExceptionMapperInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|SETUP
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXRSOutExceptionMapperInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|==
name|message
operator|.
name|get
argument_list|(
literal|"jaxrs.out.fault"
argument_list|)
condition|)
block|{
comment|// Exception comes from JAXRSOutInterceptor or the one which follows it
return|return;
block|}
name|Response
name|r
init|=
name|JAXRSUtils
operator|.
name|convertFaultToResponse
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|removeContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|setFaultObserver
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
operator|new
name|MessageContentsList
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|FaultListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|FaultListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|NoOpFaultListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|PhaseManager
name|pm
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|PhaseInterceptorChain
name|chain
init|=
operator|new
name|PhaseInterceptorChain
argument_list|(
name|pm
operator|.
name|getOutPhases
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|jaxrsOutOrAfter
init|=
literal|false
decl_stmt|;
name|Iterator
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|iterator
init|=
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|outInterceptor
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|outInterceptor
operator|.
name|getClass
argument_list|()
operator|==
name|JAXRSOutInterceptor
operator|.
name|class
condition|)
block|{
name|jaxrsOutOrAfter
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|jaxrsOutOrAfter
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
name|outInterceptor
argument_list|)
expr_stmt|;
block|}
block|}
name|message
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|doInterceptStartingAt
argument_list|(
name|message
argument_list|,
name|JAXRSOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cleanup thread local variables"
argument_list|)
expr_stmt|;
name|Object
name|rootInstance
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|remove
argument_list|(
name|JAXRSUtils
operator|.
name|ROOT_INSTANCE
argument_list|)
decl_stmt|;
name|Object
name|rootProvider
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|remove
argument_list|(
name|JAXRSUtils
operator|.
name|ROOT_PROVIDER
argument_list|)
decl_stmt|;
if|if
condition|(
name|rootInstance
operator|!=
literal|null
operator|&&
name|rootProvider
operator|!=
literal|null
condition|)
block|{
try|try
block|{
operator|(
operator|(
name|ResourceProvider
operator|)
name|rootProvider
operator|)
operator|.
name|releaseInstance
argument_list|(
name|message
argument_list|,
name|rootInstance
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|tex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Exception occurred during releasing the service instance, "
operator|+
name|tex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|(
name|message
argument_list|)
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
name|ClassResourceInfo
name|cri
init|=
operator|(
name|ClassResourceInfo
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|JAXRSUtils
operator|.
name|ROOT_RESOURCE_CLASS
argument_list|)
decl_stmt|;
if|if
condition|(
name|cri
operator|!=
literal|null
condition|)
block|{
name|cri
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{     }
block|}
end_class

end_unit

