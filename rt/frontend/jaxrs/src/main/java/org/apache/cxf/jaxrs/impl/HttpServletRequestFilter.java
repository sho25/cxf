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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|LinkedHashMap
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
name|servlet
operator|.
name|ServletInputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequestWrapper
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
name|core
operator|.
name|MultivaluedMap
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
name|io
operator|.
name|DelegatingInputStream
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
name|FormUtils
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

begin_class
specifier|public
class|class
name|HttpServletRequestFilter
extends|extends
name|HttpServletRequestWrapper
block|{
specifier|private
name|Message
name|m
decl_stmt|;
specifier|private
name|boolean
name|isPostFormRequest
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|formParams
decl_stmt|;
specifier|public
name|HttpServletRequestFilter
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|m
operator|=
name|message
expr_stmt|;
name|isPostFormRequest
operator|=
name|FormUtils
operator|.
name|isFormPostRequest
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ServletInputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
name|m
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|instanceof
name|DelegatingInputStream
condition|)
block|{
name|is
operator|=
operator|(
operator|(
name|DelegatingInputStream
operator|)
name|is
operator|)
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|is
operator|instanceof
name|ServletInputStream
condition|)
block|{
return|return
operator|(
name|ServletInputStream
operator|)
name|is
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getInputStream
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
index|[]
name|values
init|=
name|this
operator|.
name|getParameterValues
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|values
operator|==
literal|null
condition|?
literal|null
else|:
name|values
index|[
literal|0
index|]
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|getParameterValues
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
index|[]
name|value
init|=
name|super
operator|.
name|getParameterValues
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|isPostFormRequest
condition|)
block|{
name|readFromParamsIfNeeded
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|formParams
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|values
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|value
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|getParameterMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|map1
init|=
name|super
operator|.
name|getParameterMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|isPostFormRequest
condition|)
block|{
name|readFromParamsIfNeeded
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|map2
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
argument_list|()
decl_stmt|;
name|map2
operator|.
name|putAll
argument_list|(
name|map1
argument_list|)
expr_stmt|;
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
name|e
range|:
name|formParams
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|map2
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|map2
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|map1
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getParameterNames
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|map
init|=
name|this
operator|.
name|getParameterMap
argument_list|()
decl_stmt|;
specifier|final
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|Enumeration
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|hasMoreElements
parameter_list|()
block|{
return|return
name|it
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|nextElement
parameter_list|()
block|{
return|return
name|it
operator|.
name|next
argument_list|()
return|;
block|}
block|}
return|;
block|}
specifier|private
name|void
name|readFromParamsIfNeeded
parameter_list|()
block|{
if|if
condition|(
name|formParams
operator|==
literal|null
condition|)
block|{
name|formParams
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
name|MediaType
name|mt
init|=
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
operator|(
name|String
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|enc
init|=
name|HttpUtils
operator|.
name|getEncoding
argument_list|(
name|mt
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|body
init|=
name|FormUtils
operator|.
name|readBody
argument_list|(
name|m
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
name|enc
argument_list|)
decl_stmt|;
name|FormUtils
operator|.
name|populateMapFromString
argument_list|(
name|formParams
argument_list|,
name|m
argument_list|,
name|body
argument_list|,
name|enc
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

