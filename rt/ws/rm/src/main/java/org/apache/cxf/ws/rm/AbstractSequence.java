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
name|ws
operator|.
name|rm
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
name|ws
operator|.
name|rm
operator|.
name|v200702
operator|.
name|Identifier
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
name|ws
operator|.
name|rm
operator|.
name|v200702
operator|.
name|SequenceAcknowledgement
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
name|ws
operator|.
name|rm
operator|.
name|v200702
operator|.
name|SequenceAcknowledgement
operator|.
name|AcknowledgementRange
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSequence
block|{
specifier|protected
specifier|final
name|Identifier
name|id
decl_stmt|;
specifier|protected
name|SequenceAcknowledgement
name|acknowledgement
decl_stmt|;
specifier|private
specifier|final
name|ProtocolVariation
name|protocol
decl_stmt|;
specifier|protected
name|AbstractSequence
parameter_list|(
name|Identifier
name|i
parameter_list|,
name|ProtocolVariation
name|p
parameter_list|)
block|{
name|id
operator|=
name|i
expr_stmt|;
name|protocol
operator|=
name|p
expr_stmt|;
block|}
comment|/**      * @return the sequence identifier      */
specifier|public
name|Identifier
name|getIdentifier
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|ProtocolVariation
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|id
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
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
name|other
operator|instanceof
name|AbstractSequence
condition|)
block|{
name|AbstractSequence
name|otherSeq
init|=
operator|(
name|AbstractSequence
operator|)
name|other
decl_stmt|;
return|return
name|otherSeq
operator|.
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|boolean
name|identifierEquals
parameter_list|(
name|Identifier
name|id1
parameter_list|,
name|Identifier
name|id2
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|id1
condition|)
block|{
return|return
literal|null
operator|==
name|id2
return|;
block|}
else|else
block|{
return|return
literal|null
operator|!=
name|id2
operator|&&
name|id1
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|id2
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|synchronized
name|boolean
name|isAcknowledged
parameter_list|(
name|long
name|m
parameter_list|)
block|{
for|for
control|(
name|AcknowledgementRange
name|r
range|:
name|acknowledgement
operator|.
name|getAcknowledgementRange
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|>=
name|r
operator|.
name|getLower
argument_list|()
operator|.
name|longValue
argument_list|()
operator|&&
name|r
operator|.
name|getUpper
argument_list|()
operator|.
name|longValue
argument_list|()
operator|>=
name|m
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

