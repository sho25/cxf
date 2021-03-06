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
name|staxutils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
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

begin_comment
comment|/**  * XMLStreamReader implementation which can be used to enforce a number of  * depth-restricting policies. The following properties are currently supported:  * - total number of elements in the document  * - the maximum depth of the given element; the root element will be checked by default  * - the maximum number of immediate child nodes for individual elements  *  * More sophisticated policies can be supported in the future.  */
end_comment

begin_class
specifier|public
class|class
name|DepthRestrictingStreamReader
extends|extends
name|DepthXMLStreamReader
block|{
specifier|private
name|DocumentDepthProperties
name|props
decl_stmt|;
specifier|private
name|int
name|totalElementCount
decl_stmt|;
specifier|private
specifier|final
name|Deque
argument_list|<
name|Integer
argument_list|>
name|stack
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|DepthRestrictingStreamReader
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|int
name|elementCountThreshold
parameter_list|,
name|int
name|innerElementLevelThreshold
parameter_list|,
name|int
name|innerElementCountThreshold
parameter_list|)
block|{
name|this
argument_list|(
name|reader
argument_list|,
operator|new
name|DocumentDepthProperties
argument_list|(
name|elementCountThreshold
argument_list|,
name|innerElementLevelThreshold
argument_list|,
name|innerElementCountThreshold
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DepthRestrictingStreamReader
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|DocumentDepthProperties
name|props
parameter_list|)
block|{
name|super
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|this
operator|.
name|props
operator|=
name|props
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|next
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|int
name|next
init|=
name|super
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|next
operator|==
name|START_ELEMENT
condition|)
block|{
if|if
condition|(
name|props
operator|.
name|getInnerElementLevelThreshold
argument_list|()
operator|!=
operator|-
literal|1
operator|&&
name|getDepth
argument_list|()
operator|>=
name|props
operator|.
name|getInnerElementLevelThreshold
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DepthExceededStaxException
argument_list|()
throw|;
block|}
if|if
condition|(
name|props
operator|.
name|getElementCountThreshold
argument_list|()
operator|!=
operator|-
literal|1
operator|&&
operator|++
name|totalElementCount
operator|>=
name|props
operator|.
name|getElementCountThreshold
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DepthExceededStaxException
argument_list|()
throw|;
block|}
if|if
condition|(
name|props
operator|.
name|getInnerElementCountThreshold
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
operator|!
name|stack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|int
name|currentCount
init|=
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
operator|++
name|currentCount
operator|>=
name|props
operator|.
name|getInnerElementCountThreshold
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DepthExceededStaxException
argument_list|()
throw|;
block|}
name|stack
operator|.
name|push
argument_list|(
name|currentCount
argument_list|)
expr_stmt|;
block|}
name|stack
operator|.
name|push
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|next
operator|==
name|END_ELEMENT
operator|&&
name|props
operator|.
name|getInnerElementCountThreshold
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|stack
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
return|return
name|next
return|;
block|}
block|}
end_class

end_unit

