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
name|jaxrs
operator|.
name|validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|ContainerRequestContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|ContainerRequestFilter
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
name|message
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|validation
operator|.
name|ValidationInInterceptor
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSValidationInInterceptor
extends|extends
name|ValidationInInterceptor
implements|implements
name|ContainerRequestFilter
block|{
specifier|public
name|JAXRSValidationInInterceptor
parameter_list|()
block|{     }
specifier|public
name|JAXRSValidationInInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|getServiceObject
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|ValidationUtils
operator|.
name|getResourceInstance
argument_list|(
name|message
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|handleMessage
argument_list|(
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

