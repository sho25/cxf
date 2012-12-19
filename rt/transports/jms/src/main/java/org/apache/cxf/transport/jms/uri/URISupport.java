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
name|jms
operator|.
name|uri
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
name|URISyntaxException
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
name|net
operator|.
name|URLEncoder
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

begin_comment
comment|/**  * URI utilities.  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|URISupport
block|{
specifier|private
name|URISupport
parameter_list|()
block|{
comment|// Helper class
block|}
comment|/**      * Holder to get parts of the URI.      */
specifier|public
specifier|static
class|class
name|CompositeData
block|{
specifier|private
name|String
name|host
decl_stmt|;
specifier|private
name|String
name|scheme
decl_stmt|;
specifier|private
name|String
name|path
decl_stmt|;
specifier|private
name|URI
name|components
index|[]
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
decl_stmt|;
specifier|private
name|String
name|fragment
decl_stmt|;
specifier|public
name|URI
index|[]
name|getComponents
parameter_list|()
block|{
return|return
name|components
return|;
block|}
specifier|public
name|String
name|getFragment
parameter_list|()
block|{
return|return
name|fragment
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
name|parameters
return|;
block|}
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|scheme
return|;
block|}
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
specifier|public
name|URI
name|toURI
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|scheme
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|host
operator|!=
literal|null
operator|&&
name|host
operator|.
name|length
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|components
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'('
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
name|components
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|!=
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|components
index|[
name|i
index|]
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|createQueryString
argument_list|(
name|parameters
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fragment
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"#"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|fragment
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|URI
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parseQuery
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|rc
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
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|parameters
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|uri
argument_list|,
literal|"&"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|parameter
range|:
name|parameters
control|)
block|{
name|int
name|p
init|=
name|parameter
operator|.
name|indexOf
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|>=
literal|0
condition|)
block|{
name|String
name|name
init|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|parameter
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|parameter
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|1
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|rc
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rc
operator|.
name|put
argument_list|(
name|parameter
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|rc
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|URISyntaxException
name|se
init|=
operator|new
name|URISyntaxException
argument_list|(
name|e
operator|.
name|toString
argument_list|()
argument_list|,
literal|"Invalid encoding"
argument_list|)
decl_stmt|;
name|se
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|se
throw|;
block|}
block|}
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parseParameters
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
name|query
init|=
name|uri
operator|.
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
name|String
name|schemeSpecificPart
init|=
name|uri
operator|.
name|getSchemeSpecificPart
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|schemeSpecificPart
operator|.
name|lastIndexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
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
else|else
block|{
name|query
operator|=
name|schemeSpecificPart
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|query
operator|=
name|stripPrefix
argument_list|(
name|query
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
block|}
return|return
name|parseQuery
argument_list|(
name|query
argument_list|)
return|;
block|}
comment|/**      * Removes any URI query from the given uri      */
specifier|public
specifier|static
name|URI
name|removeQuery
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
name|createURIWithQuery
argument_list|(
name|uri
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Creates a URI with the given query      */
specifier|public
specifier|static
name|URI
name|createURIWithQuery
parameter_list|(
name|URI
name|uri
parameter_list|,
name|String
name|query
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
operator|new
name|URI
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|,
name|uri
operator|.
name|getUserInfo
argument_list|()
argument_list|,
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|uri
operator|.
name|getPort
argument_list|()
argument_list|,
name|uri
operator|.
name|getPath
argument_list|()
argument_list|,
name|query
argument_list|,
name|uri
operator|.
name|getFragment
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|CompositeData
name|parseComposite
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|CompositeData
name|rc
init|=
operator|new
name|CompositeData
argument_list|()
decl_stmt|;
name|rc
operator|.
name|scheme
operator|=
name|uri
operator|.
name|getScheme
argument_list|()
expr_stmt|;
name|String
name|ssp
init|=
name|stripPrefix
argument_list|(
name|uri
operator|.
name|getSchemeSpecificPart
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
literal|"//"
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|parseComposite
argument_list|(
name|uri
argument_list|,
name|rc
argument_list|,
name|ssp
argument_list|)
expr_stmt|;
name|rc
operator|.
name|fragment
operator|=
name|uri
operator|.
name|getFragment
argument_list|()
expr_stmt|;
return|return
name|rc
return|;
block|}
specifier|private
specifier|static
name|void
name|parseComposite
parameter_list|(
name|URI
name|uri
parameter_list|,
name|CompositeData
name|rc
parameter_list|,
name|String
name|ssp
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
name|params
decl_stmt|;
if|if
condition|(
operator|!
name|checkParenthesis
argument_list|(
name|ssp
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|URISyntaxException
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|,
literal|"Not a matching number of '(' and ')' parenthesis"
argument_list|)
throw|;
block|}
name|int
name|p
decl_stmt|;
name|int
name|intialParen
init|=
name|ssp
operator|.
name|indexOf
argument_list|(
literal|"("
argument_list|)
decl_stmt|;
if|if
condition|(
name|intialParen
operator|==
literal|0
condition|)
block|{
name|rc
operator|.
name|host
operator|=
name|ssp
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|intialParen
argument_list|)
expr_stmt|;
name|p
operator|=
name|rc
operator|.
name|host
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|>=
literal|0
condition|)
block|{
name|rc
operator|.
name|path
operator|=
name|rc
operator|.
name|host
operator|.
name|substring
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|rc
operator|.
name|host
operator|=
name|rc
operator|.
name|host
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
name|p
operator|=
name|ssp
operator|.
name|lastIndexOf
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
name|params
operator|=
name|ssp
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|params
operator|=
literal|""
expr_stmt|;
block|}
name|p
operator|=
name|params
operator|.
name|indexOf
argument_list|(
literal|"?"
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|>=
literal|0
condition|)
block|{
if|if
condition|(
name|p
operator|>
literal|0
condition|)
block|{
name|rc
operator|.
name|path
operator|=
name|stripPrefix
argument_list|(
name|params
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p
argument_list|)
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
block|}
name|rc
operator|.
name|parameters
operator|=
name|parseQuery
argument_list|(
name|params
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|params
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|rc
operator|.
name|path
operator|=
name|stripPrefix
argument_list|(
name|params
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
block|}
name|rc
operator|.
name|parameters
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|String
name|stripPrefix
parameter_list|(
name|String
name|value
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
return|return
name|value
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
return|return
name|value
return|;
block|}
specifier|public
specifier|static
name|URI
name|stripScheme
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
operator|new
name|URI
argument_list|(
name|stripPrefix
argument_list|(
name|uri
operator|.
name|getSchemeSpecificPart
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
literal|"//"
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|createQueryString
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
parameter_list|)
throws|throws
name|URISyntaxException
block|{
try|try
block|{
if|if
condition|(
name|options
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|rc
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
name|String
name|key
range|:
name|options
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|rc
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
block|}
name|String
name|value
init|=
name|options
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|rc
operator|.
name|append
argument_list|(
name|URLEncoder
operator|.
name|encode
argument_list|(
name|key
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|rc
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
name|rc
operator|.
name|append
argument_list|(
name|URLEncoder
operator|.
name|encode
argument_list|(
name|value
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|rc
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|URISyntaxException
name|se
init|=
operator|new
name|URISyntaxException
argument_list|(
name|e
operator|.
name|toString
argument_list|()
argument_list|,
literal|"Invalid encoding"
argument_list|)
decl_stmt|;
name|se
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|se
throw|;
block|}
block|}
comment|/**      * Creates a URI from the original URI and the remaining parameters      */
specifier|public
specifier|static
name|URI
name|createRemainingURI
parameter_list|(
name|URI
name|originalURI
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
name|s
init|=
name|createQueryString
argument_list|(
name|params
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|s
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|createURIWithQuery
argument_list|(
name|originalURI
argument_list|,
name|s
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|URI
name|changeScheme
parameter_list|(
name|URI
name|bindAddr
parameter_list|,
name|String
name|scheme
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
operator|new
name|URI
argument_list|(
name|scheme
argument_list|,
name|bindAddr
operator|.
name|getUserInfo
argument_list|()
argument_list|,
name|bindAddr
operator|.
name|getHost
argument_list|()
argument_list|,
name|bindAddr
operator|.
name|getPort
argument_list|()
argument_list|,
name|bindAddr
operator|.
name|getPath
argument_list|()
argument_list|,
name|bindAddr
operator|.
name|getQuery
argument_list|()
argument_list|,
name|bindAddr
operator|.
name|getFragment
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|checkParenthesis
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|str
operator|!=
literal|null
condition|)
block|{
name|int
name|open
init|=
literal|0
decl_stmt|;
name|int
name|closed
init|=
literal|0
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|i
operator|=
name|str
operator|.
name|indexOf
argument_list|(
literal|'('
argument_list|,
name|i
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|i
operator|++
expr_stmt|;
name|open
operator|++
expr_stmt|;
block|}
name|i
operator|=
literal|0
expr_stmt|;
while|while
condition|(
operator|(
name|i
operator|=
name|str
operator|.
name|indexOf
argument_list|(
literal|')'
argument_list|,
name|i
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|i
operator|++
expr_stmt|;
name|closed
operator|++
expr_stmt|;
block|}
name|result
operator|=
name|open
operator|==
name|closed
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

