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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|RequestSecurityTokenCollectionType
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
name|RequestSecurityTokenResponseCollectionType
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
name|IssueSingleOperation
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
name|RenewOperation
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
name|RequestCollectionOperation
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
name|ValidateOperation
import|;
end_import

begin_comment
comment|/**  * An implementation of the RequestCollectionOperation interface. It is composed of the different  * Operation implementations  */
end_comment

begin_class
specifier|public
class|class
name|TokenRequestCollectionOperation
extends|extends
name|AbstractOperation
implements|implements
name|RequestCollectionOperation
block|{
specifier|public
specifier|static
specifier|final
name|String
name|WSTRUST_REQUESTTYPE_BATCH_ISSUE
init|=
name|STSConstants
operator|.
name|WST_NS_05_12
operator|+
literal|"/BatchIssue"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSTRUST_REQUESTTYPE_BATCH_CANCEL
init|=
name|STSConstants
operator|.
name|WST_NS_05_12
operator|+
literal|"/BatchCancel"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSTRUST_REQUESTTYPE_BATCH_RENEW
init|=
name|STSConstants
operator|.
name|WST_NS_05_12
operator|+
literal|"/BatchRenew"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSTRUST_REQUESTTYPE_BATCH_VALIDATE
init|=
name|STSConstants
operator|.
name|WST_NS_05_12
operator|+
literal|"/BatchValidate"
decl_stmt|;
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|TokenRequestCollectionOperation
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|IssueSingleOperation
name|issueSingleOperation
decl_stmt|;
specifier|private
name|ValidateOperation
name|validateOperation
decl_stmt|;
specifier|private
name|RenewOperation
name|renewOperation
decl_stmt|;
specifier|private
name|CancelOperation
name|cancelOperation
decl_stmt|;
specifier|public
name|RequestSecurityTokenResponseCollectionType
name|requestCollection
parameter_list|(
name|RequestSecurityTokenCollectionType
name|requestCollection
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
name|RequestSecurityTokenResponseCollectionType
name|responseCollection
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestSecurityTokenResponseCollectionType
argument_list|()
decl_stmt|;
name|String
name|requestType
init|=
literal|null
decl_stmt|;
for|for
control|(
name|RequestSecurityTokenType
name|request
range|:
name|requestCollection
operator|.
name|getRequestSecurityToken
argument_list|()
control|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|objectList
init|=
name|request
operator|.
name|getAny
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|objectList
control|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|JAXBElement
condition|)
block|{
name|QName
name|qname
init|=
operator|(
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|o
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|qname
operator|.
name|equals
argument_list|(
operator|new
name|QName
argument_list|(
name|STSConstants
operator|.
name|WST_NS_05_12
argument_list|,
literal|"RequestType"
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|val
init|=
operator|(
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|o
operator|)
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// All batch requests must have the same RequestType
if|if
condition|(
name|val
operator|==
literal|null
operator|||
operator|(
name|requestType
operator|!=
literal|null
operator|&&
operator|!
name|requestType
operator|.
name|equals
argument_list|(
name|val
argument_list|)
operator|)
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
literal|"All RequestSecurityTokenCollection elements do not share the same "
operator|+
literal|"RequestType"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in requesting a token"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
name|requestType
operator|=
name|val
expr_stmt|;
block|}
block|}
block|}
name|RequestSecurityTokenResponseType
name|response
init|=
name|handleRequest
argument_list|(
name|request
argument_list|,
name|principal
argument_list|,
name|messageContext
argument_list|,
name|requestType
argument_list|)
decl_stmt|;
name|responseCollection
operator|.
name|getRequestSecurityTokenResponse
argument_list|()
operator|.
name|add
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
return|return
name|responseCollection
return|;
block|}
specifier|public
name|RequestSecurityTokenResponseType
name|handleRequest
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
parameter_list|,
name|String
name|requestType
parameter_list|)
block|{
if|if
condition|(
name|WSTRUST_REQUESTTYPE_BATCH_ISSUE
operator|.
name|equals
argument_list|(
name|requestType
argument_list|)
condition|)
block|{
if|if
condition|(
name|issueSingleOperation
operator|==
literal|null
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
literal|"IssueSingleOperation is null"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in requesting a token"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
return|return
name|issueSingleOperation
operator|.
name|issueSingle
argument_list|(
name|request
argument_list|,
name|principal
argument_list|,
name|messageContext
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|WSTRUST_REQUESTTYPE_BATCH_VALIDATE
operator|.
name|equals
argument_list|(
name|requestType
argument_list|)
condition|)
block|{
if|if
condition|(
name|validateOperation
operator|==
literal|null
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
literal|"ValidateOperation is null"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in requesting a token"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
return|return
name|validateOperation
operator|.
name|validate
argument_list|(
name|request
argument_list|,
name|principal
argument_list|,
name|messageContext
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|WSTRUST_REQUESTTYPE_BATCH_CANCEL
operator|.
name|equals
argument_list|(
name|requestType
argument_list|)
condition|)
block|{
if|if
condition|(
name|cancelOperation
operator|==
literal|null
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
literal|"CancelOperation is null"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in requesting a token"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
return|return
name|cancelOperation
operator|.
name|cancel
argument_list|(
name|request
argument_list|,
name|principal
argument_list|,
name|messageContext
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|WSTRUST_REQUESTTYPE_BATCH_RENEW
operator|.
name|equals
argument_list|(
name|requestType
argument_list|)
condition|)
block|{
if|if
condition|(
name|renewOperation
operator|==
literal|null
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
literal|"RenewOperation is null"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in requesting a token"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
return|return
name|renewOperation
operator|.
name|renew
argument_list|(
name|request
argument_list|,
name|principal
argument_list|,
name|messageContext
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Unknown operation requested"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Error in requesting a token"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
block|}
specifier|public
name|IssueSingleOperation
name|getIssueSingleOperation
parameter_list|()
block|{
return|return
name|issueSingleOperation
return|;
block|}
specifier|public
name|void
name|setIssueSingleOperation
parameter_list|(
name|IssueSingleOperation
name|issueSingleOperation
parameter_list|)
block|{
name|this
operator|.
name|issueSingleOperation
operator|=
name|issueSingleOperation
expr_stmt|;
block|}
specifier|public
name|ValidateOperation
name|getValidateOperation
parameter_list|()
block|{
return|return
name|validateOperation
return|;
block|}
specifier|public
name|void
name|setValidateOperation
parameter_list|(
name|ValidateOperation
name|validateOperation
parameter_list|)
block|{
name|this
operator|.
name|validateOperation
operator|=
name|validateOperation
expr_stmt|;
block|}
specifier|public
name|RenewOperation
name|getRenewOperation
parameter_list|()
block|{
return|return
name|renewOperation
return|;
block|}
specifier|public
name|void
name|setRenewOperation
parameter_list|(
name|RenewOperation
name|renewOperation
parameter_list|)
block|{
name|this
operator|.
name|renewOperation
operator|=
name|renewOperation
expr_stmt|;
block|}
specifier|public
name|CancelOperation
name|getCancelOperation
parameter_list|()
block|{
return|return
name|cancelOperation
return|;
block|}
specifier|public
name|void
name|setCancelOperation
parameter_list|(
name|CancelOperation
name|cancelOperation
parameter_list|)
block|{
name|this
operator|.
name|cancelOperation
operator|=
name|cancelOperation
expr_stmt|;
block|}
block|}
end_class

end_unit

