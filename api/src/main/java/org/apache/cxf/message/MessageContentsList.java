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
name|Arrays
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
import|;
end_import

begin_class
specifier|public
class|class
name|MessageContentsList
extends|extends
name|ArrayList
argument_list|<
name|Object
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|Object
name|REMOVED_MARKER
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|5780720048950696258L
decl_stmt|;
specifier|public
name|MessageContentsList
parameter_list|()
block|{
name|super
argument_list|(
literal|6
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MessageContentsList
parameter_list|(
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|super
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MessageContentsList
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|values
parameter_list|)
block|{
name|super
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|MessageContentsList
name|getContentsList
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|o
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|msg
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
name|o
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|MessageContentsList
operator|)
condition|)
block|{
name|MessageContentsList
name|l2
init|=
operator|new
name|MessageContentsList
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|l2
argument_list|)
expr_stmt|;
return|return
name|l2
return|;
block|}
return|return
operator|(
name|MessageContentsList
operator|)
name|o
return|;
block|}
specifier|public
name|Object
name|set
parameter_list|(
name|int
name|idx
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|ensureSize
argument_list|(
name|idx
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|set
argument_list|(
name|idx
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|private
name|void
name|ensureSize
parameter_list|(
name|int
name|idx
parameter_list|)
block|{
while|while
condition|(
name|idx
operator|>=
name|size
argument_list|()
condition|)
block|{
name|add
argument_list|(
name|REMOVED_MARKER
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|put
parameter_list|(
name|MessagePartInfo
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|ensureSize
argument_list|(
name|key
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|set
argument_list|(
name|key
operator|.
name|getIndex
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasValue
parameter_list|(
name|MessagePartInfo
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|.
name|getIndex
argument_list|()
operator|>=
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|super
operator|.
name|get
argument_list|(
name|key
operator|.
name|getIndex
argument_list|()
argument_list|)
operator|!=
name|REMOVED_MARKER
return|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|MessagePartInfo
name|key
parameter_list|)
block|{
name|Object
name|o
init|=
name|super
operator|.
name|get
argument_list|(
name|key
operator|.
name|getIndex
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|o
operator|==
name|REMOVED_MARKER
condition|?
literal|null
else|:
name|o
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|(
name|MessagePartInfo
name|key
parameter_list|)
block|{
name|put
argument_list|(
name|key
argument_list|,
name|REMOVED_MARKER
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

