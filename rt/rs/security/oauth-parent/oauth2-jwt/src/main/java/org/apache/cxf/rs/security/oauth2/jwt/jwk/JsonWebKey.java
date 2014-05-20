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
name|jwt
operator|.
name|jwk
package|;
end_package

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
name|Map
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
name|helpers
operator|.
name|CastUtils
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
name|jwt
operator|.
name|AbstractJwtObject
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
name|jwt
operator|.
name|JwtConstants
import|;
end_import

begin_class
specifier|public
class|class
name|JsonWebKey
extends|extends
name|AbstractJwtObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|KEY_TYPE
init|=
literal|"kty"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PUBLIC_KEY_USE
init|=
literal|"use"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_OPERATIONS
init|=
literal|"key_ops"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_ALGO
init|=
name|JwtConstants
operator|.
name|HEADER_ALGORITHM
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_ID
init|=
name|JwtConstants
operator|.
name|HEADER_KEY_ID
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|X509_URL
init|=
name|JwtConstants
operator|.
name|HEADER_X509_URL
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|X509_CHAIN
init|=
name|JwtConstants
operator|.
name|HEADER_X509_CHAIN
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|X509_THUMBPRINT
init|=
name|JwtConstants
operator|.
name|HEADER_X509_THUMBPRINT
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_TYPE_OCTET
init|=
literal|"oct"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_TYPE_RSA
init|=
literal|"RSA"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_TYPE_ECURVE
init|=
literal|"EC"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PUBLIC_KEY_USE_SIGN
init|=
literal|"sig"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PUBLIC_KEY_USE_ENCRYPT
init|=
literal|"enc"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_OPER_SIGN
init|=
literal|"sign"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_OPER_VERIFY
init|=
literal|"verify"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_OPER_ENCRYPT
init|=
literal|"encrypt"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_OPER_DECRYPT
init|=
literal|"decrypt"
decl_stmt|;
specifier|public
name|JsonWebKey
parameter_list|()
block|{              }
specifier|public
name|JsonWebKey
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|values
parameter_list|)
block|{
name|super
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setKeyType
parameter_list|(
name|String
name|keyType
parameter_list|)
block|{
name|super
operator|.
name|setValue
argument_list|(
name|KEY_TYPE
argument_list|,
name|keyType
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getKeyType
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|super
operator|.
name|getValue
argument_list|(
name|KEY_TYPE
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPublicKeyUse
parameter_list|(
name|String
name|use
parameter_list|)
block|{
name|super
operator|.
name|setValue
argument_list|(
name|PUBLIC_KEY_USE
argument_list|,
name|use
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPublicKeyUse
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|super
operator|.
name|getValue
argument_list|(
name|PUBLIC_KEY_USE
argument_list|)
return|;
block|}
specifier|public
name|void
name|setKeyOperation
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|keyOperation
parameter_list|)
block|{
name|super
operator|.
name|setValue
argument_list|(
name|KEY_OPERATIONS
argument_list|,
name|keyOperation
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getKeyOperation
parameter_list|()
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|super
operator|.
name|getValue
argument_list|(
name|KEY_OPERATIONS
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setAlgorithm
parameter_list|(
name|String
name|algorithm
parameter_list|)
block|{
name|super
operator|.
name|setValue
argument_list|(
name|KEY_ALGO
argument_list|,
name|algorithm
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|super
operator|.
name|getValue
argument_list|(
name|KEY_ALGO
argument_list|)
return|;
block|}
specifier|public
name|void
name|setKid
parameter_list|(
name|String
name|kid
parameter_list|)
block|{
name|super
operator|.
name|setValue
argument_list|(
name|KEY_ID
argument_list|,
name|kid
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getKid
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|super
operator|.
name|getValue
argument_list|(
name|KEY_ID
argument_list|)
return|;
block|}
specifier|public
name|void
name|setX509Url
parameter_list|(
name|String
name|x509Url
parameter_list|)
block|{
name|super
operator|.
name|setValue
argument_list|(
name|X509_URL
argument_list|,
name|x509Url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getX509Url
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|super
operator|.
name|getValue
argument_list|(
name|X509_URL
argument_list|)
return|;
block|}
specifier|public
name|void
name|setX509Chain
parameter_list|(
name|String
name|x509Chain
parameter_list|)
block|{
name|super
operator|.
name|setValue
argument_list|(
name|X509_CHAIN
argument_list|,
name|x509Chain
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getX509Chain
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|super
operator|.
name|getValue
argument_list|(
name|X509_CHAIN
argument_list|)
return|;
block|}
specifier|public
name|void
name|setX509Thumbprint
parameter_list|(
name|String
name|x509Thumbprint
parameter_list|)
block|{
name|super
operator|.
name|setValue
argument_list|(
name|X509_THUMBPRINT
argument_list|,
name|x509Thumbprint
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getX509Thumbprint
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|super
operator|.
name|getValue
argument_list|(
name|X509_THUMBPRINT
argument_list|)
return|;
block|}
specifier|public
name|JsonWebKey
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|super
operator|.
name|setValue
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|super
operator|.
name|getValue
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

