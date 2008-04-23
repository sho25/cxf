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
name|spring
package|;
end_package

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
name|namespace
operator|.
name|QName
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
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|TLSClientParameters
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
name|configuration
operator|.
name|security
operator|.
name|TLSClientParametersType
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
name|spring
operator|.
name|AbstractBeanDefinitionParser
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
name|HttpBasicAuthSupplier
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|support
operator|.
name|BeanDefinitionBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|ParserContext
import|;
end_import

begin_class
specifier|public
class|class
name|HttpConduitBeanDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_NS
init|=
literal|"http://cxf.apache.org/transports/http/configuration"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|doParse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|)
block|{
name|bean
operator|.
name|setAbstract
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|element
argument_list|,
name|bean
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
name|element
argument_list|,
name|bean
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
name|element
argument_list|,
name|bean
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
name|mapSpecificElements
argument_list|(
name|element
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method specifically maps the "trustDecider" and "basicAuthSupplier"      * elements to properties on the HttpConduit.      *       * @param parent This should represent "conduit".      * @param bean   The bean parser.      */
specifier|private
name|void
name|mapSpecificElements
parameter_list|(
name|Element
name|parent
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|)
block|{
name|NodeList
name|nl
init|=
name|parent
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nl
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|n
init|=
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Node
operator|.
name|ELEMENT_NODE
operator|!=
name|n
operator|.
name|getNodeType
argument_list|()
operator|||
operator|!
name|HTTP_NS
operator|.
name|equals
argument_list|(
name|n
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|String
name|elementName
init|=
name|n
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
comment|// Schema should require that no more than one each of these exist.
if|if
condition|(
literal|"trustDecider"
operator|.
name|equals
argument_list|(
name|elementName
argument_list|)
condition|)
block|{
name|mapBeanOrClassElement
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|bean
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
literal|"basicAuthSupplier"
operator|.
name|equals
argument_list|(
name|elementName
argument_list|)
condition|)
block|{
name|mapBeanOrClassElement
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|bean
argument_list|,
name|HttpBasicAuthSupplier
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"tlsClientParameters"
operator|.
name|equals
argument_list|(
name|elementName
argument_list|)
condition|)
block|{
name|mapTLSClientParameters
argument_list|(
name|n
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Inject the "setTlsClientParameters" method with      * a TLSClientParametersConfig object initialized with the JAXB      * generated type unmarshalled from the selected node.      */
specifier|public
name|void
name|mapTLSClientParameters
parameter_list|(
name|Node
name|n
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|)
block|{
comment|// Unmarshal the JAXB Generated Type from Config and inject
comment|// the configured TLSClientParameters into the HTTPConduit.
name|JAXBContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|TLSClientParametersType
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|Unmarshaller
name|u
init|=
name|context
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|TLSClientParametersType
argument_list|>
name|jaxb
init|=
name|u
operator|.
name|unmarshal
argument_list|(
name|n
argument_list|,
name|TLSClientParametersType
operator|.
name|class
argument_list|)
decl_stmt|;
name|TLSClientParameters
name|params
init|=
operator|new
name|TLSClientParametersConfig
argument_list|(
name|jaxb
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"tlsClientParameters"
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not process configuration."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * This method finds the class or bean associated with the named element      * and sets the bean property that is associated with the same name as      * the element.      *<p>      * The element has either a "class" attribute or "bean" attribute, but       * not both.      *       * @param element      The element.      * @param bean         The Bean Definition Parser.      * @param elementClass The Class a bean or class is supposed to be.      */
specifier|protected
name|void
name|mapBeanOrClassElement
parameter_list|(
name|Element
name|element
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|Class
name|elementClass
parameter_list|)
block|{
name|String
name|elementName
init|=
name|element
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|classProperty
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"class"
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
try|try
block|{
name|Object
name|obj
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|classProperty
argument_list|,
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|elementClass
operator|.
name|isInstance
argument_list|(
name|obj
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Element '"
operator|+
name|elementName
operator|+
literal|"' must be of type "
operator|+
name|elementClass
operator|.
name|getName
argument_list|()
operator|+
literal|"."
argument_list|)
throw|;
block|}
name|bean
operator|.
name|addPropertyValue
argument_list|(
name|elementName
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Element '"
operator|+
name|elementName
operator|+
literal|"' could not load "
operator|+
name|classProperty
operator|+
literal|" - "
operator|+
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Element '"
operator|+
name|elementName
operator|+
literal|"' could not load "
operator|+
name|classProperty
operator|+
literal|" - "
operator|+
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Element '"
operator|+
name|elementName
operator|+
literal|"' could not load "
operator|+
name|classProperty
operator|+
literal|" - "
operator|+
name|ex
argument_list|)
throw|;
block|}
block|}
name|String
name|beanref
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"bean"
argument_list|)
decl_stmt|;
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Element '"
operator|+
name|elementName
operator|+
literal|"' cannot have both "
operator|+
literal|"\"bean\" and \"class\" attributes."
argument_list|)
throw|;
block|}
name|bean
operator|.
name|addPropertyReference
argument_list|(
name|elementName
argument_list|,
name|beanref
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|classProperty
operator|==
literal|null
operator|||
name|classProperty
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Element '"
operator|+
name|elementName
operator|+
literal|"' requires at least one of the "
operator|+
literal|"\"bean\" or \"class\" attributes."
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Class
name|getBeanClass
parameter_list|(
name|Element
name|arg0
parameter_list|)
block|{
return|return
name|HTTPConduit
operator|.
name|class
return|;
block|}
block|}
end_class

end_unit

