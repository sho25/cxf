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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|JAXBContext
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
name|bind
operator|.
name|Unmarshaller
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
name|util
operator|.
name|JAXBSource
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|ServiceMode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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
name|binding
operator|.
name|soap
operator|.
name|SoapFault
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
name|binding
operator|.
name|soap
operator|.
name|SoapVersion
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
name|jaxb
operator|.
name|JAXBContextCache
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
name|jaxb
operator|.
name|JAXBContextCache
operator|.
name|CachedContextAndSchemas
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
name|ObjectFactory
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
name|IssueOperation
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
name|KeyExchangeTokenOperation
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

begin_class
annotation|@
name|ServiceMode
argument_list|(
name|value
operator|=
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
specifier|public
class|class
name|SecurityTokenServiceProvider
implements|implements
name|Provider
argument_list|<
name|Source
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|String
name|WSTRUST_13_NAMESPACE
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSTRUST_REQUESTTYPE_ELEMENTNAME
init|=
literal|"RequestType"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSTRUST_REQUESTTYPE_ISSUE
init|=
name|WSTRUST_13_NAMESPACE
operator|+
literal|"/Issue"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSTRUST_REQUESTTYPE_CANCEL
init|=
name|WSTRUST_13_NAMESPACE
operator|+
literal|"/Cancel"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSTRUST_REQUESTTYPE_RENEW
init|=
name|WSTRUST_13_NAMESPACE
operator|+
literal|"/Renew"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSTRUST_REQUESTTYPE_VALIDATE
init|=
name|WSTRUST_13_NAMESPACE
operator|+
literal|"/Validate"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSTRUST_REQUESTTYPE_REQUESTCOLLECTION
init|=
name|WSTRUST_13_NAMESPACE
operator|+
literal|"/RequestCollection"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSTRUST_REQUESTTYPE_KEYEXCHANGETOKEN
init|=
name|WSTRUST_13_NAMESPACE
operator|+
literal|"/KeyExchangeToken"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
name|OPERATION_METHODS
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
try|try
block|{
name|Method
name|m
init|=
name|IssueOperation
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"issue"
argument_list|,
name|RequestSecurityTokenType
operator|.
name|class
argument_list|,
name|WebServiceContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|OPERATION_METHODS
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_ISSUE
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|m
operator|=
name|CancelOperation
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"cancel"
argument_list|,
name|RequestSecurityTokenType
operator|.
name|class
argument_list|,
name|WebServiceContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|OPERATION_METHODS
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_CANCEL
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|m
operator|=
name|RenewOperation
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"renew"
argument_list|,
name|RequestSecurityTokenType
operator|.
name|class
argument_list|,
name|WebServiceContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|OPERATION_METHODS
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_RENEW
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|m
operator|=
name|ValidateOperation
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"validate"
argument_list|,
name|RequestSecurityTokenType
operator|.
name|class
argument_list|,
name|WebServiceContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|OPERATION_METHODS
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_VALIDATE
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|m
operator|=
name|KeyExchangeTokenOperation
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"keyExchangeToken"
argument_list|,
name|RequestSecurityTokenType
operator|.
name|class
argument_list|,
name|WebServiceContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|OPERATION_METHODS
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_KEYEXCHANGETOKEN
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|m
operator|=
name|RequestCollectionOperation
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"requestCollection"
argument_list|,
name|RequestSecurityTokenCollectionType
operator|.
name|class
argument_list|,
name|WebServiceContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|OPERATION_METHODS
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_REQUESTCOLLECTION
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|protected
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|jaxbContextClasses
decl_stmt|;
specifier|private
name|CancelOperation
name|cancelOperation
decl_stmt|;
specifier|private
name|IssueOperation
name|issueOperation
decl_stmt|;
specifier|private
name|KeyExchangeTokenOperation
name|keyExchangeTokenOperation
decl_stmt|;
specifier|private
name|RenewOperation
name|renewOperation
decl_stmt|;
specifier|private
name|RequestCollectionOperation
name|requestCollectionOperation
decl_stmt|;
specifier|private
name|ValidateOperation
name|validateOperation
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|operationMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Resource
specifier|private
name|WebServiceContext
name|context
decl_stmt|;
specifier|public
name|SecurityTokenServiceProvider
parameter_list|()
throws|throws
name|Exception
block|{
name|CachedContextAndSchemas
name|cache
init|=
name|JAXBContextCache
operator|.
name|getCachedContextAndSchemas
argument_list|(
name|ObjectFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|jaxbContext
operator|=
name|cache
operator|.
name|getContext
argument_list|()
expr_stmt|;
name|jaxbContextClasses
operator|=
name|cache
operator|.
name|getClasses
argument_list|()
expr_stmt|;
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
name|operationMap
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_CANCEL
argument_list|,
name|cancelOperation
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setIssueOperation
parameter_list|(
name|IssueOperation
name|issueOperation
parameter_list|)
block|{
name|this
operator|.
name|issueOperation
operator|=
name|issueOperation
expr_stmt|;
name|operationMap
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_ISSUE
argument_list|,
name|issueOperation
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setKeyExchangeTokenOperation
parameter_list|(
name|KeyExchangeTokenOperation
name|keyExchangeTokenOperation
parameter_list|)
block|{
name|this
operator|.
name|keyExchangeTokenOperation
operator|=
name|keyExchangeTokenOperation
expr_stmt|;
name|operationMap
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_KEYEXCHANGETOKEN
argument_list|,
name|keyExchangeTokenOperation
argument_list|)
expr_stmt|;
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
name|operationMap
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_RENEW
argument_list|,
name|renewOperation
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setRequestCollectionOperation
parameter_list|(
name|RequestCollectionOperation
name|requestCollectionOperation
parameter_list|)
block|{
name|this
operator|.
name|requestCollectionOperation
operator|=
name|requestCollectionOperation
expr_stmt|;
name|operationMap
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_REQUESTCOLLECTION
argument_list|,
name|requestCollectionOperation
argument_list|)
expr_stmt|;
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
name|operationMap
operator|.
name|put
argument_list|(
name|WSTRUST_REQUESTTYPE_VALIDATE
argument_list|,
name|validateOperation
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Source
name|invoke
parameter_list|(
name|Source
name|request
parameter_list|)
block|{
name|Source
name|response
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Object
name|obj
init|=
name|convertToJAXBObject
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|Object
name|operationImpl
init|=
literal|null
decl_stmt|;
name|Method
name|method
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|RequestSecurityTokenCollectionType
condition|)
block|{
name|operationImpl
operator|=
name|operationMap
operator|.
name|get
argument_list|(
name|WSTRUST_REQUESTTYPE_REQUESTCOLLECTION
argument_list|)
expr_stmt|;
name|method
operator|=
name|OPERATION_METHODS
operator|.
name|get
argument_list|(
name|WSTRUST_REQUESTTYPE_REQUESTCOLLECTION
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|RequestSecurityTokenType
name|rst
init|=
operator|(
name|RequestSecurityTokenType
operator|)
name|obj
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objectList
init|=
name|rst
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
name|WSTRUST_13_NAMESPACE
argument_list|,
name|WSTRUST_REQUESTTYPE_ELEMENTNAME
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
name|operationImpl
operator|=
name|operationMap
operator|.
name|get
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|method
operator|=
name|OPERATION_METHODS
operator|.
name|get
argument_list|(
name|val
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
if|if
condition|(
name|operationImpl
operator|==
literal|null
operator|||
name|method
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Implementation for this operation not found."
argument_list|)
throw|;
block|}
name|obj
operator|=
name|method
operator|.
name|invoke
argument_list|(
name|operationImpl
argument_list|,
name|obj
argument_list|,
name|context
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Error in implementation class."
argument_list|)
throw|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|RequestSecurityTokenResponseCollectionType
condition|)
block|{
name|RequestSecurityTokenResponseCollectionType
name|tokenResponse
init|=
operator|(
name|RequestSecurityTokenResponseCollectionType
operator|)
name|obj
decl_stmt|;
name|response
operator|=
operator|new
name|JAXBSource
argument_list|(
name|jaxbContext
argument_list|,
operator|new
name|ObjectFactory
argument_list|()
operator|.
name|createRequestSecurityTokenResponseCollection
argument_list|(
name|tokenResponse
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|RequestSecurityTokenResponseType
name|tokenResponse
init|=
operator|(
name|RequestSecurityTokenResponseType
operator|)
name|obj
decl_stmt|;
name|response
operator|=
operator|new
name|JAXBSource
argument_list|(
name|jaxbContext
argument_list|,
operator|new
name|ObjectFactory
argument_list|()
operator|.
name|createRequestSecurityTokenResponse
argument_list|(
name|tokenResponse
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ex
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|ex
operator|.
name|getCause
argument_list|()
decl_stmt|;
throw|throw
name|createSOAPFault
argument_list|(
name|cause
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|createSOAPFault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|response
return|;
block|}
specifier|private
name|SoapFault
name|createSOAPFault
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|String
name|faultString
init|=
literal|"Internal STS error"
decl_stmt|;
name|QName
name|faultCode
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ex
operator|instanceof
name|STSException
operator|&&
operator|(
operator|(
name|STSException
operator|)
name|ex
operator|)
operator|.
name|getFaultCode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|faultCode
operator|=
operator|(
operator|(
name|STSException
operator|)
name|ex
operator|)
operator|.
name|getFaultCode
argument_list|()
expr_stmt|;
block|}
name|faultString
operator|=
name|ex
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
name|MessageContext
name|messageContext
init|=
name|context
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
name|SoapVersion
name|soapVersion
init|=
operator|(
name|SoapVersion
operator|)
name|messageContext
operator|.
name|get
argument_list|(
name|SoapVersion
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|SoapFault
name|fault
decl_stmt|;
if|if
condition|(
name|soapVersion
operator|.
name|getVersion
argument_list|()
operator|==
literal|1.1
operator|&&
name|faultCode
operator|!=
literal|null
condition|)
block|{
name|fault
operator|=
operator|new
name|SoapFault
argument_list|(
name|faultString
argument_list|,
name|faultCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fault
operator|=
operator|new
name|SoapFault
argument_list|(
name|faultString
argument_list|,
name|soapVersion
operator|.
name|getSender
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|soapVersion
operator|.
name|getVersion
argument_list|()
operator|!=
literal|1.1
operator|&&
name|faultCode
operator|!=
literal|null
condition|)
block|{
name|fault
operator|.
name|setSubCode
argument_list|(
name|faultCode
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fault
return|;
block|}
specifier|private
name|Object
name|convertToJAXBObject
parameter_list|(
name|Source
name|source
parameter_list|)
throws|throws
name|Exception
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|jaxbContext
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|jaxbElement
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|source
argument_list|)
decl_stmt|;
return|return
name|jaxbElement
operator|.
name|getValue
argument_list|()
return|;
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
name|IssueOperation
name|getIssueOperation
parameter_list|()
block|{
return|return
name|issueOperation
return|;
block|}
specifier|public
name|KeyExchangeTokenOperation
name|getKeyExchangeTokenOperation
parameter_list|()
block|{
return|return
name|keyExchangeTokenOperation
return|;
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
name|RequestCollectionOperation
name|getRequestCollectionOperation
parameter_list|()
block|{
return|return
name|requestCollectionOperation
return|;
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
block|}
end_class

end_unit

