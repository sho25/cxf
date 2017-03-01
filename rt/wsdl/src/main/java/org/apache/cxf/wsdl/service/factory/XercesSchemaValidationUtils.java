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
name|wsdl
operator|.
name|service
operator|.
name|factory
package|;
end_package

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
name|io
operator|.
name|Reader
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
name|DOMConfiguration
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
name|DOMImplementation
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
name|bootstrap
operator|.
name|DOMImplementationRegistry
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

begin_comment
comment|/**  *  */
end_comment

begin_class
class|class
name|XercesSchemaValidationUtils
block|{
specifier|static
class|class
name|DOMLSInput
implements|implements
name|LSInput
block|{
specifier|private
name|String
name|systemId
decl_stmt|;
specifier|private
name|String
name|data
decl_stmt|;
name|DOMLSInput
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|systemId
parameter_list|)
throws|throws
name|TransformerException
block|{
name|this
operator|.
name|systemId
operator|=
name|systemId
expr_stmt|;
name|data
operator|=
name|StaxUtils
operator|.
name|toString
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|getBaseURI
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|InputStream
name|getByteStream
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|boolean
name|getCertifiedText
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|Reader
name|getCharacterStream
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
literal|"utf-8"
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|getPublicId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|getStringData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
name|systemId
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setBaseURI
parameter_list|(
name|String
name|baseURI
parameter_list|)
block|{         }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setByteStream
parameter_list|(
name|InputStream
name|byteStream
parameter_list|)
block|{         }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setCertifiedText
parameter_list|(
name|boolean
name|certifiedText
parameter_list|)
block|{         }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setCharacterStream
parameter_list|(
name|Reader
name|characterStream
parameter_list|)
block|{         }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{         }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setPublicId
parameter_list|(
name|String
name|publicId
parameter_list|)
block|{         }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setStringData
parameter_list|(
name|String
name|stringData
parameter_list|)
block|{         }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setSystemId
parameter_list|(
name|String
name|systemId
parameter_list|)
block|{         }
block|}
specifier|private
name|DOMImplementation
name|impl
decl_stmt|;
name|XercesSchemaValidationUtils
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|InstantiationException
throws|,
name|IllegalAccessException
throws|,
name|ClassCastException
block|{
name|DOMImplementationRegistry
name|registry
init|=
name|DOMImplementationRegistry
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|impl
operator|=
name|registry
operator|.
name|getDOMImplementation
argument_list|(
literal|"XS-Loader"
argument_list|)
expr_stmt|;
block|}
name|Method
name|findMethod
parameter_list|(
name|Object
name|o
parameter_list|,
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|Method
name|m
range|:
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
name|m
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|m
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
block|}
return|return
literal|null
return|;
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
name|Exception
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
argument_list|<>
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
try|try
block|{
name|Object
name|schemaLoader
init|=
name|findMethod
argument_list|(
name|impl
argument_list|,
literal|"createXSLoader"
argument_list|)
operator|.
name|invoke
argument_list|(
name|impl
argument_list|,
operator|new
name|Object
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|DOMConfiguration
name|config
init|=
operator|(
name|DOMConfiguration
operator|)
name|findMethod
argument_list|(
name|schemaLoader
argument_list|,
literal|"getConfig"
argument_list|)
operator|.
name|invoke
argument_list|(
name|schemaLoader
argument_list|)
decl_stmt|;
name|config
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
name|config
operator|.
name|setParameter
argument_list|(
literal|"error-handler"
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|config
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
name|Method
name|m
init|=
name|findMethod
argument_list|(
name|schemaLoader
argument_list|,
literal|"loadInputList"
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|m
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
operator|+
literal|"Impl"
decl_stmt|;
name|name
operator|=
name|name
operator|.
name|replace
argument_list|(
literal|"xs.LS"
argument_list|,
literal|"impl.xs.util.LS"
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
name|Class
operator|.
name|forName
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Object
name|inputList
init|=
name|c
operator|.
name|getConstructor
argument_list|(
name|LSInput
index|[]
operator|.
expr|class
argument_list|,
name|Integer
operator|.
name|TYPE
argument_list|)
operator|.
name|newInstance
argument_list|(
name|inputs
operator|.
name|toArray
argument_list|(
operator|new
name|LSInput
index|[
name|inputs
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|inputs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|m
operator|.
name|invoke
argument_list|(
name|schemaLoader
argument_list|,
name|inputList
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|e
operator|.
name|getTargetException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

