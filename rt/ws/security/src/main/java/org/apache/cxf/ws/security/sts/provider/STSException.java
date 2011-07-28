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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  * A RuntimeException that can be thrown by an STS implementation. If the FaultCode is set, then this   * code/String will be returned to the user, otherwise the Exception message is returned.  */
end_comment

begin_class
specifier|public
class|class
name|STSException
extends|extends
name|RuntimeException
block|{
comment|/**      * WS-Trust 1.3 namespace      */
specifier|public
specifier|static
specifier|final
name|String
name|WST_NS_05_12
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512"
decl_stmt|;
comment|/**      * Specification Fault Codes      */
specifier|public
specifier|static
specifier|final
name|QName
name|INVALID_REQUEST
init|=
operator|new
name|QName
argument_list|(
name|WST_NS_05_12
argument_list|,
literal|"InvalidRequest"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|FAILED_AUTH
init|=
operator|new
name|QName
argument_list|(
name|WST_NS_05_12
argument_list|,
literal|"FailedAuthentication"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUEST_FAILED
init|=
operator|new
name|QName
argument_list|(
name|WST_NS_05_12
argument_list|,
literal|"RequestFailed"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|INVALID_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|WST_NS_05_12
argument_list|,
literal|"InvalidSecurityToken"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|AUTH_BAD_ELEMENTS
init|=
operator|new
name|QName
argument_list|(
name|WST_NS_05_12
argument_list|,
literal|"AuthenticationBadElements"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|BAD_REQUEST
init|=
operator|new
name|QName
argument_list|(
name|WST_NS_05_12
argument_list|,
literal|"BadRequest"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|EXPIRED_DATA
init|=
operator|new
name|QName
argument_list|(
name|WST_NS_05_12
argument_list|,
literal|"ExpiredData"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|INVALID_TIME
init|=
operator|new
name|QName
argument_list|(
name|WST_NS_05_12
argument_list|,
literal|"InvalidTimeRange"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|INVALID_SCOPE
init|=
operator|new
name|QName
argument_list|(
name|WST_NS_05_12
argument_list|,
literal|"InvalidScope"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|RENEW_NEEDED
init|=
operator|new
name|QName
argument_list|(
name|WST_NS_05_12
argument_list|,
literal|"RenewNeeded"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|UNABLE_TO_RENEW
init|=
operator|new
name|QName
argument_list|(
name|WST_NS_05_12
argument_list|,
literal|"UnableToRenew"
argument_list|)
decl_stmt|;
comment|/**      * A map of Fault Code to Fault Strings      */
specifier|private
specifier|static
specifier|final
name|java
operator|.
name|util
operator|.
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|FAULT_CODE_MAP
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|HashMap
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|FAULT_CODE_MAP
operator|.
name|put
argument_list|(
name|INVALID_REQUEST
argument_list|,
literal|"The request was invalid or malformed"
argument_list|)
expr_stmt|;
name|FAULT_CODE_MAP
operator|.
name|put
argument_list|(
name|FAILED_AUTH
argument_list|,
literal|"Authentication failed"
argument_list|)
expr_stmt|;
name|FAULT_CODE_MAP
operator|.
name|put
argument_list|(
name|REQUEST_FAILED
argument_list|,
literal|"The specified request failed"
argument_list|)
expr_stmt|;
name|FAULT_CODE_MAP
operator|.
name|put
argument_list|(
name|INVALID_TOKEN
argument_list|,
literal|"Security token has been revoked"
argument_list|)
expr_stmt|;
name|FAULT_CODE_MAP
operator|.
name|put
argument_list|(
name|AUTH_BAD_ELEMENTS
argument_list|,
literal|"Insufficient Digest Elements"
argument_list|)
expr_stmt|;
name|FAULT_CODE_MAP
operator|.
name|put
argument_list|(
name|BAD_REQUEST
argument_list|,
literal|"The specified RequestSecurityToken is not understood"
argument_list|)
expr_stmt|;
name|FAULT_CODE_MAP
operator|.
name|put
argument_list|(
name|EXPIRED_DATA
argument_list|,
literal|"The request data is out-of-date"
argument_list|)
expr_stmt|;
name|FAULT_CODE_MAP
operator|.
name|put
argument_list|(
name|INVALID_TIME
argument_list|,
literal|"The requested time range is invalid or unsupported"
argument_list|)
expr_stmt|;
name|FAULT_CODE_MAP
operator|.
name|put
argument_list|(
name|INVALID_SCOPE
argument_list|,
literal|"The request scope is invalid or unsupported"
argument_list|)
expr_stmt|;
name|FAULT_CODE_MAP
operator|.
name|put
argument_list|(
name|RENEW_NEEDED
argument_list|,
literal|"A renewable security token has expired"
argument_list|)
expr_stmt|;
name|FAULT_CODE_MAP
operator|.
name|put
argument_list|(
name|UNABLE_TO_RENEW
argument_list|,
literal|"The requested renewal failed"
argument_list|)
expr_stmt|;
block|}
comment|/**      *       */
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2186924985128534490L
decl_stmt|;
specifier|private
name|QName
name|faultCode
decl_stmt|;
specifier|public
name|STSException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|STSException
parameter_list|(
name|String
name|message
parameter_list|,
name|QName
name|faultCode
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|faultCode
operator|=
name|faultCode
expr_stmt|;
block|}
specifier|public
name|STSException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|STSException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|e
parameter_list|,
name|QName
name|faultCode
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|this
operator|.
name|faultCode
operator|=
name|faultCode
expr_stmt|;
block|}
specifier|public
name|void
name|setFaultCode
parameter_list|(
name|QName
name|faultCode
parameter_list|)
block|{
name|this
operator|.
name|faultCode
operator|=
name|faultCode
expr_stmt|;
block|}
specifier|public
name|QName
name|getFaultCode
parameter_list|()
block|{
return|return
name|faultCode
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
if|if
condition|(
name|faultCode
operator|!=
literal|null
operator|&&
name|FAULT_CODE_MAP
operator|.
name|get
argument_list|(
name|faultCode
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|FAULT_CODE_MAP
operator|.
name|get
argument_list|(
name|faultCode
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|getMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

