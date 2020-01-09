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
name|service
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|LinkedHashMap
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractMessageContainer
extends|extends
name|AbstractPropertiesHolder
implements|implements
name|NamedItem
block|{
specifier|protected
name|QName
name|mName
decl_stmt|;
specifier|private
name|OperationInfo
name|operation
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|MessagePartInfo
argument_list|>
name|messageParts
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|outOfBandParts
decl_stmt|;
specifier|private
name|String
name|documentation
decl_stmt|;
comment|/**      * Initializes a new instance of the<code>MessagePartContainer</code>.      * @param op the operation.      * @param nm      */
name|AbstractMessageContainer
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|QName
name|nm
parameter_list|)
block|{
name|operation
operator|=
name|op
expr_stmt|;
name|mName
operator|=
name|nm
expr_stmt|;
block|}
specifier|public
name|String
name|getMessageDocumentation
parameter_list|()
block|{
return|return
name|documentation
return|;
block|}
specifier|public
name|void
name|setMessageDocumentation
parameter_list|(
name|String
name|doc
parameter_list|)
block|{
name|documentation
operator|=
name|doc
expr_stmt|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|mName
return|;
block|}
comment|/**      * Returns the operation of this container.      *      * @return the operation.      */
specifier|public
name|OperationInfo
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * Adds a message part to this container.      *      * @param name  the qualified name of the message part      * @return name  the newly created<code>MessagePartInfo</code> object      */
specifier|public
name|MessagePartInfo
name|addMessagePart
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid name [null]"
argument_list|)
throw|;
block|}
name|MessagePartInfo
name|part
init|=
operator|new
name|MessagePartInfo
argument_list|(
name|name
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|addMessagePart
argument_list|(
name|part
argument_list|)
expr_stmt|;
return|return
name|part
return|;
block|}
specifier|public
name|QName
name|getMessagePartQName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|this
operator|.
name|getOperation
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|name
argument_list|)
return|;
block|}
specifier|public
name|MessagePartInfo
name|addMessagePart
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|addMessagePart
argument_list|(
name|getMessagePartQName
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds a message part to this container.      *      * @param part the message part.      */
specifier|public
name|void
name|addMessagePart
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|)
block|{
if|if
condition|(
name|messageParts
operator|.
name|containsKey
argument_list|(
name|part
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|part
operator|.
name|setIndex
argument_list|(
name|messageParts
operator|.
name|get
argument_list|(
name|part
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|part
operator|.
name|setIndex
argument_list|(
name|messageParts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|messageParts
operator|.
name|put
argument_list|(
name|part
operator|.
name|getName
argument_list|()
argument_list|,
name|part
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getMessagePartIndex
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|)
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|MessagePartInfo
name|p
range|:
name|messageParts
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|part
operator|==
name|p
condition|)
block|{
return|return
name|i
return|;
block|}
name|i
operator|++
expr_stmt|;
block|}
for|for
control|(
name|MessagePartInfo
name|p
range|:
name|getOutOfBandParts
argument_list|()
control|)
block|{
if|if
condition|(
name|part
operator|==
name|p
condition|)
block|{
return|return
name|i
return|;
block|}
name|i
operator|++
expr_stmt|;
block|}
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|MessagePartInfo
name|getMessagePartByIndex
parameter_list|(
name|int
name|i
parameter_list|)
block|{
for|for
control|(
name|MessagePartInfo
name|p
range|:
name|messageParts
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|p
operator|.
name|getIndex
argument_list|()
operator|==
name|i
condition|)
block|{
return|return
name|p
return|;
block|}
block|}
for|for
control|(
name|MessagePartInfo
name|p
range|:
name|getOutOfBandParts
argument_list|()
control|)
block|{
if|if
condition|(
name|p
operator|.
name|getIndex
argument_list|()
operator|==
name|i
condition|)
block|{
return|return
name|p
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Removes an message part from this container.      *      * @param name the qualified message part name.      */
specifier|public
name|void
name|removeMessagePart
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|MessagePartInfo
name|messagePart
init|=
name|getMessagePart
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|messagePart
operator|!=
literal|null
condition|)
block|{
name|messageParts
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the message part with the given name, if found.      *      * @param name the qualified name.      * @return the message part; or<code>null</code> if not found.      */
specifier|public
name|MessagePartInfo
name|getMessagePart
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|MessagePartInfo
name|mpi
init|=
name|messageParts
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|mpi
operator|!=
literal|null
condition|)
block|{
return|return
name|mpi
return|;
block|}
for|for
control|(
name|MessagePartInfo
name|mpi2
range|:
name|messageParts
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|mpi2
operator|.
name|getConcreteName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|mpi2
return|;
block|}
block|}
for|for
control|(
name|MessagePartInfo
name|mpi2
range|:
name|getOutOfBandParts
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|mpi2
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|name
operator|.
name|equals
argument_list|(
name|mpi2
operator|.
name|getConcreteName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|mpi2
return|;
block|}
block|}
return|return
name|mpi
return|;
block|}
comment|/**      * Returns the n'th message part.      *      * @param n the n'th part to retrieve.      * @return the message part; or<code>null</code> if not found.      */
specifier|public
name|MessagePartInfo
name|getMessagePart
parameter_list|(
name|int
name|n
parameter_list|)
block|{
if|if
condition|(
name|n
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|mpis
init|=
name|getMessageParts
argument_list|()
decl_stmt|;
return|return
name|n
operator|<
name|mpis
operator|.
name|size
argument_list|()
condition|?
name|mpis
operator|.
name|get
argument_list|(
name|n
argument_list|)
else|:
literal|null
return|;
block|}
specifier|public
name|MessagePartInfo
name|addOutOfBandMessagePart
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid name [null]"
argument_list|)
throw|;
block|}
name|MessagePartInfo
name|part
init|=
operator|new
name|MessagePartInfo
argument_list|(
name|name
argument_list|,
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|outOfBandParts
operator|==
literal|null
condition|)
block|{
name|outOfBandParts
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|part
operator|.
name|setIndex
argument_list|(
name|messageParts
operator|.
name|size
argument_list|()
operator|+
name|outOfBandParts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|outOfBandParts
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
return|return
name|part
return|;
block|}
comment|/**      * Returns all message parts for this message.      *      * @return all message parts.      */
specifier|public
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|getMessageParts
parameter_list|()
block|{
if|if
condition|(
name|outOfBandParts
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|messageParts
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|parts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|messageParts
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|parts
operator|.
name|addAll
argument_list|(
name|outOfBandParts
argument_list|)
expr_stmt|;
return|return
name|parts
return|;
block|}
specifier|public
name|int
name|getMessagePartsNumber
parameter_list|()
block|{
if|if
condition|(
name|outOfBandParts
operator|==
literal|null
condition|)
block|{
return|return
name|messageParts
operator|.
name|size
argument_list|()
return|;
block|}
return|return
name|messageParts
operator|.
name|size
argument_list|()
operator|+
name|outOfBandParts
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|MessagePartInfo
name|getFirstMessagePart
parameter_list|()
block|{
if|if
condition|(
operator|!
name|messageParts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|messageParts
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|outOfBandParts
operator|!=
literal|null
operator|&&
operator|!
name|outOfBandParts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|outOfBandParts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|getOutOfBandParts
parameter_list|()
block|{
if|if
condition|(
name|outOfBandParts
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|outOfBandParts
argument_list|)
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|messageParts
operator|.
name|size
argument_list|()
operator|+
name|getOutOfBandParts
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|mName
operator|==
literal|null
condition|?
operator|-
literal|1
else|:
name|mName
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|AbstractMessageContainer
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|AbstractMessageContainer
name|oi
init|=
operator|(
name|AbstractMessageContainer
operator|)
name|o
decl_stmt|;
return|return
name|equals
argument_list|(
name|mName
argument_list|,
name|oi
operator|.
name|mName
argument_list|)
operator|&&
name|equals
argument_list|(
name|messageParts
argument_list|,
name|oi
operator|.
name|messageParts
argument_list|)
operator|&&
name|equals
argument_list|(
name|outOfBandParts
argument_list|,
name|oi
operator|.
name|outOfBandParts
argument_list|)
return|;
block|}
block|}
end_class

end_unit

