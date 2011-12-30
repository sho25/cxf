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
name|oauth
operator|.
name|services
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
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
name|jaxrs
operator|.
name|ext
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
name|rs
operator|.
name|security
operator|.
name|oauth
operator|.
name|provider
operator|.
name|OAuthDataProvider
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
name|rs
operator|.
name|security
operator|.
name|oauth
operator|.
name|utils
operator|.
name|OAuthUtils
import|;
end_import

begin_comment
comment|/**  * Abstract utility class which OAuth services extend  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractOAuthService
block|{
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|private
name|OAuthDataProvider
name|dataProvider
decl_stmt|;
annotation|@
name|Context
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|mc
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|MessageContext
name|getMessageContext
parameter_list|()
block|{
return|return
name|mc
return|;
block|}
specifier|public
name|void
name|setDataProvider
parameter_list|(
name|OAuthDataProvider
name|dataProvider
parameter_list|)
block|{
name|this
operator|.
name|dataProvider
operator|=
name|dataProvider
expr_stmt|;
block|}
specifier|protected
name|OAuthDataProvider
name|getDataProvider
parameter_list|()
block|{
return|return
name|OAuthUtils
operator|.
name|getOAuthDataProvider
argument_list|(
name|dataProvider
argument_list|,
name|mc
operator|.
name|getServletContext
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

