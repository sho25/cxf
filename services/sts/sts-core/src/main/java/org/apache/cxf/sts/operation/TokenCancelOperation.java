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
name|sts
operator|.
name|operation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
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
name|sts
operator|.
name|QNameConstants
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
name|sts
operator|.
name|STSConstants
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
name|sts
operator|.
name|event
operator|.
name|STSCancelFailureEvent
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
name|sts
operator|.
name|event
operator|.
name|STSCancelSuccessEvent
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
name|sts
operator|.
name|request
operator|.
name|KeyRequirements
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
name|sts
operator|.
name|request
operator|.
name|ReceivedToken
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
name|sts
operator|.
name|request
operator|.
name|ReceivedToken
operator|.
name|STATE
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
name|sts
operator|.
name|request
operator|.
name|RequestRequirements
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
name|sts
operator|.
name|request
operator|.
name|TokenRequirements
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
name|sts
operator|.
name|token
operator|.
name|canceller
operator|.
name|TokenCanceller
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
name|sts
operator|.
name|token
operator|.
name|canceller
operator|.
name|TokenCancellerParameters
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
name|sts
operator|.
name|token
operator|.
name|canceller
operator|.
name|TokenCancellerResponse
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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|STSException
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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestSecurityTokenResponseType
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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestSecurityTokenType
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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestedTokenCancelledType
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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|operation
operator|.
name|CancelOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_comment
comment|/**  *  An implementation for Cancel operation interface.  */
end_comment

begin_class
specifier|public
class|class
name|TokenCancelOperation
extends|extends
name|AbstractOperation
implements|implements
name|CancelOperation
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
name|TokenCancelOperation
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|TokenCanceller
argument_list|>
name|tokencancellers
init|=
operator|new
name|ArrayList
argument_list|<
name|TokenCanceller
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setTokenCancellers
parameter_list|(
name|List
argument_list|<
name|TokenCanceller
argument_list|>
name|tokenCancellerList
parameter_list|)
block|{
name|this
operator|.
name|tokencancellers
operator|=
name|tokenCancellerList
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|TokenCanceller
argument_list|>
name|getTokenCancellers
parameter_list|()
block|{
return|return
name|tokencancellers
return|;
block|}
specifier|public
name|RequestSecurityTokenResponseType
name|cancel
parameter_list|(
name|RequestSecurityTokenType
name|request
parameter_list|,
name|Principal
name|principal
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|messageContext
parameter_list|)
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|TokenCancellerParameters
name|cancellerParameters
init|=
operator|new
name|TokenCancellerParameters
argument_list|()
decl_stmt|;
try|try
block|{
name|RequestRequirements
name|requestRequirements
init|=
name|parseRequest
argument_list|(
name|request
argument_list|,
name|messageContext
argument_list|)
decl_stmt|;
name|KeyRequirements
name|keyRequirements
init|=
name|requestRequirements
operator|.
name|getKeyRequirements
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|requestRequirements
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
name|cancellerParameters
operator|.
name|setStsProperties
argument_list|(
name|stsProperties
argument_list|)
expr_stmt|;
name|cancellerParameters
operator|.
name|setPrincipal
argument_list|(
name|principal
argument_list|)
expr_stmt|;
name|cancellerParameters
operator|.
name|setMessageContext
argument_list|(
name|messageContext
argument_list|)
expr_stmt|;
name|cancellerParameters
operator|.
name|setTokenStore
argument_list|(
name|getTokenStore
argument_list|()
argument_list|)
expr_stmt|;
name|cancellerParameters
operator|.
name|setKeyRequirements
argument_list|(
name|keyRequirements
argument_list|)
expr_stmt|;
name|cancellerParameters
operator|.
name|setTokenRequirements
argument_list|(
name|tokenRequirements
argument_list|)
expr_stmt|;
name|ReceivedToken
name|cancelTarget
init|=
name|tokenRequirements
operator|.
name|getCancelTarget
argument_list|()
decl_stmt|;
if|if
condition|(
name|cancelTarget
operator|==
literal|null
operator|||
name|cancelTarget
operator|.
name|getToken
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No element presented for cancellation"
argument_list|,
name|STSException
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
name|cancellerParameters
operator|.
name|setToken
argument_list|(
name|cancelTarget
argument_list|)
expr_stmt|;
if|if
condition|(
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|tokenRequirements
operator|.
name|setTokenType
argument_list|(
name|STSConstants
operator|.
name|STATUS
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Received TokenType is null, falling back to default token type: "
operator|+
name|STSConstants
operator|.
name|STATUS
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// Cancel token
comment|//
name|TokenCancellerResponse
name|tokenResponse
init|=
literal|null
decl_stmt|;
for|for
control|(
name|TokenCanceller
name|tokenCanceller
range|:
name|tokencancellers
control|)
block|{
if|if
condition|(
name|tokenCanceller
operator|.
name|canHandleToken
argument_list|(
name|cancelTarget
argument_list|)
condition|)
block|{
try|try
block|{
name|tokenResponse
operator|=
name|tokenCanceller
operator|.
name|cancelToken
argument_list|(
name|cancellerParameters
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error while cancelling a token"
argument_list|,
name|ex
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|tokenResponse
operator|==
literal|null
operator|||
name|tokenResponse
operator|.
name|getToken
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No Token Canceller has been found that can handle this token"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"No token canceller found for requested token type: "
operator|+
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
if|if
condition|(
name|tokenResponse
operator|.
name|getToken
argument_list|()
operator|.
name|getState
argument_list|()
operator|!=
name|STATE
operator|.
name|CANCELLED
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Token cancellation failed."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Token cancellation failed."
argument_list|)
throw|;
block|}
comment|// prepare response
try|try
block|{
name|RequestSecurityTokenResponseType
name|response
init|=
name|createResponse
argument_list|(
name|tokenRequirements
argument_list|)
decl_stmt|;
name|STSCancelSuccessEvent
name|event
init|=
operator|new
name|STSCancelSuccessEvent
argument_list|(
name|cancellerParameters
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
argument_list|)
decl_stmt|;
name|publishEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in creating the response"
argument_list|,
name|ex
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
name|STSCancelFailureEvent
name|event
init|=
operator|new
name|STSCancelFailureEvent
argument_list|(
name|cancellerParameters
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
argument_list|,
name|ex
argument_list|)
decl_stmt|;
name|publishEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|private
name|RequestSecurityTokenResponseType
name|createResponse
parameter_list|(
name|TokenRequirements
name|tokenRequirements
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|RequestSecurityTokenResponseType
name|response
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestSecurityTokenResponseType
argument_list|()
decl_stmt|;
name|String
name|context
init|=
name|tokenRequirements
operator|.
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
name|RequestedTokenCancelledType
name|cancelType
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedTokenCancelledType
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|RequestedTokenCancelledType
argument_list|>
name|cancel
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedTokenCancelled
argument_list|(
name|cancelType
argument_list|)
decl_stmt|;
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|cancel
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
block|}
end_class

end_unit

