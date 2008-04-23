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
name|corba
operator|.
name|types
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaStructEventProducer
extends|extends
name|AbstractStartEndEventProducer
block|{
specifier|public
name|CorbaStructEventProducer
parameter_list|(
name|CorbaObjectHandler
name|h
parameter_list|,
name|ServiceInfo
name|service
parameter_list|,
name|ORB
name|orbRef
parameter_list|)
block|{
name|CorbaStructHandler
name|handler
init|=
operator|(
name|CorbaStructHandler
operator|)
name|h
decl_stmt|;
name|name
operator|=
name|handler
operator|.
name|getName
argument_list|()
expr_stmt|;
name|iterator
operator|=
name|handler
operator|.
name|members
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|serviceInfo
operator|=
name|service
expr_stmt|;
name|orb
operator|=
name|orbRef
expr_stmt|;
block|}
specifier|public
name|int
name|next
parameter_list|()
block|{
name|int
name|event
init|=
name|states
index|[
name|state
index|]
decl_stmt|;
if|if
condition|(
name|event
operator|!=
literal|0
condition|)
block|{
name|state
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|currentEventProducer
operator|!=
literal|null
operator|&&
name|currentEventProducer
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|event
operator|=
name|currentEventProducer
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CorbaObjectHandler
name|obj
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
comment|//Special case for primitive sequence inside struct
if|if
condition|(
operator|(
name|obj
operator|instanceof
name|CorbaSequenceHandler
operator|)
operator|&&
operator|(
name|CorbaHandlerUtils
operator|.
name|isPrimitiveIDLTypeSequence
argument_list|(
name|obj
argument_list|)
operator|)
operator|&&
operator|(
operator|!
operator|(
operator|(
name|CorbaSequenceHandler
operator|)
name|obj
operator|)
operator|.
name|getElements
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
operator|(
operator|!
name|CorbaHandlerUtils
operator|.
name|isOctets
argument_list|(
name|obj
operator|.
name|getType
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|currentEventProducer
operator|=
operator|new
name|CorbaPrimitiveSequenceEventProducer
argument_list|(
name|obj
argument_list|,
name|serviceInfo
argument_list|,
name|orb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|currentEventProducer
operator|=
name|CorbaHandlerUtils
operator|.
name|getTypeEventProducer
argument_list|(
name|obj
argument_list|,
name|serviceInfo
argument_list|,
name|orb
argument_list|)
expr_stmt|;
block|}
name|event
operator|=
name|currentEventProducer
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// all done with content, move past state 0
name|event
operator|=
name|states
index|[
operator|++
name|state
index|]
expr_stmt|;
name|state
operator|++
expr_stmt|;
name|currentEventProducer
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|event
return|;
block|}
block|}
end_class

end_unit

