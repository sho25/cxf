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
name|xkms
operator|.
name|service
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
name|List
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
name|xkms
operator|.
name|exception
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
name|xkms
operator|.
name|handlers
operator|.
name|Locator
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
name|xkms
operator|.
name|handlers
operator|.
name|Register
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
name|xkms
operator|.
name|handlers
operator|.
name|Validator
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
name|xkms
operator|.
name|handlers
operator|.
name|XKMSConstants
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|CompoundRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|CompoundResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|KeyBindingAbstractType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|KeyBindingEnum
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|KeyBindingType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|KeyUsageEnum
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|LocateRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|LocateResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|MessageAbstractType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|PendingRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RecoverRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RecoverResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RegisterRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RegisterResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ReissueRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ReissueResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ResultMinorEnum
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RevokeRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RevokeResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|StatusRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|StatusResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|StatusType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|UnverifiedKeyBindingType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ValidateRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ValidateResultType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3
operator|.
name|_2002
operator|.
name|_03
operator|.
name|xkms_wsdl
operator|.
name|XKMSPortType
import|;
end_import

begin_class
specifier|public
class|class
name|XKMSService
implements|implements
name|XKMSPortType
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|XKMSService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|serviceName
init|=
name|XKMSConstants
operator|.
name|XKMS_ENDPOINT_NAME
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Locator
argument_list|>
name|locators
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Validator
argument_list|>
name|validators
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Register
argument_list|>
name|keyRegisterHandlers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|enableXKRSS
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|logExceptions
decl_stmt|;
annotation|@
name|Override
specifier|public
name|ReissueResultType
name|reissue
parameter_list|(
name|ReissueRequestType
name|request
parameter_list|)
block|{
name|ReissueResultType
name|response
init|=
name|XKMSResponseFactory
operator|.
name|createResponse
argument_list|(
name|request
argument_list|,
operator|new
name|ReissueResultType
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|assertXKRSSAllowed
argument_list|()
expr_stmt|;
name|validateRequest
argument_list|(
name|request
argument_list|)
expr_stmt|;
for|for
control|(
name|Register
name|handler
range|:
name|keyRegisterHandlers
control|)
block|{
if|if
condition|(
name|handler
operator|.
name|canProcess
argument_list|(
name|request
argument_list|)
condition|)
block|{
return|return
name|handler
operator|.
name|reissue
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Service was unable to handle your request"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|handleException
argument_list|(
literal|"reissue"
argument_list|,
name|e
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|CompoundResultType
name|compound
parameter_list|(
name|CompoundRequestType
name|request
parameter_list|)
block|{
name|validateRequest
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|RuntimeException
name|ex
init|=
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"XKMS compound request is currently not supported"
argument_list|)
decl_stmt|;
name|CompoundResultType
name|response
init|=
name|XKMSResponseFactory
operator|.
name|createResponse
argument_list|(
name|request
argument_list|,
operator|new
name|CompoundResultType
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|handleException
argument_list|(
literal|"compound"
argument_list|,
name|ex
argument_list|,
name|response
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|RegisterResultType
name|register
parameter_list|(
name|RegisterRequestType
name|request
parameter_list|)
block|{
name|RegisterResultType
name|response
init|=
name|XKMSResponseFactory
operator|.
name|createResponse
argument_list|(
name|request
argument_list|,
operator|new
name|RegisterResultType
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|assertXKRSSAllowed
argument_list|()
expr_stmt|;
name|validateRequest
argument_list|(
name|request
argument_list|)
expr_stmt|;
for|for
control|(
name|Register
name|handler
range|:
name|keyRegisterHandlers
control|)
block|{
if|if
condition|(
name|handler
operator|.
name|canProcess
argument_list|(
name|request
argument_list|)
condition|)
block|{
return|return
name|handler
operator|.
name|register
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Service was unable to handle your request"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|handleException
argument_list|(
literal|"register"
argument_list|,
name|e
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|ResultType
name|pending
parameter_list|(
name|PendingRequestType
name|request
parameter_list|)
block|{
name|validateRequest
argument_list|(
name|request
argument_list|)
expr_stmt|;
return|return
name|ExceptionMapper
operator|.
name|toResponse
argument_list|(
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"XKMS request is currently not supported"
argument_list|)
argument_list|,
name|XKMSResponseFactory
operator|.
name|createResponse
argument_list|(
name|request
argument_list|,
operator|new
name|ResultType
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|RevokeResultType
name|revoke
parameter_list|(
name|RevokeRequestType
name|request
parameter_list|)
block|{
name|RevokeResultType
name|response
init|=
name|XKMSResponseFactory
operator|.
name|createResponse
argument_list|(
name|request
argument_list|,
operator|new
name|RevokeResultType
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|assertXKRSSAllowed
argument_list|()
expr_stmt|;
name|validateRequest
argument_list|(
name|request
argument_list|)
expr_stmt|;
for|for
control|(
name|Register
name|handler
range|:
name|keyRegisterHandlers
control|)
block|{
if|if
condition|(
name|handler
operator|.
name|canProcess
argument_list|(
name|request
argument_list|)
condition|)
block|{
return|return
name|handler
operator|.
name|revoke
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Service was unable to handle your request"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|handleException
argument_list|(
literal|"revoke"
argument_list|,
name|e
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|LocateResultType
name|locate
parameter_list|(
name|LocateRequestType
name|request
parameter_list|)
block|{
name|LocateResultType
name|response
init|=
name|XKMSResponseFactory
operator|.
name|createResponse
argument_list|(
name|request
argument_list|,
operator|new
name|LocateResultType
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|validateRequest
argument_list|(
name|request
argument_list|)
expr_stmt|;
comment|// Search
for|for
control|(
name|Locator
name|locator
range|:
name|locators
control|)
block|{
name|UnverifiedKeyBindingType
name|keyBinding
init|=
name|locator
operator|.
name|locate
argument_list|(
name|request
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyBinding
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|getUnverifiedKeyBinding
argument_list|()
operator|.
name|add
argument_list|(
name|keyBinding
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
block|}
comment|// No matches found
name|response
operator|.
name|setResultMinor
argument_list|(
name|ResultMinorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_NO_MATCH
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|handleException
argument_list|(
literal|"locate"
argument_list|,
name|e
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RecoverResultType
name|recover
parameter_list|(
name|RecoverRequestType
name|request
parameter_list|)
block|{
name|RecoverResultType
name|response
init|=
name|XKMSResponseFactory
operator|.
name|createResponse
argument_list|(
name|request
argument_list|,
operator|new
name|RecoverResultType
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|assertXKRSSAllowed
argument_list|()
expr_stmt|;
name|validateRequest
argument_list|(
name|request
argument_list|)
expr_stmt|;
for|for
control|(
name|Register
name|handler
range|:
name|keyRegisterHandlers
control|)
block|{
if|if
condition|(
name|handler
operator|.
name|canProcess
argument_list|(
name|request
argument_list|)
condition|)
block|{
return|return
name|handler
operator|.
name|recover
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Service was unable to handle your request"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|handleException
argument_list|(
literal|"recover"
argument_list|,
name|e
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|StatusResultType
name|status
parameter_list|(
name|StatusRequestType
name|request
parameter_list|)
block|{
name|validateRequest
argument_list|(
name|request
argument_list|)
expr_stmt|;
return|return
name|ExceptionMapper
operator|.
name|toResponse
argument_list|(
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"XKMS request is currently not supported"
argument_list|)
argument_list|,
name|XKMSResponseFactory
operator|.
name|createResponse
argument_list|(
name|request
argument_list|,
operator|new
name|StatusResultType
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ValidateResultType
name|validate
parameter_list|(
name|ValidateRequestType
name|request
parameter_list|)
block|{
name|ValidateResultType
name|response
init|=
name|XKMSResponseFactory
operator|.
name|createResponse
argument_list|(
name|request
argument_list|,
operator|new
name|ValidateResultType
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|validateRequest
argument_list|(
name|request
argument_list|)
expr_stmt|;
comment|// Create basic response
name|KeyBindingType
name|binding
init|=
name|createKeyBinding
argument_list|(
name|response
argument_list|)
decl_stmt|;
comment|// Validate request
for|for
control|(
name|Validator
name|validator
range|:
name|validators
control|)
block|{
name|StatusType
name|status
init|=
name|validator
operator|.
name|validate
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|addValidationReasons
argument_list|(
name|binding
argument_list|,
name|status
argument_list|)
expr_stmt|;
block|}
name|resolveValidationStatus
argument_list|(
name|binding
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|handleException
argument_list|(
literal|"recover"
argument_list|,
name|e
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
specifier|private
parameter_list|<
name|T
extends|extends
name|ResultType
parameter_list|>
name|T
name|handleException
parameter_list|(
name|String
name|method
parameter_list|,
name|Exception
name|e
parameter_list|,
name|T
name|response
parameter_list|)
block|{
if|if
condition|(
name|logExceptions
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Error during "
operator|+
name|method
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|ExceptionMapper
operator|.
name|toResponse
argument_list|(
name|e
argument_list|,
name|response
argument_list|)
return|;
block|}
comment|/**      * Performs basic validations on request message to ensure XKMS standard is applied correctly.      *      * The following validations are performed: 1) Check if a request ID is set 2) Check if service name equals this      * XKMS service instance      *      * @param request XKMS request      */
specifier|private
name|void
name|validateRequest
parameter_list|(
name|MessageAbstractType
name|request
parameter_list|)
block|{
comment|// Check if ID is set
if|if
condition|(
name|request
operator|.
name|getId
argument_list|()
operator|==
literal|null
operator|||
name|request
operator|.
name|getId
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Message Id is not set"
argument_list|)
throw|;
block|}
comment|// Check if Service matches this instance
if|if
condition|(
operator|!
name|serviceName
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getService
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Service "
operator|+
name|request
operator|.
name|getService
argument_list|()
operator|+
literal|" is not responsible to process request"
argument_list|)
throw|;
block|}
block|}
comment|// TODO refactoring into factory class?
specifier|public
specifier|static
name|KeyBindingType
name|createKeyBinding
parameter_list|(
name|ValidateResultType
name|result
parameter_list|)
block|{
name|KeyBindingType
name|binding
init|=
operator|new
name|KeyBindingType
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setId
argument_list|(
name|XKMSResponseFactory
operator|.
name|generateUniqueID
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|getKeyBinding
argument_list|()
operator|.
name|add
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|StatusType
name|status
init|=
operator|new
name|StatusType
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setStatus
argument_list|(
name|status
argument_list|)
expr_stmt|;
return|return
name|binding
return|;
block|}
specifier|private
name|void
name|addValidationReasons
parameter_list|(
name|KeyBindingType
name|binding
parameter_list|,
name|StatusType
name|status
parameter_list|)
block|{
name|StatusType
name|resultStatus
init|=
name|binding
operator|.
name|getStatus
argument_list|()
decl_stmt|;
name|resultStatus
operator|.
name|getValidReason
argument_list|()
operator|.
name|addAll
argument_list|(
name|status
operator|.
name|getValidReason
argument_list|()
argument_list|)
expr_stmt|;
name|resultStatus
operator|.
name|getInvalidReason
argument_list|()
operator|.
name|addAll
argument_list|(
name|status
operator|.
name|getInvalidReason
argument_list|()
argument_list|)
expr_stmt|;
name|resultStatus
operator|.
name|getIndeterminateReason
argument_list|()
operator|.
name|addAll
argument_list|(
name|status
operator|.
name|getIndeterminateReason
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
block|}
specifier|public
name|void
name|setLocators
parameter_list|(
name|List
argument_list|<
name|Locator
argument_list|>
name|locators
parameter_list|)
block|{
name|this
operator|.
name|locators
operator|=
name|locators
expr_stmt|;
block|}
specifier|public
name|void
name|setValidators
parameter_list|(
name|List
argument_list|<
name|Validator
argument_list|>
name|validators
parameter_list|)
block|{
name|this
operator|.
name|validators
operator|=
name|validators
expr_stmt|;
block|}
specifier|public
name|void
name|setKeyRegisterHandlers
parameter_list|(
name|List
argument_list|<
name|Register
argument_list|>
name|keyRegisterHandlers
parameter_list|)
block|{
name|this
operator|.
name|keyRegisterHandlers
operator|=
name|keyRegisterHandlers
expr_stmt|;
block|}
comment|/**      * http://www.w3.org/TR/xkms2/#XKMS_2_0_Section_4_1 [206]      *      * If no (or indeterminate) reasons are present total status is INDETERMINATE.      * If no invalid and indeterminate reasons are present status is VALID.      * If invalid reasons are present status is INVALID.      *      * @param binding KeyBinding to check validation reasons for      */
specifier|private
name|void
name|resolveValidationStatus
parameter_list|(
name|KeyBindingType
name|binding
parameter_list|)
block|{
name|StatusType
name|status
init|=
name|binding
operator|.
name|getStatus
argument_list|()
decl_stmt|;
name|status
operator|.
name|setStatusValue
argument_list|(
name|KeyBindingEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_INDETERMINATE
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|status
operator|.
name|getValidReason
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|status
operator|.
name|getIndeterminateReason
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|status
operator|.
name|setStatusValue
argument_list|(
name|KeyBindingEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_VALID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|status
operator|.
name|getInvalidReason
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|status
operator|.
name|setStatusValue
argument_list|(
name|KeyBindingEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_INVALID
argument_list|)
expr_stmt|;
comment|// Only return invalid reasons
name|status
operator|.
name|getValidReason
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Sets encryption, signature and exchange as key usage for provided keyBinding.      *      * @param keyBinding KeyBinding to set KeyUsage within      */
specifier|protected
name|void
name|setKeyUsageAll
parameter_list|(
name|KeyBindingAbstractType
name|keyBinding
parameter_list|)
block|{
name|keyBinding
operator|.
name|getKeyUsage
argument_list|()
operator|.
name|add
argument_list|(
name|KeyUsageEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_ENCRYPTION
argument_list|)
expr_stmt|;
name|keyBinding
operator|.
name|getKeyUsage
argument_list|()
operator|.
name|add
argument_list|(
name|KeyUsageEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_SIGNATURE
argument_list|)
expr_stmt|;
name|keyBinding
operator|.
name|getKeyUsage
argument_list|()
operator|.
name|add
argument_list|(
name|KeyUsageEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_EXCHANGE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setEnableXKRSS
parameter_list|(
name|boolean
name|enableXKRSS
parameter_list|)
block|{
name|this
operator|.
name|enableXKRSS
operator|=
name|enableXKRSS
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"enableXKRSS:"
operator|+
name|enableXKRSS
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLogExceptions
parameter_list|(
name|boolean
name|logExceptions
parameter_list|)
block|{
name|this
operator|.
name|logExceptions
operator|=
name|logExceptions
expr_stmt|;
block|}
specifier|private
name|void
name|assertXKRSSAllowed
parameter_list|()
block|{
if|if
condition|(
operator|!
name|enableXKRSS
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"XKRSS Operations are disabled"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

