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
name|util
operator|.
name|Iterator
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
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensibilityElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|UnknownExtensibilityElement
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
name|dom
operator|.
name|DOMSource
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
name|wsaddressing
operator|.
name|W3CEndpointReference
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
name|wsaddressing
operator|.
name|W3CEndpointReferenceBuilder
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
name|Element
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
name|helpers
operator|.
name|DOMUtils
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
name|binding
operator|.
name|soap
operator|.
name|SOAPBindingImpl
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
name|ws
operator|.
name|addressing
operator|.
name|Names
import|;
end_import

begin_class
specifier|public
class|class
name|EndpointReferenceBuilder
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
name|EndpointReferenceBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|JaxWsEndpointImpl
name|endpoint
decl_stmt|;
specifier|public
name|EndpointReferenceBuilder
parameter_list|(
name|JaxWsEndpointImpl
name|e
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|e
expr_stmt|;
block|}
specifier|public
name|EndpointReference
name|getEndpointReference
parameter_list|()
block|{
comment|//if there is epr in wsdl, direct return this EPR
name|List
argument_list|<
name|ExtensibilityElement
argument_list|>
name|portExtensors
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getExtensors
argument_list|(
name|ExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|portExtensors
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|ExtensibilityElement
argument_list|>
name|extensionElements
init|=
name|portExtensors
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|QName
name|wsaEpr
init|=
operator|new
name|QName
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
literal|"EndpointReference"
argument_list|)
decl_stmt|;
while|while
condition|(
name|extensionElements
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ExtensibilityElement
name|ext
init|=
name|extensionElements
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|ext
operator|instanceof
name|UnknownExtensibilityElement
operator|&&
name|wsaEpr
operator|.
name|equals
argument_list|(
name|ext
operator|.
name|getElementType
argument_list|()
argument_list|)
condition|)
block|{
name|Element
name|eprEle
init|=
operator|(
operator|(
name|UnknownExtensibilityElement
operator|)
name|ext
operator|)
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|addressElements
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|eprEle
argument_list|,
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|Names
operator|.
name|WSA_ADDRESS_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|addressElements
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|/*                          * [WSA-WSDL Binding] : in a SOAP 1.1 port described using WSDL 1.1, the location                          * attribute of a soap11:address element (if present) would have the same value as the                          * wsa:Address child element of the wsa:EndpointReference element.                          */
name|addressElements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setTextContent
argument_list|(
name|this
operator|.
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
return|return
name|W3CEndpointReference
operator|.
name|readFrom
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|eprEle
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
name|String
name|bindingId
init|=
name|endpoint
operator|.
name|getJaxwsBinding
argument_list|()
operator|.
name|getBindingID
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|SOAPBindingImpl
operator|.
name|isSoapBinding
argument_list|(
name|bindingId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"GET_ENDPOINTREFERENCE_UNSUPPORTED_BINDING"
argument_list|,
name|LOG
argument_list|,
name|bindingId
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|W3CEndpointReferenceBuilder
name|builder
init|=
operator|new
name|W3CEndpointReferenceBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|address
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|serviceName
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|endpointName
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getDescription
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|wsdlDocumentLocation
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getDescription
argument_list|()
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ClassLoader
name|cl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|EndpointReferenceBuilder
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|cl
argument_list|)
expr_stmt|;
block|}
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
name|clazz
operator|!=
name|W3CEndpointReference
operator|.
name|class
condition|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
literal|"Unsupported EPR type: "
operator|+
name|clazz
argument_list|)
throw|;
block|}
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|getEndpointReference
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

