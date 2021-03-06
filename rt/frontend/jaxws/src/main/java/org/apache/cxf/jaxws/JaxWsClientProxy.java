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
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|concurrent
operator|.
name|Future
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
name|soap
operator|.
name|SOAPConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPFault
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
name|AsyncHandler
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
name|Binding
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
name|BindingProvider
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
name|EndpointReference
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
name|Response
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
name|WebServiceException
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
operator|.
name|Scope
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
name|http
operator|.
name|HTTPBinding
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
name|http
operator|.
name|HTTPException
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
name|soap
operator|.
name|SOAPBinding
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
name|soap
operator|.
name|SOAPFaultException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|saaj
operator|.
name|SAAJFactoryResolver
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
name|saaj
operator|.
name|SAAJUtils
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
name|i18n
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
name|endpoint
operator|.
name|Client
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
name|endpoint
operator|.
name|ClientCallback
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
name|endpoint
operator|.
name|Endpoint
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
name|helpers
operator|.
name|CastUtils
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
name|interceptor
operator|.
name|Fault
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
name|jaxws
operator|.
name|context
operator|.
name|WrappedMessageContext
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
name|jaxws
operator|.
name|support
operator|.
name|JaxWsEndpointImpl
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
name|service
operator|.
name|invoker
operator|.
name|MethodDispatcher
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
name|service
operator|.
name|model
operator|.
name|BindingFaultInfo
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
import|;
end_import

