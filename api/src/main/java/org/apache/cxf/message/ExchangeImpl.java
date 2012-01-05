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
name|message
package|;
end_package

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
name|binding
operator|.
name|Binding
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
name|Endpoint
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
name|PreexistingConduitSelector
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
name|model
operator|.
name|BindingOperationInfo
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
name|Destination
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
name|Session
import|;
end_import

begin_class
specifier|public
class|class
name|ExchangeImpl
extends|extends
name|StringMapImpl
implements|implements
name|Exchange
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3112077559217623594L
decl_stmt|;
specifier|private
name|Destination
name|destination
decl_stmt|;
specifier|private
name|boolean
name|oneWay
decl_stmt|;
specifier|private
name|boolean
name|synchronous
init|=
literal|true
decl_stmt|;
specifier|private
name|Message
name|inMessage
decl_stmt|;
specifier|private
name|Message
name|outMessage
decl_stmt|;
specifier|private
name|Message
name|inFaultMessage
decl_stmt|;
specifier|private
name|Message
name|outFaultMessage
decl_stmt|;
specifier|private
name|Session
name|session
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
specifier|private
name|Service
name|service
decl_stmt|;
specifier|private
name|Binding
name|binding
decl_stmt|;
specifier|private
name|BindingOperationInfo
name|bindingOp
decl_stmt|;
comment|/*     public<T> T get(Class<T> key) {          if (key == Bus.class) {             return (T)bus;         } else if (key == Service.class) {             return (T)service;         } else if (key == Endpoint.class) {             return (T)endpoint;         } else if (key == BindingOperationInfo.class) {             return (T)bindingOp;         } else if (key == Binding.class) {             return (T)binding;         } else if (key == OperationInfo.class) {             return super.get(key);         }         return super.get(key);     }     */
specifier|private
name|void
name|resetContextCaches
parameter_list|()
block|{
if|if
condition|(
name|inMessage
operator|!=
literal|null
condition|)
block|{
name|inMessage
operator|.
name|resetContextCache
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|outMessage
operator|!=
literal|null
condition|)
block|{
name|outMessage
operator|.
name|resetContextCache
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|inFaultMessage
operator|!=
literal|null
condition|)
block|{
name|inFaultMessage
operator|.
name|resetContextCache
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|outFaultMessage
operator|!=
literal|null
condition|)
block|{
name|outFaultMessage
operator|.
name|resetContextCache
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|put
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|key
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|key
operator|==
name|Bus
operator|.
name|class
condition|)
block|{
name|resetContextCaches
argument_list|()
expr_stmt|;
name|bus
operator|=
operator|(
name|Bus
operator|)
name|value
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|==
name|Endpoint
operator|.
name|class
condition|)
block|{
name|resetContextCaches
argument_list|()
expr_stmt|;
name|endpoint
operator|=
operator|(
name|Endpoint
operator|)
name|value
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|==
name|Service
operator|.
name|class
condition|)
block|{
name|resetContextCaches
argument_list|()
expr_stmt|;
name|service
operator|=
operator|(
name|Service
operator|)
name|value
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|==
name|BindingOperationInfo
operator|.
name|class
condition|)
block|{
name|bindingOp
operator|=
operator|(
name|BindingOperationInfo
operator|)
name|value
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|==
name|Binding
operator|.
name|class
condition|)
block|{
name|binding
operator|=
operator|(
name|Binding
operator|)
name|value
expr_stmt|;
block|}
block|}
specifier|public
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
name|inMessage
operator|!=
literal|null
condition|)
block|{
name|inMessage
operator|.
name|setContextualProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|outMessage
operator|!=
literal|null
condition|)
block|{
name|outMessage
operator|.
name|setContextualProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|inFaultMessage
operator|!=
literal|null
condition|)
block|{
name|inFaultMessage
operator|.
name|setContextualProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|outFaultMessage
operator|!=
literal|null
condition|)
block|{
name|outFaultMessage
operator|.
name|setContextualProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|super
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
name|Destination
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
specifier|public
name|Message
name|getInMessage
parameter_list|()
block|{
return|return
name|inMessage
return|;
block|}
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|get
argument_list|(
name|ConduitSelector
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|?
name|get
argument_list|(
name|ConduitSelector
operator|.
name|class
argument_list|)
operator|.
name|selectConduit
argument_list|(
name|message
argument_list|)
else|:
literal|null
return|;
block|}
specifier|public
name|Message
name|getOutMessage
parameter_list|()
block|{
return|return
name|outMessage
return|;
block|}
specifier|public
name|Message
name|getInFaultMessage
parameter_list|()
block|{
return|return
name|inFaultMessage
return|;
block|}
specifier|public
name|void
name|setInFaultMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|inFaultMessage
operator|=
name|m
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Message
name|getOutFaultMessage
parameter_list|()
block|{
return|return
name|outFaultMessage
return|;
block|}
specifier|public
name|void
name|setOutFaultMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|outFaultMessage
operator|=
name|m
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDestination
parameter_list|(
name|Destination
name|d
parameter_list|)
block|{
name|destination
operator|=
name|d
expr_stmt|;
block|}
specifier|public
name|void
name|setInMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|inMessage
operator|=
name|m
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|m
condition|)
block|{
name|m
operator|.
name|setExchange
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setConduit
parameter_list|(
name|Conduit
name|c
parameter_list|)
block|{
name|put
argument_list|(
name|ConduitSelector
operator|.
name|class
argument_list|,
operator|new
name|PreexistingConduitSelector
argument_list|(
name|c
argument_list|,
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOutMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|outMessage
operator|=
name|m
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|m
condition|)
block|{
name|m
operator|.
name|setExchange
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isOneWay
parameter_list|()
block|{
return|return
name|oneWay
return|;
block|}
specifier|public
name|void
name|setOneWay
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|oneWay
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSynchronous
parameter_list|()
block|{
return|return
name|synchronous
return|;
block|}
specifier|public
name|void
name|setSynchronous
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|synchronous
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
specifier|public
name|void
name|setSession
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|super
operator|.
name|clear
argument_list|()
expr_stmt|;
name|resetContextCaches
argument_list|()
expr_stmt|;
name|destination
operator|=
literal|null
expr_stmt|;
name|oneWay
operator|=
literal|false
expr_stmt|;
name|inMessage
operator|=
literal|null
expr_stmt|;
name|outMessage
operator|=
literal|null
expr_stmt|;
name|inFaultMessage
operator|=
literal|null
expr_stmt|;
name|outFaultMessage
operator|=
literal|null
expr_stmt|;
name|session
operator|=
literal|null
expr_stmt|;
name|bus
operator|=
literal|null
expr_stmt|;
block|}
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
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
specifier|public
name|Service
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
specifier|public
name|Binding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
specifier|public
name|BindingOperationInfo
name|getBindingOperationInfo
parameter_list|()
block|{
return|return
name|bindingOp
return|;
block|}
block|}
end_class

end_unit

