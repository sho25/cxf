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
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|logging
operator|.
name|LogUtils
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
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|UrlUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESERVED_CHARS
init|=
block|{
literal|"+"
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|ENCODED_CHARS
init|=
block|{
literal|"%2b"
block|}
decl_stmt|;
specifier|private
name|UrlUtils
parameter_list|()
block|{              }
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
try|try
block|{
name|value
operator|=
name|URLDecoder
operator|.
name|decode
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
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"UTF-8 encoding can not be used to decode "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|value
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
literal|"UTF-8"
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
comment|// TODO: we actually need to do a proper URI analysis here according to
comment|// http://tools.ietf.org/html/rfc3986
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|RESERVED_CHARS
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|value
operator|.
name|indexOf
argument_list|(
name|RESERVED_CHARS
index|[
name|i
index|]
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|replace
argument_list|(
name|RESERVED_CHARS
index|[
name|i
index|]
argument_list|,
name|ENCODED_CHARS
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|urlDecode
argument_list|(
name|value
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
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
name|baseURI
argument_list|)
decl_stmt|;
name|baseURI
operator|=
name|uri
operator|.
name|getRawPath
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|baseURI
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
name|URI
operator|.
name|create
argument_list|(
name|baseURI
argument_list|)
operator|.
name|getPath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

