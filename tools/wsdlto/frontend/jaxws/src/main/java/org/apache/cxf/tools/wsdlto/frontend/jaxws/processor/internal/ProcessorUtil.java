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
name|processor
operator|.
name|internal
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|DOMException
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
name|jaxb
operator|.
name|JAXBUtils
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
name|URIParserUtil
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
name|xmlschema
operator|.
name|SchemaCollection
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
name|JavaUtils
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
name|ServiceInfo
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
name|ToolContext
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
name|model
operator|.
name|DefaultValueWriter
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
name|util
operator|.
name|ClassCollector
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
name|util
operator|.
name|NameUtil
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
name|wsdlto
operator|.
name|core
operator|.
name|DataBindingProfile
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
name|XmlSchemaComplexContentExtension
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
name|XmlSchemaComplexType
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
name|XmlSchemaContent
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
name|XmlSchemaElement
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
name|XmlSchemaForm
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
name|XmlSchemaSequence
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
name|XmlSchemaSequenceMember
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
name|XmlSchemaType
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ProcessorUtil
block|{
specifier|private
specifier|static
specifier|final
name|String
name|KEYWORDS_PREFIX
init|=
literal|"_"
decl_stmt|;
specifier|private
name|ProcessorUtil
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
name|resolvePartName
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|)
block|{
return|return
name|NameUtil
operator|.
name|mangleNameToVariableName
argument_list|(
name|part
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getPartType
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|)
block|{
return|return
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|resolvePartType
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|)
block|{
return|return
name|NameUtil
operator|.
name|mangleNameToClassName
argument_list|(
name|getPartType
argument_list|(
name|part
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getType
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|,
name|ToolContext
name|context
parameter_list|,
name|boolean
name|fullname
parameter_list|)
block|{
name|String
name|type
init|=
literal|""
decl_stmt|;
name|DataBindingProfile
name|dataBinding
init|=
name|context
operator|.
name|get
argument_list|(
name|DataBindingProfile
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|part
operator|.
name|isElement
argument_list|()
condition|)
block|{
name|type
operator|=
name|dataBinding
operator|.
name|getType
argument_list|(
name|getElementName
argument_list|(
name|part
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
name|dataBinding
operator|.
name|getType
argument_list|(
name|part
operator|.
name|getTypeQName
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|resolvePartType
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
return|return
name|type
return|;
block|}
specifier|public
specifier|static
name|DefaultValueWriter
name|getDefaultValueWriter
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|,
name|ToolContext
name|context
parameter_list|)
block|{
name|DataBindingProfile
name|dataBinding
init|=
name|context
operator|.
name|get
argument_list|(
name|DataBindingProfile
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|part
operator|.
name|isElement
argument_list|()
condition|)
block|{
return|return
name|dataBinding
operator|.
name|createDefaultValueWriter
argument_list|(
name|getElementName
argument_list|(
name|part
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
return|return
name|dataBinding
operator|.
name|createDefaultValueWriter
argument_list|(
name|part
operator|.
name|getTypeQName
argument_list|()
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|DefaultValueWriter
name|getDefaultValueWriterForWrappedElement
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|,
name|ToolContext
name|context
parameter_list|,
name|QName
name|subElement
parameter_list|)
block|{
name|DataBindingProfile
name|dataBinding
init|=
name|context
operator|.
name|get
argument_list|(
name|DataBindingProfile
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|dataBinding
operator|.
name|createDefaultValueWriterForWrappedElement
argument_list|(
name|part
operator|.
name|getElementQName
argument_list|()
argument_list|,
name|subElement
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|QName
name|getElementName
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|)
block|{
return|return
name|part
operator|==
literal|null
condition|?
literal|null
else|:
name|part
operator|.
name|getConcreteName
argument_list|()
return|;
block|}
comment|//
comment|// support multiple -p options
comment|// if user change the package name through -p namespace=package name
comment|//
specifier|public
specifier|static
name|QName
name|getMappedElementName
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|,
name|ToolContext
name|env
parameter_list|)
block|{
name|QName
name|origin
init|=
name|getElementName
argument_list|(
name|part
argument_list|)
decl_stmt|;
if|if
condition|(
name|origin
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|env
operator|.
name|hasNamespace
argument_list|(
name|origin
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|origin
return|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|env
operator|.
name|getCustomizedNS
argument_list|(
name|origin
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
argument_list|,
name|origin
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|resolvePartType
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|,
name|ToolContext
name|env
parameter_list|)
block|{
if|if
condition|(
name|env
operator|!=
literal|null
condition|)
block|{
return|return
name|resolvePartType
argument_list|(
name|part
argument_list|,
name|env
argument_list|,
literal|false
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|resolvePartType
argument_list|(
name|part
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|resolvePartType
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|,
name|ToolContext
name|context
parameter_list|,
name|boolean
name|fullName
parameter_list|)
block|{
name|DataBindingProfile
name|dataBinding
init|=
name|context
operator|.
name|get
argument_list|(
name|DataBindingProfile
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataBinding
operator|==
literal|null
condition|)
block|{
name|String
name|primitiveType
init|=
name|JAXBUtils
operator|.
name|builtInTypeToJavaType
argument_list|(
name|part
operator|.
name|getTypeQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|part
operator|.
name|getTypeQName
argument_list|()
operator|!=
literal|null
operator|&&
name|primitiveType
operator|!=
literal|null
condition|)
block|{
return|return
name|primitiveType
return|;
block|}
else|else
block|{
return|return
name|resolvePartType
argument_list|(
name|part
argument_list|)
return|;
block|}
block|}
name|String
name|name
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|part
operator|.
name|isElement
argument_list|()
condition|)
block|{
name|name
operator|=
name|dataBinding
operator|.
name|getType
argument_list|(
name|getElementName
argument_list|(
name|part
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|dataBinding
operator|.
name|getType
argument_list|(
name|part
operator|.
name|getTypeQName
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|String
name|namespace
init|=
name|resolvePartNamespace
argument_list|(
name|part
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"http://www.w3.org/2005/08/addressing"
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
comment|//The ws-addressing stuff isn't mapped in jaxb as jax-ws specifies they
comment|//need to be mapped differently
name|String
name|pn
init|=
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"EndpointReference"
operator|.
name|equals
argument_list|(
name|pn
argument_list|)
operator|||
literal|"ReplyTo"
operator|.
name|equals
argument_list|(
name|pn
argument_list|)
operator|||
literal|"From"
operator|.
name|equals
argument_list|(
name|pn
argument_list|)
operator|||
literal|"FaultTo"
operator|.
name|equals
argument_list|(
name|pn
argument_list|)
condition|)
block|{
name|name
operator|=
literal|"javax.xml.ws.wsaddressing.W3CEndpointReference"
expr_stmt|;
block|}
block|}
block|}
return|return
name|name
return|;
block|}
specifier|public
specifier|static
name|String
name|resolvePartNamespace
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|)
block|{
return|return
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|mangleNameToVariableName
parameter_list|(
name|String
name|vName
parameter_list|)
block|{
name|String
name|result
init|=
name|NameUtil
operator|.
name|mangleNameToVariableName
argument_list|(
name|vName
argument_list|)
decl_stmt|;
if|if
condition|(
name|JavaUtils
operator|.
name|isJavaKeyword
argument_list|(
name|result
argument_list|)
condition|)
block|{
return|return
name|KEYWORDS_PREFIX
operator|+
name|result
return|;
block|}
else|else
block|{
return|return
name|result
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|parsePackageName
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|defaultPackageName
parameter_list|)
block|{
name|String
name|packageName
init|=
operator|(
name|defaultPackageName
operator|!=
literal|null
operator|&&
name|defaultPackageName
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
condition|?
name|defaultPackageName
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|packageName
operator|==
literal|null
condition|)
block|{
name|packageName
operator|=
name|URIParserUtil
operator|.
name|getPackageName
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
return|return
name|packageName
return|;
block|}
specifier|public
specifier|static
name|String
name|getAbsolutePath
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|location
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
condition|)
block|{
return|return
name|location
return|;
block|}
else|else
block|{
return|return
name|resolvePath
argument_list|(
operator|new
name|File
argument_list|(
name|location
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|URL
name|getWSDLURL
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|location
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
condition|)
block|{
return|return
operator|new
name|URL
argument_list|(
name|location
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|File
argument_list|(
name|getAbsolutePath
argument_list|(
name|location
argument_list|)
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
return|;
block|}
block|}
specifier|private
specifier|static
name|String
name|resolvePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|classNameToFilePath
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|String
name|str
decl_stmt|;
if|if
condition|(
name|className
operator|.
name|indexOf
argument_list|(
literal|"."
argument_list|)
operator|<
literal|0
condition|)
block|{
return|return
name|className
return|;
block|}
else|else
block|{
name|str
operator|=
name|className
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
block|}
return|return
name|str
return|;
block|}
comment|//
comment|// the non-wrapper style will get the type info from the part directly
comment|//
specifier|public
specifier|static
name|String
name|getFullClzName
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|,
name|ToolContext
name|context
parameter_list|,
name|boolean
name|primitiveType
parameter_list|)
block|{
name|DataBindingProfile
name|dataBinding
init|=
name|context
operator|.
name|get
argument_list|(
name|DataBindingProfile
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|jtype
init|=
literal|null
decl_stmt|;
name|QName
name|xmlTypeName
init|=
name|getElementName
argument_list|(
name|part
argument_list|)
decl_stmt|;
comment|// if this flag  is true , mapping to java Type first;
comment|// if not found , findd the primitive type : int ,long
comment|// if not found,  find in the generated class
if|if
condition|(
operator|!
name|primitiveType
operator|&&
name|dataBinding
operator|!=
literal|null
condition|)
block|{
name|jtype
operator|=
name|dataBinding
operator|.
name|getType
argument_list|(
name|xmlTypeName
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|primitiveType
operator|&&
name|dataBinding
operator|==
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|holderClass
init|=
name|JAXBUtils
operator|.
name|holderClass
argument_list|(
name|xmlTypeName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|jtype
operator|=
name|holderClass
operator|==
literal|null
condition|?
literal|null
else|:
name|holderClass
operator|.
name|getName
argument_list|()
expr_stmt|;
if|if
condition|(
name|jtype
operator|==
literal|null
condition|)
block|{
name|jtype
operator|=
name|JAXBUtils
operator|.
name|builtInTypeToJavaType
argument_list|(
name|xmlTypeName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|primitiveType
condition|)
block|{
name|jtype
operator|=
name|JAXBUtils
operator|.
name|builtInTypeToJavaType
argument_list|(
name|xmlTypeName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|namespace
init|=
name|xmlTypeName
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|type
init|=
name|resolvePartType
argument_list|(
name|part
argument_list|,
name|context
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|String
name|userPackage
init|=
name|context
operator|.
name|mapPackageName
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
name|ClassCollector
name|collector
init|=
name|context
operator|.
name|get
argument_list|(
name|ClassCollector
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|jtype
operator|==
literal|null
condition|)
block|{
name|jtype
operator|=
name|collector
operator|.
name|getTypesFullClassName
argument_list|(
name|parsePackageName
argument_list|(
name|namespace
argument_list|,
name|userPackage
argument_list|)
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jtype
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|resolvePartType
argument_list|(
name|part
argument_list|)
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|jtype
operator|=
name|resolvePartType
argument_list|(
name|part
argument_list|,
name|context
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jtype
operator|=
name|parsePackageName
argument_list|(
name|namespace
argument_list|,
name|userPackage
argument_list|)
operator|+
literal|"."
operator|+
name|type
expr_stmt|;
block|}
block|}
return|return
name|jtype
return|;
block|}
specifier|public
specifier|static
name|String
name|getFileOrURLName
parameter_list|(
name|String
name|fileOrURL
parameter_list|)
block|{
try|try
block|{
try|try
block|{
return|return
name|escapeSpace
argument_list|(
operator|new
name|URL
argument_list|(
name|fileOrURL
argument_list|)
operator|.
name|toExternalForm
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|fileOrURL
argument_list|)
operator|.
name|getCanonicalFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|fileOrURL
return|;
block|}
block|}
specifier|private
specifier|static
name|String
name|escapeSpace
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
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
name|url
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|url
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|' '
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"%20"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
name|url
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|absolutize
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// absolutize all the system IDs in the input,
comment|// so that we can map system IDs to DOM trees.
try|try
block|{
name|URL
name|baseURL
init|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getCanonicalFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
return|return
operator|new
name|URL
argument_list|(
name|baseURL
argument_list|,
name|name
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
argument_list|)
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|name
return|;
block|}
specifier|public
specifier|static
name|String
name|getHandlerConfigFileName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|+
literal|"_handler"
return|;
block|}
specifier|public
specifier|static
name|Node
name|cloneNode
parameter_list|(
name|Document
name|document
parameter_list|,
name|Node
name|node
parameter_list|,
name|boolean
name|deep
parameter_list|)
throws|throws
name|DOMException
block|{
if|if
condition|(
name|document
operator|==
literal|null
operator|||
name|node
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|type
init|=
name|node
operator|.
name|getNodeType
argument_list|()
decl_stmt|;
if|if
condition|(
name|node
operator|.
name|getOwnerDocument
argument_list|()
operator|==
name|document
condition|)
block|{
return|return
name|node
operator|.
name|cloneNode
argument_list|(
name|deep
argument_list|)
return|;
block|}
name|Node
name|clone
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Node
operator|.
name|CDATA_SECTION_NODE
case|:
name|clone
operator|=
name|document
operator|.
name|createCDATASection
argument_list|(
name|node
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|Node
operator|.
name|COMMENT_NODE
case|:
name|clone
operator|=
name|document
operator|.
name|createComment
argument_list|(
name|node
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|Node
operator|.
name|ENTITY_REFERENCE_NODE
case|:
name|clone
operator|=
name|document
operator|.
name|createEntityReference
argument_list|(
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|Node
operator|.
name|ELEMENT_NODE
case|:
name|clone
operator|=
name|document
operator|.
name|createElementNS
argument_list|(
name|node
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
name|NamedNodeMap
name|attributes
init|=
name|node
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
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Attr
name|attr
init|=
operator|(
name|Attr
operator|)
name|attributes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Attr
name|attrnew
init|=
operator|(
operator|(
name|Element
operator|)
name|clone
operator|)
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createAttributeNS
argument_list|(
name|attr
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|attr
operator|.
name|getNodeName
argument_list|()
argument_list|)
decl_stmt|;
name|attrnew
operator|.
name|setValue
argument_list|(
name|attr
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Element
operator|)
name|clone
operator|)
operator|.
name|setAttributeNodeNS
argument_list|(
name|attrnew
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|Node
operator|.
name|TEXT_NODE
case|:
name|clone
operator|=
name|document
operator|.
name|createTextNode
argument_list|(
name|node
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
return|return
literal|null
return|;
block|}
if|if
condition|(
name|deep
operator|&&
name|type
operator|==
name|Node
operator|.
name|ELEMENT_NODE
condition|)
block|{
name|Node
name|child
init|=
name|node
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|clone
operator|.
name|appendChild
argument_list|(
name|cloneNode
argument_list|(
name|document
argument_list|,
name|child
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|child
operator|=
name|child
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|clone
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|QName
argument_list|>
name|getWrappedElementQNames
parameter_list|(
name|ToolContext
name|context
parameter_list|,
name|QName
name|partElement
parameter_list|)
block|{
name|List
argument_list|<
name|QName
argument_list|>
name|qnames
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|partElement
operator|==
literal|null
condition|)
block|{
return|return
name|qnames
return|;
block|}
for|for
control|(
name|WrapperElement
name|element
range|:
name|getWrappedElement
argument_list|(
name|context
argument_list|,
name|partElement
argument_list|)
control|)
block|{
name|qnames
operator|.
name|add
argument_list|(
name|element
operator|.
name|getElementName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|qnames
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|WrapperElement
argument_list|>
name|getWrappedElement
parameter_list|(
name|ToolContext
name|context
parameter_list|,
name|QName
name|partElement
parameter_list|)
block|{
name|List
argument_list|<
name|WrapperElement
argument_list|>
name|qnames
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|context
operator|.
name|get
argument_list|(
name|ServiceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|SchemaCollection
name|schema
init|=
name|serviceInfo
operator|.
name|getXmlSchemaCollection
argument_list|()
decl_stmt|;
name|XmlSchemaElement
name|elementByName
init|=
name|schema
operator|.
name|getElementByQName
argument_list|(
name|partElement
argument_list|)
decl_stmt|;
name|XmlSchemaComplexType
name|type
init|=
operator|(
name|XmlSchemaComplexType
operator|)
name|elementByName
operator|.
name|getSchemaType
argument_list|()
decl_stmt|;
name|XmlSchemaSequence
name|seq
init|=
operator|(
name|XmlSchemaSequence
operator|)
name|type
operator|.
name|getParticle
argument_list|()
decl_stmt|;
name|qnames
operator|.
name|addAll
argument_list|(
name|createWrappedElements
argument_list|(
name|seq
argument_list|)
argument_list|)
expr_stmt|;
comment|//If it's extension
if|if
condition|(
name|seq
operator|==
literal|null
operator|&&
name|type
operator|.
name|getContentModel
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|XmlSchemaContent
name|xmlSchemaConent
init|=
name|type
operator|.
name|getContentModel
argument_list|()
operator|.
name|getContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|xmlSchemaConent
operator|instanceof
name|XmlSchemaComplexContentExtension
condition|)
block|{
name|XmlSchemaComplexContentExtension
name|extension
init|=
operator|(
name|XmlSchemaComplexContentExtension
operator|)
name|type
operator|.
name|getContentModel
argument_list|()
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|QName
name|baseTypeName
init|=
name|extension
operator|.
name|getBaseTypeName
argument_list|()
decl_stmt|;
name|XmlSchemaType
name|schemaType
init|=
name|schema
operator|.
name|getTypeByQName
argument_list|(
name|baseTypeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|schemaType
operator|instanceof
name|XmlSchemaComplexType
condition|)
block|{
name|XmlSchemaComplexType
name|complexType
init|=
operator|(
name|XmlSchemaComplexType
operator|)
name|schemaType
decl_stmt|;
if|if
condition|(
name|complexType
operator|.
name|getParticle
argument_list|()
operator|instanceof
name|XmlSchemaSequence
condition|)
block|{
name|seq
operator|=
operator|(
name|XmlSchemaSequence
operator|)
name|complexType
operator|.
name|getParticle
argument_list|()
expr_stmt|;
name|qnames
operator|.
name|addAll
argument_list|(
name|createWrappedElements
argument_list|(
name|seq
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|extension
operator|.
name|getParticle
argument_list|()
operator|instanceof
name|XmlSchemaSequence
condition|)
block|{
name|XmlSchemaSequence
name|xmlSchemaSeq
init|=
operator|(
name|XmlSchemaSequence
operator|)
name|extension
operator|.
name|getParticle
argument_list|()
decl_stmt|;
name|qnames
operator|.
name|addAll
argument_list|(
name|createWrappedElements
argument_list|(
name|xmlSchemaSeq
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|qnames
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|WrapperElement
argument_list|>
name|createWrappedElements
parameter_list|(
name|XmlSchemaSequence
name|seq
parameter_list|)
block|{
name|List
argument_list|<
name|WrapperElement
argument_list|>
name|qnames
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|seq
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|XmlSchemaSequenceMember
argument_list|>
name|items
init|=
name|seq
operator|.
name|getItems
argument_list|()
decl_stmt|;
for|for
control|(
name|XmlSchemaSequenceMember
name|seqMember
range|:
name|items
control|)
block|{
name|XmlSchemaElement
name|subElement
init|=
operator|(
name|XmlSchemaElement
operator|)
name|seqMember
decl_stmt|;
if|if
condition|(
name|subElement
operator|.
name|getQName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|qnames
operator|.
name|add
argument_list|(
operator|new
name|WrapperElement
argument_list|(
name|subElement
operator|.
name|getWireName
argument_list|()
argument_list|,
name|subElement
operator|.
name|getSchemaTypeName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|qnames
operator|.
name|add
argument_list|(
operator|new
name|WrapperElement
argument_list|(
name|subElement
operator|.
name|getRef
argument_list|()
operator|.
name|getTargetQName
argument_list|()
argument_list|,
name|subElement
operator|.
name|getSchemaTypeName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|qnames
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isSchemaFormQualified
parameter_list|(
name|ToolContext
name|context
parameter_list|,
name|QName
name|partElement
parameter_list|)
block|{
name|ServiceInfo
name|serviceInfo
init|=
name|context
operator|.
name|get
argument_list|(
name|ServiceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|SchemaCollection
name|schemaCol
init|=
name|serviceInfo
operator|.
name|getXmlSchemaCollection
argument_list|()
decl_stmt|;
name|XmlSchema
name|schema
init|=
name|schemaCol
operator|.
name|getSchemaForElement
argument_list|(
name|partElement
argument_list|)
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
return|return
name|schema
operator|.
name|getElementFormDefault
argument_list|()
operator|==
name|XmlSchemaForm
operator|.
name|QUALIFIED
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

