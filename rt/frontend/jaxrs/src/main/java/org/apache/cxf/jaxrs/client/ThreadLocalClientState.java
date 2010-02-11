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
name|net
operator|.
name|URI
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|WeakHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|MultivaluedMap
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
operator|.
name|ResponseBuilder
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
name|UriBuilder
import|;
end_import

begin_comment
comment|/**  * Keeps the client state such as the baseURI, currentURI, requestHeaders, current response  * in a thread local storage  *  */
end_comment

begin_class
specifier|public
class|class
name|ThreadLocalClientState
implements|implements
name|ClientState
block|{
specifier|private
name|Map
argument_list|<
name|Thread
argument_list|,
name|LocalClientState
argument_list|>
name|state
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|WeakHashMap
argument_list|<
name|Thread
argument_list|,
name|LocalClientState
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|LocalClientState
name|initialState
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Thread
argument_list|,
name|Long
argument_list|>
name|checkpointMap
decl_stmt|;
specifier|private
name|long
name|secondsToKeepState
decl_stmt|;
specifier|public
name|ThreadLocalClientState
parameter_list|(
name|String
name|baseURI
parameter_list|)
block|{
name|this
operator|.
name|initialState
operator|=
operator|new
name|LocalClientState
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|baseURI
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ThreadLocalClientState
parameter_list|(
name|LocalClientState
name|initialState
parameter_list|)
block|{
name|this
operator|.
name|initialState
operator|=
name|initialState
expr_stmt|;
block|}
specifier|public
name|void
name|setCurrentBuilder
parameter_list|(
name|UriBuilder
name|currentBuilder
parameter_list|)
block|{
name|getState
argument_list|()
operator|.
name|setCurrentBuilder
argument_list|(
name|currentBuilder
argument_list|)
expr_stmt|;
block|}
specifier|public
name|UriBuilder
name|getCurrentBuilder
parameter_list|()
block|{
return|return
name|getState
argument_list|()
operator|.
name|getCurrentBuilder
argument_list|()
return|;
block|}
specifier|public
name|void
name|setBaseURI
parameter_list|(
name|URI
name|baseURI
parameter_list|)
block|{
name|getState
argument_list|()
operator|.
name|setBaseURI
argument_list|(
name|baseURI
argument_list|)
expr_stmt|;
block|}
specifier|public
name|URI
name|getBaseURI
parameter_list|()
block|{
return|return
name|getState
argument_list|()
operator|.
name|getBaseURI
argument_list|()
return|;
block|}
specifier|public
name|void
name|setResponseBuilder
parameter_list|(
name|ResponseBuilder
name|responseBuilder
parameter_list|)
block|{
name|getState
argument_list|()
operator|.
name|setResponseBuilder
argument_list|(
name|responseBuilder
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ResponseBuilder
name|getResponseBuilder
parameter_list|()
block|{
return|return
name|getState
argument_list|()
operator|.
name|getResponseBuilder
argument_list|()
return|;
block|}
specifier|public
name|void
name|setRequestHeaders
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestHeaders
parameter_list|)
block|{
name|getState
argument_list|()
operator|.
name|setRequestHeaders
argument_list|(
name|requestHeaders
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getRequestHeaders
parameter_list|()
block|{
return|return
name|getState
argument_list|()
operator|.
name|getRequestHeaders
argument_list|()
return|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|removeThreadLocalState
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClientState
name|newState
parameter_list|(
name|URI
name|baseURI
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|)
block|{
name|LocalClientState
name|ls
init|=
operator|new
name|LocalClientState
argument_list|(
name|baseURI
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|ls
operator|.
name|setRequestHeaders
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ThreadLocalClientState
argument_list|(
name|ls
argument_list|)
return|;
block|}
specifier|private
name|void
name|removeThreadLocalState
parameter_list|(
name|Thread
name|t
parameter_list|)
block|{
name|state
operator|.
name|remove
argument_list|(
name|t
argument_list|)
expr_stmt|;
if|if
condition|(
name|checkpointMap
operator|!=
literal|null
condition|)
block|{
name|checkpointMap
operator|.
name|remove
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|ClientState
name|getState
parameter_list|()
block|{
name|LocalClientState
name|cs
init|=
name|state
operator|.
name|get
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cs
operator|==
literal|null
condition|)
block|{
name|cs
operator|=
operator|new
name|LocalClientState
argument_list|(
name|initialState
argument_list|)
expr_stmt|;
name|state
operator|.
name|put
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|,
name|cs
argument_list|)
expr_stmt|;
if|if
condition|(
name|secondsToKeepState
operator|>
literal|0
condition|)
block|{
name|long
name|currentTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|checkpointMap
operator|.
name|put
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|,
name|currentTime
argument_list|)
expr_stmt|;
operator|new
name|CleanupThread
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|,
name|currentTime
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|cs
return|;
block|}
specifier|public
name|void
name|setSecondsToKeepState
parameter_list|(
name|long
name|secondsToKeepState
parameter_list|)
block|{
name|this
operator|.
name|secondsToKeepState
operator|=
name|secondsToKeepState
expr_stmt|;
if|if
condition|(
name|secondsToKeepState
operator|>
literal|0
condition|)
block|{
name|checkpointMap
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Thread
argument_list|,
name|Long
argument_list|>
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
class|class
name|CleanupThread
extends|extends
name|Thread
block|{
specifier|private
name|Thread
name|thread
decl_stmt|;
specifier|private
name|long
name|originalTime
decl_stmt|;
specifier|public
name|CleanupThread
parameter_list|(
name|Thread
name|thread
parameter_list|,
name|long
name|originalTime
parameter_list|)
block|{
name|this
operator|.
name|thread
operator|=
name|thread
expr_stmt|;
name|this
operator|.
name|originalTime
operator|=
name|originalTime
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|secondsToKeepState
argument_list|)
expr_stmt|;
name|long
name|actualTime
init|=
name|checkpointMap
operator|.
name|get
argument_list|(
name|thread
argument_list|)
decl_stmt|;
comment|// if times do not match then the original worker thread
comment|// has called reset() but came back again to create new local state
comment|// hence there's another cleanup thread nearby which will clean the state
if|if
condition|(
name|actualTime
operator|==
name|originalTime
condition|)
block|{
name|removeThreadLocalState
argument_list|(
name|thread
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
end_class

end_unit

