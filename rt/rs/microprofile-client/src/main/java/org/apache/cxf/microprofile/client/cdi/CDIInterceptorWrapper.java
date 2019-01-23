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
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedActionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
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

begin_interface
specifier|public
interface|interface
name|CDIInterceptorWrapper
block|{
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CDIInterceptorWrapper
operator|.
name|class
argument_list|)
decl_stmt|;
class|class
name|BasicCDIInterceptorWrapper
implements|implements
name|CDIInterceptorWrapper
block|{
name|BasicCDIInterceptorWrapper
parameter_list|()
block|{         }
annotation|@
name|Override
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|restClient
parameter_list|,
name|Method
name|m
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Callable
argument_list|<
name|Object
argument_list|>
name|callable
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|callable
operator|.
name|call
argument_list|()
return|;
block|}
block|}
specifier|static
name|CDIInterceptorWrapper
name|createWrapper
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|restClient
parameter_list|)
block|{
try|try
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
call|(
name|PrivilegedExceptionAction
argument_list|<
name|CDIInterceptorWrapper
argument_list|>
call|)
argument_list|()
operator|->
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cdiClass
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"javax.enterprise.inject.spi.CDI"
argument_list|,
literal|false
argument_list|,
name|restClient
operator|.
name|getClassLoader
argument_list|()
argument_list|)
block|;
name|Method
name|currentMethod
operator|=
name|cdiClass
operator|.
name|getMethod
argument_list|(
literal|"current"
argument_list|)
block|;
name|Object
name|cdiCurrent
operator|=
name|currentMethod
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
block|;
name|Method
name|getBeanMgrMethod
operator|=
name|cdiClass
operator|.
name|getMethod
argument_list|(
literal|"getBeanManager"
argument_list|)
block|;
return|return
operator|new
name|CDIInterceptorWrapperImpl
argument_list|(
name|restClient
argument_list|,
name|getBeanMgrMethod
operator|.
name|invoke
argument_list|(
name|cdiCurrent
argument_list|)
argument_list|)
return|;
block|}
block|)
empty_stmt|;
block|}
catch|catch
parameter_list|(
name|PrivilegedActionException
name|pae
parameter_list|)
block|{
comment|// expected for environments where CDI is not supported
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|,
literal|"Unable to load CDI SPI classes, assuming no CDI is available"
argument_list|,
name|pae
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|BasicCDIInterceptorWrapper
argument_list|()
return|;
block|}
block|}
end_interface

begin_function_decl
name|Object
name|invoke
parameter_list|(
name|Object
name|restClient
parameter_list|,
name|Method
name|m
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Callable
argument_list|<
name|Object
argument_list|>
name|callable
parameter_list|)
throws|throws
name|Exception
function_decl|;
end_function_decl

unit|}
end_unit

