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
return|return
name|chain
return|;
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
block|}
end_class

end_unit

