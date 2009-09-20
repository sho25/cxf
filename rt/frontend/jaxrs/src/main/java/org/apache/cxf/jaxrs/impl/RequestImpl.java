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
name|Date
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
comment|// TODO Auto-generated method stub
return|return
literal|null
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
name|ResponseBuilder
name|rb
init|=
name|evaluateIfMatch
argument_list|(
name|eTag
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
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
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
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
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
literal|"GET"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
operator|||
literal|"HEAD"
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
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|evaluateIfNotModifiedSince
argument_list|(
name|lastModified
argument_list|)
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
specifier|public
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
name|size
argument_list|()
operator|==
literal|0
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
name|ResponseBuilder
name|rb
init|=
name|evaluatePreconditions
argument_list|(
name|eTag
argument_list|)
decl_stmt|;
if|if
condition|(
name|rb
operator|!=
literal|null
condition|)
block|{
return|return
name|rb
return|;
block|}
return|return
name|evaluatePreconditions
argument_list|(
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
block|}
end_class

end_unit

