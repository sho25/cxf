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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|assertion
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
name|Collections
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

begin_class
specifier|public
class|class
name|Claim
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_ROLE_NAME
init|=
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_NAME_FORMAT
init|=
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims"
decl_stmt|;
specifier|private
name|String
name|nameFormat
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|friendlyName
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|Claim
parameter_list|()
block|{              }
specifier|public
name|Claim
parameter_list|(
name|String
name|nameFormat
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|nameFormat
operator|=
name|nameFormat
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Claim
parameter_list|(
name|String
name|nameFormat
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
argument_list|(
name|nameFormat
argument_list|,
name|name
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Claim
parameter_list|(
name|String
name|nameFormat
parameter_list|,
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
name|this
operator|.
name|nameFormat
operator|=
name|nameFormat
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|values
operator|=
name|values
expr_stmt|;
block|}
specifier|public
name|void
name|setNameFormat
parameter_list|(
name|String
name|nameFormat
parameter_list|)
block|{
name|this
operator|.
name|nameFormat
operator|=
name|nameFormat
expr_stmt|;
block|}
specifier|public
name|String
name|getNameFormat
parameter_list|()
block|{
return|return
name|nameFormat
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setFriendlyName
parameter_list|(
name|String
name|friendlyName
parameter_list|)
block|{
name|this
operator|.
name|friendlyName
operator|=
name|friendlyName
expr_stmt|;
block|}
specifier|public
name|String
name|getFriendlyName
parameter_list|()
block|{
return|return
name|friendlyName
return|;
block|}
specifier|public
name|void
name|setValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
name|this
operator|.
name|values
operator|=
name|values
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getValues
parameter_list|()
block|{
return|return
name|values
return|;
block|}
block|}
end_class

end_unit

