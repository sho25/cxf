begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|hw
operator|.
name|server
operator|.
name|data
package|;
end_package

begin_class
specifier|public
class|class
name|Ingredient
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|Category
name|category
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
try|try
block|{
name|Ingredient
name|other
init|=
operator|(
name|Ingredient
operator|)
name|obj
decl_stmt|;
return|return
name|other
operator|.
name|name
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
name|other
operator|.
name|category
operator|==
name|category
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|cce
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Category
name|getCategory
parameter_list|()
block|{
return|return
name|category
return|;
block|}
specifier|public
name|void
name|setCategory
parameter_list|(
name|Category
name|category
parameter_list|)
block|{
name|this
operator|.
name|category
operator|=
name|category
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|name
operator|.
name|hashCode
argument_list|()
operator|+
operator|(
literal|37
operator|*
name|category
operator|.
name|hashCode
argument_list|()
operator|)
return|;
block|}
block|}
end_class

end_unit

