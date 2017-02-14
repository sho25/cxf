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
name|jose
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
name|jaxrs
operator|.
name|json
operator|.
name|basic
operator|.
name|JsonMapObject
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
name|jose
operator|.
name|common
operator|.
name|JoseConstants
import|;
end_import

begin_class
specifier|public
class|class
name|JsonWebKey
extends|extends
name|JsonMapObject
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
name|JoseConstants
operator|.
name|HEADER_ALGORITHM
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_ID
init|=
name|JoseConstants
operator|.
name|HEADER_KEY_ID
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|X509_URL
init|=
name|JoseConstants
operator|.
name|HEADER_X509_URL
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|X509_CHAIN
init|=
name|JoseConstants
operator|.
name|HEADER_X509_CHAIN
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|X509_THUMBPRINT
init|=
name|JoseConstants
operator|.
name|HEADER_X509_THUMBPRINT
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|X509_THUMBPRINT_SHA256
init|=
name|JoseConstants
operator|.
name|HEADER_X509_THUMBPRINT_SHA256
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
name|RSA_MODULUS
init|=
literal|"n"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSA_PUBLIC_EXP
init|=
literal|"e"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSA_PRIVATE_EXP
init|=
literal|"d"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSA_FIRST_PRIME_FACTOR
init|=
literal|"p"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSA_SECOND_PRIME_FACTOR
init|=
literal|"q"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSA_FIRST_PRIME_CRT
init|=
literal|"dp"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSA_SECOND_PRIME_CRT
init|=
literal|"dq"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSA_FIRST_CRT_COEFFICIENT
init|=
literal|"qi"
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
name|OCTET_KEY_VALUE
init|=
literal|"k"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_TYPE_ELLIPTIC
init|=
literal|"EC"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EC_CURVE
init|=
literal|"crv"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EC_CURVE_P256
init|=
literal|"P-256"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EC_CURVE_P384
init|=
literal|"P-384"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EC_CURVE_P521
init|=
literal|"P-521"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EC_X_COORDINATE
init|=
literal|"x"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EC_Y_COORDINATE
init|=
literal|"y"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EC_PRIVATE_KEY
init|=
literal|"d"
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
specifier|static
specifier|final
name|String
name|KEY_OPER_WRAP_KEY
init|=
literal|"wrapKey"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_OPER_UNWRAP_KEY
init|=
literal|"unwrapKey"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_OPER_DERIVE_KEY
init|=
literal|"deriveKey"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY_OPER_DERIVE_BITS
init|=
literal|"deriveBits"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3201315996547826368L
decl_stmt|;
specifier|public
name|JsonWebKey
parameter_list|()
block|{      }
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
name|KeyType
name|keyType
parameter_list|)
block|{
name|setProperty
argument_list|(
name|KEY_TYPE
argument_list|,
name|keyType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|KeyType
name|getKeyType
parameter_list|()
block|{
name|Object
name|prop
init|=
name|getProperty
argument_list|(
name|KEY_TYPE
argument_list|)
decl_stmt|;
return|return
name|prop
operator|==
literal|null
condition|?
literal|null
else|:
name|KeyType
operator|.
name|getKeyType
argument_list|(
name|prop
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPublicKeyUse
parameter_list|(
name|PublicKeyUse
name|use
parameter_list|)
block|{
name|setProperty
argument_list|(
name|PUBLIC_KEY_USE
argument_list|,
name|use
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PublicKeyUse
name|getPublicKeyUse
parameter_list|()
block|{
name|Object
name|prop
init|=
name|getProperty
argument_list|(
name|PUBLIC_KEY_USE
argument_list|)
decl_stmt|;
return|return
name|prop
operator|==
literal|null
condition|?
literal|null
else|:
name|PublicKeyUse
operator|.
name|getPublicKeyUse
argument_list|(
name|prop
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|setKeyOperation
parameter_list|(
name|List
argument_list|<
name|KeyOperation
argument_list|>
name|keyOperation
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ops
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|keyOperation
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|KeyOperation
name|op
range|:
name|keyOperation
control|)
block|{
name|ops
operator|.
name|add
argument_list|(
name|op
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setProperty
argument_list|(
name|KEY_OPERATIONS
argument_list|,
name|ops
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|KeyOperation
argument_list|>
name|getKeyOperation
parameter_list|()
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|ops
init|=
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
name|getProperty
argument_list|(
name|KEY_OPERATIONS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|ops
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|KeyOperation
argument_list|>
name|keyOps
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|ops
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|op
range|:
name|ops
control|)
block|{
name|keyOps
operator|.
name|add
argument_list|(
name|KeyOperation
operator|.
name|getKeyOperation
argument_list|(
name|op
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|keyOps
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
name|setProperty
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
name|getProperty
argument_list|(
name|KEY_ALGO
argument_list|)
return|;
block|}
specifier|public
name|void
name|setKeyId
parameter_list|(
name|String
name|kid
parameter_list|)
block|{
name|setProperty
argument_list|(
name|KEY_ID
argument_list|,
name|kid
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getKeyId
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
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
name|setProperty
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
name|getProperty
argument_list|(
name|X509_URL
argument_list|)
return|;
block|}
specifier|public
name|void
name|setX509Chain
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|x509Chain
parameter_list|)
block|{
name|setProperty
argument_list|(
name|X509_CHAIN
argument_list|,
name|x509Chain
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getX509Chain
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
name|getProperty
argument_list|(
name|X509_CHAIN
argument_list|)
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
name|setProperty
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
name|getProperty
argument_list|(
name|X509_THUMBPRINT
argument_list|)
return|;
block|}
specifier|public
name|void
name|setX509ThumbprintSHA256
parameter_list|(
name|String
name|x509Thumbprint
parameter_list|)
block|{
name|setProperty
argument_list|(
name|X509_THUMBPRINT_SHA256
argument_list|,
name|x509Thumbprint
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getX509ThumbprintSHA256
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|X509_THUMBPRINT_SHA256
argument_list|)
return|;
block|}
specifier|public
name|JsonWebKey
name|setKeyProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|setProperty
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
name|getKeyProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

