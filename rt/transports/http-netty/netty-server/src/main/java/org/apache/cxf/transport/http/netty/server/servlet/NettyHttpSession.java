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
name|servlet
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpSession
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpSessionBindingEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpSessionBindingListener
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
name|util
operator|.
name|Utils
import|;
end_import

begin_class
specifier|public
class|class
name|NettyHttpSession
implements|implements
name|HttpSession
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SESSION_ID_KEY
init|=
literal|"JSESSIONID"
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|long
name|creationTime
decl_stmt|;
specifier|private
name|long
name|lastAccessedTime
decl_stmt|;
specifier|private
name|int
name|maxInactiveInterval
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
decl_stmt|;
specifier|public
name|NettyHttpSession
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|creationTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
name|this
operator|.
name|lastAccessedTime
operator|=
name|this
operator|.
name|creationTime
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|attributes
operator|!=
literal|null
condition|?
name|attributes
operator|.
name|get
argument_list|(
name|name
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
specifier|public
name|Enumeration
name|getAttributeNames
parameter_list|()
block|{
return|return
name|Utils
operator|.
name|enumerationFromKeys
argument_list|(
name|attributes
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getCreationTime
parameter_list|()
block|{
return|return
name|this
operator|.
name|creationTime
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getLastAccessedTime
parameter_list|()
block|{
return|return
name|this
operator|.
name|lastAccessedTime
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
comment|// TODO do we need to support this
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|Override
specifier|public
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpSessionContext
name|getSessionContext
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"As of Version 2.1, this method is deprecated and has no replacement."
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getValue
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getAttribute
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|getValueNames
parameter_list|()
block|{
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|attributes
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|attributes
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invalidate
parameter_list|()
block|{
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
block|{
name|attributes
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|putValue
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|this
operator|.
name|setAttribute
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
block|{
name|Object
name|value
init|=
name|attributes
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|HttpSessionBindingListener
condition|)
block|{
operator|(
operator|(
name|HttpSessionBindingListener
operator|)
name|value
operator|)
operator|.
name|valueUnbound
argument_list|(
operator|new
name|HttpSessionBindingEvent
argument_list|(
name|this
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|attributes
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeValue
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|removeAttribute
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
name|attributes
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|attributes
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|HttpSessionBindingListener
condition|)
block|{
operator|(
operator|(
name|HttpSessionBindingListener
operator|)
name|value
operator|)
operator|.
name|valueBound
argument_list|(
operator|new
name|HttpSessionBindingEvent
argument_list|(
name|this
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|getMaxInactiveInterval
parameter_list|()
block|{
return|return
name|this
operator|.
name|maxInactiveInterval
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setMaxInactiveInterval
parameter_list|(
name|int
name|interval
parameter_list|)
block|{
name|this
operator|.
name|maxInactiveInterval
operator|=
name|interval
expr_stmt|;
block|}
specifier|public
name|void
name|touch
parameter_list|()
block|{
name|this
operator|.
name|lastAccessedTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isNew
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Method 'isNew' not yet implemented!"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

