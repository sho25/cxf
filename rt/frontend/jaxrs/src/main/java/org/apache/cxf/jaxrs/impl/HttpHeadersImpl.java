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
name|Comparator
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
name|Locale
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
name|Cookie
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
name|HttpHeadersImpl
implements|implements
name|HttpHeaders
block|{
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|HttpHeadersImpl
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
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
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|MediaType
argument_list|>
name|getAcceptableMediaTypes
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|lValues
init|=
name|headers
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|)
decl_stmt|;
if|if
condition|(
name|lValues
operator|==
literal|null
operator|||
name|lValues
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|MediaType
argument_list|>
name|mediaTypes
init|=
name|JAXRSUtils
operator|.
name|parseMediaTypes
argument_list|(
name|lValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|sortMediaTypesUsingQualityFactor
argument_list|(
name|mediaTypes
argument_list|)
expr_stmt|;
return|return
name|mediaTypes
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
name|getCookies
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|cs
init|=
name|getListValues
argument_list|(
name|HttpHeaders
operator|.
name|COOKIE
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
name|cl
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|c
range|:
name|cs
control|)
block|{
name|Cookie
name|cookie
init|=
name|Cookie
operator|.
name|valueOf
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|cl
operator|.
name|put
argument_list|(
name|cookie
operator|.
name|getName
argument_list|()
argument_list|,
name|cookie
argument_list|)
expr_stmt|;
block|}
return|return
name|cl
return|;
block|}
specifier|public
name|Locale
name|getLanguage
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|getListValues
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|)
decl_stmt|;
return|return
name|values
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
name|createLocale
argument_list|(
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|MediaType
name|getMediaType
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|getListValues
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
return|return
name|values
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
name|MediaType
operator|.
name|valueOf
argument_list|(
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|getRequestHeaders
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|newHeaders
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
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
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|newHeaders
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|getListValues
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|newHeaders
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|Locale
argument_list|>
name|getAcceptableLanguages
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ls
init|=
name|getListValues
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Locale
argument_list|>
name|newLs
init|=
operator|new
name|ArrayList
argument_list|<
name|Locale
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Locale
argument_list|,
name|Float
argument_list|>
name|prefs
init|=
operator|new
name|HashMap
argument_list|<
name|Locale
argument_list|,
name|Float
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|l
range|:
name|ls
control|)
block|{
name|String
index|[]
name|pair
init|=
name|l
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
name|Locale
name|locale
init|=
name|createLocale
argument_list|(
name|pair
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
name|newLs
operator|.
name|add
argument_list|(
name|locale
argument_list|)
expr_stmt|;
if|if
condition|(
name|pair
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|String
index|[]
name|pair2
init|=
name|pair
index|[
literal|1
index|]
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|pair2
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|prefs
operator|.
name|put
argument_list|(
name|locale
argument_list|,
name|JAXRSUtils
operator|.
name|getMediaTypeQualityFactor
argument_list|(
name|pair2
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|prefs
operator|.
name|put
argument_list|(
name|locale
argument_list|,
literal|1F
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|prefs
operator|.
name|put
argument_list|(
name|locale
argument_list|,
literal|1F
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|newLs
operator|.
name|size
argument_list|()
operator|<=
literal|1
condition|)
block|{
return|return
name|newLs
return|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|newLs
argument_list|,
operator|new
name|AcceptLanguageComparator
argument_list|(
name|prefs
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|newLs
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRequestHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getListValues
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getListValues
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|headers
operator|.
name|get
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
if|if
condition|(
name|HttpUtils
operator|.
name|isDateRelatedHeader
argument_list|(
name|headerName
argument_list|)
condition|)
block|{
return|return
name|values
return|;
block|}
name|String
index|[]
name|ls
init|=
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|ls
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|ls
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|newValues
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
name|String
name|v
range|:
name|ls
control|)
block|{
name|newValues
operator|.
name|add
argument_list|(
name|v
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|newValues
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|AcceptLanguageComparator
implements|implements
name|Comparator
argument_list|<
name|Locale
argument_list|>
block|{
specifier|private
name|Map
argument_list|<
name|Locale
argument_list|,
name|Float
argument_list|>
name|prefs
decl_stmt|;
specifier|public
name|AcceptLanguageComparator
parameter_list|(
name|Map
argument_list|<
name|Locale
argument_list|,
name|Float
argument_list|>
name|prefs
parameter_list|)
block|{
name|this
operator|.
name|prefs
operator|=
name|prefs
expr_stmt|;
block|}
specifier|public
name|int
name|compare
parameter_list|(
name|Locale
name|lang1
parameter_list|,
name|Locale
name|lang2
parameter_list|)
block|{
name|float
name|p1
init|=
name|prefs
operator|.
name|get
argument_list|(
name|lang1
argument_list|)
decl_stmt|;
name|float
name|p2
init|=
name|prefs
operator|.
name|get
argument_list|(
name|lang2
argument_list|)
decl_stmt|;
return|return
name|Float
operator|.
name|compare
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
operator|*
operator|-
literal|1
return|;
block|}
block|}
specifier|private
name|Locale
name|createLocale
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|String
index|[]
name|values
init|=
name|value
operator|.
name|split
argument_list|(
literal|"-"
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|.
name|length
operator|==
literal|0
operator|||
name|values
operator|.
name|length
operator|>
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal locale value : "
operator|+
name|value
argument_list|)
throw|;
block|}
if|if
condition|(
name|values
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
operator|new
name|Locale
argument_list|(
name|values
index|[
literal|0
index|]
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|Locale
argument_list|(
name|values
index|[
literal|0
index|]
argument_list|,
name|values
index|[
literal|1
index|]
argument_list|)
return|;
block|}
block|}
specifier|private
name|void
name|sortMediaTypesUsingQualityFactor
parameter_list|(
name|List
argument_list|<
name|MediaType
argument_list|>
name|types
parameter_list|)
block|{
if|if
condition|(
name|types
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|types
argument_list|,
operator|new
name|Comparator
argument_list|<
name|MediaType
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|MediaType
name|mt1
parameter_list|,
name|MediaType
name|mt2
parameter_list|)
block|{
return|return
name|JAXRSUtils
operator|.
name|compareMediaTypesQualityFactors
argument_list|(
name|mt1
argument_list|,
name|mt2
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

