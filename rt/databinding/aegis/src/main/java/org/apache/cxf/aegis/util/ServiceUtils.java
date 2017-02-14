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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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

begin_comment
comment|/**  * Helps when constructing, or using services.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ServiceUtils
block|{
specifier|private
name|ServiceUtils
parameter_list|()
block|{      }
comment|/**      * Generates a suitable service name from a given class. The returned name      * is the simple name of the class, i.e. without the package name.      *      * @param clazz the class.      * @return the name.      */
specifier|public
specifier|static
name|String
name|makeServiceNameFromClassName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|String
name|name
init|=
name|clazz
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|last
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|last
operator|!=
operator|-
literal|1
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|last
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|int
name|inner
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|"$"
argument_list|)
decl_stmt|;
if|if
condition|(
name|inner
operator|!=
operator|-
literal|1
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|inner
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
specifier|public
specifier|static
name|QName
name|makeQualifiedNameFromClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|String
name|namespace
init|=
name|NamespaceHelper
operator|.
name|makeNamespaceFromClassName
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|String
name|localPart
init|=
name|makeServiceNameFromClassName
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|localPart
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getMethodName
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|m
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|params
init|=
name|m
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|params
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|param
init|=
name|params
index|[
name|i
index|]
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|param
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
name|params
operator|.
name|length
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

