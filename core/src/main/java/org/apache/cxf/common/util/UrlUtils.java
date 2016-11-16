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
name|common
operator|.
name|util
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
name|net
operator|.
name|URLEncoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|BufferUnderflowException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
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
name|Charset
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
name|HashMap
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
name|StringTokenizer
import|;
end_import

begin_comment
comment|/**  * Utility class for decoding and encoding URLs  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|UrlUtils
block|{
specifier|private
specifier|static
specifier|final
name|int
name|RADIX
init|=
literal|16
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
name|ESCAPE_CHAR
init|=
literal|'%'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
name|PLUS_CHAR
init|=
literal|'+'
decl_stmt|;
specifier|private
name|UrlUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|String
name|urlEncode
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|urlEncode
argument_list|(
name|value
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|urlEncode
parameter_list|(
name|String
name|value
parameter_list|,
name|String
name|enc
parameter_list|)
block|{
try|try
block|{
name|value
operator|=
name|URLEncoder
operator|.
name|encode
argument_list|(
name|value
argument_list|,
name|enc
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
comment|/**      * Decodes using URLDecoder - use when queries or form post values are decoded      * @param value value to decode      * @param enc encoding      */
specifier|public
specifier|static
name|String
name|urlDecode
parameter_list|(
name|String
name|value
parameter_list|,
name|String
name|enc
parameter_list|)
block|{
return|return
name|urlDecode
argument_list|(
name|value
argument_list|,
name|enc
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|urlDecode
parameter_list|(
name|String
name|value
parameter_list|,
name|String
name|enc
parameter_list|,
name|boolean
name|isPath
parameter_list|)
block|{
name|boolean
name|needDecode
init|=
literal|false
decl_stmt|;
name|int
name|escapesCount
init|=
literal|0
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
specifier|final
name|int
name|length
init|=
name|value
operator|.
name|length
argument_list|()
decl_stmt|;
while|while
condition|(
name|i
operator|<
name|length
condition|)
block|{
name|char
name|ch
init|=
name|value
operator|.
name|charAt
argument_list|(
name|i
operator|++
argument_list|)
decl_stmt|;
if|if
condition|(
name|ch
operator|==
name|ESCAPE_CHAR
condition|)
block|{
name|escapesCount
operator|+=
literal|1
expr_stmt|;
name|i
operator|+=
literal|2
expr_stmt|;
name|needDecode
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isPath
operator|&&
name|ch
operator|==
name|PLUS_CHAR
condition|)
block|{
name|needDecode
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|needDecode
condition|)
block|{
specifier|final
name|byte
index|[]
name|valueBytes
init|=
name|StringUtils
operator|.
name|toBytes
argument_list|(
name|value
argument_list|,
name|enc
argument_list|)
decl_stmt|;
name|ByteBuffer
name|in
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|valueBytes
argument_list|)
decl_stmt|;
name|ByteBuffer
name|out
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|in
operator|.
name|capacity
argument_list|()
operator|-
literal|2
operator|*
name|escapesCount
argument_list|)
decl_stmt|;
while|while
condition|(
name|in
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
specifier|final
name|int
name|b
init|=
name|in
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isPath
operator|&&
name|b
operator|==
name|PLUS_CHAR
condition|)
block|{
name|out
operator|.
name|put
argument_list|(
operator|(
name|byte
operator|)
literal|' '
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|b
operator|==
name|ESCAPE_CHAR
condition|)
block|{
try|try
block|{
specifier|final
name|int
name|u
init|=
name|digit16
argument_list|(
operator|(
name|byte
operator|)
name|in
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|int
name|l
init|=
name|digit16
argument_list|(
operator|(
name|byte
operator|)
name|in
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|out
operator|.
name|put
argument_list|(
call|(
name|byte
call|)
argument_list|(
operator|(
name|u
operator|<<
literal|4
operator|)
operator|+
name|l
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|BufferUnderflowException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URL encoding: Incomplete trailing escape (%) pattern"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|out
operator|.
name|put
argument_list|(
operator|(
name|byte
operator|)
name|b
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|flip
argument_list|()
expr_stmt|;
return|return
name|Charset
operator|.
name|forName
argument_list|(
name|enc
argument_list|)
operator|.
name|decode
argument_list|(
name|out
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|value
return|;
block|}
block|}
specifier|private
specifier|static
name|int
name|digit16
parameter_list|(
specifier|final
name|byte
name|b
parameter_list|)
block|{
specifier|final
name|int
name|i
init|=
name|Character
operator|.
name|digit
argument_list|(
operator|(
name|char
operator|)
name|b
argument_list|,
name|RADIX
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URL encoding: not a valid digit (radix "
operator|+
name|RADIX
operator|+
literal|"): "
operator|+
name|b
argument_list|)
throw|;
block|}
return|return
name|i
return|;
block|}
specifier|public
specifier|static
name|String
name|urlDecode
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|urlDecode
argument_list|(
name|value
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * URL path segments may contain '+' symbols which should not be decoded into ' '      * This method replaces '+' with %2B and delegates to URLDecoder      * @param value value to decode      */
specifier|public
specifier|static
name|String
name|pathDecode
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|urlDecode
argument_list|(
name|value
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Create a map from String to String that represents the contents of the query      * portion of a URL. For each x=y, x is the key and y is the value.      * @param s the query part of the URI.      * @return the map.      */
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parseQueryString
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ht
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|s
argument_list|,
literal|"&"
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|pair
init|=
name|st
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|int
name|pos
init|=
name|pair
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|==
operator|-
literal|1
condition|)
block|{
name|ht
operator|.
name|put
argument_list|(
name|pair
operator|.
name|toLowerCase
argument_list|()
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ht
operator|.
name|put
argument_list|(
name|pair
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|pair
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ht
return|;
block|}
comment|/**      * Return everything in the path up to the last slash in a URI.      * @param baseURI      * @return the trailing       */
specifier|public
specifier|static
name|String
name|getStem
parameter_list|(
name|String
name|baseURI
parameter_list|)
block|{
name|int
name|idx
init|=
name|baseURI
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|String
name|result
init|=
name|baseURI
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|result
operator|=
name|baseURI
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

