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
name|transport
operator|.
name|http
operator|.
name|auth
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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

begin_comment
comment|/**  * Use the AuthorizationPolicy type + value to create the authorization header.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CustomAuthSupplier
implements|implements
name|HttpAuthSupplier
block|{
specifier|public
name|boolean
name|requiresRequestCaching
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|getAuthorization
parameter_list|(
name|AuthorizationPolicy
name|authPolicy
parameter_list|,
name|URI
name|currentURI
parameter_list|,
name|Message
name|message
parameter_list|,
name|String
name|fullHeader
parameter_list|)
block|{
if|if
condition|(
name|authPolicy
operator|.
name|getAuthorizationType
argument_list|()
operator|!=
literal|null
operator|&&
name|authPolicy
operator|.
name|getAuthorization
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|authPolicy
operator|.
name|getAuthorizationType
argument_list|()
operator|+
literal|" "
operator|+
name|authPolicy
operator|.
name|getAuthorization
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit
