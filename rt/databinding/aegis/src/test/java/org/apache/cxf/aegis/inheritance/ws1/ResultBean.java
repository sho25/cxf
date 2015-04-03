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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_class
specifier|public
class|class
name|ResultBean
block|{
specifier|private
name|BeanA
index|[]
name|result1
decl_stmt|;
specifier|private
name|RootBean
index|[]
name|result2
decl_stmt|;
specifier|public
name|BeanA
index|[]
name|getResult1
parameter_list|()
block|{
return|return
name|result1
return|;
block|}
specifier|public
name|void
name|setResult1
parameter_list|(
name|BeanA
index|[]
name|result1
parameter_list|)
block|{
name|this
operator|.
name|result1
operator|=
name|result1
expr_stmt|;
block|}
specifier|public
name|RootBean
index|[]
name|getResult2
parameter_list|()
block|{
return|return
name|result2
return|;
block|}
specifier|public
name|void
name|setResult2
parameter_list|(
name|RootBean
index|[]
name|result2
parameter_list|)
block|{
name|this
operator|.
name|result2
operator|=
name|result2
expr_stmt|;
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
name|ResultBean
name|that
init|=
operator|(
name|ResultBean
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|result1
argument_list|,
name|that
operator|.
name|result1
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|Arrays
operator|.
name|equals
argument_list|(
name|result2
argument_list|,
name|that
operator|.
name|result2
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

