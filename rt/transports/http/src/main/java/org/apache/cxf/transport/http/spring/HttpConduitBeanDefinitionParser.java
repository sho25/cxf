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
name|Attr
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
name|NamedNodeMap
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
name|CertificateConstraintsType
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
name|CipherSuites
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
name|FiltersType
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
name|KeyManagersType
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
name|SecureRandomParameters
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
name|TrustManagersType
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
specifier|private
specifier|static
specifier|final
name|String
name|SECURITY_NS
init|=
literal|"http://cxf.apache.org/configuration/security"
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
comment|/**      * This method specifically maps the "trustDecider" and "basicAuthSupplier"      * elements to properties on the HttpConduit.      *      * @param parent This should represent "conduit".      * @param bean   The bean parser.      */
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
name|Node
name|n
init|=
name|parent
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|n
operator|!=
literal|null
condition|)
block|{
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
name|n
operator|=
name|n
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
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
literal|"authSupplier"
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
name|HttpAuthSupplier
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
name|HttpAuthSupplier
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
operator|(
name|Element
operator|)
name|n
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
name|n
operator|=
name|n
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Inject the "setTlsClientParameters" method with      * a TLSClientParametersConfig object initialized with the JAXB      * generated type unmarshalled from the selected node.      */
specifier|public
name|void
name|mapTLSClientParameters
parameter_list|(
name|Element
name|e
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|)
block|{
name|BeanDefinitionBuilder
name|paramsbean
init|=
name|BeanDefinitionBuilder
operator|.
name|rootBeanDefinition
argument_list|(
name|TLSClientParametersConfig
operator|.
name|TLSClientParametersTypeInternal
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// read the attributes
name|NamedNodeMap
name|as
init|=
name|e
operator|.
name|getAttributes
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
name|as
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Attr
name|a
init|=
operator|(
name|Attr
operator|)
name|as
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|.
name|getNamespaceURI
argument_list|()
operator|==
literal|null
condition|)
block|{
name|String
name|aname
init|=
name|a
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"useHttpsURLConnectionDefaultSslSocketFactory"
operator|.
name|equals
argument_list|(
name|aname
argument_list|)
operator|||
literal|"useHttpsURLConnectionDefaultHostnameVerifier"
operator|.
name|equals
argument_list|(
name|aname
argument_list|)
operator|||
literal|"disableCNCheck"
operator|.
name|equals
argument_list|(
name|aname
argument_list|)
operator|||
literal|"enableRevocation"
operator|.
name|equals
argument_list|(
name|aname
argument_list|)
operator|||
literal|"jsseProvider"
operator|.
name|equals
argument_list|(
name|aname
argument_list|)
operator|||
literal|"secureSocketProtocol"
operator|.
name|equals
argument_list|(
name|aname
argument_list|)
operator|||
literal|"sslCacheTimeout"
operator|.
name|equals
argument_list|(
name|aname
argument_list|)
condition|)
block|{
name|paramsbean
operator|.
name|addPropertyValue
argument_list|(
name|aname
argument_list|,
name|a
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// read the child elements
name|Node
name|n
init|=
name|e
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|n
operator|!=
literal|null
condition|)
block|{
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
name|SECURITY_NS
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
name|n
operator|=
name|n
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
continue|continue;
block|}
name|String
name|ename
init|=
name|n
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
comment|// Schema should require that no more than one each of these exist.
name|String
name|ref
init|=
operator|(
operator|(
name|Element
operator|)
name|n
operator|)
operator|.
name|getAttribute
argument_list|(
literal|"ref"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"keyManagers"
operator|.
name|equals
argument_list|(
name|ename
argument_list|)
condition|)
block|{
if|if
condition|(
name|ref
operator|!=
literal|null
operator|&&
name|ref
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|paramsbean
operator|.
name|addPropertyReference
argument_list|(
literal|"keyManagersRef"
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mapElementToJaxbProperty
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|paramsbean
argument_list|,
name|ename
argument_list|,
name|KeyManagersType
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"trustManagers"
operator|.
name|equals
argument_list|(
name|ename
argument_list|)
condition|)
block|{
if|if
condition|(
name|ref
operator|!=
literal|null
operator|&&
name|ref
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|paramsbean
operator|.
name|addPropertyReference
argument_list|(
literal|"trustManagersRef"
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mapElementToJaxbProperty
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|paramsbean
argument_list|,
name|ename
argument_list|,
name|TrustManagersType
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"cipherSuites"
operator|.
name|equals
argument_list|(
name|ename
argument_list|)
condition|)
block|{
name|mapElementToJaxbProperty
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|paramsbean
argument_list|,
name|ename
argument_list|,
name|CipherSuites
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"cipherSuitesFilter"
operator|.
name|equals
argument_list|(
name|ename
argument_list|)
condition|)
block|{
name|mapElementToJaxbProperty
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|paramsbean
argument_list|,
name|ename
argument_list|,
name|FiltersType
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"secureRandomParameters"
operator|.
name|equals
argument_list|(
name|ename
argument_list|)
condition|)
block|{
name|mapElementToJaxbProperty
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|paramsbean
argument_list|,
name|ename
argument_list|,
name|SecureRandomParameters
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"certConstraints"
operator|.
name|equals
argument_list|(
name|ename
argument_list|)
condition|)
block|{
name|mapElementToJaxbProperty
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|paramsbean
argument_list|,
name|ename
argument_list|,
name|CertificateConstraintsType
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"certAlias"
operator|.
name|equals
argument_list|(
name|ename
argument_list|)
condition|)
block|{
name|paramsbean
operator|.
name|addPropertyValue
argument_list|(
name|ename
argument_list|,
name|n
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|n
operator|=
name|n
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|BeanDefinitionBuilder
name|jaxbbean
init|=
name|BeanDefinitionBuilder
operator|.
name|rootBeanDefinition
argument_list|(
name|TLSClientParametersConfig
operator|.
name|class
argument_list|)
decl_stmt|;
name|jaxbbean
operator|.
name|getRawBeanDefinition
argument_list|()
operator|.
name|setFactoryMethodName
argument_list|(
literal|"createTLSClientParametersFromType"
argument_list|)
expr_stmt|;
name|jaxbbean
operator|.
name|addConstructorArgValue
argument_list|(
name|paramsbean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"tlsClientParameters"
argument_list|,
name|jaxbbean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method finds the class or bean associated with the named element      * and sets the bean property that is associated with the same name as      * the element.      *<p>      * The element has either a "class" attribute or "bean" attribute, but      * not both.      *      * @param element      The element.      * @param bean         The Bean Definition Parser.      * @param elementClass The Class a bean or class is supposed to be.      */
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
argument_list|<
name|?
argument_list|>
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
name|isEmpty
argument_list|()
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
decl||
name|ClassNotFoundException
decl||
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
name|isEmpty
argument_list|()
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
name|isEmpty
argument_list|()
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
name|isEmpty
argument_list|()
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
argument_list|<
name|?
argument_list|>
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

