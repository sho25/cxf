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
name|Arrays
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
name|Link
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
name|Link
operator|.
name|Builder
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

begin_class
specifier|public
class|class
name|LinkBuilderImpl
implements|implements
name|Builder
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DOUBLE_QUOTE
init|=
literal|"\""
decl_stmt|;
specifier|private
name|UriBuilder
name|ub
decl_stmt|;
specifier|private
name|URI
name|baseUri
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
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
literal|6
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Link
name|build
parameter_list|(
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|URI
name|resolvedLinkUri
init|=
name|getResolvedUri
argument_list|(
name|values
argument_list|)
decl_stmt|;
return|return
operator|new
name|LinkImpl
argument_list|(
name|resolvedLinkUri
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|params
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Link
name|buildRelativized
parameter_list|(
name|URI
name|requestUri
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|URI
name|resolvedLinkUri
init|=
name|getResolvedUri
argument_list|(
name|values
argument_list|)
decl_stmt|;
name|URI
name|relativized
init|=
name|HttpUtils
operator|.
name|relativize
argument_list|(
name|requestUri
argument_list|,
name|resolvedLinkUri
argument_list|)
decl_stmt|;
return|return
operator|new
name|LinkImpl
argument_list|(
name|relativized
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|params
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|URI
name|getResolvedUri
parameter_list|(
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|URI
name|uri
init|=
name|ub
operator|.
name|build
argument_list|(
name|values
argument_list|)
decl_stmt|;
return|return
name|baseUri
operator|!=
literal|null
condition|?
name|HttpUtils
operator|.
name|resolve
argument_list|(
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|baseUri
argument_list|)
argument_list|,
name|uri
argument_list|)
else|:
name|uri
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|link
parameter_list|(
name|Link
name|link
parameter_list|)
block|{
name|ub
operator|=
name|UriBuilder
operator|.
name|fromLink
argument_list|(
name|link
argument_list|)
expr_stmt|;
name|params
operator|.
name|putAll
argument_list|(
name|link
operator|.
name|getParams
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|link
parameter_list|(
name|String
name|link
parameter_list|)
block|{
name|String
index|[]
name|tokens
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|link
argument_list|,
literal|";"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|token
range|:
name|tokens
control|)
block|{
name|String
name|theToken
init|=
name|token
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|theToken
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
operator|&&
name|theToken
operator|.
name|endsWith
argument_list|(
literal|">"
argument_list|)
condition|)
block|{
name|ub
operator|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|theToken
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|theToken
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|i
init|=
name|theToken
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|name
init|=
name|theToken
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|stripQuotes
argument_list|(
name|theToken
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|param
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
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
name|Builder
name|rel
parameter_list|(
name|String
name|rel
parameter_list|)
block|{
name|String
name|exisingRel
init|=
name|params
operator|.
name|get
argument_list|(
name|Link
operator|.
name|REL
argument_list|)
decl_stmt|;
name|String
name|newRel
init|=
name|exisingRel
operator|==
literal|null
condition|?
name|rel
else|:
name|exisingRel
operator|+
literal|" "
operator|+
name|rel
decl_stmt|;
return|return
name|param
argument_list|(
name|Link
operator|.
name|REL
argument_list|,
name|newRel
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|title
parameter_list|(
name|String
name|title
parameter_list|)
block|{
return|return
name|param
argument_list|(
name|Link
operator|.
name|TITLE
argument_list|,
name|title
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
name|param
argument_list|(
name|Link
operator|.
name|TYPE
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|uri
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|ub
operator|=
name|UriBuilder
operator|.
name|fromUri
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
name|Builder
name|uri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|ub
operator|=
name|UriBuilder
operator|.
name|fromUri
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
name|Builder
name|uriBuilder
parameter_list|(
name|UriBuilder
name|builder
parameter_list|)
block|{
name|this
operator|.
name|ub
operator|=
name|builder
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|private
name|String
name|stripQuotes
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|replaceAll
argument_list|(
name|DOUBLE_QUOTE
argument_list|,
literal|""
argument_list|)
return|;
block|}
specifier|private
name|void
name|checkNotNull
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|value
argument_list|)
throw|;
block|}
block|}
specifier|static
class|class
name|LinkImpl
extends|extends
name|Link
block|{
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|MAIN_PARAMETERS
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Link
operator|.
name|REL
argument_list|,
name|Link
operator|.
name|TITLE
argument_list|,
name|Link
operator|.
name|TYPE
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|URI
name|uri
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
name|LinkImpl
parameter_list|(
name|URI
name|uri
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
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|params
operator|=
name|params
expr_stmt|;
block|}
annotation|@
name|Override
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
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|params
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRel
parameter_list|()
block|{
return|return
name|params
operator|.
name|get
argument_list|(
name|Link
operator|.
name|REL
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRels
parameter_list|()
block|{
name|String
name|rel
init|=
name|getRel
argument_list|()
decl_stmt|;
if|if
condition|(
name|rel
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
return|;
block|}
else|else
block|{
name|String
index|[]
name|values
init|=
name|rel
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|rels
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|values
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|val
range|:
name|values
control|)
block|{
name|rels
operator|.
name|add
argument_list|(
name|val
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|rels
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|params
operator|.
name|get
argument_list|(
name|Link
operator|.
name|TITLE
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|params
operator|.
name|get
argument_list|(
name|Link
operator|.
name|TYPE
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|getUriBuilder
parameter_list|()
block|{
return|return
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|uri
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
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
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
name|String
name|rel
init|=
name|getRel
argument_list|()
decl_stmt|;
if|if
condition|(
name|rel
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|";"
argument_list|)
operator|.
name|append
argument_list|(
name|Link
operator|.
name|REL
argument_list|)
operator|.
name|append
argument_list|(
literal|"=\""
argument_list|)
operator|.
name|append
argument_list|(
name|rel
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|String
name|title
init|=
name|getTitle
argument_list|()
decl_stmt|;
if|if
condition|(
name|title
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|";"
argument_list|)
operator|.
name|append
argument_list|(
name|Link
operator|.
name|TITLE
argument_list|)
operator|.
name|append
argument_list|(
literal|"=\""
argument_list|)
operator|.
name|append
argument_list|(
name|title
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|String
name|type
init|=
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|";"
argument_list|)
operator|.
name|append
argument_list|(
name|Link
operator|.
name|TYPE
argument_list|)
operator|.
name|append
argument_list|(
literal|"=\""
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
operator|!
name|MAIN_PARAMETERS
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|";"
argument_list|)
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
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
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
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|uri
operator|.
name|hashCode
argument_list|()
operator|+
literal|37
operator|*
name|params
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Link
condition|)
block|{
name|Link
name|other
init|=
operator|(
name|Link
operator|)
name|o
decl_stmt|;
return|return
name|uri
operator|.
name|equals
argument_list|(
name|other
operator|.
name|getUri
argument_list|()
argument_list|)
operator|&&
name|getParams
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|getParams
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|baseUri
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|uri
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|baseUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|baseUri
operator|=
name|URI
operator|.
name|create
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

