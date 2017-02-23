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
name|common
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
name|jwk
operator|.
name|JsonWebKey
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|JoseHeaders
extends|extends
name|JsonMapObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1101185302425283553L
decl_stmt|;
specifier|public
name|JoseHeaders
parameter_list|()
block|{     }
specifier|public
name|JoseHeaders
parameter_list|(
name|JoseType
name|type
parameter_list|)
block|{
name|init
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JoseHeaders
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
name|this
argument_list|(
name|headers
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JoseHeaders
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
specifier|private
name|void
name|init
parameter_list|(
name|JoseType
name|type
parameter_list|)
block|{
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|setType
parameter_list|(
name|JoseType
name|type
parameter_list|)
block|{
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_TYPE
argument_list|,
name|type
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JoseType
name|getType
parameter_list|()
block|{
name|Object
name|prop
init|=
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_TYPE
argument_list|)
decl_stmt|;
return|return
name|prop
operator|==
literal|null
condition|?
literal|null
else|:
name|JoseType
operator|.
name|getType
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
name|setContentType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_CONTENT_TYPE
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_CONTENT_TYPE
argument_list|)
return|;
block|}
specifier|public
name|void
name|setAlgorithm
parameter_list|(
name|String
name|algo
parameter_list|)
block|{
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_ALGORITHM
argument_list|,
name|algo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
name|Object
name|prop
init|=
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_ALGORITHM
argument_list|)
decl_stmt|;
return|return
name|prop
operator|==
literal|null
condition|?
literal|null
else|:
name|prop
operator|.
name|toString
argument_list|()
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
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_KEY_ID
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
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_KEY_ID
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
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_X509_URL
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
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_X509_URL
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
name|JoseConstants
operator|.
name|HEADER_X509_CHAIN
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
name|JoseConstants
operator|.
name|HEADER_X509_CHAIN
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
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_X509_THUMBPRINT
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
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_X509_THUMBPRINT
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
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_X509_THUMBPRINT_SHA256
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
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_X509_THUMBPRINT_SHA256
argument_list|)
return|;
block|}
specifier|public
name|void
name|setCritical
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|crit
parameter_list|)
block|{
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_CRITICAL
argument_list|,
name|crit
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getCritical
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
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_CRITICAL
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setJsonWebKey
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
name|setJsonWebKey
argument_list|(
name|JoseConstants
operator|.
name|HEADER_JSON_WEB_KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setJsonWebKey
parameter_list|(
name|String
name|headerName
parameter_list|,
name|JsonWebKey
name|key
parameter_list|)
block|{
name|setHeader
argument_list|(
name|headerName
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setJsonWebKeysUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_JSON_WEB_KEY_SET
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getJsonWebKeysUrl
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|HEADER_JSON_WEB_KEY_SET
argument_list|)
return|;
block|}
specifier|public
name|JsonWebKey
name|getJsonWebKey
parameter_list|()
block|{
return|return
name|getJsonWebKey
argument_list|(
name|JoseConstants
operator|.
name|HEADER_JSON_WEB_KEY
argument_list|)
return|;
block|}
specifier|public
name|JsonWebKey
name|getJsonWebKey
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|Object
name|jsonWebKey
init|=
name|getHeader
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
if|if
condition|(
name|jsonWebKey
operator|==
literal|null
operator|||
name|jsonWebKey
operator|instanceof
name|JsonWebKey
condition|)
block|{
return|return
operator|(
name|JsonWebKey
operator|)
name|jsonWebKey
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|jsonWebKey
argument_list|)
decl_stmt|;
return|return
operator|new
name|JsonWebKey
argument_list|(
name|map
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|JoseHeaders
name|setHeader
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
name|getHeader
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
specifier|public
name|JoseHeaders
name|setIntegerHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Integer
name|value
parameter_list|)
block|{
name|setHeader
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
name|Integer
name|getIntegerHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getIntegerProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|JoseHeaders
name|setLongHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Long
name|value
parameter_list|)
block|{
name|setHeader
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
name|Long
name|getLongHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getLongProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containsHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|containsProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

