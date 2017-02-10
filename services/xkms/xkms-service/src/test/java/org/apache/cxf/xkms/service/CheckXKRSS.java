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
name|extensions
operator|.
name|ResultDetails
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
name|RequestAbstractType
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
name|ResultMajorEnum
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|CheckXKRSS
block|{
annotation|@
name|Test
specifier|public
name|void
name|checkRegisterWithXKRSS
parameter_list|()
block|{
name|RegisterRequestType
name|request
init|=
operator|new
name|RegisterRequestType
argument_list|()
decl_stmt|;
name|request
operator|.
name|setId
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|request
operator|.
name|setService
argument_list|(
name|XKMSConstants
operator|.
name|XKMS_ENDPOINT_NAME
argument_list|)
expr_stmt|;
name|RegisterResultType
name|result
init|=
name|createXKMSService
argument_list|(
literal|true
argument_list|)
operator|.
name|register
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|showResult
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|checkRegisterWithoutXKRSS
parameter_list|()
block|{
name|RegisterRequestType
name|request
init|=
operator|new
name|RegisterRequestType
argument_list|()
decl_stmt|;
name|request
operator|.
name|setId
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|request
operator|.
name|setService
argument_list|(
name|XKMSConstants
operator|.
name|XKMS_ENDPOINT_NAME
argument_list|)
expr_stmt|;
name|createXKMSService
argument_list|(
literal|false
argument_list|)
operator|.
name|register
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|RegisterResultType
name|result
init|=
name|createXKMSService
argument_list|(
literal|false
argument_list|)
operator|.
name|register
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertNotSupported
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|checkRevokeWithXKRSS
parameter_list|()
block|{
name|RegisterRequestType
name|request
init|=
operator|new
name|RegisterRequestType
argument_list|()
decl_stmt|;
name|request
operator|.
name|setId
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|request
operator|.
name|setService
argument_list|(
name|XKMSConstants
operator|.
name|XKMS_ENDPOINT_NAME
argument_list|)
expr_stmt|;
name|ResultType
name|result
init|=
name|createXKMSService
argument_list|(
literal|true
argument_list|)
operator|.
name|register
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|checkRevokeWithoutXKRSS
parameter_list|()
block|{
name|RevokeRequestType
name|request
init|=
operator|new
name|RevokeRequestType
argument_list|()
decl_stmt|;
name|request
operator|.
name|setId
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|request
operator|.
name|setService
argument_list|(
name|XKMSConstants
operator|.
name|XKMS_ENDPOINT_NAME
argument_list|)
expr_stmt|;
name|ResultType
name|result
init|=
name|createXKMSService
argument_list|(
literal|false
argument_list|)
operator|.
name|revoke
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertNotSupported
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|checkRecoverWithXKRSS
parameter_list|()
block|{
name|RecoverRequestType
name|request
init|=
operator|new
name|RecoverRequestType
argument_list|()
decl_stmt|;
name|request
operator|.
name|setId
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|request
operator|.
name|setService
argument_list|(
name|XKMSConstants
operator|.
name|XKMS_ENDPOINT_NAME
argument_list|)
expr_stmt|;
name|ResultType
name|result
init|=
name|createXKMSService
argument_list|(
literal|true
argument_list|)
operator|.
name|recover
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|showResult
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|checkRecoverWithoutXKRSS
parameter_list|()
block|{
name|RecoverRequestType
name|request
init|=
operator|new
name|RecoverRequestType
argument_list|()
decl_stmt|;
name|request
operator|.
name|setId
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|request
operator|.
name|setService
argument_list|(
name|XKMSConstants
operator|.
name|XKMS_ENDPOINT_NAME
argument_list|)
expr_stmt|;
name|ResultType
name|result
init|=
name|createXKMSService
argument_list|(
literal|false
argument_list|)
operator|.
name|recover
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertNotSupported
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertNotSupported
parameter_list|(
name|ResultType
name|result
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_SENDER
operator|.
name|value
argument_list|()
argument_list|,
name|result
operator|.
name|getResultMajor
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ResultMinorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_MESSAGE_NOT_SUPPORTED
operator|.
name|value
argument_list|()
argument_list|,
name|result
operator|.
name|getResultMinor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertSuccess
parameter_list|(
name|ResultType
name|result
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_SUCCESS
operator|.
name|value
argument_list|()
argument_list|,
name|result
operator|.
name|getResultMajor
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|result
operator|.
name|getResultMinor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|XKMSService
name|createXKMSService
parameter_list|(
name|boolean
name|enableXKRSS
parameter_list|)
block|{
name|XKMSService
name|xkmsService
init|=
operator|new
name|XKMSService
argument_list|()
decl_stmt|;
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
name|keyRegisterHandlers
operator|.
name|add
argument_list|(
operator|new
name|Register
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|RevokeResultType
name|revoke
parameter_list|(
name|RevokeRequestType
name|request
parameter_list|,
name|RevokeResultType
name|response
parameter_list|)
block|{
return|return
name|response
return|;
block|}
annotation|@
name|Override
specifier|public
name|ReissueResultType
name|reissue
parameter_list|(
name|ReissueRequestType
name|request
parameter_list|,
name|ReissueResultType
name|response
parameter_list|)
block|{
return|return
name|response
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
parameter_list|,
name|RegisterResultType
name|response
parameter_list|)
block|{
return|return
name|response
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|canProcess
parameter_list|(
name|RequestAbstractType
name|request
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|RecoverResultType
name|recover
parameter_list|(
name|RecoverRequestType
name|request
parameter_list|,
name|RecoverResultType
name|response
parameter_list|)
block|{
return|return
name|response
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xkmsService
operator|.
name|setKeyRegisterHandlers
argument_list|(
name|keyRegisterHandlers
argument_list|)
expr_stmt|;
name|xkmsService
operator|.
name|setEnableXKRSS
argument_list|(
name|enableXKRSS
argument_list|)
expr_stmt|;
return|return
name|xkmsService
return|;
block|}
specifier|private
name|void
name|showResult
parameter_list|(
name|ResultType
name|result
parameter_list|)
block|{
name|String
name|message
init|=
literal|""
decl_stmt|;
if|if
condition|(
operator|!
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_SUCCESS
operator|.
name|value
argument_list|()
operator|.
name|equals
argument_list|(
name|result
operator|.
name|getResultMajor
argument_list|()
argument_list|)
condition|)
block|{
name|ResultDetails
name|details
init|=
operator|(
name|ResultDetails
operator|)
name|result
operator|.
name|getMessageExtension
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|message
operator|=
name|details
operator|.
name|getDetails
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Major: "
operator|+
name|result
operator|.
name|getResultMajor
argument_list|()
operator|+
literal|", Minor: "
operator|+
name|result
operator|.
name|getResultMinor
argument_list|()
operator|+
literal|", Message: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

