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
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InterceptionType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Interceptor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|interceptor
operator|.
name|InvocationContext
import|;
end_import

begin_class
specifier|public
class|class
name|InterceptorInvoker
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|private
specifier|final
name|Interceptor
name|interceptor
decl_stmt|;
specifier|private
specifier|final
name|Object
name|interceptorInstance
decl_stmt|;
specifier|public
name|InterceptorInvoker
parameter_list|(
name|Interceptor
argument_list|<
name|?
argument_list|>
name|interceptor
parameter_list|,
name|Object
name|interceptorInstance
parameter_list|)
block|{
name|this
operator|.
name|interceptor
operator|=
name|interceptor
expr_stmt|;
name|this
operator|.
name|interceptorInstance
operator|=
name|interceptorInstance
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Object
name|invoke
parameter_list|(
name|InvocationContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|interceptor
operator|.
name|intercept
argument_list|(
name|InterceptionType
operator|.
name|AROUND_INVOKE
argument_list|,
name|interceptorInstance
argument_list|,
name|ctx
argument_list|)
return|;
block|}
block|}
end_class

end_unit

