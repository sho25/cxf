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
name|transport
operator|.
name|http
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|aries
operator|.
name|blueprint
operator|.
name|ParserContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutableBeanMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutablePassThroughMetadata
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
name|configuration
operator|.
name|blueprint
operator|.
name|AbstractBPBeanDefinitionParser
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
name|configuration
operator|.
name|jsse
operator|.
name|spring
operator|.
name|TLSClientParametersConfig
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|configuration
operator|.
name|security
operator|.
name|ProxyAuthorizationPolicy
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
name|staxutils
operator|.
name|StaxUtils
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
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
name|transport
operator|.
name|http
operator|.
name|MessageTrustDecider
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
name|transport
operator|.
name|http
operator|.
name|auth
operator|.
name|HttpAuthSupplier
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|Metadata
import|;
end_import

begin_class
specifier|public
class|class
name|HttpConduitBPBeanDefinitionParser
extends|extends
name|AbstractBPBeanDefinitionParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_NS
init|=
literal|"http://cxf.apache.org/transports/http/configuration"
decl_stmt|;
specifier|public
name|Metadata
name|parse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|MutableBeanMetadata
name|bean
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableBeanMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setRuntimeClass
argument_list|(
name|HTTPConduit
operator|.
name|class
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|context
argument_list|,
name|bean
argument_list|,
name|element
argument_list|,
operator|new
name|QName
argument_list|(
name|HTTP_NS
argument_list|,
literal|"client"
argument_list|)
argument_list|,
literal|"client"
argument_list|,
name|HTTPClientPolicy
operator|.
name|class
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|context
argument_list|,
name|bean
argument_list|,
name|element
argument_list|,
operator|new
name|QName
argument_list|(
name|HTTP_NS
argument_list|,
literal|"proxyAuthorization"
argument_list|)
argument_list|,
literal|"proxyAuthorization"
argument_list|,
name|ProxyAuthorizationPolicy
operator|.
name|class
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|context
argument_list|,
name|bean
argument_list|,
name|element
argument_list|,
operator|new
name|QName
argument_list|(
name|HTTP_NS
argument_list|,
literal|"authorization"
argument_list|)
argument_list|,
literal|"authorization"
argument_list|,
name|AuthorizationPolicy
operator|.
name|class
argument_list|)
expr_stmt|;
name|parseAttributes
argument_list|(
name|element
argument_list|,
name|context
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|parseChildElements
argument_list|(
name|element
argument_list|,
name|context
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setScope
argument_list|(
name|MutableBeanMetadata
operator|.
name|SCOPE_PROTOTYPE
argument_list|)
expr_stmt|;
return|return
name|bean
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processNameAttribute
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|,
name|MutableBeanMetadata
name|bean
parameter_list|,
name|String
name|val
parameter_list|)
block|{
name|bean
operator|.
name|setId
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|mapElement
parameter_list|(
name|ParserContext
name|ctx
parameter_list|,
name|MutableBeanMetadata
name|bean
parameter_list|,
name|Element
name|el
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"tlsClientParameters"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|mapTLSClientParameters
argument_list|(
name|ctx
argument_list|,
name|bean
argument_list|,
name|el
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"trustDecider"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|mapBeanOrClassElement
argument_list|(
name|ctx
argument_list|,
name|bean
argument_list|,
name|el
argument_list|,
name|MessageTrustDecider
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"authSupplier"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|mapBeanOrClassElement
argument_list|(
name|ctx
argument_list|,
name|bean
argument_list|,
name|el
argument_list|,
name|HttpAuthSupplier
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|mapTLSClientParameters
parameter_list|(
name|ParserContext
name|ctx
parameter_list|,
name|MutableBeanMetadata
name|bean
parameter_list|,
name|Element
name|el
parameter_list|)
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|xmlWriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|writer
argument_list|)
decl_stmt|;
try|try
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
name|el
argument_list|,
name|xmlWriter
argument_list|)
expr_stmt|;
name|xmlWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|Object
name|v
init|=
name|TLSClientParametersConfig
operator|.
name|createTLSClientParameters
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|MutablePassThroughMetadata
name|value
init|=
name|ctx
operator|.
name|createMetadata
argument_list|(
name|MutablePassThroughMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|value
operator|.
name|setObject
argument_list|(
name|v
argument_list|)
expr_stmt|;
name|bean
operator|.
name|addProperty
argument_list|(
literal|"tlsClientParameters"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|mapBeanOrClassElement
parameter_list|(
name|ParserContext
name|ctx
parameter_list|,
name|MutableBeanMetadata
name|bean
parameter_list|,
name|Element
name|el
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|String
name|elementName
init|=
name|el
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|classProperty
init|=
name|el
operator|.
name|getAttribute
argument_list|(
literal|"class"
argument_list|)
decl_stmt|;
name|String
name|beanref
init|=
name|el
operator|.
name|getAttribute
argument_list|(
literal|"bean"
argument_list|)
decl_stmt|;
if|if
condition|(
name|classProperty
operator|!=
literal|null
operator|&&
operator|!
name|classProperty
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addProperty
argument_list|(
name|elementName
argument_list|,
name|createObjectOfClass
argument_list|(
name|ctx
argument_list|,
name|classProperty
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|beanref
operator|!=
literal|null
operator|&&
operator|!
name|beanref
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addProperty
argument_list|(
name|elementName
argument_list|,
name|createRef
argument_list|(
name|ctx
argument_list|,
name|beanref
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

