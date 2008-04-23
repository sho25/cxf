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
name|provider
package|;
end_package

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
name|Iterator
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
name|jaxrs
operator|.
name|MetadataMap
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
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|matrix
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
decl_stmt|;
specifier|public
name|UriBuilderImpl
parameter_list|()
block|{     }
specifier|public
name|UriBuilderImpl
parameter_list|(
name|URI
name|uri
parameter_list|)
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
parameter_list|()
throws|throws
name|UriBuilderException
block|{
try|try
block|{
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
name|buildPath
argument_list|()
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
annotation|@
name|Override
specifier|public
name|URI
name|build
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parts
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|UriBuilderException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|build
parameter_list|(
name|String
modifier|...
name|values
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|UriBuilderException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
comment|//CHECKSTYLE:OFF
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
comment|//CHECKSTYLE:ON
annotation|@
name|Override
specifier|public
name|UriBuilder
name|encode
parameter_list|(
name|boolean
name|enable
parameter_list|)
block|{
comment|//this.encode = enable;
return|return
name|this
return|;
block|}
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
name|Override
specifier|public
name|UriBuilder
name|matrixParam
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|matrix
operator|.
name|putSingle
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
annotation|@
name|Override
specifier|public
name|UriBuilder
name|path
parameter_list|(
name|String
modifier|...
name|segments
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|paths
operator|==
literal|null
condition|)
block|{
name|paths
operator|=
operator|new
name|ArrayList
argument_list|<
name|PathSegment
argument_list|>
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|String
name|segment
range|:
name|segments
control|)
block|{
name|paths
operator|.
name|add
argument_list|(
operator|new
name|PathSegmentImpl
argument_list|(
name|segment
argument_list|,
literal|false
argument_list|)
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
name|path
parameter_list|(
name|Class
name|resource
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|path
parameter_list|(
name|Method
modifier|...
name|methods
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
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
comment|// TODO Auto-generated method stub
return|return
literal|null
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
name|queryParam
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
name|query
operator|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|query
operator|.
name|add
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
annotation|@
name|Override
specifier|public
name|UriBuilder
name|replaceMatrixParams
parameter_list|(
name|String
name|m
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|matrix
operator|=
name|JAXRSUtils
operator|.
name|getStructuredParams
argument_list|(
name|m
argument_list|,
literal|";"
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
name|replaceQueryParams
parameter_list|(
name|String
name|q
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|this
operator|.
name|query
operator|=
name|JAXRSUtils
operator|.
name|getStructuredParams
argument_list|(
name|q
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
comment|//schemeSpPart = ssp;
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
for|for
control|(
name|PathSegment
name|ps
range|:
name|paths
control|)
block|{
name|String
name|p
init|=
name|ps
operator|.
name|getPath
argument_list|()
decl_stmt|;
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
name|boolean
name|isEncode
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|replacePath
parameter_list|(
name|String
modifier|...
name|segments
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

