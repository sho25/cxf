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
operator|.
name|context
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
name|HashMap
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
name|Set
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

begin_class
specifier|public
class|class
name|WrappedMessageContext
implements|implements
name|MessageContext
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SCOPES
init|=
name|WrappedMessageContext
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".SCOPES"
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|contextMap
decl_stmt|;
specifier|private
specifier|final
name|Message
name|message
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Scope
argument_list|>
name|scopes
decl_stmt|;
specifier|private
name|Scope
name|defaultScope
decl_stmt|;
specifier|public
name|WrappedMessageContext
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|this
argument_list|(
name|m
argument_list|,
name|m
argument_list|,
name|Scope
operator|.
name|HANDLER
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WrappedMessageContext
parameter_list|(
name|Message
name|m
parameter_list|,
name|Scope
name|defScope
parameter_list|)
block|{
name|this
argument_list|(
name|m
argument_list|,
name|m
argument_list|,
name|defScope
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WrappedMessageContext
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
parameter_list|,
name|Scope
name|defScope
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|m
argument_list|,
name|defScope
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WrappedMessageContext
parameter_list|(
name|Message
name|m
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|Scope
name|defScope
parameter_list|)
block|{
name|message
operator|=
name|m
expr_stmt|;
name|contextMap
operator|=
name|map
expr_stmt|;
name|defaultScope
operator|=
name|defScope
expr_stmt|;
name|scopes
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|contextMap
operator|.
name|get
argument_list|(
name|SCOPES
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|scopes
operator|==
literal|null
operator|&&
name|message
operator|!=
literal|null
operator|&&
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isRequestor
argument_list|()
operator|&&
operator|!
name|isOutbound
argument_list|()
operator|&&
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scopes
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|.
name|get
argument_list|(
name|SCOPES
argument_list|)
argument_list|)
expr_stmt|;
name|copyScopedProperties
argument_list|(
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|SCOPES
argument_list|,
name|scopes
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isRequestor
argument_list|()
operator|&&
name|isOutbound
argument_list|()
operator|&&
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scopes
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|SCOPES
argument_list|)
argument_list|)
expr_stmt|;
name|copyScopedProperties
argument_list|(
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|SCOPES
argument_list|,
name|scopes
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|scopes
operator|==
literal|null
condition|)
block|{
name|scopes
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Scope
argument_list|>
argument_list|()
expr_stmt|;
name|contextMap
operator|.
name|put
argument_list|(
name|SCOPES
argument_list|,
name|scopes
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|final
name|void
name|copyScopedProperties
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
for|for
control|(
name|String
name|k
range|:
name|scopes
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|contextMap
operator|.
name|containsKey
argument_list|(
name|k
argument_list|)
operator|&&
operator|!
name|MessageContext
operator|.
name|MESSAGE_OUTBOUND_PROPERTY
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|contextMap
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|m
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
specifier|final
name|boolean
name|isRequestor
parameter_list|()
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|contextMap
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
specifier|final
name|boolean
name|isOutbound
parameter_list|()
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
return|return
name|message
operator|!=
literal|null
operator|&&
operator|(
name|message
operator|==
name|ex
operator|.
name|getOutMessage
argument_list|()
operator|||
name|message
operator|==
name|ex
operator|.
name|getOutFaultMessage
argument_list|()
operator|)
return|;
block|}
specifier|public
specifier|final
name|Message
name|getWrappedMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|contextMap
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|final
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|contextMap
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|contextMap
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|contextMap
operator|.
name|entrySet
argument_list|()
return|;
block|}
specifier|public
specifier|final
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|Object
name|ret
init|=
name|contextMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|==
literal|null
operator|&&
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|message
return|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|final
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|contextMap
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
name|contextMap
operator|.
name|keySet
argument_list|()
return|;
block|}
specifier|public
specifier|final
name|Object
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|MessageContext
operator|.
name|MESSAGE_OUTBOUND_PROPERTY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|&&
operator|!
name|scopes
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|scopes
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|defaultScope
argument_list|)
expr_stmt|;
block|}
return|return
name|contextMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|Object
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|,
name|Scope
name|scope
parameter_list|)
block|{
if|if
condition|(
operator|!
name|MessageContext
operator|.
name|MESSAGE_OUTBOUND_PROPERTY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|scopes
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
return|return
name|contextMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|t
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|s
range|:
name|t
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|put
argument_list|(
name|s
operator|.
name|getKey
argument_list|()
argument_list|,
name|s
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|final
name|Object
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|scopes
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
name|contextMap
operator|.
name|remove
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|int
name|size
parameter_list|()
block|{
return|return
name|contextMap
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
specifier|final
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|contextMap
operator|.
name|values
argument_list|()
return|;
block|}
specifier|public
specifier|final
name|void
name|setScope
parameter_list|(
name|String
name|key
parameter_list|,
name|Scope
name|arg1
parameter_list|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"non-existant property-"
operator|+
name|key
operator|+
literal|"is specified"
argument_list|)
throw|;
block|}
name|scopes
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|arg1
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|Scope
name|getScope
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
if|if
condition|(
name|scopes
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|scopes
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|defaultScope
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"non-existant property-"
operator|+
name|key
operator|+
literal|"is specified"
argument_list|)
throw|;
block|}
specifier|public
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Scope
argument_list|>
name|getScopes
parameter_list|()
block|{
return|return
name|scopes
return|;
block|}
block|}
end_class

end_unit

