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
name|attachment
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_class
specifier|public
class|class
name|ContentDisposition
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CD_HEADER_PARAMS_EXPRESSION
init|=
literal|"(([\\w]+( )?\\*?=( )?\"[^\"]+\")|([\\w]+( )?\\*?=( )?[^;]+))"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|CD_HEADER_PARAMS_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|CD_HEADER_PARAMS_EXPRESSION
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CD_HEADER_EXT_PARAMS_EXPRESSION
init|=
literal|"(UTF-8|ISO-8859-1)''((?:%[0-9a-f]{2}|\\S)+)"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|CD_HEADER_EXT_PARAMS_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|CD_HEADER_EXT_PARAMS_EXPRESSION
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|CODEPOINT_ENCODED_VALUE_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"&#[0-9]{4};|\\S"
argument_list|)
decl_stmt|;
specifier|private
name|String
name|value
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
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
specifier|public
name|ContentDisposition
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|String
name|tempValue
init|=
name|value
decl_stmt|;
name|int
name|index
init|=
name|tempValue
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
operator|&&
operator|!
operator|(
name|tempValue
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
operator|<
name|index
operator|)
condition|)
block|{
name|type
operator|=
name|tempValue
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
expr_stmt|;
name|tempValue
operator|=
name|tempValue
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|extendedFilename
init|=
literal|null
decl_stmt|;
name|Matcher
name|m
init|=
name|CD_HEADER_PARAMS_PATTERN
operator|.
name|matcher
argument_list|(
name|tempValue
argument_list|)
decl_stmt|;
while|while
condition|(
name|m
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
index|[]
name|pair
init|=
name|m
operator|.
name|group
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
name|String
name|paramName
init|=
name|pair
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|paramValue
init|=
name|pair
operator|.
name|length
operator|==
literal|2
condition|?
name|pair
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
operator|.
name|replace
argument_list|(
literal|"\""
argument_list|,
literal|""
argument_list|)
else|:
literal|""
decl_stmt|;
comment|// filename* looks like the only CD param that is human readable
comment|// and worthy of the extended encoding support. Other parameters
comment|// can be supported if needed, see the complete list below
comment|/*                 http://www.iana.org/assignments/cont-disp/cont-disp.xhtml#cont-disp-2                  filename            name to be used when creating file [RFC2183]                 creation-date       date when content was created [RFC2183]                 modification-date   date when content was last modified [RFC2183]                 read-date           date when content was last read [RFC2183]                 size                approximate size of content in octets [RFC2183]                 name                original field name in form [RFC2388]                 voice               type or use of audio content [RFC2421]                 handling            whether or not processing is required [RFC3204]              */
if|if
condition|(
literal|"filename*"
operator|.
name|equals
argument_list|(
name|paramName
argument_list|)
condition|)
block|{
comment|// try to decode the value if it matches the spec
try|try
block|{
name|Matcher
name|matcher
init|=
name|CD_HEADER_EXT_PARAMS_PATTERN
operator|.
name|matcher
argument_list|(
name|paramValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|encodingScheme
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|encodedValue
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|paramValue
operator|=
name|Rfc5987Util
operator|.
name|decode
argument_list|(
name|encodedValue
argument_list|,
name|encodingScheme
argument_list|)
expr_stmt|;
name|extendedFilename
operator|=
name|paramValue
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// would be odd not to support UTF-8 or 8859-1
block|}
block|}
elseif|else
if|if
condition|(
literal|"filename"
operator|.
name|equals
argument_list|(
name|paramName
argument_list|)
operator|&&
name|paramValue
operator|.
name|contains
argument_list|(
literal|"&#"
argument_list|)
condition|)
block|{
name|Matcher
name|matcher
init|=
name|CODEPOINT_ENCODED_VALUE_PATTERN
operator|.
name|matcher
argument_list|(
name|paramValue
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|matched
init|=
name|matcher
operator|.
name|group
argument_list|()
decl_stmt|;
if|if
condition|(
name|matched
operator|.
name|startsWith
argument_list|(
literal|"&#"
argument_list|)
condition|)
block|{
name|int
name|codePoint
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|matched
operator|.
name|substring
argument_list|(
literal|2
argument_list|,
literal|6
argument_list|)
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toChars
argument_list|(
name|codePoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|matched
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|paramValue
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
name|params
operator|.
name|put
argument_list|(
name|paramName
argument_list|,
name|paramValue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|extendedFilename
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"filename"
argument_list|,
name|extendedFilename
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|String
name|getFilename
parameter_list|()
block|{
return|return
name|params
operator|.
name|get
argument_list|(
literal|"filename"
argument_list|)
return|;
block|}
specifier|public
name|String
name|getParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|params
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|params
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

