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
name|exception
package|;
end_package

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

begin_class
specifier|public
specifier|final
class|class
name|ExceptionMapper
block|{
specifier|private
name|ExceptionMapper
parameter_list|()
block|{     }
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|ResultType
parameter_list|>
name|T
name|toResponse
parameter_list|(
name|Exception
name|e
parameter_list|,
name|T
name|result
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|XKMSException
condition|)
block|{
name|XKMSException
name|xkmsEx
init|=
operator|(
name|XKMSException
operator|)
name|e
decl_stmt|;
name|result
operator|=
name|initResultType
argument_list|(
name|xkmsEx
operator|.
name|getMessage
argument_list|()
argument_list|,
name|xkmsEx
operator|.
name|getResultMajor
argument_list|()
argument_list|,
name|xkmsEx
operator|.
name|getResultMinor
argument_list|()
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|instanceof
name|UnsupportedOperationException
condition|)
block|{
name|result
operator|=
name|initResultType
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_SENDER
argument_list|,
name|ResultMinorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_MESSAGE_NOT_SUPPORTED
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|instanceof
name|IllegalArgumentException
condition|)
block|{
name|result
operator|=
name|initResultType
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_SENDER
argument_list|,
name|ResultMinorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_FAILURE
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|initResultType
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_RECEIVER
argument_list|,
name|ResultMinorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_FAILURE
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|static
name|XKMSException
name|fromResponse
parameter_list|(
name|ResultType
name|result
parameter_list|)
block|{
name|ResultMajorEnum
name|major
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|(
name|result
operator|.
name|getResultMajor
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
operator|!
name|result
operator|.
name|getResultMajor
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|major
operator|=
name|ResultMajorEnum
operator|.
name|fromValue
argument_list|(
name|result
operator|.
name|getResultMajor
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|major
operator|==
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_SUCCESS
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ResultMinorEnum
name|minor
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|(
name|result
operator|.
name|getResultMinor
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
operator|!
name|result
operator|.
name|getResultMinor
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|minor
operator|=
name|ResultMinorEnum
operator|.
name|fromValue
argument_list|(
name|result
operator|.
name|getResultMinor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|message
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|getMessageExtension
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|message
operator|=
operator|(
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
operator|)
operator|.
name|getDetails
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|XKMSException
argument_list|(
name|major
argument_list|,
name|minor
argument_list|,
name|message
argument_list|)
return|;
block|}
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|ResultType
parameter_list|>
name|T
name|initResultType
parameter_list|(
name|String
name|message
parameter_list|,
name|ResultMajorEnum
name|majorCode
parameter_list|,
name|ResultMinorEnum
name|minorCode
parameter_list|,
name|T
name|result
parameter_list|)
block|{
name|result
operator|.
name|setResultMajor
argument_list|(
operator|(
name|majorCode
operator|!=
literal|null
operator|)
condition|?
name|majorCode
operator|.
name|value
argument_list|()
else|:
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_RECEIVER
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setResultMinor
argument_list|(
operator|(
name|minorCode
operator|!=
literal|null
operator|)
condition|?
name|minorCode
operator|.
name|value
argument_list|()
else|:
name|ResultMinorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_FAILURE
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|ResultDetails
name|resultDetails
init|=
operator|new
name|ResultDetails
argument_list|()
decl_stmt|;
name|resultDetails
operator|.
name|setDetails
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|result
operator|.
name|getMessageExtension
argument_list|()
operator|.
name|add
argument_list|(
name|resultDetails
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

