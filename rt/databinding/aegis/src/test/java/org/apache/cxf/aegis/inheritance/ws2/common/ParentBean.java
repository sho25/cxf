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
name|aegis
operator|.
name|inheritance
operator|.
name|ws2
operator|.
name|common
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
name|aegis
operator|.
name|inheritance
operator|.
name|ws2
operator|.
name|common
operator|.
name|pack1
operator|.
name|ContentBean1
import|;
end_import

begin_comment
comment|/**  *<br/>  *   * @author xfournet  */
end_comment

begin_class
specifier|public
class|class
name|ParentBean
block|{
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|ContentBean1
name|content
decl_stmt|;
specifier|public
name|ParentBean
parameter_list|()
block|{     }
specifier|public
name|ParentBean
parameter_list|(
name|String
name|id
parameter_list|,
name|ContentBean1
name|content
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|ContentBean1
name|getContent
parameter_list|()
block|{
return|return
name|content
return|;
block|}
specifier|public
name|void
name|setContent
parameter_list|(
name|ContentBean1
name|content
parameter_list|)
block|{
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"["
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"] id="
operator|+
name|id
operator|+
literal|"; content={"
operator|+
name|content
operator|+
literal|"}"
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
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|ParentBean
name|that
init|=
operator|(
name|ParentBean
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|content
operator|!=
literal|null
condition|?
operator|!
name|content
operator|.
name|equals
argument_list|(
name|that
operator|.
name|content
argument_list|)
else|:
name|that
operator|.
name|content
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|id
operator|!=
literal|null
condition|?
operator|!
name|id
operator|.
name|equals
argument_list|(
name|that
operator|.
name|id
argument_list|)
else|:
name|that
operator|.
name|id
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
decl_stmt|;
name|result
operator|=
name|id
operator|!=
literal|null
condition|?
name|id
operator|.
name|hashCode
argument_list|()
else|:
literal|0
expr_stmt|;
name|result
operator|=
literal|29
operator|*
name|result
operator|+
operator|(
name|content
operator|!=
literal|null
condition|?
name|content
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

