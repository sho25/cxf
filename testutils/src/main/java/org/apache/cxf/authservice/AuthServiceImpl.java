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
name|authservice
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|jws
operator|.
name|WebService
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/AuthService"
argument_list|,
name|name
operator|=
literal|"AuthService"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.authservice.AuthService"
argument_list|)
specifier|public
class|class
name|AuthServiceImpl
implements|implements
name|AuthService
block|{
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|String
name|sid
parameter_list|,
name|String
name|uid
parameter_list|,
name|String
name|pwd
parameter_list|)
block|{
if|if
condition|(
name|uid
operator|==
literal|null
condition|)
block|{
comment|//test to make sure a "middle" param can be null
return|return
name|pwd
operator|!=
literal|null
return|;
block|}
return|return
name|sid
operator|.
name|equals
argument_list|(
name|uid
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|Authenticate
name|au
parameter_list|)
block|{
return|return
name|au
operator|.
name|getUid
argument_list|()
operator|.
name|equals
argument_list|(
name|au
operator|.
name|getSid
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAuthentication
parameter_list|(
name|String
name|sid
parameter_list|)
block|{
return|return
literal|"get "
operator|+
name|sid
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRoles
parameter_list|(
name|String
name|sid
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|sid
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|sid
operator|+
literal|"-1"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|sid
operator|+
literal|"-2"
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
specifier|public
name|String
index|[]
name|getRolesAsArray
parameter_list|(
name|String
name|sid
parameter_list|)
block|{
if|if
condition|(
literal|"null"
operator|.
name|equals
argument_list|(
name|sid
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
literal|"0"
operator|.
name|equals
argument_list|(
name|sid
argument_list|)
condition|)
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
return|return
operator|new
name|String
index|[]
block|{
name|sid
block|,
name|sid
operator|+
literal|"-1"
block|}
return|;
block|}
block|}
end_class

end_unit

