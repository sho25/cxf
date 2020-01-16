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
name|microprofile
operator|.
name|client
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|Callable
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
name|Bus
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|CDIFacade
block|{
specifier|private
specifier|static
specifier|final
name|boolean
name|CDI_AVAILABLE
decl_stmt|;
specifier|private
name|CDIFacade
parameter_list|()
block|{     }
static|static
block|{
name|boolean
name|b
decl_stmt|;
try|try
block|{
name|Class
operator|.
name|forName
argument_list|(
literal|"javax.enterprise.inject.spi.BeanManager"
argument_list|)
expr_stmt|;
name|b
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|b
operator|=
literal|false
expr_stmt|;
block|}
name|CDI_AVAILABLE
operator|=
name|b
expr_stmt|;
block|}
specifier|public
specifier|static
name|Optional
argument_list|<
name|Object
argument_list|>
name|getBeanManager
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
return|return
name|nullableOptional
argument_list|(
parameter_list|()
lambda|->
name|CDIUtils
operator|.
name|getCurrentBeanManager
argument_list|(
name|b
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Optional
argument_list|<
name|Object
argument_list|>
name|getBeanManager
parameter_list|()
block|{
try|try
block|{
return|return
name|nullableOptional
argument_list|(
parameter_list|()
lambda|->
name|CDIUtils
operator|.
name|getCurrentBeanManager
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
literal|null
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|Instance
argument_list|<
name|T
argument_list|>
argument_list|>
name|getInstanceFromCDI
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Bus
name|b
parameter_list|)
block|{
return|return
name|nullableOptional
argument_list|(
parameter_list|()
lambda|->
name|CDIUtils
operator|.
name|getInstanceFromCDI
argument_list|(
name|clazz
argument_list|,
name|b
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|Instance
argument_list|<
name|T
argument_list|>
argument_list|>
name|getInstanceFromCDI
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|nullableOptional
argument_list|(
parameter_list|()
lambda|->
name|CDIUtils
operator|.
name|getInstanceFromCDI
argument_list|(
name|clazz
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|nullableOptional
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|)
block|{
if|if
condition|(
operator|!
name|CDI_AVAILABLE
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
name|T
name|t
decl_stmt|;
try|try
block|{
name|t
operator|=
name|callable
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|// expected if no CDI implementation is available
name|t
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|t
argument_list|)
return|;
block|}
block|}
end_class

end_unit

