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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|helpers
operator|.
name|FileUtils
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
name|XMLUtils
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

begin_class
specifier|public
specifier|final
class|class
name|JAXBUtils
block|{
specifier|private
name|JAXBUtils
parameter_list|()
block|{     }
specifier|private
specifier|static
name|Node
name|innerJaxbBinding
parameter_list|(
name|Element
name|schema
parameter_list|)
block|{
name|String
name|schemaNamespace
init|=
name|schema
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|NodeList
name|annoList
init|=
name|schema
operator|.
name|getElementsByTagNameNS
argument_list|(
name|schemaNamespace
argument_list|,
literal|"annotation"
argument_list|)
decl_stmt|;
name|Element
name|annotation
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|annoList
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
name|annotation
operator|=
operator|(
name|Element
operator|)
name|annoList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|annotation
operator|=
name|schema
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElementNS
argument_list|(
name|schemaNamespace
argument_list|,
literal|"annotation"
argument_list|)
expr_stmt|;
block|}
name|NodeList
name|appList
init|=
name|annotation
operator|.
name|getElementsByTagNameNS
argument_list|(
name|schemaNamespace
argument_list|,
literal|"appinfo"
argument_list|)
decl_stmt|;
name|Element
name|appInfo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|appList
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
name|appInfo
operator|=
operator|(
name|Element
operator|)
name|appList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appInfo
operator|=
name|schema
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElementNS
argument_list|(
name|schemaNamespace
argument_list|,
literal|"appinfo"
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|appendChild
argument_list|(
name|appInfo
argument_list|)
expr_stmt|;
block|}
name|Element
name|jaxbBindings
init|=
literal|null
decl_stmt|;
name|NodeList
name|jaxbList
init|=
name|schema
operator|.
name|getElementsByTagNameNS
argument_list|(
name|ToolConstants
operator|.
name|NS_JAXB_BINDINGS
argument_list|,
literal|"schemaBindings"
argument_list|)
decl_stmt|;
if|if
condition|(
name|jaxbList
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
name|jaxbBindings
operator|=
operator|(
name|Element
operator|)
name|jaxbList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jaxbBindings
operator|=
name|schema
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElementNS
argument_list|(
name|ToolConstants
operator|.
name|NS_JAXB_BINDINGS
argument_list|,
literal|"schemaBindings"
argument_list|)
expr_stmt|;
name|appInfo
operator|.
name|appendChild
argument_list|(
name|jaxbBindings
argument_list|)
expr_stmt|;
block|}
return|return
name|jaxbBindings
return|;
block|}
specifier|public
specifier|static
name|Node
name|innerJaxbPackageBinding
parameter_list|(
name|Element
name|schema
parameter_list|,
name|String
name|packagevalue
parameter_list|)
block|{
name|Document
name|doc
init|=
name|schema
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|XMLUtils
operator|.
name|hasAttribute
argument_list|(
name|schema
argument_list|,
name|ToolConstants
operator|.
name|NS_JAXB_BINDINGS
argument_list|)
condition|)
block|{
name|schema
operator|.
name|setAttributeNS
argument_list|(
name|ToolConstants
operator|.
name|NS_JAXB_BINDINGS
argument_list|,
literal|"version"
argument_list|,
literal|"2.0"
argument_list|)
expr_stmt|;
block|}
name|Node
name|schemaBindings
init|=
name|innerJaxbBinding
argument_list|(
name|schema
argument_list|)
decl_stmt|;
name|NodeList
name|pkgList
init|=
name|doc
operator|.
name|getElementsByTagNameNS
argument_list|(
name|ToolConstants
operator|.
name|NS_JAXB_BINDINGS
argument_list|,
literal|"package"
argument_list|)
decl_stmt|;
name|Element
name|packagename
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|pkgList
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
name|packagename
operator|=
operator|(
name|Element
operator|)
name|pkgList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|packagename
operator|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|ToolConstants
operator|.
name|NS_JAXB_BINDINGS
argument_list|,
literal|"package"
argument_list|)
expr_stmt|;
block|}
name|packagename
operator|.
name|setAttribute
argument_list|(
literal|"name"
argument_list|,
name|packagevalue
argument_list|)
expr_stmt|;
name|schemaBindings
operator|.
name|appendChild
argument_list|(
name|packagename
argument_list|)
expr_stmt|;
return|return
name|schemaBindings
operator|.
name|getParentNode
argument_list|()
operator|.
name|getParentNode
argument_list|()
return|;
block|}
comment|/**      * Create the jaxb binding file to customize namespace to package mapping      *       * @param namespace      * @param pkgName      * @return file      */
specifier|public
specifier|static
name|File
name|getPackageMappingSchemaBindingFile
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|pkgName
parameter_list|)
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|rootElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"schema"
argument_list|)
decl_stmt|;
name|rootElement
operator|.
name|setAttribute
argument_list|(
literal|"xmlns"
argument_list|,
name|ToolConstants
operator|.
name|SCHEMA_URI
argument_list|)
expr_stmt|;
name|rootElement
operator|.
name|setAttribute
argument_list|(
literal|"xmlns:jaxb"
argument_list|,
name|ToolConstants
operator|.
name|NS_JAXB_BINDINGS
argument_list|)
expr_stmt|;
name|rootElement
operator|.
name|setAttribute
argument_list|(
literal|"jaxb:version"
argument_list|,
literal|"2.0"
argument_list|)
expr_stmt|;
name|rootElement
operator|.
name|setAttribute
argument_list|(
literal|"targetNamespace"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|Element
name|annoElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"annotation"
argument_list|)
decl_stmt|;
name|Element
name|appInfo
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"appinfo"
argument_list|)
decl_stmt|;
name|Element
name|schemaBindings
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"jaxb:schemaBindings"
argument_list|)
decl_stmt|;
name|Element
name|pkgElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"jaxb:package"
argument_list|)
decl_stmt|;
name|pkgElement
operator|.
name|setAttribute
argument_list|(
literal|"name"
argument_list|,
name|pkgName
argument_list|)
expr_stmt|;
name|annoElement
operator|.
name|appendChild
argument_list|(
name|appInfo
argument_list|)
expr_stmt|;
name|appInfo
operator|.
name|appendChild
argument_list|(
name|schemaBindings
argument_list|)
expr_stmt|;
name|schemaBindings
operator|.
name|appendChild
argument_list|(
name|pkgElement
argument_list|)
expr_stmt|;
name|rootElement
operator|.
name|appendChild
argument_list|(
name|annoElement
argument_list|)
expr_stmt|;
name|File
name|tmpFile
init|=
literal|null
decl_stmt|;
try|try
block|{
name|tmpFile
operator|=
name|FileUtils
operator|.
name|createTempFile
argument_list|(
literal|"customzied"
argument_list|,
literal|".xsd"
argument_list|)
expr_stmt|;
name|FileOutputStream
name|fout
init|=
operator|new
name|FileOutputStream
argument_list|(
name|tmpFile
argument_list|)
decl_stmt|;
name|DOMUtils
operator|.
name|writeXml
argument_list|(
name|rootElement
argument_list|,
name|fout
argument_list|)
expr_stmt|;
name|fout
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|tmpFile
return|;
block|}
block|}
end_class

end_unit

