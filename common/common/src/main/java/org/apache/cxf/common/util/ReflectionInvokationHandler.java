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
name|common
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
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|ReflectionInvokationHandler
implements|implements
name|InvocationHandler
block|{
specifier|private
name|Object
name|target
decl_stmt|;
specifier|public
name|ReflectionInvokationHandler
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
name|target
operator|=
name|obj
expr_stmt|;
block|}
specifier|public
name|Object
name|getTarget
parameter_list|()
block|{
return|return
name|target
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
name|WrapReturn
name|wr
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|WrapReturn
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|Method
name|m
init|=
name|target
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|method
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
decl_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|wrapReturn
argument_list|(
name|wr
argument_list|,
name|m
operator|.
name|invoke
argument_list|(
name|target
argument_list|,
name|args
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
for|for
control|(
name|Method
name|m2
range|:
name|target
operator|.
name|getClass
argument_list|()
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|m2
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|m2
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
condition|)
block|{
name|boolean
name|found
init|=
literal|true
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|m2
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
name|args
index|[
name|x
index|]
operator|!=
literal|null
operator|&&
operator|!
name|m2
operator|.
name|getParameterTypes
argument_list|()
index|[
name|x
index|]
operator|.
name|isInstance
argument_list|(
name|args
index|[
name|x
index|]
argument_list|)
condition|)
block|{
name|found
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|if
condition|(
name|found
condition|)
block|{
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|m2
argument_list|)
expr_stmt|;
return|return
name|wrapReturn
argument_list|(
name|wr
argument_list|,
name|m2
operator|.
name|invoke
argument_list|(
name|target
argument_list|,
name|args
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
throw|throw
name|e
throw|;
block|}
block|}
specifier|private
specifier|static
name|Object
name|wrapReturn
parameter_list|(
name|WrapReturn
name|wr
parameter_list|,
name|Object
name|t
parameter_list|)
block|{
if|if
condition|(
name|wr
operator|==
literal|null
operator|||
name|t
operator|==
literal|null
condition|)
block|{
return|return
name|t
return|;
block|}
if|if
condition|(
name|wr
operator|.
name|iterator
argument_list|()
condition|)
block|{
return|return
operator|new
name|WrapperIterator
argument_list|(
name|wr
operator|.
name|value
argument_list|()
argument_list|,
operator|(
name|Iterator
operator|)
name|t
argument_list|)
return|;
block|}
return|return
name|createProxyWrapper
argument_list|(
name|t
argument_list|,
name|wr
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|createProxyWrapper
parameter_list|(
name|Object
name|target
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|inf
parameter_list|)
block|{
name|InvocationHandler
name|h
init|=
operator|new
name|ReflectionInvokationHandler
argument_list|(
name|target
argument_list|)
decl_stmt|;
return|return
name|inf
operator|.
name|cast
argument_list|(
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|inf
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|inf
block|}
argument_list|,
name|h
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Target
argument_list|(
name|ElementType
operator|.
name|METHOD
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
specifier|public
specifier|static
annotation_defn|@interface
name|WrapReturn
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|value
parameter_list|()
function_decl|;
name|boolean
name|iterator
parameter_list|()
default|default
literal|false
function_decl|;
block|}
specifier|private
specifier|static
class|class
name|WrapperIterator
implements|implements
name|Iterator
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
decl_stmt|;
name|Iterator
name|internal
decl_stmt|;
specifier|public
name|WrapperIterator
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|Iterator
name|it
parameter_list|)
block|{
name|internal
operator|=
name|it
expr_stmt|;
name|cls
operator|=
name|c
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|internal
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|Object
name|next
parameter_list|()
block|{
name|Object
name|obj
init|=
name|internal
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
name|createProxyWrapper
argument_list|(
name|obj
argument_list|,
name|cls
argument_list|)
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|internal
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

