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
name|ws1
package|;
end_package

begin_class
specifier|public
class|class
name|BeanC
extends|extends
name|BeanB
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4199600522457096563L
decl_stmt|;
specifier|private
name|String
name|propC
decl_stmt|;
specifier|private
name|BeanD
index|[]
name|tabC
decl_stmt|;
specifier|public
name|String
name|getPropC
parameter_list|()
block|{
return|return
name|propC
return|;
block|}
specifier|public
name|void
name|setPropC
parameter_list|(
name|String
name|propC
parameter_list|)
block|{
name|this
operator|.
name|propC
operator|=
name|propC
expr_stmt|;
block|}
specifier|public
name|BeanD
index|[]
name|getTabC
parameter_list|()
block|{
return|return
name|tabC
return|;
block|}
specifier|public
name|void
name|setTabC
parameter_list|(
name|BeanD
index|[]
name|tabC
parameter_list|)
block|{
name|this
operator|.
name|tabC
operator|=
name|tabC
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
operator|+
literal|" ; propC="
operator|+
name|propC
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
if|if
condition|(
operator|!
name|super
operator|.
name|equals
argument_list|(
name|o
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|BeanC
name|beanC
init|=
operator|(
name|BeanC
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|(
name|propC
operator|!=
literal|null
operator|)
condition|?
operator|(
operator|!
name|propC
operator|.
name|equals
argument_list|(
name|beanC
operator|.
name|propC
argument_list|)
operator|)
else|:
operator|(
name|beanC
operator|.
name|propC
operator|!=
literal|null
operator|)
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
init|=
name|super
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|29
operator|*
name|result
operator|+
operator|(
name|propC
operator|!=
literal|null
condition|?
name|propC
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

