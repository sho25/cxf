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
name|tracing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tracing
operator|.
name|TracerHeaders
operator|.
name|DEFAULT_HEADER_SPAN_ID
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractTracingProvider
block|{
specifier|protected
specifier|static
name|String
name|getSpanIdHeader
parameter_list|()
block|{
return|return
name|getHeaderOrDefault
argument_list|(
name|TracerHeaders
operator|.
name|HEADER_SPAN_ID
argument_list|,
name|DEFAULT_HEADER_SPAN_ID
argument_list|)
return|;
block|}
specifier|protected
specifier|static
class|class
name|TraceScopeHolder
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|5985783659818936359L
decl_stmt|;
specifier|private
specifier|final
name|T
name|scope
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|detached
decl_stmt|;
specifier|public
name|TraceScopeHolder
parameter_list|(
specifier|final
name|T
name|scope
parameter_list|,
specifier|final
name|boolean
name|detached
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
name|this
operator|.
name|detached
operator|=
name|detached
expr_stmt|;
block|}
specifier|public
name|T
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
name|boolean
name|isDetached
parameter_list|()
block|{
return|return
name|detached
return|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getHeaderOrDefault
parameter_list|(
specifier|final
name|String
name|property
parameter_list|,
specifier|final
name|String
name|fallback
parameter_list|)
block|{
specifier|final
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
specifier|final
name|Object
name|header
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|property
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|instanceof
name|String
condition|)
block|{
specifier|final
name|String
name|name
init|=
operator|(
name|String
operator|)
name|header
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|name
return|;
block|}
block|}
block|}
return|return
name|fallback
return|;
block|}
specifier|protected
name|String
name|buildSpanDescription
parameter_list|(
specifier|final
name|String
name|path
parameter_list|,
specifier|final
name|String
name|method
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
name|path
return|;
block|}
return|return
name|method
operator|+
literal|" "
operator|+
name|path
return|;
block|}
specifier|private
specifier|static
name|String
name|safeGet
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
operator|!
name|message
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|value
init|=
name|message
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|value
operator|instanceof
name|String
operator|)
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
specifier|private
specifier|static
name|String
name|getUriAsString
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|uri
init|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|REQUEST_URL
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
name|String
name|address
init|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
decl_stmt|;
name|uri
operator|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|REQUEST_URI
argument_list|)
expr_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
operator|&&
name|uri
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
if|if
condition|(
name|address
operator|!=
literal|null
operator|&&
operator|!
name|address
operator|.
name|startsWith
argument_list|(
name|uri
argument_list|)
condition|)
block|{
if|if
condition|(
name|address
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|&&
name|address
operator|.
name|length
argument_list|()
operator|>
literal|1
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
name|uri
operator|=
name|address
operator|+
name|uri
expr_stmt|;
block|}
block|}
else|else
block|{
name|uri
operator|=
name|address
expr_stmt|;
block|}
block|}
name|String
name|query
init|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|QUERY_STRING
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
return|return
name|uri
operator|+
literal|"?"
operator|+
name|query
return|;
block|}
return|return
name|uri
return|;
block|}
specifier|protected
specifier|static
name|URI
name|getUri
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|String
name|uriSt
init|=
name|getUriAsString
argument_list|(
name|message
argument_list|)
decl_stmt|;
return|return
name|uriSt
operator|!=
literal|null
condition|?
operator|new
name|URI
argument_list|(
name|uriSt
argument_list|)
else|:
operator|new
name|URI
argument_list|(
literal|""
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

