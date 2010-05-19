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
name|IOException
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
name|activation
operator|.
name|DataSource
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
name|soap
operator|.
name|SOAPMessage
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
name|Dispatch
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
name|SAAJInInterceptor
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
name|SAAJOutInterceptor
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
name|interceptor
operator|.
name|AttachmentOutInterceptor
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
name|interceptors
operator|.
name|MessageModeInInterceptor
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
name|interceptors
operator|.
name|MessageModeOutInterceptor
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
name|model
operator|.
name|BindingInfo
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
name|MessageInfo
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
name|MessageInfo
operator|.
name|Type
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
name|MessagePartInfo
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
name|OperationInfo
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
name|ServiceInfo
import|;
end_import

begin_class
specifier|public
class|class
name|DispatchImpl
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Dispatch
argument_list|<
name|T
argument_list|>
implements|,
name|BindingProvider
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
name|DispatchImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DISPATCH_NS
init|=
literal|"http://cxf.apache.org/jaxws/dispatch"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INVOKE_NAME
init|=
literal|"Invoke"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INVOKE_ONEWAY_NAME
init|=
literal|"InvokeOneWay"
decl_stmt|;
specifier|private
specifier|final
name|Binding
name|binding
decl_stmt|;
specifier|private
specifier|final
name|EndpointReferenceBuilder
name|builder
decl_stmt|;
specifier|private
specifier|final
name|Client
name|client
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|cl
decl_stmt|;
specifier|private
specifier|final
name|JAXBContext
name|context
decl_stmt|;
specifier|private
name|Message
name|error
decl_stmt|;
name|DispatchImpl
parameter_list|(
name|Client
name|client
parameter_list|,
name|Service
operator|.
name|Mode
name|m
parameter_list|,
name|JAXBContext
name|ctx
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
operator|(
operator|(
name|JaxWsEndpointImpl
operator|)
name|client
operator|.
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getJaxwsBinding
argument_list|()
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
name|client
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
name|context
operator|=
name|ctx
expr_stmt|;
name|cl
operator|=
name|clazz
expr_stmt|;
name|setupEndpointAddressContext
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|addInvokeOperation
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|addInvokeOperation
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|==
name|Service
operator|.
name|Mode
operator|.
name|MESSAGE
operator|&&
name|binding
operator|instanceof
name|SOAPBinding
condition|)
block|{
if|if
condition|(
name|DataSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|error
operator|=
operator|new
name|Message
argument_list|(
literal|"DISPATCH_OBJECT_NOT_SUPPORTED"
argument_list|,
name|LOG
argument_list|,
literal|"DataSource"
argument_list|,
name|m
argument_list|,
literal|"SOAP/HTTP"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|m
operator|==
name|Service
operator|.
name|Mode
operator|.
name|MESSAGE
condition|)
block|{
name|SAAJOutInterceptor
name|saajOut
init|=
operator|new
name|SAAJOutInterceptor
argument_list|()
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|saajOut
argument_list|)
expr_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MessageModeOutInterceptor
argument_list|(
name|saajOut
argument_list|,
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SAAJInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MessageModeInInterceptor
argument_list|(
name|clazz
argument_list|,
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|m
operator|==
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
operator|&&
name|binding
operator|instanceof
name|SOAPBinding
operator|&&
name|SOAPMessage
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|error
operator|=
operator|new
name|Message
argument_list|(
literal|"DISPATCH_OBJECT_NOT_SUPPORTED"
argument_list|,
name|LOG
argument_list|,
literal|"SOAPMessage"
argument_list|,
name|m
argument_list|,
literal|"SOAP/HTTP"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|DataSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
operator|&&
name|binding
operator|instanceof
name|HTTPBinding
condition|)
block|{
name|error
operator|=
operator|new
name|Message
argument_list|(
literal|"DISPATCH_OBJECT_NOT_SUPPORTED"
argument_list|,
name|LOG
argument_list|,
literal|"DataSource"
argument_list|,
name|m
argument_list|,
literal|"XML/HTTP"
argument_list|)
expr_stmt|;
block|}
block|}
name|DispatchImpl
parameter_list|(
name|Client
name|cl
parameter_list|,
name|Service
operator|.
name|Mode
name|m
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
name|this
argument_list|(
name|cl
argument_list|,
name|m
argument_list|,
literal|null
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addInvokeOperation
parameter_list|(
name|boolean
name|oneWay
parameter_list|)
block|{
name|String
name|name
init|=
name|oneWay
condition|?
name|INVOKE_ONEWAY_NAME
else|:
name|INVOKE_NAME
decl_stmt|;
name|ServiceInfo
name|info
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|OperationInfo
name|opInfo
init|=
name|info
operator|.
name|getInterface
argument_list|()
operator|.
name|addOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|DISPATCH_NS
argument_list|,
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|MessageInfo
name|mInfo
init|=
name|opInfo
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
name|DISPATCH_NS
argument_list|,
name|name
operator|+
literal|"Request"
argument_list|)
argument_list|,
name|Type
operator|.
name|INPUT
argument_list|)
decl_stmt|;
name|opInfo
operator|.
name|setInput
argument_list|(
name|name
operator|+
literal|"Request"
argument_list|,
name|mInfo
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|mpi
init|=
name|mInfo
operator|.
name|addMessagePart
argument_list|(
literal|"parameters"
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|mpi
operator|.
name|setTypeClass
argument_list|(
name|cl
argument_list|)
expr_stmt|;
block|}
name|mpi
operator|.
name|setElement
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|oneWay
condition|)
block|{
name|mInfo
operator|=
name|opInfo
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
name|DISPATCH_NS
argument_list|,
name|name
operator|+
literal|"Response"
argument_list|)
argument_list|,
name|Type
operator|.
name|OUTPUT
argument_list|)
expr_stmt|;
name|opInfo
operator|.
name|setOutput
argument_list|(
name|name
operator|+
literal|"Response"
argument_list|,
name|mInfo
argument_list|)
expr_stmt|;
name|mpi
operator|=
name|mInfo
operator|.
name|addMessagePart
argument_list|(
literal|"parameters"
argument_list|)
expr_stmt|;
name|mpi
operator|.
name|setElement
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|mpi
operator|.
name|setTypeClass
argument_list|(
name|cl
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|BindingInfo
name|bind
range|:
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getBindings
argument_list|()
control|)
block|{
name|BindingOperationInfo
name|bo
init|=
operator|new
name|BindingOperationInfo
argument_list|(
name|bind
argument_list|,
name|opInfo
argument_list|)
decl_stmt|;
name|bind
operator|.
name|addOperation
argument_list|(
name|bo
argument_list|)
expr_stmt|;
block|}
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
return|return
operator|new
name|WrappedMessageContext
argument_list|(
name|client
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
return|return
operator|new
name|WrappedMessageContext
argument_list|(
name|client
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
return|return
name|binding
return|;
block|}
specifier|public
name|EndpointReference
name|getEndpointReference
parameter_list|()
block|{
return|return
name|builder
operator|.
name|getEndpointReference
argument_list|()
return|;
block|}
specifier|public
parameter_list|<
name|X
extends|extends
name|EndpointReference
parameter_list|>
name|X
name|getEndpointReference
parameter_list|(
name|Class
argument_list|<
name|X
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|builder
operator|.
name|getEndpointReference
argument_list|(
name|clazz
argument_list|)
return|;
block|}
specifier|private
name|void
name|setupEndpointAddressContext
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
comment|//NOTE for jms transport the address would be null
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|new
name|WrappedMessageContext
argument_list|(
name|client
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
decl_stmt|;
name|requestContext
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
name|T
name|invoke
parameter_list|(
name|T
name|obj
parameter_list|)
block|{
return|return
name|invoke
argument_list|(
name|obj
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|private
name|void
name|checkError
parameter_list|()
block|{
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getBinding
argument_list|()
operator|instanceof
name|SOAPBinding
condition|)
block|{
name|SOAPFault
name|soapFault
init|=
literal|null
decl_stmt|;
try|try
block|{
name|soapFault
operator|=
name|JaxWsClientProxy
operator|.
name|createSoapFault
argument_list|(
operator|(
name|SOAPBinding
operator|)
name|getBinding
argument_list|()
argument_list|,
operator|new
name|Exception
argument_list|(
name|error
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
if|if
condition|(
name|soapFault
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|SOAPFaultException
argument_list|(
name|soapFault
argument_list|)
throw|;
block|}
block|}
elseif|else
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
operator|new
name|Exception
argument_list|(
name|error
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|error
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|RuntimeException
name|mapException
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
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
throw|throw
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
throw|;
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
name|SOAPFault
name|soapFault
init|=
literal|null
decl_stmt|;
try|try
block|{
name|soapFault
operator|=
name|JaxWsClientProxy
operator|.
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
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
if|if
condition|(
name|soapFault
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|WebServiceException
argument_list|(
name|ex
argument_list|)
return|;
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
return|return
operator|new
name|WebServiceException
argument_list|(
name|ex
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|T
name|invoke
parameter_list|(
name|T
name|obj
parameter_list|,
name|boolean
name|isOneWay
parameter_list|)
block|{
name|checkError
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|obj
operator|instanceof
name|SOAPMessage
condition|)
block|{
name|SOAPMessage
name|msg
init|=
operator|(
name|SOAPMessage
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|countAttachments
argument_list|()
operator|>
literal|0
condition|)
block|{
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|AttachmentOutInterceptor
operator|.
name|WRITE_ATTACHMENTS
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
name|QName
name|opName
init|=
operator|(
name|QName
operator|)
name|getRequestContext
argument_list|()
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|WSDL_OPERATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|opName
operator|==
literal|null
condition|)
block|{
name|opName
operator|=
operator|new
name|QName
argument_list|(
name|DISPATCH_NS
argument_list|,
name|isOneWay
condition|?
name|INVOKE_ONEWAY_NAME
else|:
name|INVOKE_NAME
argument_list|)
expr_stmt|;
block|}
name|Object
name|ret
index|[]
init|=
name|client
operator|.
name|invokeWrapped
argument_list|(
name|opName
argument_list|,
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|isOneWay
operator|||
name|ret
operator|==
literal|null
operator|||
name|ret
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|T
operator|)
name|ret
index|[
literal|0
index|]
return|;
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
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|invokeAsync
parameter_list|(
name|T
name|obj
parameter_list|,
name|AsyncHandler
argument_list|<
name|T
argument_list|>
name|asyncHandler
parameter_list|)
block|{
name|checkError
argument_list|()
expr_stmt|;
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
name|ClientCallback
name|callback
init|=
operator|new
name|JaxwsClientCallback
argument_list|<
name|T
argument_list|>
argument_list|(
name|asyncHandler
argument_list|)
decl_stmt|;
name|Response
argument_list|<
name|T
argument_list|>
name|ret
init|=
operator|new
name|JaxwsResponseCallback
argument_list|<
name|T
argument_list|>
argument_list|(
name|callback
argument_list|)
decl_stmt|;
try|try
block|{
name|QName
name|opName
init|=
operator|(
name|QName
operator|)
name|getRequestContext
argument_list|()
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|WSDL_OPERATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|opName
operator|==
literal|null
condition|)
block|{
name|opName
operator|=
operator|new
name|QName
argument_list|(
name|DISPATCH_NS
argument_list|,
name|INVOKE_NAME
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|invokeWrapped
argument_list|(
name|callback
argument_list|,
name|opName
argument_list|,
name|obj
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
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
name|ex
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Response
argument_list|<
name|T
argument_list|>
name|invokeAsync
parameter_list|(
name|T
name|obj
parameter_list|)
block|{
return|return
operator|(
name|Response
operator|)
name|invokeAsync
argument_list|(
name|obj
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|void
name|invokeOneWay
parameter_list|(
name|T
name|obj
parameter_list|)
block|{
name|invoke
argument_list|(
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Client
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

