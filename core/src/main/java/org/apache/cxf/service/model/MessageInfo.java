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
name|HashMap
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
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * A message for an operation.  */
end_comment

begin_class
specifier|public
class|class
name|MessageInfo
extends|extends
name|AbstractMessageContainer
block|{
specifier|public
enum|enum
name|Type
block|{
name|INPUT
block|,
name|OUTPUT
block|;     }
specifier|private
name|Type
name|type
decl_stmt|;
specifier|public
name|MessageInfo
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Type
name|type
parameter_list|,
name|QName
name|nm
parameter_list|)
block|{
name|super
argument_list|(
name|op
argument_list|,
name|nm
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
name|mName
operator|=
name|qn
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|QName
argument_list|,
name|MessagePartInfo
argument_list|>
name|getMessagePartsMap
parameter_list|()
block|{
name|Map
argument_list|<
name|QName
argument_list|,
name|MessagePartInfo
argument_list|>
name|partsMap
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|MessagePartInfo
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|MessagePartInfo
name|part
range|:
name|getMessageParts
argument_list|()
control|)
block|{
name|partsMap
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
return|return
name|partsMap
return|;
block|}
specifier|public
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|getOrderedParts
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|order
argument_list|)
condition|)
block|{
return|return
name|getMessageParts
argument_list|()
return|;
block|}
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|orderedParts
init|=
operator|new
name|ArrayList
argument_list|<
name|MessagePartInfo
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|MessagePartInfo
argument_list|>
name|partsMap
init|=
name|getMessagePartsMap
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|order
control|)
block|{
name|QName
name|qname
init|=
name|getMessagePartQName
argument_list|(
name|part
argument_list|)
decl_stmt|;
name|orderedParts
operator|.
name|add
argument_list|(
name|partsMap
operator|.
name|get
argument_list|(
name|qname
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|orderedParts
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"[MessageInfo "
operator|+
name|type
operator|+
literal|": "
operator|+
name|mName
operator|.
name|toString
argument_list|()
operator|+
literal|"]"
return|;
block|}
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
block|}
end_class

end_unit

