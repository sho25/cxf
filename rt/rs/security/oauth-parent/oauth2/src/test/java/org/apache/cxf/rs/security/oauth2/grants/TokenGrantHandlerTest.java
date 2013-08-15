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
name|grants
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
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
name|core
operator|.
name|MultivaluedMap
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
name|impl
operator|.
name|MetadataMap
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
name|ServerAccessToken
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
name|tokens
operator|.
name|bearer
operator|.
name|BearerAccessToken
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
name|utils
operator|.
name|OAuthConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|TokenGrantHandlerTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSimpleGrantSupported
parameter_list|()
block|{
name|SimpleGrantHandler
name|handler
init|=
operator|new
name|SimpleGrantHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|setDataProvider
argument_list|(
operator|new
name|OAuthDataProviderImpl
argument_list|()
argument_list|)
expr_stmt|;
name|ServerAccessToken
name|t
init|=
name|handler
operator|.
name|createAccessToken
argument_list|(
name|createClient
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|createMap
argument_list|(
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|t
operator|instanceof
name|BearerAccessToken
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleGrantBug
parameter_list|()
block|{
try|try
block|{
operator|new
name|SimpleGrantHandler
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
operator|.
name|createAccessToken
argument_list|(
name|createClient
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|createMap
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Grant handler bug"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|ex
operator|.
name|getResponse
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testComplexGrantSupported
parameter_list|()
block|{
name|ComplexGrantHandler
name|handler
init|=
operator|new
name|ComplexGrantHandler
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|handler
operator|.
name|setDataProvider
argument_list|(
operator|new
name|OAuthDataProviderImpl
argument_list|()
argument_list|)
expr_stmt|;
name|ServerAccessToken
name|t
init|=
name|handler
operator|.
name|createAccessToken
argument_list|(
name|createClient
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|createMap
argument_list|(
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|t
operator|instanceof
name|BearerAccessToken
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Client
name|createClient
parameter_list|(
name|String
modifier|...
name|grants
parameter_list|)
block|{
name|Client
name|c
init|=
operator|new
name|Client
argument_list|(
literal|"alice"
argument_list|,
literal|"password"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|grant
range|:
name|grants
control|)
block|{
name|c
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
name|grant
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|createMap
parameter_list|(
name|String
name|grant
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
name|OAuthConstants
operator|.
name|GRANT_TYPE
argument_list|,
name|grant
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
specifier|private
specifier|static
class|class
name|SimpleGrantHandler
extends|extends
name|AbstractGrantHandler
block|{
specifier|public
name|SimpleGrantHandler
parameter_list|()
block|{
name|super
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SimpleGrantHandler
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|grants
parameter_list|)
block|{
name|super
argument_list|(
name|grants
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|super
operator|.
name|doCreateAccessToken
argument_list|(
name|client
argument_list|,
name|client
operator|.
name|getSubject
argument_list|()
argument_list|,
name|params
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ComplexGrantHandler
extends|extends
name|AbstractGrantHandler
block|{
specifier|public
name|ComplexGrantHandler
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|grants
parameter_list|)
block|{
name|super
argument_list|(
name|grants
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|super
operator|.
name|doCreateAccessToken
argument_list|(
name|client
argument_list|,
name|client
operator|.
name|getSubject
argument_list|()
argument_list|,
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|GRANT_TYPE
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

