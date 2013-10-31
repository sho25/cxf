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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|validation
operator|.
name|AbstractValidationInInterceptor
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSValidationInInterceptor
extends|extends
name|AbstractValidationInInterceptor
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
name|getResourceInstance
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
specifier|protected
name|Method
name|getResourceMethod
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ValidationUtils
operator|.
name|isAnnotatedMethodAvailable
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getResourceMethod
argument_list|(
name|message
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

