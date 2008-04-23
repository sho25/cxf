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
name|bus
operator|.
name|extension
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|Document
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
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_class
specifier|public
class|class
name|ExtensionFragmentParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EXTENSION_ELEM_NAME
init|=
literal|"extension"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAMESPACE_ELEM_NAME
init|=
literal|"namespace"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLASS_ATTR_NAME
init|=
literal|"class"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INTERFACE_ATTR_NAME
init|=
literal|"interface"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFERRED_ATTR_NAME
init|=
literal|"deferred"
decl_stmt|;
name|List
argument_list|<
name|Extension
argument_list|>
name|getExtensions
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|Document
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|parser
init|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|document
operator|=
name|parser
operator|.
name|parse
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ExtensionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ExtensionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ExtensionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|deserialiseExtensions
argument_list|(
name|document
argument_list|)
return|;
block|}
name|List
argument_list|<
name|Extension
argument_list|>
name|deserialiseExtensions
parameter_list|(
name|Document
name|document
parameter_list|)
block|{
name|List
argument_list|<
name|Extension
argument_list|>
name|extensions
init|=
operator|new
name|ArrayList
argument_list|<
name|Extension
argument_list|>
argument_list|()
decl_stmt|;
name|Element
name|root
init|=
name|document
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
for|for
control|(
name|Node
name|nd
init|=
name|root
operator|.
name|getFirstChild
argument_list|()
init|;
name|nd
operator|!=
literal|null
condition|;
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
control|)
block|{
if|if
condition|(
name|Node
operator|.
name|ELEMENT_NODE
operator|==
name|nd
operator|.
name|getNodeType
argument_list|()
operator|&&
name|EXTENSION_ELEM_NAME
operator|.
name|equals
argument_list|(
name|nd
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|Extension
name|e
init|=
operator|new
name|Extension
argument_list|()
decl_stmt|;
name|Element
name|elem
init|=
operator|(
name|Element
operator|)
name|nd
decl_stmt|;
name|e
operator|.
name|setClassname
argument_list|(
name|elem
operator|.
name|getAttribute
argument_list|(
name|CLASS_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|e
operator|.
name|setInterfaceName
argument_list|(
name|elem
operator|.
name|getAttribute
argument_list|(
name|INTERFACE_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|bval
init|=
name|elem
operator|.
name|getAttribute
argument_list|(
name|DEFERRED_ATTR_NAME
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|e
operator|.
name|setDeferred
argument_list|(
literal|"1"
operator|.
name|equals
argument_list|(
name|bval
argument_list|)
operator|||
literal|"true"
operator|.
name|equals
argument_list|(
name|bval
argument_list|)
argument_list|)
expr_stmt|;
name|deserialiseNamespaces
argument_list|(
name|elem
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|extensions
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|extensions
return|;
block|}
name|void
name|deserialiseNamespaces
parameter_list|(
name|Element
name|extensionElem
parameter_list|,
name|Extension
name|e
parameter_list|)
block|{
for|for
control|(
name|Node
name|nd
init|=
name|extensionElem
operator|.
name|getFirstChild
argument_list|()
init|;
name|nd
operator|!=
literal|null
condition|;
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
control|)
block|{
if|if
condition|(
name|Node
operator|.
name|ELEMENT_NODE
operator|==
name|nd
operator|.
name|getNodeType
argument_list|()
operator|&&
name|NAMESPACE_ELEM_NAME
operator|.
name|equals
argument_list|(
name|nd
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|e
operator|.
name|getNamespaces
argument_list|()
operator|.
name|add
argument_list|(
name|nd
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

