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
name|binding
operator|.
name|coloc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|SortedSet
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|databinding
operator|.
name|DataReader
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
name|databinding
operator|.
name|DataWriter
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
name|InterceptorChain
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
name|MessageUtils
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
name|model
operator|.
name|FaultInfo
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
name|MessageInfo
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
name|MessagePartInfo
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
name|OperationInfo
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ColocUtil
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
name|ColocUtil
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ColocUtil
parameter_list|()
block|{
comment|//Completge
block|}
specifier|public
specifier|static
name|void
name|setPhases
parameter_list|(
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|list
parameter_list|,
name|String
name|start
parameter_list|,
name|String
name|end
parameter_list|)
block|{
name|Phase
name|startPhase
init|=
operator|new
name|Phase
argument_list|(
name|start
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Phase
name|endPhase
init|=
operator|new
name|Phase
argument_list|(
name|end
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Phase
argument_list|>
name|iter
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|boolean
name|remove
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Phase
name|p
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|remove
operator|&&
name|p
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|startPhase
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|remove
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|endPhase
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|remove
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|remove
condition|)
block|{
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|InterceptorChain
name|getOutInterceptorChain
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|phases
parameter_list|)
block|{
name|Bus
name|bus
init|=
name|ex
operator|.
name|get
argument_list|(
name|Bus
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
name|phases
argument_list|)
decl_stmt|;
name|Endpoint
name|ep
init|=
name|ex
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|il
init|=
name|ep
operator|.
name|getOutInterceptors
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by endpoint: "
operator|+
name|il
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|il
argument_list|)
expr_stmt|;
name|il
operator|=
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by service: "
operator|+
name|il
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|il
argument_list|)
expr_stmt|;
name|il
operator|=
name|bus
operator|.
name|getOutInterceptors
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by bus: "
operator|+
name|il
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|il
argument_list|)
expr_stmt|;
if|if
condition|(
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
operator|instanceof
name|InterceptorProvider
condition|)
block|{
name|il
operator|=
operator|(
operator|(
name|InterceptorProvider
operator|)
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
operator|)
operator|.
name|getOutInterceptors
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by databinding: "
operator|+
name|il
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|il
argument_list|)
expr_stmt|;
block|}
name|modifyChain
argument_list|(
name|chain
argument_list|,
name|ex
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|chain
return|;
block|}
specifier|public
specifier|static
name|InterceptorChain
name|getInInterceptorChain
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|phases
parameter_list|)
block|{
name|Bus
name|bus
init|=
name|ex
operator|.
name|get
argument_list|(
name|Bus
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
name|phases
argument_list|)
decl_stmt|;
name|Endpoint
name|ep
init|=
name|ex
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|il
init|=
name|ep
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by endpoint: "
operator|+
name|il
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|il
argument_list|)
expr_stmt|;
name|il
operator|=
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by service: "
operator|+
name|il
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|il
argument_list|)
expr_stmt|;
name|il
operator|=
name|bus
operator|.
name|getInInterceptors
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by bus: "
operator|+
name|il
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|il
argument_list|)
expr_stmt|;
if|if
condition|(
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
operator|instanceof
name|InterceptorProvider
condition|)
block|{
name|il
operator|=
operator|(
operator|(
name|InterceptorProvider
operator|)
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
operator|)
operator|.
name|getInInterceptors
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Interceptors contributed by databinding: "
operator|+
name|il
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|il
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|setFaultObserver
argument_list|(
operator|new
name|ColocOutFaultObserver
argument_list|(
name|bus
argument_list|)
argument_list|)
expr_stmt|;
name|modifyChain
argument_list|(
name|chain
argument_list|,
name|ex
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|chain
return|;
block|}
specifier|private
specifier|static
name|void
name|modifyChain
parameter_list|(
name|PhaseInterceptorChain
name|chain
parameter_list|,
name|Exchange
name|ex
parameter_list|,
name|boolean
name|in
parameter_list|)
block|{
name|modifyChain
argument_list|(
name|chain
argument_list|,
name|ex
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|modifyChain
argument_list|(
name|chain
argument_list|,
name|ex
operator|.
name|getOutMessage
argument_list|()
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|modifyChain
parameter_list|(
name|PhaseInterceptorChain
name|chain
parameter_list|,
name|Message
name|m
parameter_list|,
name|boolean
name|in
parameter_list|)
block|{
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Collection
argument_list|<
name|InterceptorProvider
argument_list|>
name|providers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|INTERCEPTOR_PROVIDERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|providers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|InterceptorProvider
name|p
range|:
name|providers
control|)
block|{
if|if
condition|(
name|in
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
name|p
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|chain
operator|.
name|add
argument_list|(
name|p
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|String
name|key
init|=
name|in
condition|?
name|Message
operator|.
name|IN_INTERCEPTORS
else|:
name|Message
operator|.
name|OUT_INTERCEPTORS
decl_stmt|;
name|Collection
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|is
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|m
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isSameOperationInfo
parameter_list|(
name|OperationInfo
name|oi1
parameter_list|,
name|OperationInfo
name|oi2
parameter_list|)
block|{
return|return
name|oi1
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|oi2
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|isSameMessageInfo
argument_list|(
name|oi1
operator|.
name|getInput
argument_list|()
argument_list|,
name|oi2
operator|.
name|getInput
argument_list|()
argument_list|)
operator|&&
name|isSameMessageInfo
argument_list|(
name|oi1
operator|.
name|getOutput
argument_list|()
argument_list|,
name|oi2
operator|.
name|getOutput
argument_list|()
argument_list|)
operator|&&
name|isSameFaultInfo
argument_list|(
name|oi1
operator|.
name|getFaults
argument_list|()
argument_list|,
name|oi2
operator|.
name|getFaults
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isCompatibleOperationInfo
parameter_list|(
name|OperationInfo
name|oi1
parameter_list|,
name|OperationInfo
name|oi2
parameter_list|)
block|{
return|return
name|isSameOperationInfo
argument_list|(
name|oi1
argument_list|,
name|oi2
argument_list|)
operator|||
name|isAssignableOperationInfo
argument_list|(
name|oi1
argument_list|,
name|Source
operator|.
name|class
argument_list|)
operator|||
name|isAssignableOperationInfo
argument_list|(
name|oi2
argument_list|,
name|Source
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isAssignableOperationInfo
parameter_list|(
name|OperationInfo
name|oi
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|MessageInfo
name|mi
init|=
name|oi
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|mpis
init|=
name|mi
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
return|return
name|mpis
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|cls
operator|.
name|isAssignableFrom
argument_list|(
name|mpis
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTypeClass
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isSameMessageInfo
parameter_list|(
name|MessageInfo
name|mi1
parameter_list|,
name|MessageInfo
name|mi2
parameter_list|)
block|{
if|if
condition|(
operator|(
name|mi1
operator|==
literal|null
operator|&&
name|mi2
operator|!=
literal|null
operator|)
operator|||
operator|(
name|mi1
operator|!=
literal|null
operator|&&
name|mi2
operator|==
literal|null
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|mi1
operator|!=
literal|null
operator|&&
name|mi2
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|mpil1
init|=
name|mi1
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|mpil2
init|=
name|mi2
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
if|if
condition|(
name|mpil1
operator|.
name|size
argument_list|()
operator|!=
name|mpil2
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|idx
init|=
literal|0
decl_stmt|;
for|for
control|(
name|MessagePartInfo
name|mpi1
range|:
name|mpil1
control|)
block|{
name|MessagePartInfo
name|mpi2
init|=
name|mpil2
operator|.
name|get
argument_list|(
name|idx
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|mpi1
operator|.
name|getTypeClass
argument_list|()
operator|.
name|equals
argument_list|(
name|mpi2
operator|.
name|getTypeClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
operator|++
name|idx
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isSameFaultInfo
parameter_list|(
name|Collection
argument_list|<
name|FaultInfo
argument_list|>
name|fil1
parameter_list|,
name|Collection
argument_list|<
name|FaultInfo
argument_list|>
name|fil2
parameter_list|)
block|{
if|if
condition|(
operator|(
name|fil1
operator|==
literal|null
operator|&&
name|fil2
operator|!=
literal|null
operator|)
operator|||
operator|(
name|fil1
operator|!=
literal|null
operator|&&
name|fil2
operator|==
literal|null
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|fil1
operator|!=
literal|null
operator|&&
name|fil2
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|fil1
operator|.
name|size
argument_list|()
operator|!=
name|fil2
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|FaultInfo
name|fi1
range|:
name|fil1
control|)
block|{
name|Iterator
argument_list|<
name|FaultInfo
argument_list|>
name|iter
init|=
name|fil2
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|fiClass1
init|=
name|fi1
operator|.
name|getProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|match
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|FaultInfo
name|fi2
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|fiClass2
init|=
name|fi2
operator|.
name|getProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//Sender/Receiver Service Model not same for faults wr.t message names.
comment|//So Compare Exception Class Instance.
if|if
condition|(
name|fiClass1
operator|.
name|equals
argument_list|(
name|fiClass2
argument_list|)
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
name|void
name|convertSourceToObject
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|content
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|==
literal|null
operator|||
name|content
operator|.
name|size
argument_list|()
operator|<
literal|1
condition|)
block|{
comment|// nothing to convert
return|return;
block|}
comment|// only supporting the wrapped style for now  (one pojo<-> one source)
name|Source
name|source
init|=
operator|(
name|Source
operator|)
name|content
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|reader
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
operator|.
name|createReader
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|MessagePartInfo
name|mpi
init|=
name|getMessageInfo
argument_list|(
name|message
argument_list|)
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|streamReader
init|=
literal|null
decl_stmt|;
name|Object
name|wrappedObject
init|=
literal|null
decl_stmt|;
try|try
block|{
name|streamReader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|wrappedObject
operator|=
name|reader
operator|.
name|read
argument_list|(
name|mpi
argument_list|,
name|streamReader
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|StaxUtils
operator|.
name|close
argument_list|(
name|streamReader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
name|MessageContentsList
name|parameters
init|=
operator|new
name|MessageContentsList
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|mpi
argument_list|,
name|wrappedObject
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
name|parameters
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|convertObjectToSource
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|content
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|==
literal|null
operator|||
name|content
operator|.
name|size
argument_list|()
operator|<
literal|1
condition|)
block|{
comment|// nothing to convert
return|return;
block|}
comment|// only supporting the wrapped style for now  (one pojo<-> one source)
name|Object
name|object
init|=
name|content
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|DataWriter
argument_list|<
name|OutputStream
argument_list|>
name|writer
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
operator|.
name|createWriter
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//TODO use a better conversion method to get a Source from a pojo.
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|object
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|content
operator|.
name|set
argument_list|(
literal|0
argument_list|,
operator|new
name|StreamSource
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|MessageInfo
name|getMessageInfo
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|OperationInfo
name|oi
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|MessageUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return
name|oi
operator|.
name|getOutput
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|oi
operator|.
name|getInput
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

