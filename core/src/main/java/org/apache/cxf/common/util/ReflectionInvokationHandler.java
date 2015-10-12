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
name|Annotation
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
name|InvocationTargetException
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
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|targetClass
init|=
name|target
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|getParameterTypes
argument_list|(
name|method
argument_list|,
name|args
argument_list|)
decl_stmt|;
try|try
block|{
name|Method
name|m
decl_stmt|;
try|try
block|{
name|m
operator|=
name|targetClass
operator|.
name|getMethod
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|parameterTypes
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|nsme
parameter_list|)
block|{
name|boolean
index|[]
name|optionals
init|=
operator|new
name|boolean
index|[
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|int
name|optionalNumber
init|=
literal|0
decl_stmt|;
for|for
control|(
specifier|final
name|Annotation
index|[]
name|a
range|:
name|method
operator|.
name|getParameterAnnotations
argument_list|()
control|)
block|{
name|optionals
index|[
name|i
index|]
operator|=
literal|false
expr_stmt|;
for|for
control|(
specifier|final
name|Annotation
name|potential
range|:
name|a
control|)
block|{
if|if
condition|(
name|Optional
operator|.
name|class
operator|.
name|equals
argument_list|(
name|potential
operator|.
name|annotationType
argument_list|()
argument_list|)
condition|)
block|{
name|optionals
index|[
name|i
index|]
operator|=
literal|true
expr_stmt|;
name|optionalNumber
operator|++
expr_stmt|;
break|break;
block|}
block|}
name|i
operator|++
expr_stmt|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|newParams
init|=
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
name|args
operator|.
name|length
operator|-
name|optionalNumber
index|]
decl_stmt|;
name|Object
index|[]
name|newArgs
init|=
operator|new
name|Object
index|[
name|args
operator|.
name|length
operator|-
name|optionalNumber
index|]
decl_stmt|;
name|int
name|argI
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|parameterTypes
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|optionals
index|[
name|j
index|]
condition|)
block|{
continue|continue;
block|}
name|newArgs
index|[
name|argI
index|]
operator|=
name|args
index|[
name|j
index|]
expr_stmt|;
name|newParams
index|[
name|argI
index|]
operator|=
name|parameterTypes
index|[
name|j
index|]
expr_stmt|;
name|argI
operator|++
expr_stmt|;
block|}
name|m
operator|=
name|targetClass
operator|.
name|getMethod
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|newParams
argument_list|)
expr_stmt|;
name|args
operator|=
name|newArgs
expr_stmt|;
name|parameterTypes
operator|=
name|newParams
expr_stmt|;
block|}
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
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
name|e
operator|.
name|getCause
argument_list|()
throw|;
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
name|targetClass
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
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getParameterTypes
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|types
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
specifier|final
name|Annotation
index|[]
index|[]
name|parAnnotations
init|=
name|method
operator|.
name|getParameterAnnotations
argument_list|()
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
name|types
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|UnwrapParam
name|p
init|=
name|getUnwrapParam
argument_list|(
name|parAnnotations
index|[
name|x
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|p
operator|.
name|methodName
argument_list|()
decl_stmt|;
name|String
name|tn
init|=
name|p
operator|.
name|typeMethodName
argument_list|()
decl_stmt|;
try|try
block|{
name|Method
name|m
init|=
name|args
index|[
name|x
index|]
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"#default"
operator|.
name|equals
argument_list|(
name|tn
argument_list|)
condition|)
block|{
name|types
index|[
name|x
index|]
operator|=
name|m
operator|.
name|getReturnType
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Method
name|m2
init|=
name|args
index|[
name|x
index|]
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
name|tn
argument_list|)
decl_stmt|;
name|types
index|[
name|x
index|]
operator|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|m2
argument_list|)
operator|.
name|invoke
argument_list|(
name|args
index|[
name|x
index|]
argument_list|)
expr_stmt|;
block|}
name|args
index|[
name|x
index|]
operator|=
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|m
argument_list|)
operator|.
name|invoke
argument_list|(
name|args
index|[
name|x
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|types
return|;
block|}
specifier|private
name|UnwrapParam
name|getUnwrapParam
parameter_list|(
name|Annotation
index|[]
name|annotations
parameter_list|)
block|{
for|for
control|(
name|Annotation
name|a
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|a
operator|instanceof
name|UnwrapParam
condition|)
block|{
return|return
operator|(
name|UnwrapParam
operator|)
name|a
return|;
block|}
block|}
return|return
literal|null
return|;
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
argument_list|<
name|?
argument_list|>
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
name|PARAMETER
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
name|Optional
block|{     }
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
annotation|@
name|Target
argument_list|(
name|ElementType
operator|.
name|PARAMETER
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
name|UnwrapParam
block|{
name|String
name|methodName
parameter_list|()
default|default
literal|"getValue"
function_decl|;
name|String
name|typeMethodName
parameter_list|()
default|default
literal|"#default"
function_decl|;
block|}
specifier|private
specifier|static
class|class
name|WrapperIterator
implements|implements
name|Iterator
argument_list|<
name|Object
argument_list|>
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|internal
decl_stmt|;
name|WrapperIterator
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|Iterator
argument_list|<
name|?
argument_list|>
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

