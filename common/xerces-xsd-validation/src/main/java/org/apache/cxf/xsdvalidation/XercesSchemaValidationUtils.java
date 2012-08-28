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
name|xsdvalidation
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
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
name|TransformerException
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
name|DOMErrorHandler
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
name|ls
operator|.
name|LSInput
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
name|ls
operator|.
name|LSResourceResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaSerializer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaSerializer
operator|.
name|XmlSchemaSerializerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xerces
operator|.
name|dom
operator|.
name|DOMXSImplementationSourceImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xerces
operator|.
name|xs
operator|.
name|LSInputList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xerces
operator|.
name|xs
operator|.
name|XSImplementation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xerces
operator|.
name|xs
operator|.
name|XSLoader
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
class|class
name|XercesSchemaValidationUtils
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|private
specifier|static
specifier|final
class|class
name|ListLSInput
extends|extends
name|ArrayList
implements|implements
name|LSInputList
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|ListLSInput
parameter_list|(
name|List
name|inputs
parameter_list|)
block|{
name|super
argument_list|(
name|inputs
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|size
argument_list|()
return|;
block|}
specifier|public
name|LSInput
name|item
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
operator|(
name|LSInput
operator|)
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
block|}
specifier|private
name|XSImplementation
name|impl
decl_stmt|;
name|XercesSchemaValidationUtils
parameter_list|()
block|{
name|DOMXSImplementationSourceImpl
name|source
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|xerces
operator|.
name|dom
operator|.
name|DOMXSImplementationSourceImpl
argument_list|()
decl_stmt|;
name|impl
operator|=
operator|(
name|XSImplementation
operator|)
name|source
operator|.
name|getDOMImplementation
argument_list|(
literal|"XS-Loader"
argument_list|)
expr_stmt|;
block|}
name|void
name|tryToParseSchemas
parameter_list|(
name|XmlSchemaCollection
name|collection
parameter_list|,
name|DOMErrorHandler
name|handler
parameter_list|)
throws|throws
name|XmlSchemaSerializerException
throws|,
name|TransformerException
block|{
specifier|final
name|List
argument_list|<
name|DOMLSInput
argument_list|>
name|inputs
init|=
operator|new
name|ArrayList
argument_list|<
name|DOMLSInput
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|LSInput
argument_list|>
name|resolverMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|LSInput
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|XmlSchema
name|schema
range|:
name|collection
operator|.
name|getXmlSchemas
argument_list|()
control|)
block|{
if|if
condition|(
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
operator|.
name|equals
argument_list|(
name|schema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Document
name|document
init|=
operator|new
name|XmlSchemaSerializer
argument_list|()
operator|.
name|serializeSchema
argument_list|(
name|schema
argument_list|,
literal|false
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
name|DOMLSInput
name|input
init|=
operator|new
name|DOMLSInput
argument_list|(
name|document
argument_list|,
name|schema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
decl_stmt|;
name|resolverMap
operator|.
name|put
argument_list|(
name|schema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|input
argument_list|)
expr_stmt|;
name|inputs
operator|.
name|add
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
name|XSLoader
name|schemaLoader
init|=
name|impl
operator|.
name|createXSLoader
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|schemaLoader
operator|.
name|getConfig
argument_list|()
operator|.
name|setParameter
argument_list|(
literal|"validate"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|schemaLoader
operator|.
name|getConfig
argument_list|()
operator|.
name|setParameter
argument_list|(
literal|"error-handler"
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|schemaLoader
operator|.
name|getConfig
argument_list|()
operator|.
name|setParameter
argument_list|(
literal|"resource-resolver"
argument_list|,
operator|new
name|LSResourceResolver
argument_list|()
block|{
specifier|public
name|LSInput
name|resolveResource
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|,
name|String
name|baseURI
parameter_list|)
block|{
return|return
name|resolverMap
operator|.
name|get
argument_list|(
name|namespaceURI
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|schemaLoader
operator|.
name|loadInputList
argument_list|(
operator|new
name|ListLSInput
argument_list|(
name|inputs
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

