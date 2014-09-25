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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|AbstractJoseObject
import|;
end_import

begin_class
specifier|public
class|class
name|JsonWebKeys
extends|extends
name|AbstractJoseObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|KEYS_PROPERTY
init|=
literal|"keys"
decl_stmt|;
specifier|public
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|getKeys
parameter_list|()
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
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
name|KEYS_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
operator|&&
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Object
name|first
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|first
operator|instanceof
name|JsonWebKey
condition|)
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
name|list
argument_list|)
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
operator|new
name|LinkedList
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|listOfMaps
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
name|super
operator|.
name|getValue
argument_list|(
name|KEYS_PROPERTY
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
range|:
name|listOfMaps
control|)
block|{
name|keys
operator|.
name|add
argument_list|(
operator|new
name|JsonWebKey
argument_list|(
name|map
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|keys
return|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setKeys
parameter_list|(
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
parameter_list|)
block|{
name|super
operator|.
name|setValue
argument_list|(
name|KEYS_PROPERTY
argument_list|,
name|keys
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|JsonWebKey
argument_list|>
name|getKeyIdMap
parameter_list|()
block|{
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
name|getKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|keys
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|JsonWebKey
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|JsonWebKey
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|JsonWebKey
name|key
range|:
name|keys
control|)
block|{
name|String
name|kid
init|=
name|key
operator|.
name|getKid
argument_list|()
decl_stmt|;
if|if
condition|(
name|kid
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|kid
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
specifier|public
name|JsonWebKey
name|getKey
parameter_list|(
name|String
name|kid
parameter_list|)
block|{
return|return
name|getKeyIdMap
argument_list|()
operator|.
name|get
argument_list|(
name|kid
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|>
name|getKeyTypeMap
parameter_list|()
block|{
return|return
name|getKeyPropertyMap
argument_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|>
name|getKeyUseMap
parameter_list|()
block|{
return|return
name|getKeyPropertyMap
argument_list|(
name|JsonWebKey
operator|.
name|PUBLIC_KEY_USE
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|>
name|getKeyPropertyMap
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
name|getKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|keys
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|JsonWebKey
name|key
range|:
name|keys
control|)
block|{
name|String
name|propValue
init|=
operator|(
name|String
operator|)
name|key
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
if|if
condition|(
name|propValue
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|list
init|=
name|map
operator|.
name|get
argument_list|(
name|propValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|LinkedList
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|()
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|propValue
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|>
name|getKeyOperationMap
parameter_list|()
block|{
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
name|getKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|keys
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|JsonWebKey
name|key
range|:
name|keys
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ops
init|=
name|key
operator|.
name|getKeyOperation
argument_list|()
decl_stmt|;
if|if
condition|(
name|ops
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|op
range|:
name|ops
control|)
block|{
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|list
init|=
name|map
operator|.
name|get
argument_list|(
name|op
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|LinkedList
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|()
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|op
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|map
return|;
block|}
specifier|public
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|getKeys
parameter_list|(
name|String
name|keyType
parameter_list|)
block|{
return|return
name|getKeyTypeMap
argument_list|()
operator|.
name|get
argument_list|(
name|keyType
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|getRsaKeys
parameter_list|()
block|{
return|return
name|getKeyTypeMap
argument_list|()
operator|.
name|get
argument_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE_RSA
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|getEllipticKeys
parameter_list|()
block|{
return|return
name|getKeyTypeMap
argument_list|()
operator|.
name|get
argument_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE_ELLIPTIC
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|getSecretKeys
parameter_list|()
block|{
return|return
name|getKeyTypeMap
argument_list|()
operator|.
name|get
argument_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
argument_list|)
return|;
block|}
block|}
end_class

end_unit

