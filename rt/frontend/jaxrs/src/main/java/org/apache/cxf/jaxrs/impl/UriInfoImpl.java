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
name|net
operator|.
name|URI
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
name|Collections
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
name|logging
operator|.
name|LogUtils
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
name|MethodInvocationInfo
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
name|OperationResourceInfo
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
name|OperationResourceInfoStack
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|MessageUtils
import|;
end_import

begin_class
specifier|public
class|class
name|UriInfoImpl
implements|implements
name|UriInfo
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
name|UriInfoImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CASE_INSENSITIVE_QUERIES
init|=
literal|"org.apache.cxf.http.case_insensitive_queries"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PARSE_QUERY_VALUE_AS_COLLECTION
init|=
literal|"parse.query.value.as.collection"
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|templateParams
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|OperationResourceInfoStack
name|stack
decl_stmt|;
specifier|private
name|boolean
name|caseInsensitiveQueries
decl_stmt|;
specifier|private
name|boolean
name|queryValueIsCollection
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|UriInfoImpl
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|this
argument_list|(
name|m
argument_list|,
operator|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|m
operator|.
name|get
argument_list|(
name|URITemplate
operator|.
name|TEMPLATE_PARAMETERS
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|UriInfoImpl
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
name|templateParams
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|m
expr_stmt|;
name|this
operator|.
name|templateParams
operator|=
name|templateParams
expr_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|stack
operator|=
name|m
operator|.
name|get
argument_list|(
name|OperationResourceInfoStack
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|caseInsensitiveQueries
operator|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|m
argument_list|,
name|CASE_INSENSITIVE_QUERIES
argument_list|)
expr_stmt|;
name|this
operator|.
name|queryValueIsCollection
operator|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|m
argument_list|,
name|PARSE_QUERY_VALUE_AS_COLLECTION
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|URI
name|getAbsolutePath
parameter_list|()
block|{
name|String
name|path
init|=
name|getAbsolutePathAsString
argument_list|()
decl_stmt|;
return|return
name|URI
operator|.
name|create
argument_list|(
name|path
argument_list|)
return|;
block|}
specifier|public
name|UriBuilder
name|getAbsolutePathBuilder
parameter_list|()
block|{
return|return
operator|new
name|UriBuilderImpl
argument_list|(
name|getAbsolutePath
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|URI
name|getBaseUri
parameter_list|()
block|{
name|URI
name|u
init|=
name|URI
operator|.
name|create
argument_list|(
name|HttpUtils
operator|.
name|getEndpointAddress
argument_list|(
name|message
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|HttpUtils
operator|.
name|toAbsoluteUri
argument_list|(
name|u
argument_list|,
name|message
argument_list|)
return|;
block|}
specifier|public
name|UriBuilder
name|getBaseUriBuilder
parameter_list|()
block|{
return|return
operator|new
name|UriBuilderImpl
argument_list|(
name|getBaseUri
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|getPath
argument_list|(
literal|true
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPath
parameter_list|(
name|boolean
name|decode
parameter_list|)
block|{
name|String
name|value
init|=
name|doGetPath
argument_list|(
name|decode
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|length
argument_list|()
operator|>
literal|1
operator|&&
name|value
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
return|return
name|value
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
return|return
name|value
return|;
block|}
specifier|public
name|List
argument_list|<
name|PathSegment
argument_list|>
name|getPathSegments
parameter_list|()
block|{
return|return
name|getPathSegments
argument_list|(
literal|true
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|PathSegment
argument_list|>
name|getPathSegments
parameter_list|(
name|boolean
name|decode
parameter_list|)
block|{
return|return
name|JAXRSUtils
operator|.
name|getPathSegments
argument_list|(
name|getPath
argument_list|(
literal|false
argument_list|)
argument_list|,
name|decode
argument_list|)
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getQueryParameters
parameter_list|()
block|{
return|return
name|getQueryParameters
argument_list|(
literal|true
argument_list|)
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getQueryParameters
parameter_list|(
name|boolean
name|decode
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queries
init|=
operator|!
name|caseInsensitiveQueries
condition|?
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
else|:
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|JAXRSUtils
operator|.
name|getStructuredParams
argument_list|(
name|queries
argument_list|,
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|)
argument_list|,
literal|"&"
argument_list|,
name|decode
argument_list|,
name|decode
argument_list|,
name|queryValueIsCollection
argument_list|)
expr_stmt|;
return|return
name|queries
return|;
block|}
specifier|public
name|URI
name|getRequestUri
parameter_list|()
block|{
name|String
name|path
init|=
name|getAbsolutePathAsString
argument_list|()
decl_stmt|;
name|String
name|queries
init|=
operator|(
name|String
operator|)
name|message
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
name|queries
operator|!=
literal|null
condition|)
block|{
name|path
operator|+=
literal|"?"
operator|+
name|queries
expr_stmt|;
block|}
return|return
name|URI
operator|.
name|create
argument_list|(
name|path
argument_list|)
return|;
block|}
specifier|public
name|UriBuilder
name|getRequestUriBuilder
parameter_list|()
block|{
return|return
operator|new
name|UriBuilderImpl
argument_list|(
name|getRequestUri
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPathParameters
parameter_list|()
block|{
return|return
name|getPathParameters
argument_list|(
literal|true
argument_list|)
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPathParameters
parameter_list|(
name|boolean
name|decode
parameter_list|)
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|templateParams
operator|==
literal|null
condition|)
block|{
return|return
name|values
return|;
block|}
for|for
control|(
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
range|:
name|templateParams
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|values
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|decode
condition|?
name|HttpUtils
operator|.
name|pathDecode
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
else|:
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
block|}
return|return
name|values
return|;
block|}
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getMatchedResources
parameter_list|()
block|{
if|if
condition|(
name|stack
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|resources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|stack
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|MethodInvocationInfo
name|invocation
range|:
name|stack
control|)
block|{
name|resources
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|invocation
operator|.
name|getRealClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|resources
return|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"No resource stack information, returning empty list"
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getMatchedURIs
parameter_list|()
block|{
return|return
name|getMatchedURIs
argument_list|(
literal|true
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getMatchedURIs
parameter_list|(
name|boolean
name|decode
parameter_list|)
block|{
if|if
condition|(
name|stack
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|objects
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|uris
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|StringBuilder
name|sumPath
init|=
operator|new
name|StringBuilder
argument_list|(
literal|""
argument_list|)
decl_stmt|;
for|for
control|(
name|MethodInvocationInfo
name|invocation
range|:
name|stack
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|templateObjects
init|=
name|invocation
operator|.
name|getTemplateValues
argument_list|()
decl_stmt|;
name|OperationResourceInfo
name|ori
init|=
name|invocation
operator|.
name|getMethodInfo
argument_list|()
decl_stmt|;
name|URITemplate
index|[]
name|paths
init|=
block|{
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
operator|.
name|getURITemplate
argument_list|()
block|,
name|ori
operator|.
name|getURITemplate
argument_list|()
block|}
decl_stmt|;
if|if
condition|(
name|paths
index|[
literal|0
index|]
operator|!=
literal|null
condition|)
block|{
name|int
name|count
init|=
name|paths
index|[
literal|0
index|]
operator|.
name|getVariables
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|rootObjects
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|count
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
name|count
operator|&&
name|i
operator|<
name|templateObjects
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|rootObjects
operator|.
name|add
argument_list|(
name|templateObjects
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|uris
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|createMatchedPath
argument_list|(
name|paths
index|[
literal|0
index|]
operator|.
name|getValue
argument_list|()
argument_list|,
name|rootObjects
argument_list|,
name|decode
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|paths
index|[
literal|1
index|]
operator|!=
literal|null
operator|&&
name|paths
index|[
literal|1
index|]
operator|.
name|getValue
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
for|for
control|(
name|URITemplate
name|t
range|:
name|paths
control|)
block|{
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|sumPath
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|t
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|objects
operator|.
name|addAll
argument_list|(
name|templateObjects
argument_list|)
expr_stmt|;
name|uris
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|createMatchedPath
argument_list|(
name|sumPath
operator|.
name|toString
argument_list|()
argument_list|,
name|objects
argument_list|,
name|decode
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|uris
return|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"No resource stack information, returning empty list"
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|String
name|createMatchedPath
parameter_list|(
name|String
name|uri
parameter_list|,
name|List
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|vars
parameter_list|,
name|boolean
name|decode
parameter_list|)
block|{
name|String
name|uriPath
init|=
name|UriBuilder
operator|.
name|fromPath
argument_list|(
name|uri
argument_list|)
operator|.
name|buildFromEncoded
argument_list|(
name|vars
operator|.
name|toArray
argument_list|()
argument_list|)
operator|.
name|getRawPath
argument_list|()
decl_stmt|;
name|uriPath
operator|=
name|decode
condition|?
name|HttpUtils
operator|.
name|pathDecode
argument_list|(
name|uriPath
argument_list|)
else|:
name|uriPath
expr_stmt|;
if|if
condition|(
name|uriPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|uriPath
operator|=
name|uriPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|uriPath
return|;
block|}
specifier|private
name|String
name|doGetPath
parameter_list|(
name|boolean
name|decode
parameter_list|,
name|boolean
name|addSlash
parameter_list|)
block|{
name|String
name|path
init|=
name|HttpUtils
operator|.
name|getPathToMatch
argument_list|(
name|message
argument_list|,
name|addSlash
argument_list|)
decl_stmt|;
return|return
name|decode
condition|?
name|HttpUtils
operator|.
name|pathDecode
argument_list|(
name|path
argument_list|)
else|:
name|path
return|;
block|}
specifier|private
name|String
name|getAbsolutePathAsString
parameter_list|()
block|{
name|String
name|address
init|=
name|getBaseUri
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return
name|address
return|;
block|}
name|String
name|path
init|=
name|doGetPath
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
operator|&&
name|address
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|address
operator|=
name|address
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|address
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|path
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
operator|&&
operator|!
name|address
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|address
operator|=
name|address
operator|+
literal|"/"
expr_stmt|;
block|}
return|return
name|address
operator|+
name|path
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|relativize
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|URI
name|resolved
init|=
name|HttpUtils
operator|.
name|resolve
argument_list|(
name|getBaseUriBuilder
argument_list|()
argument_list|,
name|uri
argument_list|)
decl_stmt|;
return|return
name|HttpUtils
operator|.
name|relativize
argument_list|(
name|getRequestUri
argument_list|()
argument_list|,
name|resolved
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|resolve
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
return|return
name|HttpUtils
operator|.
name|resolve
argument_list|(
name|getBaseUriBuilder
argument_list|()
argument_list|,
name|uri
argument_list|)
return|;
block|}
block|}
end_class

end_unit

