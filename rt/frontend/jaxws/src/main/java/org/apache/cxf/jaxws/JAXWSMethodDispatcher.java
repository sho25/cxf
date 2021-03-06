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
name|jaxws
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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
name|Type
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
name|TypeVariable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Response
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
name|i18n
operator|.
name|Message
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
name|logging
operator|.
name|LogUtils
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
name|ReflectionUtil
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
name|endpoint
operator|.
name|Endpoint
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
name|jaxws
operator|.
name|support
operator|.
name|JaxWsImplementorInfo
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
name|factory
operator|.
name|ServiceConstructionException
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
name|factory
operator|.
name|SimpleMethodDispatcher
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
name|BindingOperationInfo
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
name|OperationInfo
import|;
end_import

begin_class
specifier|public
class|class
name|JAXWSMethodDispatcher
extends|extends
name|SimpleMethodDispatcher
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JAXWSMethodDispatcher
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JaxWsImplementorInfo
name|implInfo
decl_stmt|;
specifier|public
name|JAXWSMethodDispatcher
parameter_list|(
name|JaxWsImplementorInfo
name|implInfo
parameter_list|)
block|{
name|this
operator|.
name|implInfo
operator|=
name|implInfo
expr_stmt|;
block|}
specifier|public
name|void
name|bind
parameter_list|(
name|OperationInfo
name|o
parameter_list|,
name|Method
modifier|...
name|methods
parameter_list|)
block|{
name|Method
index|[]
name|newMethods
init|=
operator|new
name|Method
index|[
name|methods
operator|.
name|length
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|methods
control|)
block|{
try|try
block|{
name|newMethods
index|[
name|i
index|]
operator|=
name|getImplementationMethod
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
if|if
condition|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"Async"
argument_list|)
operator|&&
operator|(
name|Future
operator|.
name|class
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|)
operator|||
name|Response
operator|.
name|class
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|newMethods
index|[
name|i
index|]
operator|=
name|m
expr_stmt|;
name|i
operator|++
expr_stmt|;
continue|continue;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|endpointClass
init|=
name|implInfo
operator|.
name|getImplementorClass
argument_list|()
decl_stmt|;
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"SEI_METHOD_NOT_FOUND"
argument_list|,
name|LOG
argument_list|,
name|m
operator|.
name|getName
argument_list|()
argument_list|,
name|endpointClass
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|super
operator|.
name|bind
argument_list|(
name|o
argument_list|,
name|newMethods
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BindingOperationInfo
name|getBindingOperation
parameter_list|(
name|Method
name|method
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
try|try
block|{
name|method
operator|=
name|getImplementationMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|super
operator|.
name|getBindingOperation
argument_list|(
name|method
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
specifier|public
name|Method
name|getImplementationMethod
parameter_list|(
name|Method
name|method
parameter_list|)
throws|throws
name|NoSuchMethodException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|endpointClass
init|=
name|implInfo
operator|.
name|getImplementorClass
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|endpointClass
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|Method
name|m2
init|=
name|endpointClass
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
if|if
condition|(
name|Modifier
operator|.
name|isVolatile
argument_list|(
name|m2
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
comment|//bridge method, need to map the generics
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|params
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
for|for
control|(
name|Type
name|t
range|:
name|method
operator|.
name|getGenericParameterTypes
argument_list|()
control|)
block|{
if|if
condition|(
name|t
operator|instanceof
name|TypeVariable
condition|)
block|{
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|tv
init|=
operator|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
operator|)
name|t
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
name|implInfo
operator|.
name|getSEIClass
argument_list|()
operator|.
name|getTypeParameters
argument_list|()
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|t2
init|=
name|implInfo
operator|.
name|getSEIClass
argument_list|()
operator|.
name|getTypeParameters
argument_list|()
index|[
name|x
index|]
decl_stmt|;
if|if
condition|(
name|t2
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|tv
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|params
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
name|implInfo
operator|.
name|getSEIType
argument_list|()
operator|.
name|getActualTypeArguments
argument_list|()
index|[
name|x
index|]
expr_stmt|;
block|}
block|}
block|}
block|}
name|method
operator|=
name|endpointClass
operator|.
name|getMethod
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|method
operator|=
name|m2
expr_stmt|;
block|}
try|try
block|{
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|method
return|;
block|}
block|}
end_class

end_unit

