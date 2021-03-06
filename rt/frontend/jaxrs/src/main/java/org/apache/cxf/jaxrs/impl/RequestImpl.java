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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|Date
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
name|Locale
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
name|HttpMethod
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
name|EntityTag
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
name|Request
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
name|Response
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
name|Response
operator|.
name|ResponseBuilder
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
name|Variant
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
name|phase
operator|.
name|PhaseInterceptorChain
import|;
end_import

begin_comment
comment|/**  * TODO : deal with InvalidStateExceptions  *  */
end_comment

begin_class
specifier|public
class|class
name|RequestImpl
implements|implements
name|Request
block|{
specifier|private
specifier|final
name|Message
name|m
decl_stmt|;
specifier|private
specifier|final
name|HttpHeaders
name|headers
decl_stmt|;
specifier|public
name|RequestImpl
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|this
operator|.
name|m
operator|=
name|m
expr_stmt|;
name|this
operator|.
name|headers
operator|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Variant
name|selectVariant
parameter_list|(
name|List
argument_list|<
name|Variant
argument_list|>
name|vars
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|vars
operator|==
literal|null
operator|||
name|vars
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"List of Variants is either null or empty"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|MediaType
argument_list|>
name|acceptMediaTypes
init|=
name|headers
operator|.
name|getAcceptableMediaTypes
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Locale
argument_list|>
name|acceptLangs
init|=
name|headers
operator|.
name|getAcceptableLanguages
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acceptEncs
init|=
name|parseAcceptEnc
argument_list|(
name|headers
operator|.
name|getRequestHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_ENCODING
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|requestVariants
init|=
name|sortAllCombinations
argument_list|(
name|acceptMediaTypes
argument_list|,
name|acceptLangs
argument_list|,
name|acceptEncs
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|varyValues
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Variant
name|requestVar
range|:
name|requestVariants
control|)
block|{
for|for
control|(
name|Variant
name|var
range|:
name|vars
control|)
block|{
name|MediaType
name|mt
init|=
name|var
operator|.
name|getMediaType
argument_list|()
decl_stmt|;
name|Locale
name|lang
init|=
name|var
operator|.
name|getLanguage
argument_list|()
decl_stmt|;
name|String
name|enc
init|=
name|var
operator|.
name|getEncoding
argument_list|()
decl_stmt|;
name|boolean
name|mtMatched
init|=
name|mt
operator|==
literal|null
operator|||
name|requestVar
operator|.
name|getMediaType
argument_list|()
operator|.
name|isCompatible
argument_list|(
name|mt
argument_list|)
decl_stmt|;
if|if
condition|(
name|mtMatched
condition|)
block|{
name|handleVaryValues
argument_list|(
name|varyValues
argument_list|,
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|)
expr_stmt|;
block|}
name|boolean
name|langMatched
init|=
name|lang
operator|==
literal|null
operator|||
name|isLanguageMatched
argument_list|(
name|requestVar
operator|.
name|getLanguage
argument_list|()
argument_list|,
name|lang
argument_list|)
decl_stmt|;
if|if
condition|(
name|langMatched
condition|)
block|{
name|handleVaryValues
argument_list|(
name|varyValues
argument_list|,
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|)
expr_stmt|;
block|}
name|boolean
name|encMatched
init|=
name|acceptEncs
operator|.
name|isEmpty
argument_list|()
operator|||
name|enc
operator|==
literal|null
operator|||
name|isEncMatached
argument_list|(
name|requestVar
operator|.
name|getEncoding
argument_list|()
argument_list|,
name|enc
argument_list|)
decl_stmt|;
if|if
condition|(
name|encMatched
condition|)
block|{
name|handleVaryValues
argument_list|(
name|varyValues
argument_list|,
name|HttpHeaders
operator|.
name|ACCEPT_ENCODING
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mtMatched
operator|&&
name|encMatched
operator|&&
name|langMatched
condition|)
block|{
name|addVaryHeader
argument_list|(
name|varyValues
argument_list|)
expr_stmt|;
return|return
name|var
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|Variant
argument_list|>
name|sortAllCombinations
parameter_list|(
name|List
argument_list|<
name|MediaType
argument_list|>
name|mediaTypes
parameter_list|,
name|List
argument_list|<
name|Locale
argument_list|>
name|langs
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|encs
parameter_list|)
block|{
name|List
argument_list|<
name|Variant
argument_list|>
name|requestVars
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|MediaType
name|mt
range|:
name|mediaTypes
control|)
block|{
for|for
control|(
name|Locale
name|lang
range|:
name|langs
control|)
block|{
if|if
condition|(
name|encs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|requestVars
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|mt
argument_list|,
name|lang
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|String
name|enc
range|:
name|encs
control|)
block|{
name|requestVars
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|mt
argument_list|,
name|lang
argument_list|,
name|enc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|requestVars
argument_list|,
name|VariantComparator
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
return|return
name|requestVars
return|;
block|}
specifier|private
specifier|static
name|void
name|handleVaryValues
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|varyValues
parameter_list|,
name|String
modifier|...
name|values
parameter_list|)
block|{
for|for
control|(
name|String
name|v
range|:
name|values
control|)
block|{
if|if
condition|(
name|v
operator|!=
literal|null
operator|&&
operator|!
name|varyValues
operator|.
name|contains
argument_list|(
name|v
argument_list|)
condition|)
block|{
name|varyValues
operator|.
name|add
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|addVaryHeader
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|varyValues
parameter_list|)
block|{
comment|// at this point we still have no out-bound message so lets
comment|// use HttpServletResponse. If needed we can save the header on the exchange
comment|// and then copy it into the out-bound message's headers
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|Object
name|httpResponse
init|=
name|message
operator|.
name|get
argument_list|(
literal|"HTTP.RESPONSE"
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpResponse
operator|!=
literal|null
condition|)
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|varyValues
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
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
name|varyValues
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|)
name|httpResponse
operator|)
operator|.
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|VARY
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|boolean
name|isLanguageMatched
parameter_list|(
name|Locale
name|locale
parameter_list|,
name|Locale
name|l
parameter_list|)
block|{
name|String
name|language
init|=
name|locale
operator|.
name|getLanguage
argument_list|()
decl_stmt|;
return|return
literal|"*"
operator|.
name|equals
argument_list|(
name|language
argument_list|)
operator|||
name|language
operator|.
name|equalsIgnoreCase
argument_list|(
name|l
operator|.
name|getLanguage
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isEncMatached
parameter_list|(
name|String
name|accepts
parameter_list|,
name|String
name|enc
parameter_list|)
block|{
return|return
name|accepts
operator|==
literal|null
operator|||
literal|"*"
operator|.
name|equals
argument_list|(
name|accepts
argument_list|)
operator|||
name|accepts
operator|.
name|contains
argument_list|(
name|enc
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|parseAcceptEnc
parameter_list|(
name|String
name|acceptEnc
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|acceptEnc
argument_list|)
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
name|String
argument_list|>
name|list
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
index|[]
name|values
init|=
name|acceptEnc
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
name|String
index|[]
name|pair
init|=
name|value
operator|.
name|trim
argument_list|()
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
comment|// ignore encoding qualifiers if any for now
name|list
operator|.
name|add
argument_list|(
name|pair
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
specifier|public
name|ResponseBuilder
name|evaluatePreconditions
parameter_list|(
name|EntityTag
name|eTag
parameter_list|)
block|{
if|if
condition|(
name|eTag
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ETag is null"
argument_list|)
throw|;
block|}
return|return
name|evaluateAll
argument_list|(
name|eTag
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|ResponseBuilder
name|evaluateAll
parameter_list|(
name|EntityTag
name|eTag
parameter_list|,
name|Date
name|lastModified
parameter_list|)
block|{
comment|// http://tools.ietf.org/search/draft-ietf-httpbis-p4-conditional-25#section-5
comment|// Check If-Match. If it is not available proceed to checking If-Not-Modified-Since
comment|// if it is available and the preconditions are not met - return, otherwise:
comment|// Check If-Not-Match. If it is not available proceed to checking If-Modified-Since
comment|// otherwise return the evaluation result
name|ResponseBuilder
name|rb
init|=
name|evaluateIfMatch
argument_list|(
name|eTag
argument_list|,
name|lastModified
argument_list|)
decl_stmt|;
if|if
condition|(
name|rb
operator|==
literal|null
condition|)
block|{
name|rb
operator|=
name|evaluateIfNonMatch
argument_list|(
name|eTag
argument_list|,
name|lastModified
argument_list|)
expr_stmt|;
block|}
return|return
name|rb
return|;
block|}
specifier|private
name|ResponseBuilder
name|evaluateIfMatch
parameter_list|(
name|EntityTag
name|eTag
parameter_list|,
name|Date
name|date
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ifMatch
init|=
name|headers
operator|.
name|getRequestHeader
argument_list|(
name|HttpHeaders
operator|.
name|IF_MATCH
argument_list|)
decl_stmt|;
if|if
condition|(
name|ifMatch
operator|==
literal|null
operator|||
name|ifMatch
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|date
operator|==
literal|null
condition|?
literal|null
else|:
name|evaluateIfNotModifiedSince
argument_list|(
name|date
argument_list|)
return|;
block|}
try|try
block|{
for|for
control|(
name|String
name|value
range|:
name|ifMatch
control|)
block|{
if|if
condition|(
literal|"*"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|EntityTag
name|requestTag
init|=
name|EntityTag
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
decl_stmt|;
comment|// must be a strong comparison
if|if
condition|(
operator|!
name|requestTag
operator|.
name|isWeak
argument_list|()
operator|&&
operator|!
name|eTag
operator|.
name|isWeak
argument_list|()
operator|&&
name|requestTag
operator|.
name|equals
argument_list|(
name|eTag
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|Response
operator|.
name|status
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|PRECONDITION_FAILED
argument_list|)
operator|.
name|tag
argument_list|(
name|eTag
argument_list|)
return|;
block|}
specifier|private
name|ResponseBuilder
name|evaluateIfNonMatch
parameter_list|(
name|EntityTag
name|eTag
parameter_list|,
name|Date
name|lastModified
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ifNonMatch
init|=
name|headers
operator|.
name|getRequestHeader
argument_list|(
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
argument_list|)
decl_stmt|;
if|if
condition|(
name|ifNonMatch
operator|==
literal|null
operator|||
name|ifNonMatch
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|lastModified
operator|==
literal|null
condition|?
literal|null
else|:
name|evaluateIfModifiedSince
argument_list|(
name|lastModified
argument_list|)
return|;
block|}
name|String
name|method
init|=
name|getMethod
argument_list|()
decl_stmt|;
name|boolean
name|getOrHead
init|=
name|HttpMethod
operator|.
name|GET
operator|.
name|equals
argument_list|(
name|method
argument_list|)
operator|||
name|HttpMethod
operator|.
name|HEAD
operator|.
name|equals
argument_list|(
name|method
argument_list|)
decl_stmt|;
try|try
block|{
for|for
control|(
name|String
name|value
range|:
name|ifNonMatch
control|)
block|{
name|boolean
name|result
init|=
literal|"*"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
condition|)
block|{
name|EntityTag
name|requestTag
init|=
name|EntityTag
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|result
operator|=
name|getOrHead
condition|?
name|requestTag
operator|.
name|equals
argument_list|(
name|eTag
argument_list|)
else|:
operator|!
name|requestTag
operator|.
name|isWeak
argument_list|()
operator|&&
operator|!
name|eTag
operator|.
name|isWeak
argument_list|()
operator|&&
name|requestTag
operator|.
name|equals
argument_list|(
name|eTag
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
condition|)
block|{
name|Response
operator|.
name|Status
name|status
init|=
name|getOrHead
condition|?
name|Response
operator|.
name|Status
operator|.
name|NOT_MODIFIED
else|:
name|Response
operator|.
name|Status
operator|.
name|PRECONDITION_FAILED
decl_stmt|;
return|return
name|Response
operator|.
name|status
argument_list|(
name|status
argument_list|)
operator|.
name|tag
argument_list|(
name|eTag
argument_list|)
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|ResponseBuilder
name|evaluatePreconditions
parameter_list|(
name|Date
name|lastModified
parameter_list|)
block|{
if|if
condition|(
name|lastModified
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Date is null"
argument_list|)
throw|;
block|}
name|ResponseBuilder
name|rb
init|=
name|evaluateIfNotModifiedSince
argument_list|(
name|lastModified
argument_list|)
decl_stmt|;
if|if
condition|(
name|rb
operator|==
literal|null
condition|)
block|{
name|rb
operator|=
name|evaluateIfModifiedSince
argument_list|(
name|lastModified
argument_list|)
expr_stmt|;
block|}
return|return
name|rb
return|;
block|}
specifier|private
name|ResponseBuilder
name|evaluateIfModifiedSince
parameter_list|(
name|Date
name|lastModified
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ifModifiedSince
init|=
name|headers
operator|.
name|getRequestHeader
argument_list|(
name|HttpHeaders
operator|.
name|IF_MODIFIED_SINCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ifModifiedSince
operator|==
literal|null
operator|||
name|ifModifiedSince
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|SimpleDateFormat
name|dateFormat
init|=
name|HttpUtils
operator|.
name|getHttpDateFormat
argument_list|()
decl_stmt|;
name|dateFormat
operator|.
name|setLenient
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Date
name|dateSince
init|=
literal|null
decl_stmt|;
try|try
block|{
name|dateSince
operator|=
name|dateFormat
operator|.
name|parse
argument_list|(
name|ifModifiedSince
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|ex
parameter_list|)
block|{
comment|// invalid header value, request should continue
return|return
name|Response
operator|.
name|status
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|PRECONDITION_FAILED
argument_list|)
return|;
block|}
if|if
condition|(
name|dateSince
operator|.
name|before
argument_list|(
name|lastModified
argument_list|)
condition|)
block|{
comment|// request should continue
return|return
literal|null
return|;
block|}
return|return
name|Response
operator|.
name|status
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|NOT_MODIFIED
argument_list|)
return|;
block|}
specifier|private
name|ResponseBuilder
name|evaluateIfNotModifiedSince
parameter_list|(
name|Date
name|lastModified
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ifNotModifiedSince
init|=
name|headers
operator|.
name|getRequestHeader
argument_list|(
name|HttpHeaders
operator|.
name|IF_UNMODIFIED_SINCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ifNotModifiedSince
operator|==
literal|null
operator|||
name|ifNotModifiedSince
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|SimpleDateFormat
name|dateFormat
init|=
name|HttpUtils
operator|.
name|getHttpDateFormat
argument_list|()
decl_stmt|;
name|dateFormat
operator|.
name|setLenient
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Date
name|dateSince
init|=
literal|null
decl_stmt|;
try|try
block|{
name|dateSince
operator|=
name|dateFormat
operator|.
name|parse
argument_list|(
name|ifNotModifiedSince
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|ex
parameter_list|)
block|{
comment|// invalid header value, request should continue
return|return
name|Response
operator|.
name|status
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|PRECONDITION_FAILED
argument_list|)
return|;
block|}
if|if
condition|(
name|dateSince
operator|.
name|before
argument_list|(
name|lastModified
argument_list|)
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|PRECONDITION_FAILED
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|ResponseBuilder
name|evaluatePreconditions
parameter_list|(
name|Date
name|lastModified
parameter_list|,
name|EntityTag
name|eTag
parameter_list|)
block|{
if|if
condition|(
name|eTag
operator|==
literal|null
operator|||
name|lastModified
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ETag or Date is null"
argument_list|)
throw|;
block|}
return|return
name|evaluateAll
argument_list|(
name|eTag
argument_list|,
name|lastModified
argument_list|)
return|;
block|}
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|ResponseBuilder
name|evaluatePreconditions
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ifMatch
init|=
name|headers
operator|.
name|getRequestHeader
argument_list|(
name|HttpHeaders
operator|.
name|IF_MATCH
argument_list|)
decl_stmt|;
if|if
condition|(
name|ifMatch
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|value
range|:
name|ifMatch
control|)
block|{
if|if
condition|(
operator|!
literal|"*"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|PRECONDITION_FAILED
argument_list|)
operator|.
name|tag
argument_list|(
name|EntityTag
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
class|class
name|VariantComparator
implements|implements
name|Comparator
argument_list|<
name|Variant
argument_list|>
block|{
specifier|static
specifier|final
name|VariantComparator
name|INSTANCE
init|=
operator|new
name|VariantComparator
argument_list|()
decl_stmt|;
specifier|public
name|int
name|compare
parameter_list|(
name|Variant
name|v1
parameter_list|,
name|Variant
name|v2
parameter_list|)
block|{
name|int
name|result
init|=
name|compareMediaTypes
argument_list|(
name|v1
operator|.
name|getMediaType
argument_list|()
argument_list|,
name|v2
operator|.
name|getMediaType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|0
condition|)
block|{
return|return
name|result
return|;
block|}
name|result
operator|=
name|compareLanguages
argument_list|(
name|v1
operator|.
name|getLanguage
argument_list|()
argument_list|,
name|v2
operator|.
name|getLanguage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|==
literal|0
condition|)
block|{
name|result
operator|=
name|compareEncodings
argument_list|(
name|v1
operator|.
name|getEncoding
argument_list|()
argument_list|,
name|v2
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
specifier|static
name|int
name|compareMediaTypes
parameter_list|(
name|MediaType
name|mt1
parameter_list|,
name|MediaType
name|mt2
parameter_list|)
block|{
if|if
condition|(
name|mt1
operator|!=
literal|null
operator|&&
name|mt2
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|mt1
operator|==
literal|null
operator|&&
name|mt2
operator|!=
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
return|return
name|JAXRSUtils
operator|.
name|compareMediaTypes
argument_list|(
name|mt1
argument_list|,
name|mt2
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|int
name|compareLanguages
parameter_list|(
name|Locale
name|l1
parameter_list|,
name|Locale
name|l2
parameter_list|)
block|{
if|if
condition|(
name|l1
operator|!=
literal|null
operator|&&
name|l2
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|l1
operator|==
literal|null
operator|&&
name|l2
operator|!=
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
return|return
literal|0
return|;
block|}
specifier|private
specifier|static
name|int
name|compareEncodings
parameter_list|(
name|String
name|enc1
parameter_list|,
name|String
name|enc2
parameter_list|)
block|{
if|if
condition|(
name|enc1
operator|!=
literal|null
operator|&&
name|enc2
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|enc1
operator|==
literal|null
operator|&&
name|enc2
operator|!=
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
return|return
literal|0
return|;
block|}
block|}
block|}
end_class

end_unit

