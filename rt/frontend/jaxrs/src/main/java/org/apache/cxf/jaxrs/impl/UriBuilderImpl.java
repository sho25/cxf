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
name|jaxrs
operator|.
name|impl
package|;
end_package

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
name|Method
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
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
name|core
operator|.
name|PathSegment
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
name|UriBuilder
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
name|UriBuilderException
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
name|model
operator|.
name|URITemplate
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
name|utils
operator|.
name|JAXRSUtils
import|;
end_import

begin_class
specifier|public
class|class
name|UriBuilderImpl
extends|extends
name|UriBuilder
block|{
specifier|private
specifier|static
specifier|final
name|Pattern
name|DECODE_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"%[0-9a-fA-F][0-9a-fA-F]"
argument_list|)
decl_stmt|;
specifier|private
name|String
name|scheme
decl_stmt|;
specifier|private
name|String
name|userInfo
decl_stmt|;
specifier|private
name|int
name|port
decl_stmt|;
specifier|private
name|String
name|host
decl_stmt|;
specifier|private
name|List
argument_list|<
name|PathSegment
argument_list|>
name|paths
init|=
operator|new
name|ArrayList
argument_list|<
name|PathSegment
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|fragment
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|query
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Creates builder with empty URI.      */
specifier|public
name|UriBuilderImpl
parameter_list|()
block|{     }
comment|/**      * Creates builder initialized with given URI.      *       * @param uri initial value for builder      * @throws IllegalArgumentException when uri is null      */
specifier|public
name|UriBuilderImpl
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|setUriParts
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|build
parameter_list|(
name|Object
modifier|...
name|values
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|UriBuilderException
block|{
try|try
block|{
name|String
name|path
init|=
name|buildPath
argument_list|()
decl_stmt|;
name|path
operator|=
name|substituteVarargs
argument_list|(
name|path
argument_list|,
name|values
argument_list|)
expr_stmt|;
return|return
operator|new
name|URI
argument_list|(
name|scheme
argument_list|,
name|userInfo
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|path
argument_list|,
name|buildQuery
argument_list|()
argument_list|,
name|fragment
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|UriBuilderException
argument_list|(
literal|"URI can not be built"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|substituteVarargs
parameter_list|(
name|String
name|path
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|varValueMap
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
name|URITemplate
name|templ
init|=
operator|new
name|URITemplate
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// vars in set are properly ordered due to linking in hash set
name|Set
argument_list|<
name|String
argument_list|>
name|uniqueVars
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|templ
operator|.
name|getVariables
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|.
name|length
operator|<
name|uniqueVars
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unresolved variables; only "
operator|+
name|values
operator|.
name|length
operator|+
literal|" value(s) given for "
operator|+
name|uniqueVars
operator|.
name|size
argument_list|()
operator|+
literal|" unique variable(s)"
argument_list|)
throw|;
block|}
name|int
name|idx
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|var
range|:
name|uniqueVars
control|)
block|{
name|Object
name|oval
init|=
name|values
index|[
name|idx
operator|++
index|]
decl_stmt|;
name|varValueMap
operator|.
name|put
argument_list|(
name|var
argument_list|,
name|oval
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|templ
operator|.
name|substitute
argument_list|(
name|varValueMap
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|buildFromEncoded
parameter_list|(
name|Object
modifier|...
name|values
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|UriBuilderException
block|{
comment|// Problem: multi-arg URI c-tor always forces encoding, operation contract would be broken;
comment|// use os single-arg URI c-tor requires unnecessary concatenate-parse roundtrip.
comment|// Solution: decode back given values and pass as non-decoded to regular build() method
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|values
index|[
name|i
index|]
operator|=
name|decodePartiallyEncoded
argument_list|(
name|values
index|[
name|i
index|]
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|build
argument_list|(
name|values
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|buildFromMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|map
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|UriBuilderException
block|{
try|try
block|{
name|String
name|path
init|=
name|buildPath
argument_list|()
decl_stmt|;
name|path
operator|=
name|substituteMapped
argument_list|(
name|path
argument_list|,
name|map
argument_list|)
expr_stmt|;
return|return
operator|new
name|URI
argument_list|(
name|scheme
argument_list|,
name|userInfo
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|path
argument_list|,
name|buildQuery
argument_list|()
argument_list|,
name|fragment
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|UriBuilderException
argument_list|(
literal|"URI can not be built"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|substituteMapped
parameter_list|(
name|String
name|path
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|varValueMap
parameter_list|)
block|{
name|URITemplate
name|templ
init|=
operator|new
name|URITemplate
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|uniqueVars
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|templ
operator|.
name|getVariables
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|varValueMap
operator|.
name|size
argument_list|()
operator|<
name|uniqueVars
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unresolved variables; only "
operator|+
name|varValueMap
operator|.
name|size
argument_list|()
operator|+
literal|" value(s) given for "
operator|+
name|uniqueVars
operator|.
name|size
argument_list|()
operator|+
literal|" unique variable(s)"
argument_list|)
throw|;
block|}
return|return
name|templ
operator|.
name|substitute
argument_list|(
name|varValueMap
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|buildFromEncodedMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|map
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|UriBuilderException
block|{
comment|// see buildFromEncoded() comment
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|decodedMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|decodedMap
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|decodePartiallyEncoded
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|buildFromMap
argument_list|(
name|decodedMap
argument_list|)
return|;
block|}
comment|// CHECKSTYLE:OFF
annotation|@
name|Override
specifier|public
name|UriBuilder
name|clone
parameter_list|()
block|{
return|return
operator|new
name|UriBuilderImpl
argument_list|(
name|build
argument_list|()
argument_list|)
return|;
block|}
comment|// CHECKSTYLE:ON
annotation|@
name|Override
specifier|public
name|UriBuilder
name|fragment
parameter_list|(
name|String
name|theFragment
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|this
operator|.
name|fragment
operator|=
name|theFragment
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|host
parameter_list|(
name|String
name|theHost
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|this
operator|.
name|host
operator|=
name|theHost
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|UriBuilder
name|path
parameter_list|(
name|Class
name|resource
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"resource is null"
argument_list|)
throw|;
block|}
name|Annotation
name|ann
init|=
name|resource
operator|.
name|getAnnotation
argument_list|(
name|Path
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Class '"
operator|+
name|resource
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"' is not annotated with Path"
argument_list|)
throw|;
block|}
comment|// path(String) decomposes multi-segment path when necessary
return|return
name|path
argument_list|(
operator|(
operator|(
name|Path
operator|)
name|ann
operator|)
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|UriBuilder
name|path
parameter_list|(
name|Class
name|resource
parameter_list|,
name|String
name|method
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"resource is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"method is null"
argument_list|)
throw|;
block|}
name|Annotation
name|foundAnn
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Method
name|meth
range|:
name|resource
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|meth
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|Annotation
name|ann
init|=
name|meth
operator|.
name|getAnnotation
argument_list|(
name|Path
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|foundAnn
operator|!=
literal|null
operator|&&
name|ann
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Multiple Path annotations for '"
operator|+
name|method
operator|+
literal|"' overloaded method"
argument_list|)
throw|;
block|}
name|foundAnn
operator|=
name|ann
expr_stmt|;
block|}
block|}
if|if
condition|(
name|foundAnn
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No Path annotation for '"
operator|+
name|method
operator|+
literal|"' method"
argument_list|)
throw|;
block|}
comment|// path(String) decomposes multi-segment path when necessary
return|return
name|path
argument_list|(
operator|(
operator|(
name|Path
operator|)
name|foundAnn
operator|)
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|path
parameter_list|(
name|Method
name|method
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"method is null"
argument_list|)
throw|;
block|}
name|Annotation
name|ann
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Path
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method '"
operator|+
name|method
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"."
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"' is not annotated with Path"
argument_list|)
throw|;
block|}
comment|// path(String) decomposes multi-segment path when necessary
return|return
name|path
argument_list|(
operator|(
operator|(
name|Path
operator|)
name|ann
operator|)
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|path
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|List
argument_list|<
name|PathSegment
argument_list|>
name|segments
init|=
name|JAXRSUtils
operator|.
name|getPathSegments
argument_list|(
name|path
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|paths
operator|.
name|addAll
argument_list|(
name|segments
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|port
parameter_list|(
name|int
name|thePort
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|this
operator|.
name|port
operator|=
name|thePort
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|scheme
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|scheme
operator|=
name|s
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|schemeSpecificPart
parameter_list|(
name|String
name|ssp
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// scheme-specific part is whatever after ":" of URI
comment|// see: http://en.wikipedia.org/wiki/URI_scheme
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"whatever://"
operator|+
name|ssp
argument_list|)
decl_stmt|;
name|port
operator|=
name|uri
operator|.
name|getPort
argument_list|()
expr_stmt|;
name|host
operator|=
name|uri
operator|.
name|getHost
argument_list|()
expr_stmt|;
name|paths
operator|=
name|JAXRSUtils
operator|.
name|getPathSegments
argument_list|(
name|uri
operator|.
name|getPath
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fragment
operator|=
name|uri
operator|.
name|getFragment
argument_list|()
expr_stmt|;
name|query
operator|=
name|JAXRSUtils
operator|.
name|getStructuredParams
argument_list|(
name|uri
operator|.
name|getQuery
argument_list|()
argument_list|,
literal|"&"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|userInfo
operator|=
name|uri
operator|.
name|getUserInfo
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Wrong syntax of scheme-specific part"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|uri
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|setUriParts
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|userInfo
parameter_list|(
name|String
name|ui
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|this
operator|.
name|userInfo
operator|=
name|ui
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|private
name|void
name|setUriParts
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"uri is null"
argument_list|)
throw|;
block|}
name|scheme
operator|=
name|uri
operator|.
name|getScheme
argument_list|()
expr_stmt|;
name|port
operator|=
name|uri
operator|.
name|getPort
argument_list|()
expr_stmt|;
name|host
operator|=
name|uri
operator|.
name|getHost
argument_list|()
expr_stmt|;
name|paths
operator|=
name|JAXRSUtils
operator|.
name|getPathSegments
argument_list|(
name|uri
operator|.
name|getPath
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fragment
operator|=
name|uri
operator|.
name|getFragment
argument_list|()
expr_stmt|;
name|query
operator|=
name|JAXRSUtils
operator|.
name|getStructuredParams
argument_list|(
name|uri
operator|.
name|getQuery
argument_list|()
argument_list|,
literal|"&"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|userInfo
operator|=
name|uri
operator|.
name|getUserInfo
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|buildPath
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|PathSegment
argument_list|>
name|iter
init|=
name|paths
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|p
init|=
name|iter
operator|.
name|next
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|length
argument_list|()
operator|!=
literal|0
operator|||
operator|!
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|p
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|buildQuery
parameter_list|()
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|>
name|it
init|=
name|query
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|b
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
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'&'
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|b
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|b
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|matrixParam
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// TODO Auto-generated method stub
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented :/"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|queryParam
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|queryList
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
name|queryList
operator|.
name|add
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|query
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|queryList
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|replaceMatrix
parameter_list|(
name|String
name|matrix
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// TODO Auto-generated method stub
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented :/"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|replaceMatrixParam
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// TODO Auto-generated method stub
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented :/"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|replacePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|paths
operator|=
name|JAXRSUtils
operator|.
name|getPathSegments
argument_list|(
name|path
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|replaceQuery
parameter_list|(
name|String
name|queryValue
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|query
operator|=
name|JAXRSUtils
operator|.
name|getStructuredParams
argument_list|(
name|queryValue
argument_list|,
literal|"&"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|segment
parameter_list|(
name|String
modifier|...
name|segments
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
for|for
control|(
name|String
name|segment
range|:
name|segments
control|)
block|{
name|path
argument_list|(
name|segment
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|replaceQueryParam
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// TODO Auto-generated method stub
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented :/"
argument_list|)
throw|;
block|}
comment|/**      * Decode partially encoded string. Decode only values that matches patter "percent char followed by two      * hexadecimal digits".      *       * @param encoded fully or partially encoded string.      * @return decoded string      */
specifier|private
name|String
name|decodePartiallyEncoded
parameter_list|(
name|String
name|encoded
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|DECODE_PATTERN
operator|.
name|matcher
argument_list|(
name|encoded
argument_list|)
decl_stmt|;
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
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
name|found
init|=
name|m
operator|.
name|group
argument_list|()
decl_stmt|;
name|m
operator|.
name|appendReplacement
argument_list|(
name|sb
argument_list|,
name|JAXRSUtils
operator|.
name|uriDecode
argument_list|(
name|found
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|appendTail
argument_list|(
name|sb
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

