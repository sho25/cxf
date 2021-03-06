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
name|systest
operator|.
name|factory_pattern
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|WebServiceContext
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
name|handler
operator|.
name|MessageContext
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceUtils
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"NumberService"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.factory_pattern.Number"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/factory_pattern"
argument_list|)
specifier|public
class|class
name|HttpNumberImpl
extends|extends
name|NumberImpl
block|{
annotation|@
name|Resource
specifier|protected
name|WebServiceContext
name|aContext
decl_stmt|;
specifier|protected
name|WebServiceContext
name|getWebSercviceContext
parameter_list|()
block|{
return|return
name|aContext
return|;
block|}
specifier|protected
name|String
name|idFromMessageContext
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
return|return
name|EndpointReferenceUtils
operator|.
name|getEndpointReferenceId
argument_list|(
name|mc
argument_list|)
return|;
block|}
block|}
end_class

end_unit

