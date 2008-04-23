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
name|StringTokenizer
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
name|MediaType
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

begin_class
specifier|public
class|class
name|MediaTypeHeaderProvider
implements|implements
name|HeaderDelegate
argument_list|<
name|MediaType
argument_list|>
block|{
specifier|public
name|MediaType
name|fromString
parameter_list|(
name|String
name|mType
parameter_list|)
block|{
if|if
condition|(
name|mType
operator|.
name|equals
argument_list|(
name|MediaType
operator|.
name|MEDIA_TYPE_WILDCARD
argument_list|)
operator|||
name|mType
operator|.
name|startsWith
argument_list|(
literal|"*;"
argument_list|)
condition|)
block|{
return|return
operator|new
name|MediaType
argument_list|(
literal|"*"
argument_list|,
literal|"*"
argument_list|)
return|;
block|}
name|int
name|i
init|=
name|mType
operator|.
name|indexOf
argument_list|(
literal|'/'
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
literal|"Media type separator is missing"
argument_list|)
throw|;
block|}
name|int
name|paramsStart
init|=
name|mType
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|,
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|end
init|=
name|paramsStart
operator|==
operator|-
literal|1
condition|?
name|mType
operator|.
name|length
argument_list|()
else|:
name|paramsStart
decl_stmt|;
name|String
name|type
init|=
name|mType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|String
name|subtype
init|=
name|mType
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|end
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|paramsStart
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// Using Pattern.compile might be marginally faster ?
comment|// though it's rare when more than one parameter is provided
name|parameters
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|mType
operator|.
name|substring
argument_list|(
name|paramsStart
operator|+
literal|1
argument_list|)
argument_list|,
literal|";"
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
name|token
init|=
name|st
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|int
name|equalSign
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
name|equalSign
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Wrong media type  parameter, seperator is missing"
argument_list|)
throw|;
block|}
name|parameters
operator|.
name|put
argument_list|(
name|token
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|equalSign
argument_list|)
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|token
operator|.
name|substring
argument_list|(
name|equalSign
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|MediaType
argument_list|(
name|type
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|subtype
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|parameters
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|(
name|MediaType
name|type
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
name|type
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|type
operator|.
name|getSubtype
argument_list|()
argument_list|)
expr_stmt|;
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
name|String
argument_list|>
argument_list|>
name|iter
init|=
name|type
operator|.
name|getParameters
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
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
name|String
argument_list|>
name|entry
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|';'
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
operator|.
name|append
argument_list|(
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
block|}
end_class

end_unit

