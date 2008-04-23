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
name|ws
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|extension
operator|.
name|Registry
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
name|interceptor
operator|.
name|Interceptor
import|;
end_import

begin_comment
comment|/**  * InterceptorProviderRegistry is used to manage InterceptorProviders that provide  * assertion domain specific interceptors.  */
end_comment

begin_interface
specifier|public
interface|interface
name|PolicyInterceptorProviderRegistry
extends|extends
name|Registry
argument_list|<
name|QName
argument_list|,
name|PolicyInterceptorProvider
argument_list|>
block|{
name|List
argument_list|<
name|Interceptor
argument_list|>
name|getInterceptors
parameter_list|(
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|alterative
parameter_list|,
name|boolean
name|out
parameter_list|,
name|boolean
name|fault
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

