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
name|transport
operator|.
name|http
operator|.
name|auth
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
name|StreamTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_class
specifier|public
specifier|final
class|class
name|HttpAuthHeader
block|{
specifier|public
specifier|static
specifier|final
name|String
name|AUTH_TYPE_BASIC
init|=
literal|"Basic"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AUTH_TYPE_DIGEST
init|=
literal|"Digest"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AUTH_TYPE_NEGOTIATE
init|=
literal|"Negotiate"
decl_stmt|;
specifier|private
name|String
name|fullHeader
decl_stmt|;
specifier|private
name|String
name|authType
decl_stmt|;
specifier|private
name|String
name|fullContent
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
decl_stmt|;
specifier|public
name|HttpAuthHeader
parameter_list|(
name|String
name|fullHeader
parameter_list|)
block|{
name|this
operator|.
name|fullHeader
operator|=
operator|(
name|fullHeader
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
name|fullHeader
expr_stmt|;
name|int
name|spacePos
init|=
name|this
operator|.
name|fullHeader
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|spacePos
operator|==
operator|-
literal|1
condition|)
block|{
name|this
operator|.
name|authType
operator|=
name|this
operator|.
name|fullHeader
expr_stmt|;
name|this
operator|.
name|fullContent
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|authType
operator|=
name|this
operator|.
name|fullHeader
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|spacePos
argument_list|)
expr_stmt|;
name|this
operator|.
name|fullContent
operator|=
name|this
operator|.
name|fullHeader
operator|.
name|substring
argument_list|(
name|spacePos
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|params
operator|=
name|parseHeader
argument_list|()
expr_stmt|;
block|}
specifier|public
name|HttpAuthHeader
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|params
control|)
block|{
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|fullHeader
operator|+=
literal|", "
operator|+
name|s
expr_stmt|;
block|}
else|else
block|{
name|first
operator|=
literal|false
expr_stmt|;
name|fullHeader
operator|=
name|s
expr_stmt|;
block|}
block|}
name|int
name|spacePos
init|=
name|this
operator|.
name|fullHeader
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|spacePos
operator|==
operator|-
literal|1
condition|)
block|{
name|this
operator|.
name|authType
operator|=
name|this
operator|.
name|fullHeader
expr_stmt|;
name|this
operator|.
name|fullContent
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|authType
operator|=
name|this
operator|.
name|fullHeader
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|spacePos
argument_list|)
expr_stmt|;
name|this
operator|.
name|fullContent
operator|=
name|this
operator|.
name|fullHeader
operator|.
name|substring
argument_list|(
name|spacePos
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|params
operator|=
name|parseHeader
argument_list|()
expr_stmt|;
block|}
specifier|public
name|HttpAuthHeader
parameter_list|(
name|String
name|authType
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|this
operator|.
name|authType
operator|=
name|authType
expr_stmt|;
name|this
operator|.
name|params
operator|=
name|params
expr_stmt|;
name|this
operator|.
name|fullContent
operator|=
name|paramsToString
argument_list|()
expr_stmt|;
name|this
operator|.
name|fullHeader
operator|=
name|authType
operator|+
literal|" "
operator|+
name|fullContent
expr_stmt|;
block|}
specifier|private
name|String
name|paramsToString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
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
name|params
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|param
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|param
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"nc"
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|||
literal|"qop"
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|||
literal|"algorithm"
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
name|builder
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"=\""
argument_list|)
operator|.
name|append
argument_list|(
name|param
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
name|first
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parseHeader
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
name|StreamTokenizer
name|tok
init|=
operator|new
name|StreamTokenizer
argument_list|(
operator|new
name|StringReader
argument_list|(
name|this
operator|.
name|fullContent
argument_list|)
argument_list|)
decl_stmt|;
name|tok
operator|.
name|quoteChar
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|tok
operator|.
name|quoteChar
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|tok
operator|.
name|whitespaceChars
argument_list|(
literal|'='
argument_list|,
literal|'='
argument_list|)
expr_stmt|;
name|tok
operator|.
name|whitespaceChars
argument_list|(
literal|','
argument_list|,
literal|','
argument_list|)
expr_stmt|;
while|while
condition|(
name|tok
operator|.
name|nextToken
argument_list|()
operator|!=
name|StreamTokenizer
operator|.
name|TT_EOF
condition|)
block|{
name|String
name|key
init|=
name|tok
operator|.
name|sval
decl_stmt|;
if|if
condition|(
name|tok
operator|.
name|nextToken
argument_list|()
operator|==
name|StreamTokenizer
operator|.
name|TT_EOF
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
name|String
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"nc"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
comment|//nc is a 8 length HEX number so need get it as number
name|value
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|tok
operator|.
name|nval
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>
literal|0
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|value
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|StringBuilder
name|pad
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|pad
operator|.
name|append
argument_list|(
literal|""
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|8
operator|-
name|value
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|pad
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
block|}
name|value
operator|=
name|pad
operator|.
name|toString
argument_list|()
operator|+
name|value
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|tok
operator|.
name|sval
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
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|//ignore can't happen for StringReader
block|}
return|return
name|map
return|;
block|}
comment|/**      * Extracts the authorization realm from the      * "WWW-Authenticate" Http response header.      *      * @return The realm, or null if it is non-existent.      */
specifier|public
name|String
name|getRealm
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|parseHeader
argument_list|()
decl_stmt|;
return|return
name|map
operator|.
name|get
argument_list|(
literal|"realm"
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|authTypeIsDigest
parameter_list|()
block|{
return|return
name|AUTH_TYPE_DIGEST
operator|.
name|equals
argument_list|(
name|this
operator|.
name|authType
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|authTypeIsBasic
parameter_list|()
block|{
return|return
name|AUTH_TYPE_BASIC
operator|.
name|equals
argument_list|(
name|this
operator|.
name|authType
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|authTypeIsNegotiate
parameter_list|()
block|{
return|return
name|AUTH_TYPE_NEGOTIATE
operator|.
name|equals
argument_list|(
name|this
operator|.
name|authType
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAuthType
parameter_list|()
block|{
return|return
name|authType
return|;
block|}
specifier|public
name|String
name|getFullContent
parameter_list|()
block|{
return|return
name|fullContent
return|;
block|}
specifier|public
name|String
name|getFullHeader
parameter_list|()
block|{
return|return
name|this
operator|.
name|fullHeader
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getParams
parameter_list|()
block|{
return|return
name|params
return|;
block|}
block|}
end_class

end_unit

