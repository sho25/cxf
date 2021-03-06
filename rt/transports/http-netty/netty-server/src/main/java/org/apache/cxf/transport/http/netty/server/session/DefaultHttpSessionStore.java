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
name|netty
operator|.
name|server
operator|.
name|session
package|;
end_package

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
name|UUID
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
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
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|server
operator|.
name|servlet
operator|.
name|NettyHttpSession
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultHttpSessionStore
implements|implements
name|HttpSessionStore
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
name|DefaultHttpSessionStore
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|NettyHttpSession
argument_list|>
name|SESSIONS
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|NettyHttpSession
name|createSession
parameter_list|()
block|{
name|String
name|sessionId
init|=
name|this
operator|.
name|generateNewSessionId
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Creating new session with id {}"
argument_list|,
name|sessionId
argument_list|)
expr_stmt|;
name|NettyHttpSession
name|session
init|=
operator|new
name|NettyHttpSession
argument_list|(
name|sessionId
argument_list|)
decl_stmt|;
name|SESSIONS
operator|.
name|put
argument_list|(
name|sessionId
argument_list|,
name|session
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|destroySession
parameter_list|(
name|String
name|sessionId
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Destroying session with id {}"
argument_list|,
name|sessionId
argument_list|)
expr_stmt|;
name|SESSIONS
operator|.
name|remove
argument_list|(
name|sessionId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|NettyHttpSession
name|findSession
parameter_list|(
name|String
name|sessionId
parameter_list|)
block|{
if|if
condition|(
name|SESSIONS
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|SESSIONS
operator|.
name|get
argument_list|(
name|sessionId
argument_list|)
return|;
block|}
specifier|protected
name|String
name|generateNewSessionId
parameter_list|()
block|{
return|return
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|destroyInactiveSessions
parameter_list|()
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|NettyHttpSession
argument_list|>
name|entry
range|:
name|SESSIONS
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|NettyHttpSession
name|session
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|session
operator|.
name|getMaxInactiveInterval
argument_list|()
operator|<
literal|0
condition|)
block|{
continue|continue;
block|}
name|long
name|currentMillis
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentMillis
operator|-
name|session
operator|.
name|getLastAccessedTime
argument_list|()
operator|>
name|session
operator|.
name|getMaxInactiveInterval
argument_list|()
operator|*
literal|1000
condition|)
block|{
name|destroySession
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

