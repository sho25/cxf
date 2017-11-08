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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|NotFoundException
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|ExceptionUtils
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
name|logging
operator|.
name|FaultListener
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
comment|/**  * Default exception mapper for {@link WebApplicationException}.  * This class interacts with {@link FaultListener}.  * If {@link FaultListener} is available and has indicated that it handled the exception then  * no more logging is done, otherwise a message is logged at WARN (default) or FINE level  * which can be controlled with a printStackTrace property  */
end_comment

begin_class
specifier|public
class|class
name|WebApplicationExceptionMapper
implements|implements
name|ExceptionMapper
argument_list|<
name|WebApplicationException
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|WebApplicationExceptionMapper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ERROR_MESSAGE_START
init|=
literal|"WebApplicationException has been caught, status: "
decl_stmt|;
specifier|private
name|boolean
name|printStackTrace
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|addMessageToResponse
decl_stmt|;
specifier|public
name|Response
name|toResponse
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|Response
name|r
init|=
name|ex
operator|.
name|getResponse
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
name|Response
operator|.
name|serverError
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
name|boolean
name|doAddMessage
init|=
name|r
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|?
literal|false
else|:
name|addMessageToResponse
decl_stmt|;
name|Message
name|msg
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|FaultListener
name|flogger
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
name|flogger
operator|=
operator|(
name|FaultListener
operator|)
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getContextualProperty
argument_list|(
name|FaultListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|errorMessage
init|=
name|doAddMessage
operator|||
name|flogger
operator|!=
literal|null
condition|?
name|buildErrorMessage
argument_list|(
name|r
argument_list|,
name|ex
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|flogger
operator|==
literal|null
operator|||
operator|!
name|flogger
operator|.
name|faultOccurred
argument_list|(
name|ex
argument_list|,
name|errorMessage
argument_list|,
name|msg
argument_list|)
condition|)
block|{
name|Level
name|level
init|=
name|printStackTrace
condition|?
name|getStackTraceLogLevel
argument_list|(
name|msg
argument_list|,
name|r
argument_list|)
else|:
name|Level
operator|.
name|FINE
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|level
argument_list|,
name|ExceptionUtils
operator|.
name|getStackTrace
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|doAddMessage
condition|)
block|{
name|r
operator|=
name|JAXRSUtils
operator|.
name|copyResponseIfNeeded
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|r
operator|=
name|buildResponse
argument_list|(
name|r
argument_list|,
name|errorMessage
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
specifier|protected
name|Level
name|getStackTraceLogLevel
parameter_list|(
name|Message
name|msg
parameter_list|,
name|Response
name|r
parameter_list|)
block|{
if|if
condition|(
name|r
operator|.
name|getStatus
argument_list|()
operator|==
literal|404
condition|)
block|{
name|Level
name|logLevel
init|=
name|JAXRSUtils
operator|.
name|getExceptionLogLevel
argument_list|(
name|msg
argument_list|,
name|NotFoundException
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|logLevel
operator|==
literal|null
condition|?
name|Level
operator|.
name|FINE
else|:
name|logLevel
return|;
block|}
return|return
name|Level
operator|.
name|WARNING
return|;
block|}
specifier|protected
name|String
name|buildErrorMessage
parameter_list|(
name|Response
name|r
parameter_list|,
name|WebApplicationException
name|ex
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
name|ERROR_MESSAGE_START
argument_list|)
operator|.
name|append
argument_list|(
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Throwable
name|cause
init|=
name|ex
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|String
name|message
init|=
name|cause
operator|==
literal|null
condition|?
name|ex
operator|.
name|getMessage
argument_list|()
else|:
name|cause
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|==
literal|null
operator|&&
name|cause
operator|!=
literal|null
condition|)
block|{
name|message
operator|=
literal|"exception cause class: "
operator|+
name|cause
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", message: "
argument_list|)
operator|.
name|append
argument_list|(
name|message
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
specifier|protected
name|Response
name|buildResponse
parameter_list|(
name|Response
name|response
parameter_list|,
name|String
name|responseText
parameter_list|)
block|{
name|Response
operator|.
name|ResponseBuilder
name|rb
init|=
name|JAXRSUtils
operator|.
name|fromResponse
argument_list|(
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseText
operator|!=
literal|null
condition|)
block|{
name|rb
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
operator|.
name|entity
argument_list|(
name|responseText
argument_list|)
expr_stmt|;
block|}
return|return
name|rb
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Control whether to log at WARN or FINE level.      * Note this property is ignored if a registered {@link FaultListener}      * has handled the exception      * @param printStackTrace if set to true then WARN level is used (default),      *        otherwise - FINE level.      */
specifier|public
name|void
name|setPrintStackTrace
parameter_list|(
name|boolean
name|printStackTrace
parameter_list|)
block|{
name|this
operator|.
name|printStackTrace
operator|=
name|printStackTrace
expr_stmt|;
block|}
comment|/**      * Controls whether to add an error message to Response or not,      * @param addMessageToResponse add a message to Response, ignored      *        if the captuted WebApplicationException has      *        a Response with a non-null entity      */
specifier|public
name|void
name|setAddMessageToResponse
parameter_list|(
name|boolean
name|addMessageToResponse
parameter_list|)
block|{
name|this
operator|.
name|addMessageToResponse
operator|=
name|addMessageToResponse
expr_stmt|;
block|}
block|}
end_class

end_unit