begin_class
specifier|public
class|class
name|JaxWsClientProxy
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientProxy
implements|implements
name|BindingProvider
block|{
specifier|public
specifier|static
specifier|final
name|String
name|THREAD_LOCAL_REQUEST_CONTEXT
init|=
literal|"thread.local.request.context"
decl_stmt|;
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
name|JaxWsClientProxy
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Binding
name|binding
decl_stmt|;
specifier|private
name|EndpointReferenceBuilder
name|builder
decl_stmt|;
specifier|public
name|JaxWsClientProxy
parameter_list|(
name|Client
name|c
parameter_list|,
name|Binding
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|b
expr_stmt|;
name|setupEndpointAddressContext
argument_list|(
name|getClient
argument_list|()
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|builder
operator|=
operator|new
name|EndpointReferenceBuilder
argument_list|(
operator|(
name|JaxWsEndpointImpl
operator|)
name|getClient
argument_list|()
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|binding
operator|=
literal|null
expr_stmt|;
name|builder
operator|=
literal|null
expr_stmt|;
block|}
specifier|private
name|void
name|setupEndpointAddressContext
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
comment|// NOTE for jms transport the address would be null
if|if
condition|(
literal|null
operator|!=
name|endpoint
operator|&&
literal|null
operator|!=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
condition|)
block|{
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The client has been closed."
argument_list|)
throw|;
block|}
name|Endpoint
name|endpoint
init|=
name|getClient
argument_list|()
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|address
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|MethodDispatcher
name|dispatcher
init|=
operator|(
name|MethodDispatcher
operator|)
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|get
argument_list|(
name|MethodDispatcher
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Object
index|[]
name|params
init|=
name|args
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|params
condition|)
block|{
name|params
operator|=
operator|new
name|Object
index|[
literal|0
index|]
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|equals
argument_list|(
name|BindingProvider
operator|.
name|class
argument_list|)
operator|||
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|equals
argument_list|(
name|Object
operator|.
name|class
argument_list|)
operator|||
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|equals
argument_list|(
name|Closeable
operator|.
name|class
argument_list|)
operator|||
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|equals
argument_list|(
name|AutoCloseable
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|this
argument_list|,
name|params
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|isInstance
argument_list|(
name|client
argument_list|)
condition|)
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|client
argument_list|,
name|params
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
name|BindingOperationInfo
name|oi
init|=
name|dispatcher
operator|.
name|getBindingOperation
argument_list|(
name|method
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|oi
operator|==
literal|null
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"NO_BINDING_OPERATION_INFO"
argument_list|,
name|LOG
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Method
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|boolean
name|isAsync
init|=
name|isAsync
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|isAsync
condition|)
block|{
name|result
operator|=
name|invokeAsync
argument_list|(
name|method
argument_list|,
name|oi
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|invokeSync
argument_list|(
name|method
argument_list|,
name|oi
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|wex
parameter_list|)
block|{
throw|throw
name|wex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|mapException
argument_list|(
name|method
argument_list|,
name|oi
argument_list|,
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|addressChanged
argument_list|(
name|address
argument_list|)
condition|)
block|{
name|setupEndpointAddressContext
argument_list|(
name|getClient
argument_list|()
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|respContext
init|=
name|client
operator|.
name|getResponseContext
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Scope
argument_list|>
name|scopes
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|respContext
operator|.
name|get
argument_list|(
name|WrappedMessageContext
operator|.
name|SCOPES
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|scopes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Scope
argument_list|>
name|scope
range|:
name|scopes
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|scope
operator|.
name|getValue
argument_list|()
operator|==
name|Scope
operator|.
name|HANDLER
condition|)
block|{
name|respContext
operator|.
name|remove
argument_list|(
name|scope
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|adjustObject
argument_list|(
name|result
argument_list|)
return|;
block|}
name|Exception
name|mapException
parameter_list|(
name|Method
name|method
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|excls
range|:
name|method
operator|.
name|getExceptionTypes
argument_list|()
control|)
block|{
if|if
condition|(
name|excls
operator|.
name|isInstance
argument_list|(
name|ex
argument_list|)
condition|)
block|{
return|return
name|ex
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|boi
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BindingFaultInfo
name|fi
range|:
name|boi
operator|.
name|getFaults
argument_list|()
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
name|fi
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
operator|&&
name|c
operator|.
name|isInstance
argument_list|(
name|ex
argument_list|)
condition|)
block|{
return|return
name|ex
return|;
block|}
block|}
if|if
condition|(
name|ex
operator|instanceof
name|IOException
condition|)
block|{
return|return
name|ex
return|;
block|}
block|}
if|if
condition|(
name|ex
operator|instanceof
name|Fault
operator|&&
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IOException
condition|)
block|{
return|return
operator|new
name|WebServiceException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|getBinding
argument_list|()
operator|instanceof
name|HTTPBinding
condition|)
block|{
name|HTTPException
name|exception
init|=
operator|new
name|HTTPException
argument_list|(
name|HttpURLConnection
operator|.
name|HTTP_INTERNAL_ERROR
argument_list|)
decl_stmt|;
name|exception
operator|.
name|initCause
argument_list|(
name|ex
argument_list|)
expr_stmt|;
return|return
name|exception
return|;
block|}
elseif|else
if|if
condition|(
name|getBinding
argument_list|()
operator|instanceof
name|SOAPBinding
condition|)
block|{
try|try
block|{
name|SOAPFault
name|soapFault
init|=
name|createSoapFault
argument_list|(
operator|(
name|SOAPBinding
operator|)
name|getBinding
argument_list|()
argument_list|,
name|ex
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapFault
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|SOAPFaultException
name|exception
init|=
operator|new
name|SOAPFaultException
argument_list|(
name|soapFault
argument_list|)
decl_stmt|;
if|if
condition|(
name|ex
operator|instanceof
name|Fault
operator|&&
name|ex
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exception
operator|.
name|initCause
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exception
operator|.
name|initCause
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
name|exception
return|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
return|return
operator|new
name|WebServiceException
argument_list|(
name|ex
argument_list|)
return|;
block|}
block|}
return|return
operator|new
name|WebServiceException
argument_list|(
name|ex
argument_list|)
return|;
block|}
name|boolean
name|isAsync
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
return|return
name|m
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"Async"
argument_list|)
operator|&&
operator|(
name|Future
operator|.
name|class
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|)
operator|||
name|Response
operator|.
name|class
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|)
operator|)
return|;
block|}
specifier|static
name|SOAPFault
name|createSoapFault
parameter_list|(
name|SOAPBinding
name|binding
parameter_list|,
name|Exception
name|ex
parameter_list|)
throws|throws
name|SOAPException
block|{
name|SOAPFault
name|soapFault
decl_stmt|;
try|try
block|{
name|soapFault
operator|=
name|binding
operator|.
name|getSOAPFactory
argument_list|()
operator|.
name|createFault
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//probably an old version of saaj or something that is not allowing createFault
comment|//method to work.  Try the saaj 1.2 method of doing this.
try|try
block|{
name|soapFault
operator|=
name|binding
operator|.
name|getMessageFactory
argument_list|()
operator|.
name|createMessage
argument_list|()
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|addFault
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t2
parameter_list|)
block|{
comment|//still didn't work, we'll just throw what we have
return|return
literal|null
return|;
block|}
block|}
if|if
condition|(
name|ex
operator|instanceof
name|SoapFault
condition|)
block|{
if|if
condition|(
operator|!
name|soapFault
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|getFaultCode
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
name|SOAPConstants
operator|.
name|URI_NS_SOAP_1_1_ENVELOPE
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|getFaultCode
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
comment|//change to 1.1
try|try
block|{
name|soapFault
operator|=
name|SAAJFactoryResolver
operator|.
name|createSOAPFactory
argument_list|(
literal|null
argument_list|)
operator|.
name|createFault
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
block|}
specifier|final
name|boolean
name|isSoap11
init|=
name|SOAPConstants
operator|.
name|URI_NS_SOAP_1_1_ENVELOPE
operator|.
name|equals
argument_list|(
name|soapFault
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|getLang
argument_list|()
argument_list|)
condition|)
block|{
name|soapFault
operator|.
name|setFaultString
argument_list|(
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|soapFault
operator|.
name|setFaultString
argument_list|(
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|getReason
argument_list|()
argument_list|,
name|stringToLocale
argument_list|(
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|getLang
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|SAAJUtils
operator|.
name|setFaultCode
argument_list|(
name|soapFault
argument_list|,
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|role
init|=
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|getRole
argument_list|()
decl_stmt|;
if|if
condition|(
name|role
operator|!=
literal|null
condition|)
block|{
name|soapFault
operator|.
name|setFaultActor
argument_list|(
name|role
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|getSubCodes
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|isSoap11
condition|)
block|{
comment|// set the subcode only if it is supported (e.g, 1.2)
for|for
control|(
name|QName
name|fsc
range|:
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|getSubCodes
argument_list|()
control|)
block|{
name|soapFault
operator|.
name|appendFaultSubcode
argument_list|(
name|fsc
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|hasDetails
argument_list|()
condition|)
block|{
name|Node
name|nd
init|=
name|soapFault
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|importNode
argument_list|(
operator|(
operator|(
name|SoapFault
operator|)
name|ex
operator|)
operator|.
name|getDetail
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|nd
operator|=
name|nd
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
name|soapFault
operator|.
name|addDetail
argument_list|()
expr_stmt|;
while|while
condition|(
name|nd
operator|!=
literal|null
condition|)
block|{
name|Node
name|next
init|=
name|nd
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
name|soapFault
operator|.
name|getDetail
argument_list|()
operator|.
name|appendChild
argument_list|(
name|nd
argument_list|)
expr_stmt|;
name|nd
operator|=
name|next
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|String
name|msg
init|=
name|ex
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
name|soapFault
operator|.
name|setFaultString
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|soapFault
return|;
block|}
specifier|private
specifier|static
name|Locale
name|stringToLocale
parameter_list|(
name|String
name|locale
parameter_list|)
block|{
comment|// use the IETF BCP 47 delimiter but accept the toString delimiter for cxf 2.7.x
name|String
index|[]
name|parts
init|=
name|locale
operator|.
name|split
argument_list|(
literal|"-"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
operator|new
name|Locale
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|parts
operator|.
name|length
operator|==
literal|2
condition|)
block|{
return|return
operator|new
name|Locale
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|,
name|parts
index|[
literal|1
index|]
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|Locale
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|,
name|parts
index|[
literal|1
index|]
argument_list|,
name|parts
index|[
literal|2
index|]
argument_list|)
return|;
block|}
block|}
specifier|private
name|boolean
name|addressChanged
parameter_list|(
name|String
name|address
parameter_list|)
block|{
return|return
operator|!
operator|(
name|address
operator|==
literal|null
operator|||
name|getClient
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|==
literal|null
operator|||
name|address
operator|.
name|equals
argument_list|(
name|getClient
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
operator|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Object
name|invokeAsync
parameter_list|(
name|Method
name|method
parameter_list|,
specifier|final
name|BindingOperationInfo
name|oi
parameter_list|,
name|Object
index|[]
name|params
parameter_list|)
throws|throws
name|Exception
block|{
name|client
operator|.
name|setExecutor
argument_list|(
name|getClient
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|AsyncHandler
argument_list|<
name|Object
argument_list|>
name|handler
decl_stmt|;
if|if
condition|(
name|params
operator|.
name|length
operator|>
literal|0
operator|&&
name|params
index|[
name|params
operator|.
name|length
operator|-
literal|1
index|]
operator|instanceof
name|AsyncHandler
condition|)
block|{
name|handler
operator|=
operator|(
name|AsyncHandler
argument_list|<
name|Object
argument_list|>
operator|)
name|params
index|[
name|params
operator|.
name|length
operator|-
literal|1
index|]
expr_stmt|;
name|Object
index|[]
name|newParams
init|=
operator|new
name|Object
index|[
name|params
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|params
argument_list|,
literal|0
argument_list|,
name|newParams
argument_list|,
literal|0
argument_list|,
name|newParams
operator|.
name|length
argument_list|)
expr_stmt|;
name|params
operator|=
name|newParams
expr_stmt|;
block|}
else|else
block|{
name|handler
operator|=
literal|null
expr_stmt|;
block|}
name|ClientCallback
name|callback
init|=
operator|new
name|JaxwsClientCallback
argument_list|<
name|Object
argument_list|>
argument_list|(
name|handler
argument_list|,
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Throwable
name|mapThrowable
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|instanceof
name|Exception
condition|)
block|{
name|t
operator|=
name|mapException
argument_list|(
literal|null
argument_list|,
name|oi
argument_list|,
operator|(
name|Exception
operator|)
name|t
argument_list|)
expr_stmt|;
block|}
return|return
name|t
return|;
block|}
block|}
decl_stmt|;
name|Response
argument_list|<
name|Object
argument_list|>
name|ret
init|=
operator|new
name|JaxwsResponseCallback
argument_list|<>
argument_list|(
name|callback
argument_list|)
decl_stmt|;
name|client
operator|.
name|invoke
argument_list|(
name|callback
argument_list|,
name|oi
argument_list|,
name|params
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getRequestContext
parameter_list|()
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The client has been closed."
argument_list|)
throw|;
block|}
return|return
operator|new
name|WrappedMessageContext
argument_list|(
name|this
operator|.
name|getClient
argument_list|()
operator|.
name|getRequestContext
argument_list|()
argument_list|,
literal|null
argument_list|,
name|Scope
operator|.
name|APPLICATION
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getResponseContext
parameter_list|()
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The client has been closed."
argument_list|)
throw|;
block|}
return|return
operator|new
name|WrappedMessageContext
argument_list|(
name|this
operator|.
name|getClient
argument_list|()
operator|.
name|getResponseContext
argument_list|()
argument_list|,
literal|null
argument_list|,
name|Scope
operator|.
name|APPLICATION
argument_list|)
return|;
block|}
specifier|public
name|Binding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The client has been closed."
argument_list|)
throw|;
block|}
return|return
name|binding
return|;
block|}
specifier|public
name|EndpointReference
name|getEndpointReference
parameter_list|()
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The client has been closed."
argument_list|)
throw|;
block|}
return|return
name|builder
operator|.
name|getEndpointReference
argument_list|()
return|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|EndpointReference
parameter_list|>
name|T
name|getEndpointReference
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The client has been closed."
argument_list|)
throw|;
block|}
return|return
name|builder
operator|.
name|getEndpointReference
argument_list|(
name|clazz
argument_list|)
return|;
block|}
block|}
end_class

end_unit

