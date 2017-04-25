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
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|LinkedHashMap
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
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
name|Produces
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
name|MediaType
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|MessageBodyReader
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
name|ext
operator|.
name|MessageBodyWriter
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
name|ext
operator|.
name|Provider
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
name|util
operator|.
name|StringUtils
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
name|helpers
operator|.
name|IOUtils
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
name|JsonMapObjectReaderWriter
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
name|client
operator|.
name|OAuthClientUtils
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
name|ClientAccessToken
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
name|OAuthError
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
name|TokenIntrospection
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

begin_class
annotation|@
name|Provider
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
class|class
name|OAuthJSONProvider
implements|implements
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
implements|,
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
block|{
specifier|public
name|long
name|getSize
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clt
parameter_list|,
name|Type
name|t
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|boolean
name|isWriteable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|t
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|cls
operator|==
name|ClientAccessToken
operator|.
name|class
operator|||
name|cls
operator|==
name|OAuthError
operator|.
name|class
operator|||
name|cls
operator|==
name|TokenIntrospection
operator|.
name|class
return|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|t
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
if|if
condition|(
name|obj
operator|instanceof
name|ClientAccessToken
condition|)
block|{
name|writeAccessToken
argument_list|(
operator|(
name|ClientAccessToken
operator|)
name|obj
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|TokenIntrospection
condition|)
block|{
name|writeTokenIntrospection
argument_list|(
operator|(
name|TokenIntrospection
operator|)
name|obj
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writeOAuthError
argument_list|(
operator|(
name|OAuthError
operator|)
name|obj
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeTokenIntrospection
parameter_list|(
name|TokenIntrospection
name|obj
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{"
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
literal|"active"
argument_list|,
name|obj
operator|.
name|isActive
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|.
name|isActive
argument_list|()
condition|)
block|{
if|if
condition|(
name|obj
operator|.
name|getClientId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|,
name|obj
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|.
name|getUsername
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
literal|"username"
argument_list|,
name|obj
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|.
name|getTokenType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|OAuthConstants
operator|.
name|ACCESS_TOKEN_TYPE
argument_list|,
name|obj
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|.
name|getScope
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|OAuthConstants
operator|.
name|SCOPE
argument_list|,
name|obj
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|obj
operator|.
name|getAud
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|.
name|getAud
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
literal|"aud"
argument_list|,
name|obj
operator|.
name|getAud
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|StringBuilder
name|arr
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|arr
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|auds
init|=
name|obj
operator|.
name|getAud
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|auds
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|arr
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|arr
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
name|auds
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|arr
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
literal|"aud"
argument_list|,
name|arr
operator|.
name|toString
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|obj
operator|.
name|getIss
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
literal|"iss"
argument_list|,
name|obj
operator|.
name|getIss
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
literal|"iat"
argument_list|,
name|obj
operator|.
name|getIat
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|.
name|getExp
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
literal|"exp"
argument_list|,
name|obj
operator|.
name|getExp
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|obj
operator|.
name|getExtensions
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|obj
operator|.
name|getExtensions
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
if|if
condition|(
name|JoseConstants
operator|.
name|HEADER_X509_THUMBPRINT_SHA256
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|StringBuilder
name|cnfObj
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|cnfObj
operator|.
name|append
argument_list|(
literal|"{"
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|cnfObj
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|cnfObj
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
literal|"cnf"
argument_list|,
name|cnfObj
operator|.
name|toString
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
name|os
operator|.
name|write
argument_list|(
name|result
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|writeOAuthError
parameter_list|(
name|OAuthError
name|obj
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{"
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|OAuthConstants
operator|.
name|ERROR_KEY
argument_list|,
name|obj
operator|.
name|getError
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|.
name|getErrorDescription
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|OAuthConstants
operator|.
name|ERROR_DESCRIPTION_KEY
argument_list|,
name|obj
operator|.
name|getErrorDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|.
name|getErrorUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|OAuthConstants
operator|.
name|ERROR_URI_KEY
argument_list|,
name|obj
operator|.
name|getErrorUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
name|os
operator|.
name|write
argument_list|(
name|result
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|writeAccessToken
parameter_list|(
name|ClientAccessToken
name|obj
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{"
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|OAuthConstants
operator|.
name|ACCESS_TOKEN
argument_list|,
name|obj
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|OAuthConstants
operator|.
name|ACCESS_TOKEN_TYPE
argument_list|,
name|obj
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|.
name|getExpiresIn
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|OAuthConstants
operator|.
name|ACCESS_TOKEN_EXPIRES_IN
argument_list|,
name|obj
operator|.
name|getExpiresIn
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|.
name|getApprovedScope
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|OAuthConstants
operator|.
name|SCOPE
argument_list|,
name|obj
operator|.
name|getApprovedScope
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|.
name|getRefreshToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|OAuthConstants
operator|.
name|REFRESH_TOKEN
argument_list|,
name|obj
operator|.
name|getRefreshToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
name|obj
operator|.
name|getParameters
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
name|os
operator|.
name|write
argument_list|(
name|result
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|appendJsonPair
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|appendJsonPair
argument_list|(
name|sb
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|appendJsonPair
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|valueQuote
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
if|if
condition|(
name|valueQuote
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|valueQuote
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|t
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|Map
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
operator|||
name|ClientAccessToken
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
operator|||
name|TokenIntrospection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
return|;
block|}
specifier|public
name|Object
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|cls
parameter_list|,
name|Type
name|t
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
if|if
condition|(
name|TokenIntrospection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
condition|)
block|{
return|return
name|fromMapToTokenIntrospection
argument_list|(
name|is
argument_list|)
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|readJSONResponse
argument_list|(
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|Map
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
condition|)
block|{
return|return
name|params
return|;
block|}
else|else
block|{
name|ClientAccessToken
name|token
init|=
name|OAuthClientUtils
operator|.
name|fromMapToClientToken
argument_list|(
name|params
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|500
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|token
return|;
block|}
block|}
block|}
specifier|private
name|Object
name|fromMapToTokenIntrospection
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|TokenIntrospection
name|resp
init|=
operator|new
name|TokenIntrospection
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
operator|.
name|fromJson
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|resp
operator|.
name|setActive
argument_list|(
operator|(
name|Boolean
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"active"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|clientId
init|=
operator|(
name|String
operator|)
name|params
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|clientId
operator|!=
literal|null
condition|)
block|{
name|resp
operator|.
name|setClientId
argument_list|(
name|clientId
argument_list|)
expr_stmt|;
block|}
name|String
name|username
init|=
operator|(
name|String
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"username"
argument_list|)
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|resp
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
name|String
name|scope
init|=
operator|(
name|String
operator|)
name|params
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|scope
operator|!=
literal|null
condition|)
block|{
name|resp
operator|.
name|setScope
argument_list|(
name|scope
argument_list|)
expr_stmt|;
block|}
name|String
name|tokenType
init|=
operator|(
name|String
operator|)
name|params
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenType
operator|!=
literal|null
condition|)
block|{
name|resp
operator|.
name|setTokenType
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
block|}
name|Object
name|aud
init|=
name|params
operator|.
name|get
argument_list|(
literal|"aud"
argument_list|)
decl_stmt|;
if|if
condition|(
name|aud
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|aud
operator|.
name|getClass
argument_list|()
operator|==
name|String
operator|.
name|class
condition|)
block|{
name|resp
operator|.
name|setAud
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|String
operator|)
name|aud
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|auds
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|aud
decl_stmt|;
name|resp
operator|.
name|setAud
argument_list|(
name|auds
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|iss
init|=
operator|(
name|String
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"iss"
argument_list|)
decl_stmt|;
if|if
condition|(
name|iss
operator|!=
literal|null
condition|)
block|{
name|resp
operator|.
name|setIss
argument_list|(
name|iss
argument_list|)
expr_stmt|;
block|}
name|Long
name|iat
init|=
operator|(
name|Long
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"iat"
argument_list|)
decl_stmt|;
if|if
condition|(
name|iat
operator|!=
literal|null
condition|)
block|{
name|resp
operator|.
name|setIat
argument_list|(
name|iat
argument_list|)
expr_stmt|;
block|}
name|Long
name|exp
init|=
operator|(
name|Long
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"exp"
argument_list|)
decl_stmt|;
if|if
condition|(
name|exp
operator|!=
literal|null
condition|)
block|{
name|resp
operator|.
name|setExp
argument_list|(
name|exp
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cnf
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
name|params
operator|.
name|get
argument_list|(
literal|"cnf"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cnf
operator|!=
literal|null
condition|)
block|{
name|String
name|thumbprint
init|=
operator|(
name|String
operator|)
name|cnf
operator|.
name|get
argument_list|(
name|JoseConstants
operator|.
name|HEADER_X509_THUMBPRINT_SHA256
argument_list|)
decl_stmt|;
if|if
condition|(
name|thumbprint
operator|!=
literal|null
condition|)
block|{
name|resp
operator|.
name|getExtensions
argument_list|()
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|HEADER_X509_THUMBPRINT_SHA256
argument_list|,
name|thumbprint
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resp
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|readJSONResponse
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|str
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|str
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
if|if
condition|(
operator|!
name|str
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
operator|||
operator|!
name|str
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"JSON Sequence is broken"
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|str
operator|=
name|str
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|str
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
name|String
index|[]
name|jsonPairs
init|=
name|str
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|jsonPairs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|pair
init|=
name|jsonPairs
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|pair
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
name|int
name|index
init|=
name|pair
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|String
name|key
init|=
name|pair
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|&&
name|key
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|key
operator|=
name|key
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|key
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|value
init|=
name|pair
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|&&
name|value
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
end_class

end_unit

