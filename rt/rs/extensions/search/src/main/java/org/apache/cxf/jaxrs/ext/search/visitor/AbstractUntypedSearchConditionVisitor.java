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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|visitor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractUntypedSearchConditionVisitor
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
extends|extends
name|AbstractSearchConditionVisitor
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
block|{
specifier|private
name|VisitorState
argument_list|<
name|StringBuilder
argument_list|>
name|state
init|=
operator|new
name|LocalVisitorState
argument_list|<
name|StringBuilder
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|AbstractUntypedSearchConditionVisitor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldMap
parameter_list|)
block|{
name|super
argument_list|(
name|fieldMap
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setVisitorState
parameter_list|(
name|VisitorState
argument_list|<
name|StringBuilder
argument_list|>
name|s
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|VisitorState
argument_list|<
name|StringBuilder
argument_list|>
name|getVisitorState
parameter_list|()
block|{
return|return
name|this
operator|.
name|state
return|;
block|}
specifier|protected
name|StringBuilder
name|getStringBuilder
parameter_list|()
block|{
return|return
name|getVisitorState
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
specifier|protected
name|StringBuilder
name|removeStringBuilder
parameter_list|()
block|{
return|return
name|getVisitorState
argument_list|()
operator|.
name|remove
argument_list|()
return|;
block|}
specifier|protected
name|void
name|saveStringBuilder
parameter_list|(
name|StringBuilder
name|sb
parameter_list|)
block|{
name|getVisitorState
argument_list|()
operator|.
name|set
argument_list|(
name|sb
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
name|removeStringBuilder
argument_list|()
decl_stmt|;
return|return
name|sb
operator|==
literal|null
condition|?
literal|null
else|:
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

