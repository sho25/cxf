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
name|oauth2
operator|.
name|utils
operator|.
name|crypto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|oauth2
operator|.
name|common
operator|.
name|Client
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
name|oauth2
operator|.
name|common
operator|.
name|UserSubject
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
name|oauth2
operator|.
name|grants
operator|.
name|code
operator|.
name|AuthorizationCodeDataProvider
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
name|oauth2
operator|.
name|grants
operator|.
name|code
operator|.
name|AuthorizationCodeRegistration
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
name|oauth2
operator|.
name|grants
operator|.
name|code
operator|.
name|ServerAuthorizationCodeGrant
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
name|oauth2
operator|.
name|provider
operator|.
name|OAuthServiceException
import|;
end_import

begin_class
specifier|public
class|class
name|CodeGrantEncryptingDataProvider
extends|extends
name|EncryptingDataProvider
implements|implements
name|AuthorizationCodeDataProvider
block|{
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|grants
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|CodeGrantEncryptingDataProvider
parameter_list|()
throws|throws
name|Exception
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAuthorizationCodeGrant
name|createCodeGrant
parameter_list|(
name|AuthorizationCodeRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|ServerAuthorizationCodeGrant
name|grant
init|=
operator|new
name|ServerAuthorizationCodeGrant
argument_list|(
name|reg
operator|.
name|getClient
argument_list|()
argument_list|,
literal|123
argument_list|)
decl_stmt|;
name|grant
operator|.
name|setAudience
argument_list|(
name|reg
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|encrypted
init|=
name|ModelEncryptionSupport
operator|.
name|encryptCodeGrant
argument_list|(
name|grant
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|grant
operator|.
name|setCode
argument_list|(
name|encrypted
argument_list|)
expr_stmt|;
name|grants
operator|.
name|add
argument_list|(
name|encrypted
argument_list|)
expr_stmt|;
return|return
name|grant
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAuthorizationCodeGrant
name|removeCodeGrant
parameter_list|(
name|String
name|code
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|grants
operator|.
name|remove
argument_list|(
name|code
argument_list|)
expr_stmt|;
return|return
name|ModelEncryptionSupport
operator|.
name|decryptCodeGrant
argument_list|(
name|this
argument_list|,
name|code
argument_list|,
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ServerAuthorizationCodeGrant
argument_list|>
name|getCodeGrants
parameter_list|(
name|Client
name|c
parameter_list|,
name|UserSubject
name|sub
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

