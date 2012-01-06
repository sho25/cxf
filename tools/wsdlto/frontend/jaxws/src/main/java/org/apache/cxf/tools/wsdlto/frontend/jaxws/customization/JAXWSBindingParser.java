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
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|customization
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
name|WSDLException
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
name|ExtensionRegistry
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
name|NamespaceContext
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
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpressionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|tools
operator|.
name|common
operator|.
name|ToolConstants
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
name|tools
operator|.
name|common
operator|.
name|ToolException
import|;
end_import

begin_class
specifier|public
class|class
name|JAXWSBindingParser
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
name|CustomizationParser
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ExtensionRegistry
name|extReg
decl_stmt|;
specifier|public
name|JAXWSBindingParser
parameter_list|(
name|ExtensionRegistry
name|ext
parameter_list|)
block|{
name|extReg
operator|=
name|ext
expr_stmt|;
block|}
specifier|public
name|JAXWSBinding
name|parse
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|parentType
parameter_list|,
name|Element
name|element
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|WSDLException
block|{
name|JAXWSBinding
name|jaxwsBinding
init|=
operator|(
name|JAXWSBinding
operator|)
name|extReg
operator|.
name|createExtension
argument_list|(
name|parentType
argument_list|,
name|ToolConstants
operator|.
name|JAXWS_BINDINGS
argument_list|)
decl_stmt|;
name|jaxwsBinding
operator|.
name|setElementType
argument_list|(
name|ToolConstants
operator|.
name|JAXWS_BINDINGS
argument_list|)
expr_stmt|;
name|jaxwsBinding
operator|.
name|setElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|jaxwsBinding
operator|.
name|setDocumentBaseURI
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
name|parseElement
argument_list|(
name|jaxwsBinding
argument_list|,
name|element
argument_list|)
expr_stmt|;
return|return
name|jaxwsBinding
return|;
block|}
name|void
name|parseElement
parameter_list|(
name|JAXWSBinding
name|jaxwsBinding
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
name|Node
name|child
init|=
name|element
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
if|if
condition|(
name|child
operator|==
literal|null
condition|)
block|{
comment|// global binding
if|if
condition|(
name|isAsyncElement
argument_list|(
name|element
argument_list|)
condition|)
block|{
name|jaxwsBinding
operator|.
name|setEnableAsyncMapping
argument_list|(
name|getNodeValue
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isMIMEElement
argument_list|(
name|element
argument_list|)
condition|)
block|{
name|jaxwsBinding
operator|.
name|setEnableMime
argument_list|(
name|getNodeValue
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isPackageElement
argument_list|(
name|element
argument_list|)
condition|)
block|{
name|jaxwsBinding
operator|.
name|setPackage
argument_list|(
name|getPackageName
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isWrapperStyle
argument_list|(
name|element
argument_list|)
condition|)
block|{
name|jaxwsBinding
operator|.
name|setEnableWrapperStyle
argument_list|(
name|getNodeValue
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// other binding
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isAsyncElement
argument_list|(
name|child
argument_list|)
condition|)
block|{
name|jaxwsBinding
operator|.
name|setEnableAsyncMapping
argument_list|(
name|getNodeValue
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isMIMEElement
argument_list|(
name|child
argument_list|)
condition|)
block|{
name|jaxwsBinding
operator|.
name|setEnableMime
argument_list|(
name|getNodeValue
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isWrapperStyle
argument_list|(
name|child
argument_list|)
condition|)
block|{
name|jaxwsBinding
operator|.
name|setEnableWrapperStyle
argument_list|(
name|getNodeValue
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isPackageElement
argument_list|(
name|child
argument_list|)
condition|)
block|{
name|jaxwsBinding
operator|.
name|setPackage
argument_list|(
name|getPackageName
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
name|Node
name|docChild
init|=
name|DOMUtils
operator|.
name|getChild
argument_list|(
name|child
argument_list|,
name|Element
operator|.
name|ELEMENT_NODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|docChild
operator|!=
literal|null
operator|&&
name|this
operator|.
name|isJAXWSClassDoc
argument_list|(
name|docChild
argument_list|)
condition|)
block|{
name|jaxwsBinding
operator|.
name|setPackageJavaDoc
argument_list|(
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|docChild
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|isJAXWSMethodElement
argument_list|(
name|child
argument_list|)
condition|)
block|{
name|jaxwsBinding
operator|.
name|setMethodName
argument_list|(
name|getMethodName
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
name|Node
name|docChild
init|=
name|DOMUtils
operator|.
name|getChild
argument_list|(
name|child
argument_list|,
name|Element
operator|.
name|ELEMENT_NODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|docChild
operator|!=
literal|null
operator|&&
name|this
operator|.
name|isJAXWSClassDoc
argument_list|(
name|docChild
argument_list|)
condition|)
block|{
name|jaxwsBinding
operator|.
name|setMethodJavaDoc
argument_list|(
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|docChild
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|isJAXWSParameterElement
argument_list|(
name|child
argument_list|)
condition|)
block|{
name|Element
name|childElement
init|=
operator|(
name|Element
operator|)
name|child
decl_stmt|;
name|String
name|partPath
init|=
literal|"//"
operator|+
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"part"
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|queryXPathNode
argument_list|(
name|element
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|partPath
argument_list|)
decl_stmt|;
name|String
name|messageName
init|=
literal|""
decl_stmt|;
name|String
name|partName
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|partName
operator|=
operator|(
operator|(
name|Element
operator|)
name|node
operator|)
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|Node
name|messageNode
init|=
name|node
operator|.
name|getParentNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageNode
operator|!=
literal|null
condition|)
block|{
name|Element
name|messageEle
init|=
operator|(
name|Element
operator|)
name|messageNode
decl_stmt|;
name|messageName
operator|=
name|messageEle
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|name
init|=
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|elementNameString
init|=
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"childElementName"
argument_list|)
decl_stmt|;
name|QName
name|elementName
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|elementNameString
argument_list|)
condition|)
block|{
name|String
name|ns
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|elementNameString
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|ns
operator|=
name|elementNameString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|elementNameString
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
argument_list|)
expr_stmt|;
name|ns
operator|=
name|childElement
operator|.
name|lookupNamespaceURI
argument_list|(
name|ns
argument_list|)
expr_stmt|;
name|elementNameString
operator|=
name|elementNameString
operator|.
name|substring
argument_list|(
name|elementNameString
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|elementName
operator|=
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|elementNameString
argument_list|)
expr_stmt|;
block|}
name|JAXWSParameter
name|jpara
init|=
operator|new
name|JAXWSParameter
argument_list|(
name|messageName
argument_list|,
name|partName
argument_list|,
name|elementName
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|jaxwsBinding
operator|.
name|addJaxwsPara
argument_list|(
name|jpara
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isJAXWSClass
argument_list|(
name|child
argument_list|)
condition|)
block|{
name|Element
name|childElement
init|=
operator|(
name|Element
operator|)
name|child
decl_stmt|;
name|String
name|clzName
init|=
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|javadoc
init|=
literal|""
decl_stmt|;
name|Node
name|docChild
init|=
name|DOMUtils
operator|.
name|getChild
argument_list|(
name|child
argument_list|,
name|Element
operator|.
name|ELEMENT_NODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|docChild
operator|!=
literal|null
operator|&&
name|this
operator|.
name|isJAXWSClassDoc
argument_list|(
name|docChild
argument_list|)
condition|)
block|{
name|javadoc
operator|=
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|docChild
argument_list|)
expr_stmt|;
block|}
name|JAXWSClass
name|jaxwsClass
init|=
operator|new
name|JAXWSClass
argument_list|(
name|clzName
argument_list|,
name|javadoc
argument_list|)
decl_stmt|;
name|jaxwsBinding
operator|.
name|setJaxwsClass
argument_list|(
name|jaxwsClass
argument_list|)
expr_stmt|;
block|}
name|child
operator|=
name|child
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|isJAXWSMethodElement
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
return|return
name|ToolConstants
operator|.
name|NS_JAXWS_BINDINGS
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
literal|"method"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getLocalName
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|getMethodName
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|Element
name|ele
init|=
operator|(
name|Element
operator|)
name|node
decl_stmt|;
return|return
name|ele
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isPackageElement
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
if|if
condition|(
name|ToolConstants
operator|.
name|NS_JAXWS_BINDINGS
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
literal|"package"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|isJAXWSParameterElement
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
return|return
operator|(
name|ToolConstants
operator|.
name|NS_JAXWS_BINDINGS
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|)
operator|&&
literal|"parameter"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getLocalName
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isJAXWSClass
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
return|return
operator|(
name|ToolConstants
operator|.
name|NS_JAXWS_BINDINGS
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|)
operator|&&
literal|"class"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getLocalName
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isJAXWSClassDoc
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
return|return
operator|(
name|ToolConstants
operator|.
name|NS_JAXWS_BINDINGS
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|)
operator|&&
literal|"javadoc"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getLocalName
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|getPackageName
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|Element
name|ele
init|=
operator|(
name|Element
operator|)
name|node
decl_stmt|;
return|return
name|ele
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
return|;
block|}
specifier|private
name|Boolean
name|isAsyncElement
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
return|return
literal|"enableAsyncMapping"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|ToolConstants
operator|.
name|NS_JAXWS_BINDINGS
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Boolean
name|isWrapperStyle
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
return|return
literal|"enableWrapperStyle"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|ToolConstants
operator|.
name|NS_JAXWS_BINDINGS
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Boolean
name|getNodeValue
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
name|node
operator|.
name|getTextContent
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Boolean
name|isMIMEElement
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
return|return
literal|"enableMIMEContent"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|ToolConstants
operator|.
name|NS_JAXWS_BINDINGS
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Node
name|queryXPathNode
parameter_list|(
name|Node
name|target
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
name|NodeList
name|nlst
decl_stmt|;
try|try
block|{
name|ContextImpl
name|contextImpl
init|=
operator|new
name|ContextImpl
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|XPath
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|xpath
operator|.
name|setNamespaceContext
argument_list|(
name|contextImpl
argument_list|)
expr_stmt|;
name|nlst
operator|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
name|expression
argument_list|,
name|target
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XPathExpressionException
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"XPATH_ERROR"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|expression
block|}
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|nlst
operator|.
name|getLength
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"ERROR_TARGETNODE_WITH_XPATH"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|expression
block|}
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|Node
name|rnode
init|=
name|nlst
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|rnode
operator|instanceof
name|Element
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|Element
operator|)
name|rnode
return|;
block|}
class|class
name|ContextImpl
implements|implements
name|NamespaceContext
block|{
specifier|private
name|Node
name|targetNode
decl_stmt|;
specifier|public
name|ContextImpl
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|targetNode
operator|=
name|node
expr_stmt|;
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
return|return
name|targetNode
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|lookupNamespaceURI
argument_list|(
name|prefix
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|nsURI
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Iterator
argument_list|<
name|?
argument_list|>
name|getPrefixes
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

