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
name|ws
operator|.
name|wssec10
operator|.
name|server
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|security
operator|.
name|SimpleGroup
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
name|security
operator|.
name|SimplePrincipal
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
name|security
operator|.
name|wss4j
operator|.
name|AbstractUsernameTokenAuthenticatingInterceptor
import|;
end_import

begin_class
specifier|public
class|class
name|SimpleSubjectCreatingInterceptor
extends|extends
name|AbstractUsernameTokenAuthenticatingInterceptor
block|{
annotation|@
name|Override
specifier|protected
name|Subject
name|createSubject
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|password
parameter_list|,
name|boolean
name|isDigest
parameter_list|,
name|String
name|nonce
parameter_list|,
name|String
name|created
parameter_list|)
throws|throws
name|SecurityException
block|{
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
comment|// delegate to the external security system if possible
name|String
name|roleName
init|=
literal|"Alice"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|?
literal|"developers"
else|:
literal|"pms"
decl_stmt|;
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SimplePrincipal
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SimpleGroup
argument_list|(
name|roleName
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|subject
operator|.
name|setReadOnly
argument_list|()
expr_stmt|;
return|return
name|subject
return|;
block|}
block|}
end_class

end_unit

