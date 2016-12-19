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
name|HashSet
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
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
name|UriInfo
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
name|jaxrs
operator|.
name|utils
operator|.
name|HttpUtils
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|RequestPreprocessor
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ACCEPT_QUERY
init|=
literal|"_type"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CTYPE_QUERY
init|=
literal|"_ctype"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|METHOD_QUERY
init|=
literal|"_method"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|METHOD_HEADER
init|=
literal|"X-HTTP-Method-Override"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|PATHS_TO_SKIP
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|MEDIA_TYPE_SHORTCUTS
decl_stmt|;
static|static
block|{
name|MEDIA_TYPE_SHORTCUTS
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|MEDIA_TYPE_SHORTCUTS
operator|.
name|put
argument_list|(
literal|"json"
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|MEDIA_TYPE_SHORTCUTS
operator|.
name|put
argument_list|(
literal|"text"
argument_list|,
literal|"text/*"
argument_list|)
expr_stmt|;
name|MEDIA_TYPE_SHORTCUTS
operator|.
name|put
argument_list|(
literal|"xml"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|MEDIA_TYPE_SHORTCUTS
operator|.
name|put
argument_list|(
literal|"atom"
argument_list|,
literal|"application/atom+xml"
argument_list|)
expr_stmt|;
name|MEDIA_TYPE_SHORTCUTS
operator|.
name|put
argument_list|(
literal|"html"
argument_list|,
literal|"text/html"
argument_list|)
expr_stmt|;
name|MEDIA_TYPE_SHORTCUTS
operator|.
name|put
argument_list|(
literal|"wadl"
argument_list|,
literal|"application/vnd.sun.wadl+xml"
argument_list|)
expr_stmt|;
name|PATHS_TO_SKIP
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|PATHS_TO_SKIP
operator|.
name|add
argument_list|(
literal|"swagger.json"
argument_list|)
expr_stmt|;
name|PATHS_TO_SKIP
operator|.
name|add
argument_list|(
literal|"swagger.yaml"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|languageMappings
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|extensionMappings
decl_stmt|;
specifier|public
name|RequestPreprocessor
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RequestPreprocessor
parameter_list|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|languageMappings
parameter_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|extensionMappings
parameter_list|)
block|{
name|this
operator|.
name|languageMappings
operator|=
name|languageMappings
operator|==
literal|null
condition|?
name|Collections
operator|.
name|emptyMap
argument_list|()
else|:
name|languageMappings
expr_stmt|;
name|this
operator|.
name|extensionMappings
operator|=
name|extensionMappings
operator|==
literal|null
condition|?
name|Collections
operator|.
name|emptyMap
argument_list|()
else|:
name|extensionMappings
expr_stmt|;
block|}
specifier|public
name|String
name|preprocess
parameter_list|(
name|Message
name|m
parameter_list|,
name|UriInfo
name|u
parameter_list|)
block|{
name|handleExtensionMappings
argument_list|(
name|m
argument_list|,
name|u
argument_list|)
expr_stmt|;
name|handleLanguageMappings
argument_list|(
name|m
argument_list|,
name|u
argument_list|)
expr_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queries
init|=
name|u
operator|.
name|getQueryParameters
argument_list|()
decl_stmt|;
name|handleTypeQuery
argument_list|(
name|m
argument_list|,
name|queries
argument_list|)
expr_stmt|;
name|handleCType
argument_list|(
name|m
argument_list|,
name|queries
argument_list|)
expr_stmt|;
name|handleMethod
argument_list|(
name|m
argument_list|,
name|queries
argument_list|,
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|UriInfoImpl
argument_list|(
name|m
argument_list|,
literal|null
argument_list|)
operator|.
name|getPath
argument_list|()
return|;
block|}
specifier|private
name|void
name|handleLanguageMappings
parameter_list|(
name|Message
name|m
parameter_list|,
name|UriInfo
name|uriInfo
parameter_list|)
block|{
if|if
condition|(
name|languageMappings
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|PathSegmentImpl
name|ps
init|=
operator|new
name|PathSegmentImpl
argument_list|(
name|uriInfo
operator|.
name|getPath
argument_list|(
literal|false
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|ps
operator|.
name|getPath
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|languageMappings
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
literal|"."
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|updateAcceptLanguageHeader
argument_list|(
name|m
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|updatePath
argument_list|(
name|m
argument_list|,
name|path
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|ps
operator|.
name|getMatrixString
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
specifier|private
name|void
name|handleExtensionMappings
parameter_list|(
name|Message
name|m
parameter_list|,
name|UriInfo
name|uriInfo
parameter_list|)
block|{
if|if
condition|(
name|extensionMappings
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|PathSegmentImpl
name|ps
init|=
operator|new
name|PathSegmentImpl
argument_list|(
name|uriInfo
operator|.
name|getPath
argument_list|(
literal|false
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|ps
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|PATHS_TO_SKIP
operator|.
name|contains
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|extensionMappings
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
literal|"."
operator|+
name|key
argument_list|)
condition|)
block|{
name|updateAcceptTypeHeader
argument_list|(
name|m
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|updatePath
argument_list|(
name|m
argument_list|,
name|path
argument_list|,
name|key
argument_list|,
name|ps
operator|.
name|getMatrixString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"wadl"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
comment|// the path has been updated and Accept was not necessarily set to
comment|// WADL type (xml or json or html - other options)
name|String
name|query
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|query
argument_list|)
condition|)
block|{
name|query
operator|=
literal|"_wadl"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|query
operator|.
name|contains
argument_list|(
literal|"_wadl"
argument_list|)
condition|)
block|{
name|query
operator|+=
literal|"&_wadl"
expr_stmt|;
block|}
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|updateAcceptLanguageHeader
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|anotherValue
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|acceptLanguage
init|=
operator|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
operator|)
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|acceptLanguage
operator|==
literal|null
condition|)
block|{
name|acceptLanguage
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|acceptLanguage
operator|.
name|add
argument_list|(
name|anotherValue
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
operator|)
operator|.
name|put
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|,
name|acceptLanguage
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|updatePath
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|suffix
parameter_list|,
name|String
name|matrixString
parameter_list|)
block|{
name|String
name|newPath
init|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|length
argument_list|()
operator|-
operator|(
name|suffix
operator|.
name|length
argument_list|()
operator|+
literal|1
operator|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|matrixString
operator|!=
literal|null
condition|)
block|{
name|newPath
operator|+=
name|matrixString
expr_stmt|;
block|}
name|HttpUtils
operator|.
name|updatePath
argument_list|(
name|m
argument_list|,
name|newPath
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|handleMethod
parameter_list|(
name|Message
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queries
parameter_list|,
name|HttpHeaders
name|headers
parameter_list|)
block|{
name|String
name|method
init|=
name|queries
operator|.
name|getFirst
argument_list|(
name|METHOD_QUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|headers
operator|.
name|getRequestHeader
argument_list|(
name|METHOD_HEADER
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
operator|&&
name|list
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|method
operator|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|handleTypeQuery
parameter_list|(
name|Message
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queries
parameter_list|)
block|{
name|String
name|type
init|=
name|queries
operator|.
name|getFirst
argument_list|(
name|ACCEPT_QUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|MEDIA_TYPE_SHORTCUTS
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|type
operator|=
name|MEDIA_TYPE_SHORTCUTS
operator|.
name|get
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
name|updateAcceptTypeHeader
argument_list|(
name|m
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|handleCType
parameter_list|(
name|Message
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queries
parameter_list|)
block|{
name|String
name|type
init|=
name|queries
operator|.
name|getFirst
argument_list|(
name|CTYPE_QUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|MEDIA_TYPE_SHORTCUTS
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|type
operator|=
name|MEDIA_TYPE_SHORTCUTS
operator|.
name|get
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|updateAcceptTypeHeader
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|acceptValue
parameter_list|)
block|{
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|acceptValue
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
operator|)
operator|.
name|put
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|acceptValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

