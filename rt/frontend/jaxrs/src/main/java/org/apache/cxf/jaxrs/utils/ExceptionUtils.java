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
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|WebApplicationException
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
name|ext
operator|.
name|ExceptionMapper
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
name|provider
operator|.
name|ServerProviderFactory
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
specifier|final
class|class
name|ExceptionUtils
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PROPAGATE_EXCEPTION
init|=
literal|"org.apache.cxf.propagate.exception"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SUPPORT_WAE_SPEC_OPTIMIZATION
init|=
literal|"support.wae.spec.optimization"
decl_stmt|;
specifier|private
name|ExceptionUtils
parameter_list|()
block|{             }
specifier|public
specifier|static
name|String
name|getStackTrace
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sw
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getWebApplicationExceptionClass
parameter_list|(
name|Response
name|exResponse
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|defaultExceptionType
parameter_list|)
block|{
return|return
name|SpecExceptions
operator|.
name|getWebApplicationExceptionClass
argument_list|(
name|exResponse
argument_list|,
name|defaultExceptionType
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|propogateException
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|Object
name|value
init|=
name|m
operator|.
name|getContextualProperty
argument_list|(
name|PROPAGATE_EXCEPTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|||
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Throwable
parameter_list|>
name|Response
name|convertFaultToResponse
parameter_list|(
name|T
name|ex
parameter_list|,
name|Message
name|currentMessage
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|==
literal|null
operator|||
name|currentMessage
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Message
name|inMessage
init|=
name|currentMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
name|Response
name|response
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ex
operator|.
name|getClass
argument_list|()
operator|==
name|WebApplicationException
operator|.
name|class
condition|)
block|{
name|WebApplicationException
name|webEx
init|=
operator|(
name|WebApplicationException
operator|)
name|ex
decl_stmt|;
if|if
condition|(
name|webEx
operator|.
name|getResponse
argument_list|()
operator|.
name|hasEntity
argument_list|()
operator|&&
name|webEx
operator|.
name|getCause
argument_list|()
operator|==
literal|null
condition|)
block|{
name|Object
name|prop
init|=
name|inMessage
operator|.
name|getContextualProperty
argument_list|(
name|SUPPORT_WAE_SPEC_OPTIMIZATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|prop
operator|==
literal|null
operator|||
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|prop
argument_list|)
condition|)
block|{
name|response
operator|=
name|webEx
operator|.
name|getResponse
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|response
operator|==
literal|null
condition|)
block|{
name|ExceptionMapper
argument_list|<
name|T
argument_list|>
name|mapper
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|(
name|inMessage
argument_list|)
operator|.
name|createExceptionMapper
argument_list|(
name|ex
operator|.
name|getClass
argument_list|()
argument_list|,
name|inMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapper
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|response
operator|=
name|mapper
operator|.
name|toResponse
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|mapperEx
parameter_list|)
block|{
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|JAXRSUtils
operator|.
name|EXCEPTION_FROM_MAPPER
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|mapperEx
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
name|Response
operator|.
name|serverError
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
block|}
block|}
name|JAXRSUtils
operator|.
name|setMessageContentType
argument_list|(
name|currentMessage
argument_list|,
name|response
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
specifier|public
specifier|static
name|WebApplicationException
name|toWebApplicationException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
return|return
operator|new
name|WebApplicationException
argument_list|(
name|cause
argument_list|,
name|response
argument_list|)
return|;
block|}
comment|//TODO: we can simply use the reflection, investigate
specifier|public
specifier|static
name|WebApplicationException
name|toInternalServerErrorException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
try|try
block|{
return|return
name|SpecExceptions
operator|.
name|toInternalServerErrorException
argument_list|(
name|cause
argument_list|,
name|response
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|ex
parameter_list|)
block|{
return|return
name|toWebApplicationException
argument_list|(
name|ex
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|WebApplicationException
name|toBadRequestException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
try|try
block|{
return|return
name|SpecExceptions
operator|.
name|toBadRequestException
argument_list|(
name|cause
argument_list|,
name|response
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|ex
parameter_list|)
block|{
return|return
name|toWebApplicationException
argument_list|(
name|ex
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|WebApplicationException
name|toNotFoundException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
try|try
block|{
return|return
name|SpecExceptions
operator|.
name|toNotFoundException
argument_list|(
name|cause
argument_list|,
name|response
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|ex
parameter_list|)
block|{
return|return
name|toWebApplicationException
argument_list|(
name|ex
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|WebApplicationException
name|toNotAuthorizedException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
try|try
block|{
return|return
name|SpecExceptions
operator|.
name|toNotAuthorizedException
argument_list|(
name|cause
argument_list|,
name|response
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|ex
parameter_list|)
block|{
return|return
name|toWebApplicationException
argument_list|(
name|ex
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|WebApplicationException
name|toForbiddenException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
try|try
block|{
return|return
name|SpecExceptions
operator|.
name|toForbiddenException
argument_list|(
name|cause
argument_list|,
name|response
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|ex
parameter_list|)
block|{
return|return
name|toWebApplicationException
argument_list|(
name|ex
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|WebApplicationException
name|toNotAcceptableException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
try|try
block|{
return|return
name|SpecExceptions
operator|.
name|toNotAcceptableException
argument_list|(
name|cause
argument_list|,
name|response
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|ex
parameter_list|)
block|{
return|return
name|toWebApplicationException
argument_list|(
name|ex
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|WebApplicationException
name|toNotSupportedException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
try|try
block|{
return|return
name|SpecExceptions
operator|.
name|toNotSupportedException
argument_list|(
name|cause
argument_list|,
name|response
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|ex
parameter_list|)
block|{
return|return
name|toWebApplicationException
argument_list|(
name|ex
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|WebApplicationException
name|toHttpException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
try|try
block|{
return|return
name|SpecExceptions
operator|.
name|toHttpException
argument_list|(
name|cause
argument_list|,
name|response
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|ex
parameter_list|)
block|{
return|return
name|toWebApplicationException
argument_list|(
name|ex
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

