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
name|Arrays
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
name|ext
operator|.
name|RuntimeDelegate
operator|.
name|HeaderDelegate
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

begin_class
specifier|public
class|class
name|LinkHeaderProvider
implements|implements
name|HeaderDelegate
argument_list|<
name|Link
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|String
name|REL
init|=
literal|"rel"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"type"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TITLE
init|=
literal|"title"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|KNOWN_PARAMETERS
decl_stmt|;
static|static
block|{
name|KNOWN_PARAMETERS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|REL
argument_list|,
name|TYPE
argument_list|,
name|TITLE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Link
name|fromString
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
literal|"Link value can not be null"
argument_list|)
throw|;
block|}
name|value
operator|=
name|value
operator|.
name|trim
argument_list|()
expr_stmt|;
name|int
name|closeIndex
init|=
name|value
operator|.
name|indexOf
argument_list|(
literal|'>'
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|value
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
operator|||
name|closeIndex
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Link URI is missing"
argument_list|)
throw|;
block|}
name|Link
operator|.
name|Builder
name|builder
init|=
operator|new
name|LinkBuilderImpl
argument_list|()
decl_stmt|;
name|builder
operator|.
name|uri
argument_list|(
name|value
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|closeIndex
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|closeIndex
operator|<
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
name|String
index|[]
name|tokens
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|value
operator|.
name|substring
argument_list|(
name|closeIndex
operator|+
literal|1
argument_list|)
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
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|String
name|paramName
init|=
literal|null
decl_stmt|;
name|String
name|paramValue
init|=
literal|null
decl_stmt|;
name|int
name|i
init|=
name|token
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
name|paramName
operator|=
name|theToken
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
name|paramValue
operator|=
name|i
operator|==
name|theToken
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|?
literal|""
else|:
name|theToken
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|REL
operator|.
name|equals
argument_list|(
name|paramName
argument_list|)
condition|)
block|{
name|String
index|[]
name|rels
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|removeQuotesIfNeeded
argument_list|(
name|paramValue
argument_list|)
argument_list|,
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|rel
range|:
name|rels
control|)
block|{
name|builder
operator|.
name|rel
argument_list|(
name|rel
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|TYPE
operator|.
name|equals
argument_list|(
name|paramName
argument_list|)
condition|)
block|{
name|builder
operator|.
name|type
argument_list|(
name|removeQuotesIfNeeded
argument_list|(
name|paramValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|TITLE
operator|.
name|equals
argument_list|(
name|paramName
argument_list|)
condition|)
block|{
name|builder
operator|.
name|title
argument_list|(
name|removeQuotesIfNeeded
argument_list|(
name|paramValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|param
argument_list|(
name|paramName
argument_list|,
name|paramValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
specifier|private
name|String
name|removeQuotesIfNeeded
parameter_list|(
name|String
name|value
parameter_list|)
block|{
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
literal|"\""
argument_list|)
operator|&&
name|value
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
return|return
name|value
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
return|return
name|value
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|(
name|Link
name|link
parameter_list|)
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
literal|'<'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|link
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
name|String
name|rels
init|=
name|link
operator|.
name|getRel
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|rels
operator|.
name|isEmpty
argument_list|()
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
name|REL
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
name|writeListParamValues
argument_list|(
name|sb
argument_list|,
name|rels
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|link
operator|.
name|getTitle
argument_list|()
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
name|link
operator|.
name|getTitle
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|link
operator|.
name|getType
argument_list|()
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
name|TYPE
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|link
operator|.
name|getType
argument_list|()
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
name|link
operator|.
name|getParams
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|KNOWN_PARAMETERS
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
continue|continue;
block|}
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
literal|'='
argument_list|)
expr_stmt|;
name|writeListParamValues
argument_list|(
name|sb
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
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
name|void
name|writeListParamValues
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return;
block|}
name|boolean
name|commaAvailable
init|=
name|value
operator|.
name|contains
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|commaAvailable
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|commaAvailable
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

