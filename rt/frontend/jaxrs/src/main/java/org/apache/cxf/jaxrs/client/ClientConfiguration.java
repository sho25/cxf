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
name|client
package|;
end_package

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
name|common
operator|.
name|util
operator|.
name|ModCountCopyOnWriteArrayList
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
name|ConduitSelector
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
name|ConduitSelectorHolder
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
name|interceptor
operator|.
name|InterceptorProvider
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
name|transport
operator|.
name|Conduit
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
name|MessageObserver
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

begin_comment
comment|/**  * Represents the configuration of the current proxy or WebClient.  * Given an instance with the name 'client', one can access its configuration  * using a WebClient.getConfig(client) call.  */
end_comment

begin_class
specifier|public
class|class
name|ClientConfiguration
implements|implements
name|InterceptorProvider
implements|,
name|ConduitSelectorHolder
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
name|ClientConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|inInterceptors
init|=
operator|new
name|ModCountCopyOnWriteArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|outInterceptors
init|=
operator|new
name|ModCountCopyOnWriteArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|outFault
init|=
operator|new
name|ModCountCopyOnWriteArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|inFault
init|=
operator|new
name|ModCountCopyOnWriteArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ConduitSelector
name|conduitSelector
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Sets the conduit selector       * @param cs the selector      */
specifier|public
name|void
name|setConduitSelector
parameter_list|(
name|ConduitSelector
name|cs
parameter_list|)
block|{
name|this
operator|.
name|conduitSelector
operator|=
name|cs
expr_stmt|;
block|}
comment|/**      * Gets the conduit selector       * @return the conduit the selector      */
specifier|public
name|ConduitSelector
name|getConduitSelector
parameter_list|()
block|{
return|return
name|conduitSelector
return|;
block|}
name|void
name|prepareConduitSelector
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|getConduitSelector
argument_list|()
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Fault
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Failure to prepare a message from conduit selector"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets the bus      * @param bus the bus      */
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
comment|/**      * Gets the bus      * @return the bus      */
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getInFaultInterceptors
parameter_list|()
block|{
return|return
name|inFault
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getInInterceptors
parameter_list|()
block|{
return|return
name|inInterceptors
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getOutFaultInterceptors
parameter_list|()
block|{
return|return
name|outFault
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getOutInterceptors
parameter_list|()
block|{
return|return
name|outInterceptors
return|;
block|}
comment|/**      * Sets the list of in interceptors which pre-process       * the responses from remote services.      *        * @param interceptors in interceptors      */
specifier|public
name|void
name|setInInterceptors
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
name|interceptors
parameter_list|)
block|{
name|inInterceptors
operator|=
name|interceptors
expr_stmt|;
block|}
comment|/**      * Sets the list of out interceptors which post-process       * the requests to the remote services.      *        * @param interceptors out interceptors      */
specifier|public
name|void
name|setOutInterceptors
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
name|interceptors
parameter_list|)
block|{
name|outInterceptors
operator|=
name|interceptors
expr_stmt|;
block|}
comment|/**      * Sets the list of in fault interceptors which will deal with the HTTP      * faults; the client code may choose to catch {@link ServerWebApplicationException}      * exceptions instead.      *        * @param interceptors in fault interceptors      */
specifier|public
name|void
name|setInFaultInterceptors
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
name|interceptors
parameter_list|)
block|{
name|inFault
operator|=
name|interceptors
expr_stmt|;
block|}
comment|/**      * Sets the list of out fault interceptors which will deal with the client-side      * faults; the client code may choose to catch {@link ClientWebApplicationException}      * exceptions instead.      *        * @param interceptors out fault interceptors      */
specifier|public
name|void
name|setOutFaultInterceptors
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
name|interceptors
parameter_list|)
block|{
name|outFault
operator|=
name|interceptors
expr_stmt|;
block|}
comment|/**      * Gets the conduit responsible for a transport-level      * communication with the remote service.       * @return the conduit      */
specifier|public
name|Conduit
name|getConduit
parameter_list|()
block|{
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|MessageObserver
operator|.
name|class
argument_list|,
operator|new
name|ClientMessageObserver
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|prepareConduitSelector
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|getConduitSelector
argument_list|()
operator|.
name|selectConduit
argument_list|(
name|message
argument_list|)
return|;
block|}
comment|/**      * Gets the HTTP conduit responsible for a transport-level      * communication with the remote service.       * @return the HTTP conduit      */
specifier|public
name|HTTPConduit
name|getHttpConduit
parameter_list|()
block|{
name|Conduit
name|conduit
init|=
name|getConduit
argument_list|()
decl_stmt|;
return|return
name|conduit
operator|instanceof
name|HTTPConduit
condition|?
operator|(
name|HTTPConduit
operator|)
name|conduit
else|:
literal|null
return|;
block|}
comment|/**      * Get the map of properties which affect the responses only.       * These additional properties may be optionally set after a       * proxy or WebClient has been created.      * @return the response context properties      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getResponseContext
parameter_list|()
block|{
return|return
name|responseContext
return|;
block|}
comment|/**      * Get the map of properties which affect the requests only.       * These additional properties may be optionally set after a       * proxy or WebClient has been created.      * @return the request context properties      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getRequestContext
parameter_list|()
block|{
return|return
name|requestContext
return|;
block|}
block|}
end_class

end_unit

